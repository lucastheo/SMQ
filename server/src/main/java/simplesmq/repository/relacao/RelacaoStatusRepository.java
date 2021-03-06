package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.util.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RelacaoStatusRepository {
    ConcurrentHashMap<String, ConcurrentHashMap<String,ConcurrentLinkedQueue<RelacaoEntity>>> novo = new ConcurrentHashMap();

    ConcurrentHashMap<String, ConcurrentLinkedQueue<RelacaoEntity>> processamento = new ConcurrentHashMap<>();

    ConcurrentLinkedQueue<RelacaoEntity> finalizado = new ConcurrentLinkedQueue<>();

    ConcurrentHashMap<String, AtomicInteger> controleQuantidadeElementos = new ConcurrentHashMap<>();

    ConcurrentLinkedQueue<String> controleQuantidadeJaZerados = new ConcurrentLinkedQueue<>();

    Lock controleDaVoltaDosEstadosLock = new ReentrantLock();

    public void addFila(String nomeFila){
        novo.putIfAbsent(nomeFila,new ConcurrentHashMap<>());
    }

    public void addConsumidores(String nomeFila , String consumidor ){
        novo.get(nomeFila).putIfAbsent(consumidor, new ConcurrentLinkedQueue<RelacaoEntity>());
        processamento.putIfAbsent(consumidor, new ConcurrentLinkedQueue<RelacaoEntity>());
    }

    public void addRelacao(String nomeFila , String consumidor , RelacaoEntity relacaoEntity ){
        novo.get(nomeFila).get(consumidor).add(relacaoEntity);
        if( controleQuantidadeElementos.containsKey(relacaoEntity.getIdentificacaoMensagem()) ){
            controleQuantidadeElementos.get(relacaoEntity.getIdentificacaoMensagem()).addAndGet(1);
        }else{
            controleQuantidadeElementos.put(relacaoEntity.getIdentificacaoMensagem(),new AtomicInteger(1));
        }
    }

    public int quantidadeDeElementosParaProcessar(String nomeFila , String consumidor ){
        if( novo.containsKey(nomeFila) && novo.get(nomeFila).containsKey(consumidor)) {
            return novo.get(nomeFila).get(consumidor).size();
        }
        return 0;
    }

    public Boolean voltaParaNovo(RelacaoEntity relacaoEntity ){
        if (processamento.containsKey(relacaoEntity.getNome())) {
            if (processamento.get(relacaoEntity.getNome()).remove(relacaoEntity)) {
                try {
                    controleDaVoltaDosEstadosLock.lock();
                    this.addRelacao(relacaoEntity.getNomeFila(), relacaoEntity.getNome(), relacaoEntity);
                }catch(Exception exception ){
                    Logger.erro("Erro durante o processo de voltaParaNovo" , relacaoEntity , exception);
                    throw exception;
                }finally{
                    controleDaVoltaDosEstadosLock.unlock();
                }
                return true;
            }
        }
        return false;
    }

    public Optional<RelacaoEntity> reserve(String nomeFila , String consumidor ){
        if( novo.containsKey(nomeFila) && novo.get(nomeFila).containsKey(consumidor)){
            RelacaoEntity relacaoEntity = novo.get(nomeFila).get(consumidor).poll();
            if( relacaoEntity != null ){
                processamento.get(consumidor).add(relacaoEntity);
            }
            return Optional.ofNullable(relacaoEntity);
        }
        return Optional.empty();
    }

    public Optional<RelacaoEntity> reserve(String nomeFila , String consumidor , Set<String> mensagensCache){
        if( !novo.containsKey(nomeFila) || !novo.get(nomeFila).containsKey(consumidor)){
            return Optional.empty();
        }
        for( RelacaoEntity relacaoEntity : novo.get(nomeFila).get(consumidor) ){
            if( mensagensCache.contains(relacaoEntity.getIdentificacaoMensagem()) ){
                if( novo.get(nomeFila).get(consumidor).remove(relacaoEntity) ) {
                    processamento.get(consumidor).add(relacaoEntity);
                    return Optional.of(relacaoEntity);
                }
            }
        }
        return this.reserve(nomeFila,consumidor);
    }

    public Optional<RelacaoEntity> finaliza( String nomeGrupo , String identificacaoMensagem ){
        if(!processamento.containsKey(nomeGrupo) ){
            return Optional.empty();
        }
        for( RelacaoEntity relacaoEntity : processamento.get(nomeGrupo) ){
            if( relacaoEntity.getIdentificacaoMensagem().equals(identificacaoMensagem) ){
                if( processamento.get(nomeGrupo).remove(relacaoEntity)){
                    if( processamento.get(nomeGrupo).size() == 0 && !this.existeMesnagemNovaParaGrupo(nomeGrupo)){
                        processamento.remove(nomeGrupo);
                    }
                    finalizado.add(relacaoEntity);
                    return Optional.of(relacaoEntity);
                }
            }
        }
        return Optional.empty();
    }

    private Boolean existeMesnagemNovaParaGrupo( String nomeGrupo ){
        for(String nomeFila : this.novo.keySet() ){
            if( this.novo.get(nomeFila).containsKey(nomeGrupo) && this.novo.get(nomeFila).get(nomeGrupo).size() > 0 ){
                return true;
            }
        }
        return false;
    }

    public Optional<RelacaoEntity> limpaFinalizado(){
        if( finalizado.isEmpty() ){
            return Optional.empty();
        }
        RelacaoEntity relacaoEntity = finalizado.poll();
        if( controleQuantidadeElementos.get(relacaoEntity.getIdentificacaoMensagem()).addAndGet(-1) == 0) {
            controleQuantidadeJaZerados.add(relacaoEntity.getIdentificacaoMensagem());
            controleQuantidadeElementos.remove(relacaoEntity.getIdentificacaoMensagem());
        };
        limpaNovo(relacaoEntity);
        return Optional.of(relacaoEntity);
    }

    public Optional<String> limpaRelacaoZerada(){
        String identificacaoMensagem = controleQuantidadeJaZerados.poll();
        if(identificacaoMensagem == null ){
            return Optional.empty();
        }
        return Optional.of(identificacaoMensagem);
    }

    private void limpaNovo( RelacaoEntity relacaoEntity ){
        if( !novo.containsKey(relacaoEntity.getNomeFila()) || !novo.get(relacaoEntity.getNomeFila()).containsKey(relacaoEntity.getNome()) ){
            return;
        }
        if( novo.get(relacaoEntity.getNomeFila()).get(relacaoEntity.getNome()).size() == 0 ) {
            novo.get(relacaoEntity.getNomeFila()).remove(relacaoEntity.getNome());
            if (novo.get(relacaoEntity.getNomeFila()).size() == 0) {
                novo.remove(relacaoEntity.getNomeFila());
            }
        }
    }

    public void removeTodasOcorrencias(MensagemEntity mensagemEntity){
        try {
            controleDaVoltaDosEstadosLock.lock();
            for (String nomeConsumo : novo.getOrDefault(mensagemEntity.getNomeFila(), new ConcurrentHashMap<>()).keySet()) {
                for (RelacaoEntity relacaoEntity : novo.get(mensagemEntity.getNomeFila()).get(nomeConsumo)) {
                    if (relacaoEntity.getIdentificacaoMensagem().equals(mensagemEntity.getIdentificacao())) {
                        processamento.get(relacaoEntity.getNome()).add(relacaoEntity);
                        novo.get(mensagemEntity.getNomeFila()).get(nomeConsumo).remove(relacaoEntity);
                    }
                }
            }

            for (String nomeConsumo : processamento.keySet()) {
                for (RelacaoEntity relacaoEntity : processamento.get(nomeConsumo)) {
                    if (relacaoEntity.getIdentificacaoMensagem().equals(mensagemEntity.getIdentificacao())) {
                        this.finaliza(nomeConsumo, relacaoEntity.getIdentificacaoMensagem());
                    }
                }
            }
        }catch(Exception ex ){
            Logger.erro("Erro durante a remoção da MesnagemEntity no sistema" , mensagemEntity , ex );
            throw ex;
        }
        finally{
            controleDaVoltaDosEstadosLock.unlock();
        }
    }

    public  void removeTodasMensagemFiltrandoPorFila( String nomeFila ){
        try {
            controleDaVoltaDosEstadosLock.lock();
            if(novo.containsKey(nomeFila)){
                for(String nomeConsumo : novo.getOrDefault(nomeFila, new ConcurrentHashMap<>()).keySet()){
                    for( RelacaoEntity relacaoEntity : novo.get(nomeFila).get(nomeConsumo)){
                        processamento.get(relacaoEntity.getNome()).add(relacaoEntity);
                        novo.get(nomeFila).get(nomeConsumo).remove(relacaoEntity);
                    }
                }
            }
            for( String nomeConsumo : processamento.keySet() ){
                for(RelacaoEntity relacaoEntity : processamento.get(nomeConsumo)){
                    this.finaliza(nomeConsumo,relacaoEntity.getIdentificacaoMensagem());
                }
            }
        }catch(Exception ex ){
            Logger.erro("Erro durante a remoção das mensagens na fila do sistema" , nomeFila , ex );
            throw ex;
        }
        finally{
            controleDaVoltaDosEstadosLock.unlock();
        }
    }

    public HashMap<String,HashMap<String,Integer>> consultaQuantidadeMensagensParaProcessar( ){
        HashMap<String,HashMap<String,Integer>> retorno = new HashMap<>();
        for( String fila : Collections.unmodifiableSet(novo.keySet())){
            retorno.put(fila,new HashMap());
            for( String consumidor : Collections.unmodifiableSet( novo.getOrDefault(fila, new ConcurrentHashMap<>() ).keySet() ) ) {
                retorno.get(fila).put(
                        consumidor ,
                        novo.getOrDefault(fila,new ConcurrentHashMap<>()).getOrDefault(consumidor, new ConcurrentLinkedQueue<>()).size()
                );
            }
        }
        return retorno;
    }

    public HashMap<String,HashMap<String,Integer>> consultaQuantidadeMensagensParaProcessar( String fila ){
        HashMap<String,HashMap<String,Integer>> retorno = new HashMap<>();
        retorno.put(fila,new HashMap());
        for( String consumidor : Collections.unmodifiableSet( novo.getOrDefault(fila, new ConcurrentHashMap<>() ).keySet() ) ) {
            retorno.get(fila).put(
                    consumidor ,
                    novo.getOrDefault(fila,new ConcurrentHashMap<>()).getOrDefault(consumidor, new ConcurrentLinkedQueue<>()).size()
            );
        }
        return retorno;
    }

    public Integer consultaQuantidadeMensagensEmProcessamento(){
        int i = 0;
        for( String consumidor : Collections.unmodifiableSet( processamento.keySet())){
             i += processamento.getOrDefault(consumidor, new ConcurrentLinkedQueue<>()).size();
        }
        return i;
    }
}

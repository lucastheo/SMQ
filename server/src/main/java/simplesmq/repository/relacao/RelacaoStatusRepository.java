package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RelacaoStatusRepository {
    ConcurrentHashMap<String, ConcurrentHashMap<String,ConcurrentLinkedQueue<RelacaoEntity>>> novo = new ConcurrentHashMap();

    ConcurrentHashMap<String, ConcurrentLinkedQueue<RelacaoEntity>> processamento = new ConcurrentHashMap<>();

    ConcurrentLinkedQueue<RelacaoEntity> finalizado = new ConcurrentLinkedQueue<>();

    ConcurrentHashMap<String, AtomicInteger> controleQuantidadeElementos = new ConcurrentHashMap<>();

    ConcurrentLinkedQueue<String> controleQuantidadeJaZerados = new ConcurrentLinkedQueue<>();

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
                    finalizado.add(relacaoEntity);
                    return Optional.of(relacaoEntity);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<RelacaoEntity> limpaFinalizado(){
        if( finalizado.isEmpty() ){
            return Optional.empty();
        }
        RelacaoEntity relacaoEntity = finalizado.poll();
        if( controleQuantidadeElementos.get(relacaoEntity.getIdentificacaoMensagem()).addAndGet(-1) == 0) {
            controleQuantidadeJaZerados.add(relacaoEntity.getIdentificacaoMensagem());
            controleQuantidadeElementos.remove(relacaoEntity.getIdentificacao());
        };
        return Optional.of(relacaoEntity);
    }

    public Optional<String> limpaRelacaoZerada(){
        String identificacaoMensagem = controleQuantidadeJaZerados.poll();
        if(identificacaoMensagem == null ){
            return Optional.empty();
        }
        controleQuantidadeElementos.remove(identificacaoMensagem);
        return Optional.of(identificacaoMensagem);
    }


}

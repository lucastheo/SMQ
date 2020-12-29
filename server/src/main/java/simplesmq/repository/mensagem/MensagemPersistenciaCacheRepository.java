package simplesmq.repository.mensagem;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;

import java.util.*;
import java.util.concurrent.Semaphore;

@Component
public class MensagemPersistenciaCacheRepository {

    final Map<String, Set<String>> findCache = new LinkedHashMap<String, Set<String>>();
    final Map<String,MensagemEntity> cacheMensagem = new LinkedHashMap<String,MensagemEntity>();
    final Semaphore semaphoreAddInList = new Semaphore(1);

    public StatusElementoEmAgrupamentoEnum salvar( MensagemEntity mensagemEntity ) throws InterruptedException {
        if(cacheMensagem.containsKey(mensagemEntity.getIdentificacao())){
            return StatusElementoEmAgrupamentoEnum.ENCONTADO;
        }
        try {
             semaphoreAddInList.acquire();
            cacheMensagem.put(mensagemEntity.getIdentificacao(), mensagemEntity);
            if(!findCache.containsKey(mensagemEntity.getNomeFila())){
                findCache.put(mensagemEntity.getNomeFila(), new LinkedHashSet<String>());    
            }
            findCache.get(mensagemEntity.getNomeFila()).add(mensagemEntity.getIdentificacao());
        } catch (InterruptedException e) {
            throw e;
        } finally {
            semaphoreAddInList.release();
        }
        return StatusElementoEmAgrupamentoEnum.ADICIONADO;
    }

    public StatusElementoEmAgrupamentoEnum remover(MensagemEntity mensagemEntity ) throws InterruptedException {
        try {
            semaphoreAddInList.acquire();
            if( !cacheMensagem.containsKey(mensagemEntity.getIdentificacao()) ){
                return StatusElementoEmAgrupamentoEnum.NAO_ENCONTRADO;
            }
            cacheMensagem.remove(mensagemEntity.getIdentificacao());
            findCache.get(mensagemEntity.getNomeFila()).remove(mensagemEntity.getIdentificacao());
            if(findCache.get(mensagemEntity.getNomeFila()).size() == 0){
                findCache.remove(mensagemEntity.getNomeFila());
            }

        } catch (InterruptedException e) {
            throw e;
        }finally {
            semaphoreAddInList.release();
        }
        return StatusElementoEmAgrupamentoEnum.REMOVIDO;
    }

    public Set<String> chaveMensagem(String nomeFila){
        return findCache.getOrDefault(nomeFila, new LinkedHashSet<>());
    }

    public Integer quantidadeElemento(){
        return cacheMensagem.size();
    }

    public Optional<MensagemEntity> busca(String identificacao ){
        return Optional.ofNullable( cacheMensagem.getOrDefault(identificacao , null ) );
    }
}

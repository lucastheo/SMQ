package simplesmq.repository.mensagem;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Component
public class MensagemPersistenciaCacheRepository {

    final Map<String,MensagemEntity> cacheMensagem = new LinkedHashMap<String,MensagemEntity>();
    final Semaphore semaphoreAddInList = new Semaphore(1);

    public StatusElementoEmAgrupamentoEnum salvar( MensagemEntity mensagemEntity ) throws InterruptedException {
        try {
            semaphoreAddInList.acquire();
            if( cacheMensagem.containsKey(mensagemEntity.getIdentificacao()) ){
                semaphoreAddInList.release();
                return StatusElementoEmAgrupamentoEnum.ENCONTADO;
            }
            cacheMensagem.put(mensagemEntity.getIdentificacao(), mensagemEntity);
        } catch (InterruptedException e) {
            semaphoreAddInList.release();
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
                semaphoreAddInList.release();
                return StatusElementoEmAgrupamentoEnum.NAO_ENCONTRADO;
            }
            cacheMensagem.remove(mensagemEntity.getIdentificacao());
        } catch (InterruptedException e) {
            semaphoreAddInList.release();
            throw e;
        }finally {
            semaphoreAddInList.release();
        }
        return StatusElementoEmAgrupamentoEnum.REMOVIDO;
    }

    public Integer quantidadeElemento(){
        return cacheMensagem.size();
    }
}

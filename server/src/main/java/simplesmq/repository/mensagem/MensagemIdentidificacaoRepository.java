package simplesmq.repository.mensagem;

import org.springframework.stereotype.Component;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;

import java.util.*;
import java.util.concurrent.Semaphore;

@Component
public class MensagemIdentidificacaoRepository {

    Semaphore semaphoreAddInList = new Semaphore(1);
    private static final Set<UUID> listaIdMensagem = new HashSet<>();

    public StatusElementoEmAgrupamentoEnum add(UUID uuid ) throws InterruptedException {
        try {
            semaphoreAddInList.acquire();
            if( listaIdMensagem.contains(uuid) ){
                semaphoreAddInList.release();
                return StatusElementoEmAgrupamentoEnum.ENCONTADO;
            }
            listaIdMensagem.add(uuid);
        } catch (InterruptedException e) {
            semaphoreAddInList.release();
            throw e;
        }finally {
            semaphoreAddInList.release();
        }
        return StatusElementoEmAgrupamentoEnum.ADICIONADO;
    }

    public StatusElementoEmAgrupamentoEnum remove(UUID uuid ) throws InterruptedException {
        try {
            semaphoreAddInList.acquire();
            if( !listaIdMensagem.contains(uuid) ){
                semaphoreAddInList.release();
                return StatusElementoEmAgrupamentoEnum.NAO_ENCONTRADO;
            }
            listaIdMensagem.remove(uuid);
        } catch (InterruptedException e) {
            semaphoreAddInList.release();
            throw e;
        }finally {
            semaphoreAddInList.release();
        }
        return StatusElementoEmAgrupamentoEnum.REMOVIDO;
    }

    public List<UUID> todosUUIDs(){
        return new LinkedList<UUID>(listaIdMensagem);
    }
}

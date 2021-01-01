package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;
import simplesmq.repository.mensagem.MensagemIdentidificacaoRepository;

import java.util.List;
import java.util.UUID;

@Component
public class MensagemIdentidificacaoService {

    @Autowired
    MensagemIdentidificacaoRepository mensagemIdentidificacaoRepository;

    public UUID geraNova() throws InterruptedException {
        UUID uuid;
        do{
            uuid = UUID.randomUUID();
        }while(mensagemIdentidificacaoRepository.add(uuid) == StatusElementoEmAgrupamentoEnum.ENCONTADO);
        return uuid;
    }

    public void remove( UUID uuid) throws InterruptedException {
        mensagemIdentidificacaoRepository.remove(uuid);
    }

    public List<UUID> listaIdentificacaoMensagens(){
        return mensagemIdentidificacaoRepository.todosUUIDs();
    }

    public void add(UUID fromString) throws InterruptedException {
        mensagemIdentidificacaoRepository.add(fromString);
    }
}

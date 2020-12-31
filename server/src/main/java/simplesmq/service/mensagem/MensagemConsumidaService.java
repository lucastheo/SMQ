package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.repository.mensagem.MensagemIdentidificacaoRepository;
import simplesmq.repository.relacao.RelacaoStatusRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@EnableScheduling
@Component
public class MensagemConsumidaService {

    @Autowired
    RelacaoStatusRepository relacaoStatusRepository;
    @Autowired
    MensagemConsultaService mensagemConsultaService;

    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;

    @Autowired
    MensagemIdentidificacaoRepository mensagemIdentidificacaoRepository;

    /*
    * As mensagem que são consumidas são as que não contem mais relação
    * */
    @Scheduled(fixedDelay=5000)
    void mensagensConsumidas(){
        Optional<String> optionalIdentificaoMensagem = Optional.empty();
        do {
            optionalIdentificaoMensagem = relacaoStatusRepository.limpaRelacaoZerada();
            if(optionalIdentificaoMensagem.isPresent()){
                try {
                    MensagemEntity mensagemEntity =mensagemConsultaService.por(optionalIdentificaoMensagem.get());
                    mensagemPersistenciaService.removeCache(mensagemEntity);
                    mensagemPersistenciaService.remove(mensagemEntity);
                    mensagemIdentidificacaoRepository.remove(UUID.fromString(mensagemEntity.getIdentificacao()));
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(optionalIdentificaoMensagem.isPresent());
    }

}

package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.service.relacao.RelacaoStatusService;
import simplesmq.util.Logger;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@EnableScheduling
@Component
public class MensagemConsumidaService {

    @Autowired
    RelacaoStatusService relacaoStatusService;
    @Autowired
    MensagemConsultaService mensagemConsultaService;

    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;

    @Autowired
    MensagemIdentidificacaoService mensagemIdentidificacaoService;

    /*
    * As mensagem que são consumidas são as que não contem mais relação
    * */
    @Scheduled(fixedDelay=5000)
    void mensagensConsumidas(){
        Logger.info("Inicio do processo de limpeza das mensagens que já foram consumidas");
        Optional<String> optionalIdentificaoMensagem = Optional.empty();
        do {
            optionalIdentificaoMensagem = relacaoStatusService.limpaRelacaoZerada();
            if(optionalIdentificaoMensagem.isPresent()){
                try {
                    MensagemEntity mensagemEntity =mensagemConsultaService.por(optionalIdentificaoMensagem.get());
                    mensagemPersistenciaService.removeCache(mensagemEntity);
                    mensagemPersistenciaService.remove(mensagemEntity);
                    mensagemIdentidificacaoService.remove(UUID.fromString(mensagemEntity.getIdentificacao()));
                } catch (IOException | InterruptedException e) {
                    Logger.erro("Processo de limpeza das mensagens que já foram consumidas teve falha em processar" , optionalIdentificaoMensagem.get() , e );
                }
            }
        }while(optionalIdentificaoMensagem.isPresent());
    }

}

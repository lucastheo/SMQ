package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.service.reserva.ReservaFinalizadoService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EnableScheduling
@Component
public class MensagemTempoConsumoService {

    @Autowired
    MensagemIdentidificacaoService mensagemIdentidificacaoService;
    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;
    @Autowired
    MensagemConsultaService mensagemConsultaService;
    @Autowired
    ReservaFinalizadoService reservaFinalizadoService;

    public void compara(MensagemEntity mensagemEntity , LocalDateTime localDateTime , UUID uuid){
        if(mensagemEntity.getTempoMaximoConsumo() != null && localDateTime.isAfter(mensagemEntity.getTempoMaximoConsumo()) ){
            reservaFinalizadoService.finalizaTudo(mensagemEntity);
        }
    }

    @Scheduled(cron = "* * * * * *")
    public void consumo(){
        List<UUID> relacaoEntityList = mensagemIdentidificacaoService.listaIdentificacaoMensagens();
        MensagemEntity mensagemEntity;
        LocalDateTime localDateTime= LocalDateTime.now();
        for( UUID uuid : relacaoEntityList ){
            try {
                mensagemEntity = mensagemConsultaService.por(uuid.toString());
                compara(mensagemEntity,localDateTime, uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

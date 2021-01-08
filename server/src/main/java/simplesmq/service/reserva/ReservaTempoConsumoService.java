package simplesmq.service.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.relacao.RelacaoTempoConsumoRepository;
import simplesmq.service.relacao.RelacaoStatusService;
import simplesmq.util.Logger;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Component
public class ReservaTempoConsumoService {
    @Autowired
    RelacaoTempoConsumoRepository relacaoTempoConsumoRepository;
    @Autowired
    RelacaoStatusService relacaoStatusService;

    public void add(RelacaoEntity relacaoEntity, Long tempoConsumo ){
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(tempoConsumo+1).withNano(0).withSecond(0);
        relacaoTempoConsumoRepository.add(relacaoEntity, localDateTime);
    }

    @Scheduled(fixedDelay = 60*1000)
    public void consumo(){
        Logger.info("Inicio do processo que remove as mensagem que estouraram o tempo da reserva");
        List<RelacaoEntity> relacaoEntityList = relacaoTempoConsumoRepository.desempilha(LocalDateTime.now());
        relacaoEntityList.forEach(relacaoEntity -> relacaoStatusService.voltaParaNovo(relacaoEntity));
    }
}

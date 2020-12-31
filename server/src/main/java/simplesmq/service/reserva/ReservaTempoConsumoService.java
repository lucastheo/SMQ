package simplesmq.service.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.relacao.RelacaoStatusRepository;
import simplesmq.repository.relacao.RelacaoTempoConsumoRepository;

import java.time.LocalDateTime;
import java.util.List;

@EnableScheduling
@Component
public class ReservaTempoConsumoService {
    @Autowired
    RelacaoTempoConsumoRepository relacaoTempoConsumoRepository;
    @Autowired
    RelacaoStatusRepository relacaoStatusRepository;

    public void add(RelacaoEntity relacaoEntity, Long tempoConsumo ){
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(tempoConsumo+1).withNano(0).withSecond(0);
        relacaoTempoConsumoRepository.add(relacaoEntity, localDateTime);
    }

    @Scheduled(fixedDelay = 60*1000)
    public void consumo(){
         List<RelacaoEntity> relacaoEntityList = relacaoTempoConsumoRepository.desempilha(LocalDateTime.now());
         relacaoEntityList.forEach(relacaoEntity -> relacaoStatusRepository.voltaParaNovo(relacaoEntity));
    }
}

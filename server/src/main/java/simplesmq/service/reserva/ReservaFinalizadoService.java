package simplesmq.service.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;
import simplesmq.service.relacao.RelacaoPersistenciaService;
import simplesmq.service.relacao.RelacaoStatusService;
import simplesmq.util.Logger;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@EnableScheduling
public class ReservaFinalizadoService {
    @Autowired
    RelacaoStatusService relacaoStatusService;

    @Autowired
    RelacaoPersistenciaService relacaoPersistenciaService;

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

    public StatusElementoEmAgrupamentoEnum move( String nomeGrupo , String identificacaoMensagem  ){
        Optional<RelacaoEntity> optionalRelacaoEntity = relacaoStatusService.finaliza( nomeGrupo , identificacaoMensagem );
        if( optionalRelacaoEntity.isEmpty() ){
            return StatusElementoEmAgrupamentoEnum.NAO_ENCONTRADO;
        }
        return StatusElementoEmAgrupamentoEnum.REMOVIDO;
    }

    public void finalizaTudo(MensagemEntity mensagemEntity ){
        relacaoStatusService.removeTodasOcorrencias( mensagemEntity);
    }

    @Scheduled(fixedDelay = 5000)
    public void removendo(){
        Logger.info("Inicio do processo de limpeza das relações");
        Optional<RelacaoEntity> optionalRelacaoEntity = Optional.empty();
        do{
            optionalRelacaoEntity = relacaoStatusService.limpaFinalizado();
            optionalRelacaoEntity.ifPresent(relacaoEntity -> relacaoPersistenciaService.removeEmDisco(relacaoEntity));
        }while(optionalRelacaoEntity.isPresent());

    }
}

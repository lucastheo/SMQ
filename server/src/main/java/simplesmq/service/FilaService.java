package simplesmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dto.FilaDto;
import simplesmq.service.relacao.RelacaoStatusService;

@Component
public class FilaService {

    @Autowired
    RelacaoStatusService relacaoStatusService;

    public void limpar(FilaDto filaDto ){
        relacaoStatusService.removeTodasOcorrenciasPorFila(filaDto.getNome());
    }

}

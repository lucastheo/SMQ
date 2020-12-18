package simplesmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.exception.ProcessoException;
import simplesmq.mapping.domain.entity.RelacaoEntityMapping;
import simplesmq.service.relacao.RelacaoPersistenciaService;
import simplesmq.service.relacao.RelacaoStatusService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class RelacaoCriacaoService {
    @Autowired
    RelacaoPersistenciaService relacaoPersistenciaService;
    @Autowired
    RelacaoStatusService relacaoStatusService;

    public void execute( UUID identificacao , MensagemDto mensagem ) throws ProcessoException {
        List<RelacaoEntity> relacoes = RelacaoEntityMapping.mapFrom(identificacao , mensagem);
        String nomeFila = mensagem.getFila().getNome();
        try {
            relacaoPersistenciaService.persistenciaEmDisco(nomeFila, relacoes);
        } catch (IOException e) {
            throw new ProcessoException("Erro em salvar em disco a relação");
        }
        relacaoStatusService.adicionar( nomeFila,relacoes );
    }
}

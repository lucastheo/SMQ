package simplesmq.service.relacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.relacao.RelacaoPersistenciaDiscoRepository;
import simplesmq.service.configuracaoservice.FilaConfiguracaoService;

import java.io.IOException;
import java.util.List;

@Component
public class RelacaoPersistenciaService {
    @Autowired
    FilaConfiguracaoService filaConfiguracaoService;
    @Autowired
    RelacaoPersistenciaDiscoRepository relacaoPersistenciaRepository;

    public void persistenciaEmDisco(String nomeFila , List<RelacaoEntity> relacoes ) throws IOException {
        if( filaConfiguracaoService.persistenciaEmDisco(nomeFila) ){
            for( RelacaoEntity relacaoEntity : relacoes ){
                relacaoPersistenciaRepository.salvar(relacaoEntity);
            }
        }
    }

    public void removeEmDisco(RelacaoEntity relacaoEntity){
        relacaoPersistenciaRepository.remover(relacaoEntity);
    }

    public RelacaoEntity buscaDisco(String name) throws IOException {
        return relacaoPersistenciaRepository.ler(name);
    }
}

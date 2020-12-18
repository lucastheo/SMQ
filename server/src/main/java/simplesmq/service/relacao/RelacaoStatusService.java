package simplesmq.service.relacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.relacao.RelacaoStatusRepository;

import java.util.List;

@Component
public class RelacaoStatusService {
    @Autowired
    RelacaoStatusRepository relacaoStatusRepository;

    public void adicionar(String nomeFila , List<RelacaoEntity> relacoes ){
        relacaoStatusRepository.addFila(nomeFila);

        for( RelacaoEntity relacaoEntity : relacoes ) {
            relacaoStatusRepository.addConsumidores(nomeFila,relacaoEntity.getNome());
            relacaoStatusRepository.addRelacao(nomeFila, relacaoEntity.getNome(),relacaoEntity);
        }
    }
}


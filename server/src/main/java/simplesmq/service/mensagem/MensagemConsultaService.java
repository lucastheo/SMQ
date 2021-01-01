package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;

import java.io.IOException;
import java.util.Optional;

/*
* Tem como ideia organizar a busca entre cache e disco, para quem usa é transparente
* */

@Component
public class MensagemConsultaService {

    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;

    public MensagemEntity por(String identificacao ) throws IOException {
        Optional<MensagemEntity> optionalCache = mensagemPersistenciaService.buscaCache(identificacao);
        if( optionalCache.isPresent()){
            return optionalCache.get();
        }
        return mensagemPersistenciaService.buscaDisco(identificacao);
    }


}

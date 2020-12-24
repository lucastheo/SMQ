package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.exception.ProcessoException;
import simplesmq.repository.mensagem.MensagemPersistenciaCacheRepository;
import simplesmq.repository.mensagem.MensagemPersistenciaDiscoRepository;

import java.io.IOException;
import java.util.Optional;

/*
* Tem como ideia organizar a busca entre cache e disco, para quem usa é transparente
* */

@Component
public class MensagemConsultaService {

    @Autowired
    MensagemPersistenciaCacheRepository mensagemPersistenciaCacheRepository;
    @Autowired
    MensagemPersistenciaDiscoRepository mensagemPersistenciaDiscoRepository;

    public MensagemEntity por(String identificacao ) throws IOException {
        Optional<MensagemEntity> optionalCache = mensagemPersistenciaCacheRepository.busca(identificacao);
        if( optionalCache.isPresent()){
            return optionalCache.get();
        }

        MensagemEntity mensagemEntity = mensagemPersistenciaDiscoRepository.ler(identificacao);
        return mensagemEntity;
    }


}

package simplesmq.service.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.mensagem.MensagemPersistenciaCacheRepository;
import simplesmq.repository.relacao.RelacaoStatusRepository;

import java.util.Optional;
import java.util.Set;

@Component
public class ReservaServiceBusca {
    @Autowired
    MensagemPersistenciaCacheRepository mensagemPersistenciaCacheRepository;
    @Autowired
    RelacaoStatusRepository relacaoStatusRepository;

    public  Optional<RelacaoEntity>  procura( String nomeFila , String nomeGrupo ){
        Set<String> mesnagemCache = mensagemPersistenciaCacheRepository.chaveMensagem(nomeFila);
        Optional<RelacaoEntity> optionalRelacaoEntity;
        if(mesnagemCache.isEmpty()){
            optionalRelacaoEntity = relacaoStatusRepository.reserve(nomeFila, nomeGrupo);
        }else{
            optionalRelacaoEntity =  relacaoStatusRepository.reserve(nomeFila,nomeGrupo,mesnagemCache);
        }
        return optionalRelacaoEntity;
    }

}

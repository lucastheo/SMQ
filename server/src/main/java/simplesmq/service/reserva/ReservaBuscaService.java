package simplesmq.service.reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.service.relacao.RelacaoStatusService;

import java.util.Optional;

@Component
public class ReservaBuscaService {

    @Autowired
    RelacaoStatusService relacaoStatusService;

    public  Optional<RelacaoEntity>  procura( String nomeFila , String nomeGrupo ){
         return relacaoStatusService.reserva(nomeFila,nomeGrupo);
    }

}

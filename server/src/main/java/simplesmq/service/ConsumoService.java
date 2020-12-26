package simplesmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dco.ConsumoDco;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;
import simplesmq.exception.NaoEncontradoException;
import simplesmq.service.reserva.ReservaFinalizadoService;

@Component
public class ConsumoService {

    @Autowired
    ReservaFinalizadoService reservaFinalizadoService;

    public void execute(ConsumoDco consumo ) throws NaoEncontradoException {
        StatusElementoEmAgrupamentoEnum status =  reservaFinalizadoService.move(consumo.getNomeGrupo() , consumo.getIdentificacaoMensagem());
        if( status == StatusElementoEmAgrupamentoEnum.NAO_ENCONTRADO ){
            throw new NaoEncontradoException("Elemento não encontrado em agrupamanto enum");
        }
    }

}

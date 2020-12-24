package simplesmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dco.ConsumoDco;
import simplesmq.domain.dto.ConsutaMensagemDto;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.exception.NaoEncontradoException;
import simplesmq.exception.ProcessoException;
import simplesmq.mapping.domain.dto.ConsutaMensagemDtoMapping;
import simplesmq.service.mensagem.MensagemConsultaService;
import simplesmq.service.reserva.ReservaServiceBusca;

import java.io.IOException;
import java.util.Optional;

@Component
public class ReservaService {

    @Autowired
    ReservaServiceBusca reservaServiceBusca;

    @Autowired
    MensagemConsultaService mensagemConsultaService;

    public ConsutaMensagemDto execute(ConsumoDco consumo ) throws NaoEncontradoException, ProcessoException {

        Optional<RelacaoEntity> optionalRelacaoEntity = reservaServiceBusca.procura(consumo.getNomeFila(),consumo.getNomeGrupo());
        if( optionalRelacaoEntity.isEmpty() ){
            throw new NaoEncontradoException("Não foi encontrado registro para essa fila e grupo");
        }
        RelacaoEntity relacaoEntity = optionalRelacaoEntity.get();

        MensagemEntity mensagemEntity =null;
        try {
            mensagemEntity = mensagemConsultaService.por(relacaoEntity.getIdentificacaoMensagem());
        } catch (IOException e) {
            throw new ProcessoException("Falha em ler o arquivo da mensagem");
        }

        return ConsutaMensagemDtoMapping.mapFrom(relacaoEntity, mensagemEntity);
    }

}

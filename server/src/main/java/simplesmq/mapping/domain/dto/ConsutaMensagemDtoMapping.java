package simplesmq.mapping.domain.dto;

import simplesmq.domain.dto.ConsutaMensagemDto;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;

public class ConsutaMensagemDtoMapping {

    public static ConsutaMensagemDto mapFrom(RelacaoEntity relacaoEntity , MensagemEntity mensagemEntity ){
        ConsutaMensagemDto consutaMensagemDto = new ConsutaMensagemDto();
        consutaMensagemDto.setMensagem(mensagemEntity.getMensagem());
        consutaMensagemDto.setIdentificacaoMensagem(mensagemEntity.getIdentificacao());
        consutaMensagemDto.setNomeNoGrupo(relacaoEntity.getNome());
        return consutaMensagemDto;
    }
}

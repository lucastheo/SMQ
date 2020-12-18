package simplesmq.mapping.domain.dto;

import simplesmq.domain.dto.IdendificacaoMensagemDto;

import java.util.UUID;

public class IdendificacaoMensagemDtoMapping {

    public static IdendificacaoMensagemDto mapFrom(UUID uuid ){
        IdendificacaoMensagemDto out = new IdendificacaoMensagemDto();
        out.setIdentificacao(uuid);
        return out;
    }
}

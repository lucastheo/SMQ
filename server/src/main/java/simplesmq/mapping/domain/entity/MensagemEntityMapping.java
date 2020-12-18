package simplesmq.mapping.domain.entity;

import simplesmq.domain.dto.MensagemDto;
import simplesmq.domain.entity.MensagemEntity;

import java.util.UUID;

public class MensagemEntityMapping {

    public static MensagemEntity mapFrom(UUID identificacao ,MensagemDto mensagemDto ){
        MensagemEntity mensagemEntity = new MensagemEntity();
        mensagemEntity.setIdentificacao(identificacao.toString());
        mensagemEntity.setMensagem(mensagemDto.getMensagem());
        mensagemEntity.setNomeFila(mensagemDto.getFila().getNome());
        mensagemEntity.setDataDaExpiracao(mensagemDto.getDataDaExpiração() == null ? null : mensagemDto.getDataDaExpiração().toString());
        return mensagemEntity;
    }
}

package simplesmq.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MensagemEntity {

    String mensagem;

    String nomeFila;

    String dataDaExpiracao;

    String identificacao;

    LocalDateTime tempoMaximoConsumo;

}

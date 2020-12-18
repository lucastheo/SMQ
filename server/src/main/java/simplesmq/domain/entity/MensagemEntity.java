package simplesmq.domain.entity;

import lombok.Data;

@Data
public class MensagemEntity {

    String mensagem;

    String nomeFila;

    String dataDaExpiracao;

    String identificacao;

}

package simplesmq.domain.entity;

import lombok.Data;

@Data
public class RelacaoEntity {
    String nome;

    String identificacaoMensagem;

    String identificacao;

    String nomeFila;
}

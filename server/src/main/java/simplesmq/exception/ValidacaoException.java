package simplesmq.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidacaoException extends Exception {
    String descricao;

    public ValidacaoException(String descricao){
        this.descricao = descricao;
    }

}

package simplesmq.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NaoEncontradoException extends Exception {
    String descricao;
    public NaoEncontradoException(String descricao){
        this.descricao = descricao;
    }

}
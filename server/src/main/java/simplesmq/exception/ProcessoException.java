package simplesmq.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessoException extends Exception {
    String descricao;
    public ProcessoException(String descricao){
        this.descricao = descricao;
    }

}

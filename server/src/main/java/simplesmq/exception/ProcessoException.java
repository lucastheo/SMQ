package simplesmq.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessoException extends Exception {
    String descricao;
    Exception exception;
    public ProcessoException( String descricao , Exception exception ){
        this.exception=exception;
        this.descricao = descricao;
    }

    @Override
    public void printStackTrace() {
        exception.printStackTrace();
    }
}

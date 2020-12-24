package simplesmq.mapping.domain.ro;

import simplesmq.domain.dro.ErrorEo;
import simplesmq.exception.NaoEncontradoException;
import simplesmq.exception.ProcessoException;
import simplesmq.exception.ValidacaoException;

public class ErrorEoMapping {

    public static ErrorEo mapFrom( ValidacaoException validacaoException ){
        ErrorEo errorEo = new ErrorEo();
        errorEo.setDescricao(validacaoException.getDescricao());
        return errorEo;
    }

    public static ErrorEo mapFrom(NaoEncontradoException naoencontradoException ){
        ErrorEo errorEo = new ErrorEo();
        errorEo.setDescricao(naoencontradoException.getDescricao());
        return errorEo;
    }

    public static ErrorEo mapFrom( ProcessoException validacaoException ){
        ErrorEo errorEo = new ErrorEo();
        errorEo.setDescricao(validacaoException.getDescricao());
        return errorEo;
    }
}

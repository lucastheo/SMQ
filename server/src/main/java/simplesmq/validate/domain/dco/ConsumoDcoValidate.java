package simplesmq.validate.domain.dco;

import simplesmq.domain.dco.ConsumoDco;
import simplesmq.exception.ValidacaoException;

public class ConsumoDcoValidate {

    public static void execute( ConsumoDco consumo ) throws ValidacaoException {
        if(consumo.getIdentificacaoMensagem() == null ){
            throw new ValidacaoException("Consumo precisa ter a identificação mesnagem");
        }
        if(consumo.getNomeGrupo() == null ){
            throw new ValidacaoException("Consumo precisa ter o nome do grupo");
        }
    }

}

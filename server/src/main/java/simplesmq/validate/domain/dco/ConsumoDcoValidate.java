package simplesmq.validate.domain.dco;

import simplesmq.domain.dco.ConsumoDco;
import simplesmq.exception.ValidacaoException;

public class ConsumoDcoValidate {

    public static void validate( ConsumoDco consumo ) throws ValidacaoException {
        if( consumo.getNomeFila() == null ){
            throw new ValidacaoException("Nome da fila não pode ser nulo");
        }
        if( consumo.getNomeGrupo() == null ){
            throw new ValidacaoException("Grupo de consumo não pode ser vazio");
        }
    }
}

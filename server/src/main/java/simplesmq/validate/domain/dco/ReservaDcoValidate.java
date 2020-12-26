package simplesmq.validate.domain.dco;

import simplesmq.domain.dco.ReservaDco;
import simplesmq.exception.ValidacaoException;

public class ReservaDcoValidate {

    public static void execute( ReservaDco reserve ) throws ValidacaoException {
        if( reserve.getNomeFila() == null ){
            throw new ValidacaoException("Para reservar o nome da fila não pode ser nulo");
        }
        if( reserve.getNomeGrupo() == null ){
            throw new ValidacaoException("Para reservar o Grupo de consumo não pode ser vazio");
        }
    }
}

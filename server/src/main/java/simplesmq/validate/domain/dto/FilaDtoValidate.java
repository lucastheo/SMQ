package simplesmq.validate.domain.dto;

import simplesmq.domain.dto.FilaDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.exception.ValidacaoException;

public class FilaDtoValidate {
    public static void execute(FilaDto fila ) throws ValidacaoException {
        if( fila.getNome() == null ){
            throw new ValidacaoException("Nome da fila é necessario");
        }
    }
}

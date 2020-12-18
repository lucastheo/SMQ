package simplesmq.validate.domain.dto;

import simplesmq.domain.dto.ElementoConsumoDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.exception.ValidacaoException;

public class MensagemDtoValidate {

    public static void execute(MensagemDto mensagem ) throws ValidacaoException {
        if( mensagem.getFila() == null ){
            throw new ValidacaoException("Fila não pode ser nula");
        }
        if( mensagem.getFila().getNome() == null ){
            throw new ValidacaoException("O nome da fila não pode ser nulo");
        }
        if( mensagem.getConsumidores() == null || mensagem.getConsumidores().size() < 1){
            throw new ValidacaoException("Precisa existir a lista de consumidores e ter pelo memenos um elemento");
        }
        for( ElementoConsumoDto elemento : mensagem.getConsumidores()){
            if( elemento.getNome() == null ){
                throw new ValidacaoException("O nome de um consumidor não pode ser nulo");
            }
        }
    }

}

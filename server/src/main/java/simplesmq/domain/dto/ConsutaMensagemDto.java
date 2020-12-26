package simplesmq.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ConsutaMensagemDto {

    String nomeNoGrupo;

    String identificacaoMensagem;

    String mensagem;
}

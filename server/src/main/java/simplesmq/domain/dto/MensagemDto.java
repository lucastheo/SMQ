package simplesmq.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MensagemDto {
    FilaDto fila;

    LocalDateTime dataDaExpiração;

    List<ElementoConsumoDto> consumidores;

    String mensagem;
}

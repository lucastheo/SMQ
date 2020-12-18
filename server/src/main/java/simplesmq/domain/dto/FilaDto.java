package simplesmq.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import simplesmq.domain.enuns.TipoFilaEnum;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FilaDto {
    String nome;

    TipoFilaEnum tipo;
}

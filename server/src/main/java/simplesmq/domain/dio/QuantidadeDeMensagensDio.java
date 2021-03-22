package simplesmq.domain.dio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.HashMap;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuantidadeDeMensagensDio{
    HashMap<String, HashMap<String,Integer>> paraConsumir;
    Integer emProcessamento;
}

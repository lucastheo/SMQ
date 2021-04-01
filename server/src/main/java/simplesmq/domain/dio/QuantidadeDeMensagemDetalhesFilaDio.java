package simplesmq.domain.dio;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuantidadeDeMensagemDetalhesFilaDio {
    List<QuantidadeDeMensagemDetalhesConsultosDio> detalhesConsumidores;

    String nome;

    Integer quantidade;

}

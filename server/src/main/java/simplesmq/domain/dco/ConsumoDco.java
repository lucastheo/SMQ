package simplesmq.domain.dco;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsumoDco {
    String nomeFila;

    String nomeGrupo;

    LocalDateTime dataDaExpiração;
}

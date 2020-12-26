package simplesmq.domain.dco;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservaDco {
    String nomeFila;

    String nomeGrupo;

    LocalDateTime dataDaExpiração;
}

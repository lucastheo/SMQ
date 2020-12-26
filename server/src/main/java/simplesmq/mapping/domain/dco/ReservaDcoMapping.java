package simplesmq.mapping.domain.dco;

import simplesmq.domain.dco.ReservaDco;

import java.time.LocalDateTime;

public class ReservaDcoMapping {

    public static ReservaDco mapFrpm(String nomeFila , String nomeGrupo , LocalDateTime dataDaExpiração ){
        ReservaDco consumo = new ReservaDco();
        consumo.setNomeFila(nomeFila);
        consumo.setNomeGrupo(nomeGrupo);
        consumo.setDataDaExpiração(dataDaExpiração);
        return consumo;
    }
}

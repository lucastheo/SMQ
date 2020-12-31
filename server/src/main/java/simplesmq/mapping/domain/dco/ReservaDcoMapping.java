package simplesmq.mapping.domain.dco;

import simplesmq.domain.dco.ReservaDco;

public class ReservaDcoMapping {

    public static ReservaDco mapFrpm(String nomeFila , String nomeGrupo , Long tempoConsumo ){
        ReservaDco consumo = new ReservaDco();
        consumo.setNomeFila(nomeFila);
        consumo.setNomeGrupo(nomeGrupo);
        consumo.setTempoConsumo(tempoConsumo);
        return consumo;
    }
}

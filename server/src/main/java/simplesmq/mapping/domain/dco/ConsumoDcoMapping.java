package simplesmq.mapping.domain.dco;

import simplesmq.domain.dco.ConsumoDco;

import java.time.LocalDateTime;

public class ConsumoDcoMapping {

    public static ConsumoDco mapFrpm(String nomeFila , String nomeGrupo , LocalDateTime dataDaExpiração ){
        ConsumoDco consumo = new ConsumoDco();
        consumo.setNomeFila(nomeFila);
        consumo.setNomeGrupo(nomeGrupo);
        consumo.setDataDaExpiração(dataDaExpiração);
        return consumo;
    }
}

package simplesmq.mapping.domain.dco;

import simplesmq.domain.dco.ConsumoDco;

public class ConsumoDcoMapping {

    public static ConsumoDco mapFrom( String identificacaoMensagem , String nomeGrupo ){
        ConsumoDco consumo = new ConsumoDco();
        consumo.setIdentificacaoMensagem(identificacaoMensagem);
        consumo.setNomeGrupo(nomeGrupo);
        return consumo;
    }
}

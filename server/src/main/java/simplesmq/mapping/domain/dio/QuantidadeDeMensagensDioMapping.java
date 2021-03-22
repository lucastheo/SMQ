package simplesmq.mapping.domain.dio;


import simplesmq.domain.dio.QuantidadeDeMensagensDio;

import java.util.HashMap;

public class QuantidadeDeMensagensDioMapping {
    public static QuantidadeDeMensagensDio mapFrom(HashMap<String, HashMap<String,Integer>> info ){
        QuantidadeDeMensagensDio quantidadeDeMensagensDio = new QuantidadeDeMensagensDio();
        quantidadeDeMensagensDio.setParaConsumir(info);
        return quantidadeDeMensagensDio;
    }

    public static QuantidadeDeMensagensDio mapFrom(HashMap<String, HashMap<String,Integer>> info , Integer emProcessamento ){
        QuantidadeDeMensagensDio quantidadeDeMensagensDio = new QuantidadeDeMensagensDio();
        quantidadeDeMensagensDio.setParaConsumir(info);
        quantidadeDeMensagensDio.setEmProcessamento(emProcessamento);
        return quantidadeDeMensagensDio;
    }
}

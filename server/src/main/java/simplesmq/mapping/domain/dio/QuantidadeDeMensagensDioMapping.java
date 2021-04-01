package simplesmq.mapping.domain.dio;


import simplesmq.domain.dio.QuantidadeDeMensagensDio;

import java.util.HashMap;
import java.util.LinkedList;

public class QuantidadeDeMensagensDioMapping {
    public static QuantidadeDeMensagensDio mapFrom(HashMap<String, HashMap<String,Integer>> info ){
        QuantidadeDeMensagensDio quantidadeDeMensagensDio = new QuantidadeDeMensagensDio();
        quantidadeDeMensagensDio.setFila( new LinkedList<>());
        for( String chave : info.keySet() ) {
            quantidadeDeMensagensDio.getFila().add(
                    QuantidadeDeMensagemDetalhesFilaDioMapping.mapFrom(info.get(chave), chave )
            );
        }
        quantidadeDeMensagensDio.setQuantidade(
                quantidadeDeMensagensDio.getFila().stream().mapToInt(m-> m.getQuantidade() ).sum()
        );
        return quantidadeDeMensagensDio;
    }

    public static QuantidadeDeMensagensDio mapFrom(HashMap<String, HashMap<String,Integer>> info , Integer emProcessamento ){
        QuantidadeDeMensagensDio quantidadeDeMensagensDio = mapFrom(info);
        quantidadeDeMensagensDio.setEmProcessamento(emProcessamento);
        return quantidadeDeMensagensDio;
    }
}

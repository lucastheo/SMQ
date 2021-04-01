package simplesmq.mapping.domain.dio;

import simplesmq.domain.dio.QuantidadeDeMensagemDetalhesConsultosDio;
import simplesmq.domain.dio.QuantidadeDeMensagemDetalhesFilaDio;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class QuantidadeDeMensagemDetalhesFilaDioMapping {
    public static QuantidadeDeMensagemDetalhesFilaDio mapFrom(HashMap<String,Integer> dados , String nome  ){
        QuantidadeDeMensagemDetalhesFilaDio quantidadeDeMensagemDetalhesFilaDio = new QuantidadeDeMensagemDetalhesFilaDio();
        quantidadeDeMensagemDetalhesFilaDio.setNome(nome);
        quantidadeDeMensagemDetalhesFilaDio.setDetalhesConsumidores(new LinkedList<>());
        for( String nomeConsumidor : dados.keySet() ){
            quantidadeDeMensagemDetalhesFilaDio.getDetalhesConsumidores().add(
                    QuantidadeDeMensagemDetalhesConsultosDioMapping.mapFrom(nomeConsumidor, dados.get(nomeConsumidor ) )
            );
        }

        Optional<QuantidadeDeMensagemDetalhesConsultosDio> quantidadeDeMensagemDetalhesConsultosDioOptional =
                quantidadeDeMensagemDetalhesFilaDio.getDetalhesConsumidores().stream().max(
                    new Comparator<QuantidadeDeMensagemDetalhesConsultosDio>() {
                        @Override
                        public int compare(QuantidadeDeMensagemDetalhesConsultosDio o1, QuantidadeDeMensagemDetalhesConsultosDio o2) {
                            if( o1.getQuantidade() > o2.getQuantidade() ) { return 1; }
                            if( o1.getQuantidade() < o2.getQuantidade() ) { return -1;}
                            return  0;
                        }
                    }
        );

        if( quantidadeDeMensagemDetalhesConsultosDioOptional.isPresent()) {
            quantidadeDeMensagemDetalhesFilaDio.setQuantidade(
                    quantidadeDeMensagemDetalhesConsultosDioOptional.get().getQuantidade());
        }
        else{
            quantidadeDeMensagemDetalhesFilaDio.setQuantidade(0);
        }
        return quantidadeDeMensagemDetalhesFilaDio;
    }
}

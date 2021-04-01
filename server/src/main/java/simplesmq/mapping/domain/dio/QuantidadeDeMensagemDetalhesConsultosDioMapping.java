package simplesmq.mapping.domain.dio;

import simplesmq.domain.dio.QuantidadeDeMensagemDetalhesConsultosDio;

public class QuantidadeDeMensagemDetalhesConsultosDioMapping {

    public static QuantidadeDeMensagemDetalhesConsultosDio mapFrom(String nome , Integer quantidade){
        QuantidadeDeMensagemDetalhesConsultosDio quantidadeDeMensagemDetalhesConsultosDio = new QuantidadeDeMensagemDetalhesConsultosDio();
        quantidadeDeMensagemDetalhesConsultosDio.setQuantidade(quantidade);
        quantidadeDeMensagemDetalhesConsultosDio.setNome(nome);
        return quantidadeDeMensagemDetalhesConsultosDio;
    }
}

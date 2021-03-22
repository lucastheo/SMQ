package simplesmq.service.metricas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dio.QuantidadeDeMensagensDio;
import simplesmq.mapping.domain.dio.QuantidadeDeMensagensDioMapping;
import simplesmq.service.relacao.RelacaoStatusService;

import java.util.HashMap;
import java.util.Optional;

@Component
public class QuantidadeMensagensService {

    @Autowired
    RelacaoStatusService relacaoStatusService;

    public QuantidadeDeMensagensDio consulta(Optional<String> fila ){
        HashMap<String, HashMap<String,Integer>> retorno = null;
        if( fila.isPresent()){
            retorno = relacaoStatusService.consultaQuantidadeMensagensParaProcessar(fila.get());
            return QuantidadeDeMensagensDioMapping.mapFrom(retorno);
        }else {
            retorno = relacaoStatusService.consultaQuantidadeMensagensParaProcessar();
            Integer emProcessamento = relacaoStatusService.consultaQuantidadeMensagensEmProcessamento();
            return QuantidadeDeMensagensDioMapping.mapFrom(retorno, emProcessamento);
        }
    }
}

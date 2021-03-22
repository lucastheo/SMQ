package simplesmq.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import simplesmq.domain.dio.QuantidadeDeMensagensDio;
import simplesmq.service.metricas.QuantidadeMensagensService;
import simplesmq.util.Logger;

import java.util.Optional;

@RestController
public class MetricasController {

    @Autowired
    QuantidadeMensagensService quantidadeMensagensService;

    @GetMapping("/metricas/quantidade_mensagem")
    public ResponseEntity quantidadeMensagens(@RequestParam("fila") Optional<String> optionalFila ){
        Logger.info("Inicio da consulta da quantidade de mensagens" , optionalFila  );
        QuantidadeDeMensagensDio quantidadeDeMensagensDio = null;
        try {
            quantidadeDeMensagensDio = quantidadeMensagensService.consulta(optionalFila);
        }catch( Exception e ){
            Logger.erro("Erro em consultar a quantidade de mensagens processadas" , optionalFila , e );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok( quantidadeDeMensagensDio);
    }

}

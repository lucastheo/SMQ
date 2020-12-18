package simplesmq.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import simplesmq.domain.dto.IdendificacaoMensagemDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.exception.ProcessoException;
import simplesmq.exception.ValidacaoException;
import simplesmq.mapping.domain.ro.ErrorEoMapping;
import simplesmq.service.MensagemCriacaoService;
import simplesmq.service.RelacaoCriacaoService;
import simplesmq.validate.domain.dto.MensagemDtoValidate;

import java.util.UUID;

@Controller
public class MensagemController {

    @Autowired
    MensagemCriacaoService mensagemCriacaoService;

    @Autowired
    RelacaoCriacaoService relacaoCriacaoService;

    @PostMapping("/mensagem")
    public ResponseEntity getMostCited(@RequestBody MensagemDto mensagem){

        try {
            MensagemDtoValidate.execute(mensagem);

        }catch( ValidacaoException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEoMapping.mapFrom(ex));
        }
        UUID identificacao;
        try {
             identificacao= mensagemCriacaoService.geraIdentificacao(mensagem);
        } catch (ProcessoException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
        }
        try{
            mensagemCriacaoService.execute(identificacao, mensagem);
        }catch (ProcessoException ex){
            mensagemCriacaoService.reverter(identificacao);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
        }

        try {
            relacaoCriacaoService.execute(identificacao,mensagem);
        } catch (ProcessoException ex) {
            mensagemCriacaoService.reverter(identificacao);
            try {
                mensagemCriacaoService.reverter(identificacao,mensagem);
            } catch (ProcessoException processoException) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
            }
        }

        IdendificacaoMensagemDto response = mensagemCriacaoService.response(identificacao);
        return ResponseEntity.status(HttpStatus.OK).body( response );
    }
}

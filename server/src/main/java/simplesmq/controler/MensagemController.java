package simplesmq.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simplesmq.domain.dco.ConsumoDco;
import simplesmq.domain.dco.ReservaDco;
import simplesmq.domain.dto.ConsutaMensagemDto;
import simplesmq.domain.dto.IdendificacaoMensagemDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.exception.NaoEncontradoException;
import simplesmq.exception.ProcessoException;
import simplesmq.exception.ValidacaoException;
import simplesmq.mapping.domain.dco.ConsumoDcoMapping;
import simplesmq.mapping.domain.dco.ReservaDcoMapping;
import simplesmq.mapping.domain.ro.ErrorEoMapping;
import simplesmq.service.ConsumoService;
import simplesmq.service.MensagemCriacaoService;
import simplesmq.service.RelacaoCriacaoService;
import simplesmq.service.ReservaService;
import simplesmq.util.Logger;
import simplesmq.validate.domain.dco.ConsumoDcoValidate;
import simplesmq.validate.domain.dco.ReservaDcoValidate;
import simplesmq.validate.domain.dto.MensagemDtoValidate;

import java.util.Optional;
import java.util.UUID;

@RestController
public class MensagemController {

    @Autowired
    MensagemCriacaoService mensagemCriacaoService;
    @Autowired
    RelacaoCriacaoService relacaoCriacaoService;
    @Autowired
    ReservaService reservaService;
    @Autowired
    ConsumoService consumoService;

    @PostMapping("/mensagem")
    public ResponseEntity getMostCited(@RequestBody MensagemDto mensagem){
        Logger.info("Gernado novo registro" , mensagem );
        try {
            MensagemDtoValidate.execute(mensagem);
        }catch( ValidacaoException ex){
            Logger.warn("Erro de validação na geração do novo registro" , mensagem , ex );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEoMapping.mapFrom(ex));
        }
        UUID identificacao;
        try {
             identificacao= mensagemCriacaoService.geraIdentificacao(mensagem);
        } catch (ProcessoException ex) {
            Logger.erro("Erro em gerar nova identificação de mensagem" , mensagem , ex );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
        }
        try{
            mensagemCriacaoService.execute(identificacao, mensagem);
        }catch (ProcessoException ex){
            Logger.erro("Erro em criar nova mensagem" , mensagem , ex );
            mensagemCriacaoService.reverter(identificacao);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
        }

        try {
            relacaoCriacaoService.execute(identificacao,mensagem);
        } catch (ProcessoException ex) {
            Logger.erro("Erro em criar nova identificação" , mensagem , ex );
            mensagemCriacaoService.reverter(identificacao);
            try {
                mensagemCriacaoService.reverter(identificacao,mensagem);
            } catch (ProcessoException processoException) {
                Logger.erro("Erro em reverter o processo da nova identificação" , mensagem , ex );
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
            }
        }
        IdendificacaoMensagemDto response = mensagemCriacaoService.response(identificacao);
        return ResponseEntity.status(HttpStatus.OK).body( response );
    }

    @GetMapping("/mensagem")
    public ResponseEntity getMostCited(@RequestParam("fila") String nomeFila , @RequestParam("grupo") String nomeGrupo , @RequestHeader("data-expiracao") Optional<Long> optionalTempoConsumo  ){
        ReservaDco reserve = ReservaDcoMapping.mapFrpm(nomeFila,nomeGrupo,optionalTempoConsumo.orElse(5L));
        Logger.info("Inicio da reserva da mensagem" , reserve  );
        try{
            ReservaDcoValidate.execute(reserve);
        } catch (ValidacaoException ex) {
            Logger.warn("Erro de validação na reserva da mensagem" , reserve , ex );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEoMapping.mapFrom(ex));
        }

        ConsutaMensagemDto consultaMensagemDto = null;
        try {
            consultaMensagemDto = reservaService.execute(reserve) ;
        } catch (NaoEncontradoException ex) {
            Logger.info("Não encontrou mensagem para ser consumida" , reserve  );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ProcessoException ex) {
            Logger.erro("Erro em consumir mensagem" , reserve , ex );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorEoMapping.mapFrom(ex));
        }

        return ResponseEntity.status(HttpStatus.OK).body( consultaMensagemDto );
    }

    @PostMapping("/mensagem/{identificacao_mensagem}/consumidor/{nome_consumidor}")
    public ResponseEntity getMostCited(@PathVariable("identificacao_mensagem") String identificacaoMensagem, @PathVariable("nome_consumidor") String nomeGrupo){
        ConsumoDco consumo = ConsumoDcoMapping.mapFrom(identificacaoMensagem,nomeGrupo);
        Logger.info("Inicio da confirmação de consumo da mensagem" , consumo  );
        try{
            ConsumoDcoValidate.execute(consumo);
        } catch (ValidacaoException ex) {
            Logger.warn("Erro de validação em confirmação de consumo da mensagem" , consumo , ex  );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEoMapping.mapFrom(ex));
        }
        try {
            consumoService.execute(consumo);
        } catch (NaoEncontradoException ex) {
            Logger.erro("Erro ao tentar consumor a menagem" , consumo , ex );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorEoMapping.mapFrom(ex));
        }

        return ResponseEntity.status(HttpStatus.OK).build();

    }
}

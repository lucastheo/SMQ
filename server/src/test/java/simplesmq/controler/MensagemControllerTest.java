package simplesmq.controler;


import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestBody;
import simplesmq.domain.dto.ElementoConsumoDto;
import simplesmq.domain.dto.FilaDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.exception.NaoEncontradoException;
import simplesmq.exception.ProcessoException;
import simplesmq.exception.ValidacaoException;
import simplesmq.service.ConsumoService;
import simplesmq.service.MensagemCriacaoService;
import simplesmq.service.RelacaoCriacaoService;
import simplesmq.service.ReservaService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
class MensagemControllerTest {

    @InjectMocks
    public MensagemController mensagemController;
    @Mock
    MensagemCriacaoService mensagemCriacaoService;
    @Mock
    RelacaoCriacaoService relacaoCriacaoService;
    @Mock
    ReservaService reservaService;
    @Mock
    ConsumoService consumoService;

    public void setup() {
        MockitoAnnotations.openMocks(this);;
    }
    @Test
    void criandoMensagemSucesso() {
        setup();
        MensagemDto mensagem = geraMensagem();
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void criandoMensagemErroValidação() throws Exception {
        setup();
        MensagemDto mensagem = geraMensagem();
        mensagem.setFila(null);
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void criandoMensagemErroEmGeralIdentificacao() throws Exception {
        setup();
        when(mensagemCriacaoService.geraIdentificacao(any())).thenThrow(new ProcessoException("Erro teste" , null));
        MensagemDto mensagem = geraMensagem();
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void criandoMensagemErroEmGeralIdentificacao2() throws ProcessoException {
        setup();
        doThrow(new ProcessoException("Erro teste" , null)).when(mensagemCriacaoService).execute(any(),any());
        MensagemDto mensagem = geraMensagem();
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void criandoMensagemErroEmRelacaoCriacaoService() throws ProcessoException {
        setup();
        doThrow(new ProcessoException("Erro teste" , null)).when(relacaoCriacaoService).execute(any(),any());
        MensagemDto mensagem = geraMensagem();
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void criandoMensagemErroNoRevertEmRelacaoCriacaoServiceEMensagemCriacaoService() throws ProcessoException {
        setup();
        doThrow(new ProcessoException("Erro teste" , null)).when(relacaoCriacaoService).execute(any(),any());
        doThrow(new ProcessoException("Erro teste" , null)).when(mensagemCriacaoService).reverter(any(),any());
        MensagemDto mensagem = geraMensagem();
        ResponseEntity response = mensagemController.criandoMensagem(mensagem);
        assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void reservandoMensagemSucesso(){
        setup();
        ResponseEntity response = mensagemController.reservandoMensagem("teste-fila" , "teste-grupo" , Optional.empty());
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void reservandoMensagemErroValidacao(){
        setup();
        ResponseEntity response = mensagemController.reservandoMensagem(null, "teste-grupo" , Optional.empty());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }
    @Test
    void reservandoMensagemErroValidacao2(){
        setup();
        ResponseEntity response = mensagemController.reservandoMensagem("teste-fila", null , Optional.empty());
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void reservandoMensagemNaoEncontrado() throws ProcessoException, NaoEncontradoException {
        setup();
        doThrow(new NaoEncontradoException("Erro teste" )).when(reservaService).execute(any());
        ResponseEntity response = mensagemController.reservandoMensagem("teste-fila", "teste-grupo" , Optional.empty());
        assertEquals(response.getStatusCode(),HttpStatus.NO_CONTENT);
    }

    @Test
    void reservandoMensagemErroNoProcesso() throws ProcessoException, NaoEncontradoException {
        setup();
        doThrow(new ProcessoException("Erro teste" , null )).when(reservaService).execute(any());
        ResponseEntity response = mensagemController.reservandoMensagem("teste-fila", "teste-grupo" , Optional.empty());
        assertEquals(response.getStatusCode(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void confirmaConsumoOk(){
        setup();
        ResponseEntity response = mensagemController.confirmaConsumo(UUID.randomUUID().toString() , "teste-consumo");
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }

    @Test
    void confirmaConsumoErroValidacao1(){
        setup();
        ResponseEntity response = mensagemController.confirmaConsumo( null, "teste-consumo");
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void confirmaConsumoErroValidacao2(){
        setup();
        ResponseEntity response = mensagemController.confirmaConsumo(UUID.randomUUID().toString() , null);
        assertEquals(response.getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    void confirmaConsumoNaoEncontrado() throws NaoEncontradoException {
        setup();
        doThrow(new NaoEncontradoException("Teste nao encontrado")).when(consumoService).execute(any());
        ResponseEntity response = mensagemController.confirmaConsumo(UUID.randomUUID().toString() , "teste-consumo");
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
    }

    public MensagemDto geraMensagem(){
        MensagemDto mensagem = new MensagemDto();
        mensagem.setMensagem("Teste");
        mensagem.setConsumidores(new LinkedList<>());
        mensagem.getConsumidores().add(new ElementoConsumoDto());
        mensagem.getConsumidores().get(0).setNome("Teste");
        mensagem.setDataExpiração(LocalDateTime.now());
        mensagem.setFila(new FilaDto());
        mensagem.getFila().setNome("Teste");
        return mensagem;
    }
}
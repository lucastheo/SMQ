package simplesmq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.domain.dto.IdendificacaoMensagemDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.exception.ProcessoException;
import simplesmq.mapping.domain.dto.IdendificacaoMensagemDtoMapping;
import simplesmq.mapping.domain.entity.MensagemEntityMapping;
import simplesmq.service.mensagem.MensagemIdentidificacaoService;
import simplesmq.service.mensagem.MensagemPersistenciaService;
import simplesmq.util.Logger;

import java.io.IOException;
import java.util.UUID;

@Component
public class MensagemCriacaoService {

    @Autowired
    MensagemIdentidificacaoService mensagemIdentidificacaoService;

    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;

    public UUID geraIdentificacao( MensagemDto mensagem ) throws ProcessoException {
        UUID identificacao = null;
        try {
            identificacao = mensagemIdentidificacaoService.geraNova();
        } catch (InterruptedException e) {
            this.reverter(identificacao);
            throw new ProcessoException("Erro durante adição da mensagem" , e);
        }
        return identificacao;
    }

    public void execute( UUID identificacao , MensagemDto mensagem  ) throws ProcessoException {
        MensagemEntity mensagemEntity = MensagemEntityMapping.mapFrom(identificacao,mensagem);
        try {
            mensagemPersistenciaService.persiste(mensagemEntity);
        }
        catch (JsonProcessingException e ){ throw new ProcessoException("Erro em converter a mensagem para Json" , e); }
        catch (IOException e) {
            this.reverter(identificacao);
            this.reverterDisco(mensagemEntity);
            throw new ProcessoException("Erro em salvar o disco" , e);
        }

        try{
                mensagemPersistenciaService.persisteCache(mensagemEntity);
        } catch (InterruptedException e) {
            this.reverter(identificacao);
            this.reverterDisco(mensagemEntity);
            try {
                this.reverterCache(mensagemEntity);
            } catch (InterruptedException interruptedException) {
                throw new ProcessoException("Erro critico em reverter cache" , e);
            }
            throw new ProcessoException("Erro em salvar a mensagem na cache" , e);
        }
    }

    public void reverter( UUID identificacao, MensagemDto mensagemDto  ) throws ProcessoException {
        MensagemEntity mensagemEntity = MensagemEntityMapping.mapFrom(identificacao,mensagemDto);
        this.reverterDisco(mensagemEntity);
        try {
            this.reverterCache(mensagemEntity);
        } catch (InterruptedException e) {
            throw new ProcessoException("Erro critico em reverter a cache de mensagens" ,e );
        }
    }
    public void reverter( UUID uuid ){
        if( uuid != null){
            try {
                mensagemIdentidificacaoService.remove(uuid);
            } catch (InterruptedException e) {
                Logger.erro("Erro em remover a identificação das mesnagens" , uuid );
            }
        }
    }

    private void reverterDisco( MensagemEntity mensagemEntity ){
        mensagemPersistenciaService.remove(mensagemEntity);
    }

    private void reverterCache( MensagemEntity mensagemEntity ) throws InterruptedException {
        mensagemPersistenciaService.removeCache(mensagemEntity);
    }

    public IdendificacaoMensagemDto response (UUID identificacao ){
        return IdendificacaoMensagemDtoMapping.mapFrom(identificacao);
    }
}

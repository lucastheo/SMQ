package simplesmq.service;

import org.springframework.stereotype.Component;
import simplesmq.configuration.LocalDeArquivosConfiguration;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.service.configuracaoservice.FilaConfiguracaoService;
import simplesmq.service.mensagem.MensagemIdentidificacaoService;
import simplesmq.service.mensagem.MensagemPersistenciaService;
import simplesmq.service.relacao.RelacaoPersistenciaService;
import simplesmq.service.relacao.RelacaoStatusService;
import simplesmq.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class CarregaDadosService {
    MensagemIdentidificacaoService mensagemIdentidificacaoService;
    MensagemPersistenciaService mensagemPersistenciaService;
    RelacaoPersistenciaService relacaoPersistenciaService;
    RelacaoStatusService relacaoStatusService;
    FilaConfiguracaoService filaConfiguracaoService;

    public CarregaDadosService(MensagemIdentidificacaoService mensagemIdentidificacaoService,
                               MensagemPersistenciaService mensagemPersistenciaService,
                               RelacaoPersistenciaService relacaoPersistenciaService,
                               RelacaoStatusService relacaoStatusService,
                               FilaConfiguracaoService filaConfiguracaoService){
        this.mensagemIdentidificacaoService = mensagemIdentidificacaoService;
        this.mensagemPersistenciaService = mensagemPersistenciaService;
        this.relacaoPersistenciaService = relacaoPersistenciaService;
        this.relacaoStatusService = relacaoStatusService;
        this.filaConfiguracaoService = filaConfiguracaoService;
        this.execute();
    }


    public void execute() {
        File mensagens = new File(LocalDeArquivosConfiguration.MENSAGEM);
        if(!mensagens.exists()){
            mensagens.mkdirs();
        }
        if( !mensagens.isDirectory() ){
            Logger.erro("Caminho das mensagens precisa ser uma pasta");
        }
        File relacoes = new File(LocalDeArquivosConfiguration.RELACAO);
        if(!relacoes.exists()){
            relacoes.mkdirs();
        }
        if( !relacoes.isDirectory() ){
            Logger.erro("Caminho das relações precisa ser uma pasta");
        }


        MensagemEntity mensagemEntity = null;
        Set<String> mensagensExistentes = new LinkedHashSet<>();
        for(File mensagemFilha : mensagens.listFiles()){
            try {
                 mensagemEntity = mensagemPersistenciaService.buscaDisco(mensagemFilha.getName());
                 mensagensExistentes.add(mensagemEntity.getIdentificacao());
            } catch (IOException e) {
                Logger.warn("Erro ao buscar as mensagem já existentes no disco");
            }
        }

        RelacaoEntity relacaoEntity;
        Set<String> mensagensExistentesNasRelacoes = new LinkedHashSet<>();
        for(File relacaoFilha : relacoes.listFiles()){
            try {
                relacaoEntity = relacaoPersistenciaService.buscaDisco(relacaoFilha.getName());
                if( mensagensExistentes.contains(relacaoEntity.getIdentificacaoMensagem())) {
                    mensagemEntity = mensagemPersistenciaService.buscaDisco(relacaoEntity.getIdentificacaoMensagem());
                    relacaoStatusService.addFilaConsumidorRelacao(relacaoEntity,mensagemEntity);
                    mensagensExistentesNasRelacoes.add(relacaoEntity.getIdentificacaoMensagem());
                }else{
                    Logger.warn( "Não foi encontrada a mensagem dentro das mensagens existentes, ou seja existe uma relação que aponta para uma mensagem que não existe");
                }
            } catch (IOException e) {
                Logger.erro("Erro em buscar do disco a relação ou a mensagem já existente" , e );
            }
        }
        for(File mensagemFilha : mensagens.listFiles()){
            try {
                mensagemEntity = mensagemPersistenciaService.buscaDisco(mensagemFilha.getName());
                if(mensagensExistentesNasRelacoes.contains(mensagemEntity.getIdentificacao())) {
                    mensagemIdentidificacaoService.add(UUID.fromString(mensagemEntity.getIdentificacao()));
                }
            } catch (IOException e ) {
                Logger.erro("Erro em buscar do disco a mensagem já existente");
            }catch ( InterruptedException e){
                Logger.erro("Erro em carregar a mensagem na estrutura de dados");
            }
        }




    }

}

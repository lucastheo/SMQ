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
        if( !mensagens.isDirectory() ){
            System.err.println("Caminho das mensagens precisa ser uma pasta");
        }
        File relacoes = new File(LocalDeArquivosConfiguration.RELACAO);
        if( !relacoes.isDirectory() ){
            System.err.println("Caminho das relações precisa ser uma pasta");
        }


        MensagemEntity mensagemEntity = null;
        Set<String> mensagensExistentes = new LinkedHashSet<>();
        for(File mensagemFilha : mensagens.listFiles()){
            try {
                 mensagemEntity = mensagemPersistenciaService.buscaDisco(mensagemFilha.getName());
                 mensagensExistentes.add(mensagemEntity.getIdentificacao());
            } catch (IOException e) {
                e.printStackTrace();
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
                    System.err.println("Não foi encontrada a mensagem");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(File mensagemFilha : mensagens.listFiles()){
            try {
                mensagemEntity = mensagemPersistenciaService.buscaDisco(mensagemFilha.getName());
                if(mensagensExistentesNasRelacoes.contains(mensagemEntity.getIdentificacao())) {
                    mensagemIdentidificacaoService.add(UUID.fromString(mensagemEntity.getIdentificacao()));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }




    }

}

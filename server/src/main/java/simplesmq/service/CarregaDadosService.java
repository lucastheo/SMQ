package simplesmq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.LocalDeArquivosConfiguration;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.exception.ProcessoException;
import simplesmq.repository.mensagem.MensagemIdentidificacaoRepository;
import simplesmq.repository.mensagem.MensagemPersistenciaCacheRepository;
import simplesmq.repository.mensagem.MensagemPersistenciaDiscoRepository;
import simplesmq.repository.relacao.RelacaoPersistenciaDiscoRepository;
import simplesmq.repository.relacao.RelacaoStatusRepository;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class CarregaDadosService {
    MensagemIdentidificacaoRepository mensagemIdentidificacaoRepository;
    MensagemPersistenciaDiscoRepository mensagemPersistenciaDiscoRepository;
    RelacaoPersistenciaDiscoRepository relacaoPersistenciaDiscoRepository;
    RelacaoStatusRepository relacaoStatusRepository;

    public CarregaDadosService(MensagemIdentidificacaoRepository mensagemIdentidificacaoRepository,
                               MensagemPersistenciaDiscoRepository mensagemPersistenciaDiscoRepository,
                               RelacaoPersistenciaDiscoRepository relacaoPersistenciaDiscoRepository,
                               RelacaoStatusRepository relacaoStatusRepository){
        this.mensagemIdentidificacaoRepository = mensagemIdentidificacaoRepository;
        this.mensagemPersistenciaDiscoRepository = mensagemPersistenciaDiscoRepository;
        this.relacaoPersistenciaDiscoRepository = relacaoPersistenciaDiscoRepository;
        this.relacaoStatusRepository = relacaoStatusRepository;
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
                 mensagemEntity = mensagemPersistenciaDiscoRepository.ler(mensagemFilha.getName());
                 mensagensExistentes.add(mensagemEntity.getIdentificacao());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RelacaoEntity relacaoEntity;
        Set<String> mensagensExistentesNasRelacoes = new LinkedHashSet<>();
        for(File relacaoFilha : relacoes.listFiles()){
            try {
                relacaoEntity = relacaoPersistenciaDiscoRepository.ler(relacaoFilha.getName());
                if( mensagensExistentes.contains(relacaoEntity.getIdentificacaoMensagem())) {
                    mensagemEntity = mensagemPersistenciaDiscoRepository.ler(relacaoEntity.getIdentificacaoMensagem());
                    relacaoStatusRepository.addFila(mensagemEntity.getNomeFila());
                    relacaoStatusRepository.addConsumidores(mensagemEntity.getNomeFila(), relacaoEntity.getNome());
                    relacaoStatusRepository.addRelacao(mensagemEntity.getNomeFila(), relacaoEntity.getNome(), relacaoEntity);
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
                mensagemEntity = mensagemPersistenciaDiscoRepository.ler(mensagemFilha.getName());
                if(mensagensExistentesNasRelacoes.contains(mensagemEntity.getIdentificacao())) {
                    mensagemIdentidificacaoRepository.add(UUID.fromString(mensagemEntity.getIdentificacao()));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }




    }

}

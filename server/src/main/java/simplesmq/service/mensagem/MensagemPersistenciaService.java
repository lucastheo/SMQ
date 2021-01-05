package simplesmq.service.mensagem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.CacheConfiguration;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.repository.mensagem.MensagemPersistenciaCacheRepository;
import simplesmq.repository.mensagem.MensagemPersistenciaDiscoRepository;
import simplesmq.service.configuracaoservice.FilaConfiguracaoService;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class MensagemPersistenciaService {

    @Autowired
    FilaConfiguracaoService filaConfiguracaoService;
    @Autowired
    CacheConfiguration cacheConfiguration;
    @Autowired
    MensagemPersistenciaDiscoRepository mensagemPersistenciaDiscoRepository;
    @Autowired
    MensagemPersistenciaCacheRepository mensagemPersistenciaCacheRepository;

    public void persiste(MensagemEntity mensagemEntity ) throws IOException {
        if( filaConfiguracaoService.persistenciaEmDisco(mensagemEntity.getNomeFila()) ){
            mensagemPersistenciaDiscoRepository.salvar(mensagemEntity);
        }
    }
    public void remove( MensagemEntity mensagemEntity ){
        mensagemPersistenciaDiscoRepository.remover( mensagemEntity);
    }

    public void persisteCache( MensagemEntity mensagemEntity ) throws InterruptedException {
        Boolean var0 = filaConfiguracaoService.persistenciaEmCache(mensagemEntity.getNomeFila());
        Boolean var1 = mensagemPersistenciaCacheRepository.quantidadeElemento() < cacheConfiguration.getSizeCacheMensagem();
        if( var0 && var1){
            mensagemPersistenciaCacheRepository.salvar(mensagemEntity);
        }
    }
    public void removeCache( MensagemEntity mensagemEntity ) throws InterruptedException {
        mensagemPersistenciaCacheRepository.remover(mensagemEntity);
    }

    public Optional<MensagemEntity> buscaCache(String identificacao){
        return mensagemPersistenciaCacheRepository.busca(identificacao);
    }
    public  MensagemEntity buscaDisco(String identificacao) throws IOException {
        return mensagemPersistenciaDiscoRepository.ler(identificacao);
    }

    public Set<String> mensagensEmCache(String nomeFila) {
        return new HashSet<String>(mensagemPersistenciaCacheRepository.mensagensEmCache(nomeFila));
    }
}

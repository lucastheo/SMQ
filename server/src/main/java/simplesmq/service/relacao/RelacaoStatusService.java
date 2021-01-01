package simplesmq.service.relacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.CacheConfiguration;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.repository.relacao.RelacaoStatusRepository;
import simplesmq.service.mensagem.MensagemPersistenciaService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class RelacaoStatusService {
    @Autowired
    RelacaoStatusRepository relacaoStatusRepository;
    @Autowired
    MensagemPersistenciaService mensagemPersistenciaService;
    @Autowired
    CacheConfiguration cacheConfiguration;

    public void adicionar(String nomeFila , List<RelacaoEntity> relacoes ){
        relacaoStatusRepository.addFila(nomeFila);

        for( RelacaoEntity relacaoEntity : relacoes ) {
            relacaoStatusRepository.addConsumidores(nomeFila,relacaoEntity.getNome());
            relacaoStatusRepository.addRelacao(nomeFila, relacaoEntity.getNome(),relacaoEntity);
        }
    }

    public Optional<String> limpaRelacaoZerada(){
        return relacaoStatusRepository.limpaRelacaoZerada();
    }

    public  Optional<RelacaoEntity>  reserva( String nomeFila , String nomeGrupo ) {
        Set<String> mesnagemCache = mensagemPersistenciaService.mensagensEmCache(nomeFila);
        Optional<RelacaoEntity> optionalRelacaoEntity;

        if (mesnagemCache.isEmpty() || cacheConfiguration.getTamanhoMaximoDaBuscaPelaCache() < relacaoStatusRepository.quantidadeDeElementosParaProcessar(nomeFila, nomeGrupo)) {
            optionalRelacaoEntity = relacaoStatusRepository.reserve(nomeFila, nomeGrupo);
        } else {
            optionalRelacaoEntity = relacaoStatusRepository.reserve(nomeFila, nomeGrupo, mesnagemCache);
        }
        return optionalRelacaoEntity;
    }

    public void voltaParaNovo(RelacaoEntity relacaoEntity) {
        relacaoStatusRepository.voltaParaNovo(relacaoEntity);
    }

    public Optional<RelacaoEntity> finaliza(String nomeGrupo, String identificacaoMensagem) {
        return relacaoStatusRepository.finaliza(nomeGrupo,identificacaoMensagem);
    }

    public void removeTodasOcorrencias(MensagemEntity mensagemEntity) {
        relacaoStatusRepository.removeTodasOcorrencias(mensagemEntity);
    }

    public Optional<RelacaoEntity> limpaFinalizado() {
        return relacaoStatusRepository.limpaFinalizado();
    }


    public void addFilaConsumidorRelacao(RelacaoEntity relacaoEntity,MensagemEntity mensagemEntity ) {
        relacaoStatusRepository.addFila(mensagemEntity.getNomeFila());
        relacaoStatusRepository.addConsumidores(mensagemEntity.getNomeFila(), relacaoEntity.getNome());
        relacaoStatusRepository.addRelacao(mensagemEntity.getNomeFila(), relacaoEntity.getNome(), relacaoEntity);
    }
}


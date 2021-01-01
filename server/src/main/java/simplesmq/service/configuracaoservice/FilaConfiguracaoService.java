package simplesmq.service.configuracaoservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.FilaGrupoConfiguration;
import simplesmq.domain.enuns.TipoFilaEnum;
import simplesmq.repository.FilaConfiguracaoRepository;

import java.util.Optional;

@Component
public class FilaConfiguracaoService {

    @Autowired
    FilaConfiguracaoRepository filaConfiguracaoRepository;

    @Autowired
    FilaGrupoConfiguration filaConfiguracao;

    public Boolean persistenciaEmDisco(String nomeFila){
        TipoFilaEnum tipoFila = this.getTipoFilaEnum(nomeFila);
        return filaConfiguracao.persistenciaEmDisco(tipoFila);
    }

    public Boolean persistenciaEmCache( String nomeFila ){
        TipoFilaEnum tipoFila = this.getTipoFilaEnum(nomeFila);
        return filaConfiguracao.persistenciaEmCache(tipoFila);
    }

    private TipoFilaEnum getTipoFilaEnum(String nomeFila ){
        Optional<TipoFilaEnum> optionalTipoFilaEnum = filaConfiguracaoRepository.getTipoFilaEnum(nomeFila);
        if( optionalTipoFilaEnum.isEmpty()){
            return TipoFilaEnum.PADRAO;
        }
        return optionalTipoFilaEnum.get();
    }

}

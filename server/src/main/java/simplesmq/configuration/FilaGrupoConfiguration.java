package simplesmq.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;
import simplesmq.domain.enuns.TipoFilaEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@Component
public class FilaGrupoConfiguration {
    final private Set<TipoFilaEnum> grupoPersistenciaEmDisco = geradorGrupoiPersistenciaEmDisco();
    final private Set<TipoFilaEnum> grupoPersistenciaEmCache = geradorGrupoiPersistenciaEmCache();

    public Boolean persistenciaEmDisco( TipoFilaEnum tipoFila ){
        return grupoPersistenciaEmDisco.contains(tipoFila);
    }

    public Boolean persistenciaEmCache( TipoFilaEnum tipoFila ){
        return grupoPersistenciaEmCache.contains(tipoFila);
    }

    private Set<TipoFilaEnum> geradorGrupoiPersistenciaEmDisco(){
        Set<TipoFilaEnum> var = new HashSet();
        var.add(TipoFilaEnum.PADRAO);
        var.add(TipoFilaEnum.EM_DISCO);
        return var;
    }

    private Set<TipoFilaEnum> geradorGrupoiPersistenciaEmCache(){
        Set<TipoFilaEnum> var = new HashSet();
        var.add(TipoFilaEnum.PADRAO);
        var.add(TipoFilaEnum.EM_CACHE);
        return var;
    }


}

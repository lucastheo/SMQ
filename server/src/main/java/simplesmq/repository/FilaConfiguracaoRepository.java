package simplesmq.repository;

import org.springframework.stereotype.Component;
import simplesmq.domain.enuns.StatusElementoEmAgrupamentoEnum;
import simplesmq.domain.enuns.TipoFilaEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class FilaConfiguracaoRepository {

    public final Map<String, TipoFilaEnum> tipoFilaEnumHashMap = new HashMap<>();

    public StatusElementoEmAgrupamentoEnum add(String nome , TipoFilaEnum tipoFila){
        if( tipoFilaEnumHashMap.containsKey(nome) ){
            return StatusElementoEmAgrupamentoEnum.ENCONTADO;
        }
        tipoFilaEnumHashMap.put(nome, tipoFila);
        return StatusElementoEmAgrupamentoEnum.ADICIONADO;
    }

    public Optional<TipoFilaEnum> getTipoFilaEnum(String nome){
        if( tipoFilaEnumHashMap.containsKey(nome) ){
            return Optional.of(tipoFilaEnumHashMap.get(nome));
        }
        return Optional.empty();
    }

}

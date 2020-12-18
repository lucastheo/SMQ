package simplesmq.repository.relacao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.LocalDeArquivosConfiguration;
import simplesmq.domain.entity.RelacaoEntity;
import simplesmq.domain.enuns.StatusArquivoEnum;
import simplesmq.util.FileUtils;

import java.io.IOException;

@Component
public class RelacaoPersistenciaDiscoRepository {
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void salvar( RelacaoEntity relacaoEntity ) throws IOException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = null;
        json = ow.writeValueAsString(relacaoEntity);
        String caminho = getCaminhoBase(relacaoEntity);
        FileUtils.salvar( caminho, json );
    }

    public StatusArquivoEnum remover(RelacaoEntity relacaoEntity ){
        String caminho = getCaminhoBase(relacaoEntity);
        return FileUtils.remover(caminho);
    }

    private String getCaminhoBase(RelacaoEntity relacaoEntity){
        return LocalDeArquivosConfiguration.RELACAO+relacaoEntity.getIdentificacao();
    }
}

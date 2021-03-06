package simplesmq.repository.mensagem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import simplesmq.configuration.LocalDeArquivosConfiguration;
import simplesmq.domain.entity.MensagemEntity;
import simplesmq.domain.enuns.StatusArquivoEnum;
import simplesmq.util.FileUtils;

import java.io.IOException;

@Component
public class MensagemPersistenciaDiscoRepository {

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void salvar( MensagemEntity mensagemEntity ) throws IOException {
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = null;
        json = ow.writeValueAsString(mensagemEntity);
        String caminho = getCaminhoBase(mensagemEntity);
        FileUtils.salvar( caminho, json );
    }

    public StatusArquivoEnum remover( MensagemEntity mensagemEntity ){
        String caminho = getCaminhoBase(mensagemEntity);
        return FileUtils.remover(caminho);
    }

    public MensagemEntity ler( String identificacao ) throws IOException {
        String caminho = getCaminhoBase(identificacao);
        String jsonString = FileUtils.ler(caminho);
        return objectMapper.readValue(jsonString, MensagemEntity.class);
    }

    private String getCaminhoBase(MensagemEntity mensagemEntity){
        return this.getCaminhoBase(mensagemEntity.getIdentificacao());
    }

    private String getCaminhoBase(String identificacao){
        return LocalDeArquivosConfiguration.MENSAGEM+identificacao;
    }

}

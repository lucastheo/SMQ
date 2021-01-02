package simplesmq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import simplesmq.util.FileUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ArquivoConfiguration {

    private static String CAMINHO_ARQUIVO_CONFIGURACAO = "./configuracoes_servidor.json";
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> carregar(String tag){
        try {
            String arquivo = FileUtils.ler(CAMINHO_ARQUIVO_CONFIGURACAO);
            HashMap<String,String> resultado = objectMapper.readValue(arquivo, HashMap.class);
            if(resultado.containsKey(tag)){
                return Optional.of(resultado.get(tag));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}

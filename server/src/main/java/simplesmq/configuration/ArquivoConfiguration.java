package simplesmq.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import simplesmq.util.FileUtils;
import simplesmq.util.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class ArquivoConfiguration {

    private static String CAMINHO_ARQUIVO_CONFIGURACAO = "./configuracoes_servidor.json";
    private static String CAMINHO_ARQUIVO_CONFIGURACAO_2 = "../configuracoes_servidor.json";

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Optional<String> carregar(String tag){
        String arquivo = null;
        try {
            try {
                arquivo = FileUtils.ler(CAMINHO_ARQUIVO_CONFIGURACAO);
            } catch (IOException e) {
                Logger.warn("Não encontrou o arquivo de configuração", e);
            }
            if (arquivo == null) {
                try {
                    arquivo = FileUtils.ler(CAMINHO_ARQUIVO_CONFIGURACAO_2);
                } catch (IOException e) {
                    Logger.erro("Não encontrou o arquivo de configuração_2", e);
                }
            }
            if (arquivo != null) {
                try {
                    HashMap<String, String> resultado = objectMapper.readValue(arquivo, HashMap.class);
                    if (resultado.containsKey(tag)) {
                        return Optional.of(String.valueOf(resultado.get(tag)));
                    }
                } catch (IOException e) {
                    Logger.erro("Erro em mapear o json", e.toString());
                }
            }
        }catch(Exception e ){
            Logger.erro("Erro em encontrar a variavel " + tag , e.toString());
        }
        return Optional.empty();
    }



}

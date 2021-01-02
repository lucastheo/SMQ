package simplesmq.configuration;

public class LocalDeArquivosConfiguration{
        public static String MENSAGEM= ArquivoConfiguration.carregar("local-armazenar-mensagem").get();
        public static String RELACAO = ArquivoConfiguration.carregar("local-armazenar-relacao").get();
}

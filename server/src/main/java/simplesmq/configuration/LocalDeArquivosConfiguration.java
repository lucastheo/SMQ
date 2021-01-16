package simplesmq.configuration;

public class LocalDeArquivosConfiguration{
        public static String MENSAGEM= ArquivoConfiguration.carregar("local-armazenar-mensagem").orElse("./.SMQ-files/mensagem/");
        public static String RELACAO = ArquivoConfiguration.carregar("local-armazenar-relacao").orElse("./.SMQ-files/relacao/");
}

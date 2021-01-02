package simplesmq.configuration;

public class LocalDeArquivosConfiguration{
        public static String MENSAGEM= ArquivoConfiguration.carregar("local-armazenar-mensagem").get();
                //"/home/theo/Documents/Projetos/SMQ-Files/mensagem/";
        public static String RELACAO = ArquivoConfiguration.carregar("local-armazenar-relacao").get();
                //"/home/theo/Documents/Projetos/SMQ-Files/relacao/";
}

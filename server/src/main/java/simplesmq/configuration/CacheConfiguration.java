package simplesmq.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CacheConfiguration {
    Integer sizeCacheMensagem = Integer.valueOf(ArquivoConfiguration.carregar("tamanho-da-cache").orElse("1000"));
    Integer tamanhoMaximoDaBuscaPelaCache = Integer.valueOf(ArquivoConfiguration.carregar("tamanho-maximo-busca").orElse("1000"));
}

package simplesmq.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CacheConfiguration {
    Integer sizeCacheMensagem = 10;

    Integer tamanhoMaximoDaBuscaPelaCache = 10000;
}

package simplesmq.configuration;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CacheConfiguration {
    Integer sizeCacheMensagem = 1000;

    Integer sizeCacheRelacao = 1000;
}

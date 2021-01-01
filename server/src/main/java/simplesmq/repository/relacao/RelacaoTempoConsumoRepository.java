package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class RelacaoTempoConsumoRepository {

    LinkedHashMap<LocalDateTime, LinkedList<RelacaoEntity>> relacaoTempoConsumo = new LinkedHashMap<>();
    Lock lock = new ReentrantLock();

    public void add(RelacaoEntity relacaoEntity , LocalDateTime localDateTime){
        lock.lock();
        relacaoTempoConsumo.putIfAbsent(localDateTime ,  new LinkedList() );
        relacaoTempoConsumo.get(localDateTime).add(relacaoEntity);
        lock.unlock();
    }

    public List<RelacaoEntity> desempilha(LocalDateTime busca ){
        List<RelacaoEntity> relacaoEstouro = new LinkedList<>();
        lock.lock();
        Set<LocalDateTime> relacaoTempoConsumoCache = new HashSet<>(relacaoTempoConsumo.keySet());
        lock.unlock();
        for(LocalDateTime localDateTime : relacaoTempoConsumoCache ) {
            if( busca.isAfter(localDateTime)){
                lock.lock();
                relacaoEstouro.addAll(relacaoTempoConsumo.get(localDateTime));
                relacaoTempoConsumo.remove(localDateTime);
                lock.unlock();
            }
        }
        return relacaoEstouro;
    }

}

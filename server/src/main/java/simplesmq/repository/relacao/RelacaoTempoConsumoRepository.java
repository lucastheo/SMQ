package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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
        List<RelacaoEntity> relacaoEstouro = new LinkedList();
        for(LocalDateTime localDateTime : relacaoTempoConsumo.keySet() ) {
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

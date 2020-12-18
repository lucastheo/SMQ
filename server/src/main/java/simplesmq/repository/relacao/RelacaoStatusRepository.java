package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RelacaoStatusRepository {
    ConcurrentHashMap<String, ConcurrentHashMap<String,ConcurrentLinkedQueue<RelacaoEntity>>> novo = new ConcurrentHashMap();

    Map<String, List<RelacaoEntity>> processamento = new ConcurrentHashMap<>();

    Queue<RelacaoEntity> finalizado = new ConcurrentLinkedQueue<>();

    public void addFila(String nomeFila){
        novo.putIfAbsent(nomeFila,new ConcurrentHashMap<>());
    }

    public void addConsumidores(String nomeFila , String consumidor ){
        novo.get(nomeFila).putIfAbsent(consumidor, new ConcurrentLinkedQueue<RelacaoEntity>());
    }

    public void addRelacao(String nomeFila , String consumidor , RelacaoEntity relacaoEntity ){
        novo.get(nomeFila).get(consumidor).add(relacaoEntity);
    }
}

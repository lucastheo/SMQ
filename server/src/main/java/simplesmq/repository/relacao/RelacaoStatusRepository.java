package simplesmq.repository.relacao;

import org.springframework.stereotype.Component;
import simplesmq.domain.entity.RelacaoEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RelacaoStatusRepository {
    ConcurrentHashMap<String, ConcurrentHashMap<String,ConcurrentLinkedQueue<RelacaoEntity>>> novo = new ConcurrentHashMap();

    ConcurrentHashMap<String, ConcurrentHashMap<String,ConcurrentLinkedQueue<RelacaoEntity>>> processamento = new ConcurrentHashMap<>();

    Queue<RelacaoEntity> finalizado = new ConcurrentLinkedQueue<>();

    public void addFila(String nomeFila){
        novo.putIfAbsent(nomeFila,new ConcurrentHashMap<>());
        processamento.putIfAbsent(nomeFila,new ConcurrentHashMap<>());
    }

    public void addConsumidores(String nomeFila , String consumidor ){
        novo.get(nomeFila).putIfAbsent(consumidor, new ConcurrentLinkedQueue<RelacaoEntity>());
        processamento.get(nomeFila).putIfAbsent(consumidor, new ConcurrentLinkedQueue<RelacaoEntity>());
    }

    public void addRelacao(String nomeFila , String consumidor , RelacaoEntity relacaoEntity ){
        novo.get(nomeFila).get(consumidor).add(relacaoEntity);
    }

    public Optional<RelacaoEntity> reserve(String nomeFila , String consumidor ){
        if( novo.containsKey(nomeFila) && novo.get(nomeFila).containsKey(consumidor)){
            RelacaoEntity relacaoEntity = novo.get(nomeFila).get(consumidor).poll();
            if( relacaoEntity != null ){
                processamento.get(nomeFila).get(consumidor).add(relacaoEntity);
            }
            return Optional.ofNullable(relacaoEntity);
        }
        return Optional.empty();
    }

    public Optional<RelacaoEntity> reserve(String nomeFila , String consumidor , Set<String> mensagensCache){
        if( !novo.containsKey(nomeFila) || !novo.get(nomeFila).containsKey(consumidor)){
            return Optional.empty();
        }
        for( RelacaoEntity relacaoEntity : novo.get(nomeFila).get(consumidor) ){
            if( mensagensCache.contains(relacaoEntity.getIdentificacaoMensagem()) ){
                if( novo.get(nomeFila).get(consumidor).remove(relacaoEntity) ) {
                    processamento.get(nomeFila).get(consumidor).add(relacaoEntity);
                    return Optional.of(relacaoEntity);
                }
            }
        }
        return this.reserve(nomeFila,consumidor);
    }
}

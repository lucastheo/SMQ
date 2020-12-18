package simplesmq.mapping.domain.entity;

import simplesmq.domain.dto.ElementoConsumoDto;
import simplesmq.domain.dto.MensagemDto;
import simplesmq.domain.entity.RelacaoEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class RelacaoEntityMapping {
    public static List<RelacaoEntity> mapFrom( UUID identificacaoMensagem , MensagemDto mensagemDto ){
        List<RelacaoEntity> lista = new LinkedList();
        RelacaoEntity relacaoEntity;
        for(ElementoConsumoDto elemento:  mensagemDto.getConsumidores() ){
            relacaoEntity = new RelacaoEntity();
            relacaoEntity.setNome( elemento.getNome() );
            relacaoEntity.setIdentificacaoMensagem(identificacaoMensagem.toString());
            relacaoEntity.setIdentificacao(identificacaoMensagem.toString()+elemento.getNome());
            lista.add(relacaoEntity);
        }
        return lista;
    }
}

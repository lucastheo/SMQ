package simplesmq.mapping.domain.dto;

import simplesmq.domain.dto.FilaDto;

public class FilaDtoMapping {

    public static FilaDto mapping(String nomeFila ){
        FilaDto fila = new FilaDto();
        fila.setNome(nomeFila);
        return fila;
    }
}

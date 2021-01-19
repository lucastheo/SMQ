package simplesmq.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import simplesmq.domain.dto.FilaDto;
import simplesmq.exception.ValidacaoException;
import simplesmq.mapping.domain.dto.FilaDtoMapping;
import simplesmq.mapping.domain.ro.ErrorEoMapping;
import simplesmq.service.FilaService;
import simplesmq.util.Logger;
import simplesmq.validate.domain.dto.FilaDtoValidate;

@RestController
public class FilaController {

    @Autowired
    FilaService filaService;

    @DeleteMapping("fila/{nome_fila}")
    public ResponseEntity limpaFila(@PathVariable("nome_fila") String nomeFila ){
        FilaDto fila = FilaDtoMapping.mapping(nomeFila);
        try {
            FilaDtoValidate.execute(fila);
        } catch (ValidacaoException ex) {
            Logger.warn("Erro de validação no limpar a fila" , fila , ex );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorEoMapping.mapFrom(ex));
        }

        filaService.limpar(fila);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

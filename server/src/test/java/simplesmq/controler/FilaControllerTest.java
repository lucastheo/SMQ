package simplesmq.controler;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import simplesmq.service.FilaService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class FilaControllerTest {

    @InjectMocks
    FilaController filaControle;

    @Mock
    FilaService filaService;

    public void setup() {
        MockitoAnnotations.openMocks(this);;
    }
    @Test
    void fialControllerOk(){
        setup();
        ResponseEntity response = filaControle.limpaFila("fila-teste");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void fialControllerValidacao(){
        setup();
        ResponseEntity response = filaControle.limpaFila(null);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

}

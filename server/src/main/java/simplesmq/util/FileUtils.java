package simplesmq.util;

import simplesmq.domain.enuns.StatusArquivoEnum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static void salvar(String caminho , String arquivo) throws IOException {
        File file = new File(caminho);
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(arquivo);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            if(fw != null){
                fw.close();
            }
            throw e;
        }
    }

    public static StatusArquivoEnum remover(String caminho ){
        File file = new File(caminho);
        if( file.exists() ) {
            return StatusArquivoEnum.NAO_ENCONTRADO;
        }
        if( file.delete() ){
            return StatusArquivoEnum.REMOVIDO;
        }
        return StatusArquivoEnum.FALHA;
    }
}

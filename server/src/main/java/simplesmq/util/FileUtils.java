package simplesmq.util;

import simplesmq.domain.enuns.StatusArquivoEnum;

import java.io.*;
import java.nio.CharBuffer;
import java.util.Optional;

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

    public static String ler(String caminho ) throws IOException{
        File file = new File(caminho);

        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        FileReader reader = null;

        reader = new FileReader(file);
        while(reader.read(buffer)!=-1){
            stringBuilder.append(buffer);
        }
        reader.close();
        return stringBuilder.toString();

    }
}

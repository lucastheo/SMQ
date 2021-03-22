package simplesmq.util;

import simplesmq.configuration.ArquivoConfiguration;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Logger {

    static ExecutorService threadPool = Executors.newFixedThreadPool(1);
    static Semaphore semaphore = new Semaphore(1000);
    static Integer nivel = Integer.valueOf(ArquivoConfiguration.carregar("nivel-log").orElse("2"));
    public static void info( String mensagem ){
        if( nivel <= 1 ) {
            threadPool.submit(() -> {
                log(tempoAtual() + "  INFO  -------------------------- " + mensagem);
            });
        }
    }

    public static void info( String mensagem , Object object){
        if(nivel <= 1 ) {
            threadPool.submit(() -> {
                log(tempoAtual() + "  INFO -------------------------- " + mensagem + ":" + object.toString());
            });
        }
    }

    public static void warn( String mensagem ){
        if( nivel <= 2 ) {
            threadPool.submit(() -> {
                log(tempoAtual() + "  WARN -------------------------- " + mensagem);
            });
        }
    }

    public static void warn( String mensagem , Object object ){
        if( nivel <= 2) {
            threadPool.submit(() -> {
                log(tempoAtual() + "  WARN -------------------------- " + mensagem + ":" + object.toString() + ":" + object.toString());
            });
        }
    }

    public static void warn( String mensagem , Object object , Object object1 ){
        if( nivel <= 2 ) {
            threadPool.submit(() -> {
                log(tempoAtual() + "  WARN -------------------------- " + mensagem + ":" + object.toString() + ":" + object.toString() + ":" + object1.toString());
            });
        }
    }

    public static void erro( String mensagem , Object object ){
        threadPool.submit(() -> { log( tempoAtual() + "  ERRO -------------------------- " + mensagem + ":" + mensagem + ":"+ object.toString()); } );
    }
    public static void erro( String mensagem , Object object , Exception ex ){
        threadPool.submit(() -> {
            log(tempoAtual() +  "  ERRO --------------------------- " + mensagem + ":"+ object.toString());
            ex.printStackTrace();
        } );
    }
    public static void erro( String mensagem , Exception ex ){
        threadPool.submit(() -> {
            log( tempoAtual() +  "  ERRO -------------------------- " + mensagem  );
            ex.printStackTrace();
        } );
    }
    public static void erro( String mensagem ){
        threadPool.submit(() -> {
            log(tempoAtual() +  " ERRO -------------------------- " + mensagem  );
        } );
    }

    public static String tempoAtual(){
        return LocalDateTime.now().toString().substring(0,23);
    }

    public static void log(String string ){
        try {
            semaphore.acquire();
            System.out.println(string);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }
    }
}

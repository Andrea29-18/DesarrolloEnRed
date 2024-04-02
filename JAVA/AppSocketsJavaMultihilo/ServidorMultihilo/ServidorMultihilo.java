package ServidorMultihilo;

import java.io.IOException;
import java.net.ServerSocket;

public class ServidorMultihilo {

    private static int clientCount = 0;


    public static void main(String[] args){
        int puerto = 8080;

        try(ServerSocket ss = new ServerSocket(puerto)){
            System.out.println("Servidor escuchando en el puerto: "+ puerto + "...");
            while(true){
                HiloHandler cliente = new HiloHandler(ss.accept());
                Thread h1 = new Thread(cliente);
                h1.start();

                clientCount++;
                System.out.println("Clientes conectados: " + clientCount);
            }
        }catch (Exception ex) {
            
        }
    }
}
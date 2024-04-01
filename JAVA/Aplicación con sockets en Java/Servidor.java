import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static void main(String[] args){
        int puerto = 8080;
        try {
            ServerSocket ss = new ServerSocket(puerto);
            System.out.println("Servidor escuchando en el puerto: "+ puerto + " ...");

            Socket ch = ss.accept();

            PrintWriter out = new PrintWriter(ch.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(ch.getInputStream()));
            
            out.println(" Hola ");
            out.println(" Mundo ");
            out.println(" Desde el servidor ! ");
            out.println(" Para cerrar el servidor es con: Adios");

            Thread inputThread = new Thread(() -> {
                try {
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                    String userInputLine;
                    while ((userInputLine = userInput.readLine()) != null) {
                        out.println("Servidor: " + userInputLine);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            inputThread.start();

            String mensajeCliente;
            while ((mensajeCliente = in.readLine()) != null) {
                System.out.println("Cliente: " + mensajeCliente);
                if (mensajeCliente.equalsIgnoreCase("Adios")) {
                    break;
                }
            }

            System.out.println("Cliente: " + in.readLine());

            out.close();
            in.close();
            ch.close();
            ss.close();
        } catch (Exception e) {
           System.out.println(e);
        }
    }
}
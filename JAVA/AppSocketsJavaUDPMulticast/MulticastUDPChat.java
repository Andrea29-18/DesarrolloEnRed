import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;

public class MulticastUDPChat {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Por favor, escribe tu nombre:");
        String nombre = scanner.nextLine();
        int puerto = 8080; 

        try {
            InetAddress grupo = InetAddress.getByName("224.0.0.0");
            MulticastSocket socket = new MulticastSocket(puerto);
            socket.joinGroup(grupo);

            new Thread(new EnviarMensajes(socket, grupo, puerto, nombre)).start();
            new Thread(new RecibirMensajes(socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class EnviarMensajes implements Runnable {
        private MulticastSocket socket;
        private InetAddress grupo;
        private int puerto;
        private String nombre;

        public EnviarMensajes(MulticastSocket socket, InetAddress grupo, int puerto, String nombre) {
            this.socket = socket;
            this.grupo = grupo;
            this.puerto = puerto;
            this.nombre = nombre;
        }

        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    String mensaje = scanner.nextLine();
                    if ("Adios".equalsIgnoreCase(mensaje)) {
                        byte[] datos = (nombre + ": " + mensaje).getBytes();
                        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, puerto);
                        socket.send(paquete);
                        socket.leaveGroup(grupo);
                        break;
                    } else {
                        byte[] datos = (nombre + ": " + mensaje).getBytes();
                        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, puerto);
                        socket.send(paquete);
                    }
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class RecibirMensajes implements Runnable {
        private MulticastSocket socket;

        public RecibirMensajes(MulticastSocket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    socket.receive(paquete);
                    String mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength());
                    System.out.println(mensajeRecibido);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

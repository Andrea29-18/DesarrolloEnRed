import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cliente {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {
        String mensaje = "Dame la hora local";
        String servidor = "localhost";
        int puerto = 8080;
        int espera = 5000; 
        DatagramSocket socketUDP = new DatagramSocket();
        InetAddress hostServidor = InetAddress.getByName(servidor);
        DatagramPacket peticion = new DatagramPacket(mensaje.getBytes(), mensaje.getBytes().length, hostServidor,
                puerto);
        socketUDP.setSoTimeout(espera);
        System.err.println("Esperamos dato en un máximo de " + espera + " milisegundos...");
        socketUDP.send(peticion);

        try {
            byte[] buffer = new byte[1024];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            socketUDP.receive(respuesta);

            String strHoraServidor = new String(respuesta.getData(), 0, respuesta.getLength());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime horaLocal = LocalDateTime.now();
            LocalDateTime horaServidor = LocalDateTime.parse(strHoraServidor, formatter);

            Duration diferencia = Duration.between(horaServidor, horaLocal).abs(); // Tomamos el valor absoluto de la diferencia
            long segundosDiferencia = diferencia.getSeconds();

            System.err.println("Hora del servidor es: " + horaServidor.format(formatter));
            System.err.println("Hora del cliente es: " + horaLocal.format(formatter));
            System.err.println("Diferencia entre hora del servidor y hora del cliente: " + segundosDiferencia
                    + " segundos.");
        } catch (SocketTimeoutException e) {
            System.err.println("Tiempo expirado para recibir respuestas: " + e.getMessage());
        }
        socketUDP.close();
    }
}

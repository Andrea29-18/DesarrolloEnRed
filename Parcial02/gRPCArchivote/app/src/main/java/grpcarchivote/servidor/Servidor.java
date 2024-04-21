package grpcarchivote.servidor;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class Servidor {
    public static void main(String[] args) throws IOException, InterruptedException {
        int puerto = 8080;

        Server servidor = ServerBuilder.forPort(puerto)
                .addService(new ServidorImpl())
                .build();

        servidor.start();

        System.out.println("Servidor iniciado...");
        System.out.println("Escuchando en el puerto: " + puerto);

        servidor.awaitTermination();
    }
}

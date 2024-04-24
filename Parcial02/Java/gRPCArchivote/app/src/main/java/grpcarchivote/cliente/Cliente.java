package grpcarchivote.cliente;

import com.proto.enviar.EnviarArchivo.ArchivoRequest;
import com.proto.enviar.ArchivoServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;


public class Cliente {

    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        ManagedChannel ch = ManagedChannelBuilder.forAddress(host, puerto).usePlaintext().build();

        ArchivoServiceGrpc.ArchivoServiceBlockingStub stub = ArchivoServiceGrpc.newBlockingStub(ch);
        ArchivoRequest peticion = ArchivoRequest.newBuilder().setArchivo("/archivote.csv").build();

        stub.enviarArchivo(peticion).forEachRemaining(respuesta ->{
            System.out.println("Respuesta RPC: " + respuesta.getCadena());
        });

        System.out.println("Apagando...");
        ch.shutdown();
    }

    
}
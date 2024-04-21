package grpcstreaming.cliente;

import com.proto.saludo.SaludoServiceGrpc;
import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Cliente {

    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        ManagedChannel ch = ManagedChannelBuilder
                .forAddress(host, puerto)
                .usePlaintext()
                .build();

        
        //Saludo de una vez
       // saludarUnitario(ch);

        //Saludo muchas veces (stream)
        saludarStream(ch);


        System.out.println("Apagado...");
        ch.shutdown();
    }

    private static void saludarStream(ManagedChannel ch) {
       
        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);
        
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("Puchetaaa").build();

        stub.saludoStream(peticion).forEachRemaining(repuesta -> {
            System.out.println("Repuesta RPC: " + repuesta.getResultado());
        });

    }

    public static void saludarUnitario(ManagedChannel ch){

        SaludoServiceGrpc.SaludoServiceBlockingStub stub = SaludoServiceGrpc.newBlockingStub(ch);
        
        SaludoRequest peticion = SaludoRequest.newBuilder().setNombre("Puchetaaa").build();

        SaludoResponse respuesta = stub.saludo(peticion);

        System.out.println("Respuesta RPC: " + respuesta.getResultado());
    }

}

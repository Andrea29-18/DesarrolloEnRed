package grpcarchivote.cliente;

import com.proto.enviar.EnviarArchivo.ArchivoChunk;
import com.proto.enviar.EnviarArchivo.ArchivoRequest;
import com.proto.enviar.ArchivoServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.FileOutputStream;
import java.io.IOException;


public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, puerto)
                .usePlaintext()
                .build();

        ArchivoServiceGrpc.ArchivoServiceStub stub = ArchivoServiceGrpc.newStub(channel);

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream("archivote.csv"); // Nombre del archivo de salida
            
            StreamObserver<ArchivoChunk> responseObserver = new StreamObserver<ArchivoChunk>() {
                @Override
                public void onNext(ArchivoChunk value) {
                    try {
                        outputStream.write(value.getChunk().toByteArray());
                    } catch (IOException e) {
                        onError(e);
                    }
                }

                @Override
                public void onError(Throwable t) {
                    handleError(t);
                }

                @Override
                public void onCompleted() {
                    try {
                        outputStream.close();
                        System.out.println("Archivo recibido correctamente.");
                    } catch (IOException e) {
                        onError(e);
                    }
                }
            };

            StreamObserver<ArchivoRequest> requestObserver = stub.enviarArchivo(responseObserver);

            ArchivoRequest request = ArchivoRequest.newBuilder().setNombreArchivo("archivote.csv").build();
            requestObserver.onNext(request);
            requestObserver.onCompleted();

            // Espera a que la transferencia del archivo se complete
            Thread.sleep(5000);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            channel.shutdown();
        }
    }

    private static void handleError(Throwable t) {
        System.err.println("Error en la comunicación con el servidor: " + t.getMessage());
    }
}
package grpcarchivote.servidor;

import java.util.Scanner;

import com.proto.enviar.EnviarArchivo.ArchivoChunk;
import com.proto.enviar.EnviarArchivo.ArchivoRequest;
import com.proto.enviar.ArchivoServiceGrpc;

import io.grpc.stub.StreamObserver;


public class ServidorImpl extends ArchivoServiceGrpc.ArchivoServiceImplBase {
    @Override
    public void enviarArchivo(ArchivoRequest request, StreamObserver<ArchivoChunk> responseObserver) {
        try (Scanner scanner = new Scanner(ServidorImpl.class.getResourceAsStream(request.getArchivo()))) {
            while (scanner.hasNextLine()) {
                ArchivoChunk cadena = ArchivoChunk.newBuilder().setCadena(scanner.nextLine()).build();
                responseObserver.onNext(cadena);
            }

            responseObserver.onCompleted();
        }
    }
    
}
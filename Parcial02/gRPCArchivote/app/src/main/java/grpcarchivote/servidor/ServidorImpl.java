package grpcarchivote.servidor;

import java.io.IOException;
import java.io.InputStream;

import com.proto.enviar.EnviarArchivo.ArchivoChunk;
import com.proto.enviar.EnviarArchivo.ArchivoRequest;
import com.google.protobuf.ByteString;
import com.proto.enviar.ArchivoServiceGrpc;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends ArchivoServiceGrpc.ArchivoServiceImplBase {
    @Override
    public void enviarArchivo(ArchivoRequest request, StreamObserver<ArchivoChunk> responseObserver) {
        try {
            String nombreArchivo = request.getNombreArchivo();
            InputStream inputStream = ServidorImpl.class.getResourceAsStream("/" + nombreArchivo);
            if (inputStream == null) {
                throw new IOException("Archivo no encontrado: " + nombreArchivo);
            }

            byte[] buffer = new byte[1024]; // Tamaño del chunk
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] chunk = new byte[bytesRead];
                System.arraycopy(buffer, 0, chunk, 0, bytesRead);
                ArchivoChunk archivoChunk = ArchivoChunk.newBuilder().setChunk(ByteString.copyFrom(chunk)).build();
                responseObserver.onNext(archivoChunk);
            }

            responseObserver.onCompleted();
            inputStream.close();
        } catch (IOException e) {
            responseObserver.onError(e);
        }
    }
}
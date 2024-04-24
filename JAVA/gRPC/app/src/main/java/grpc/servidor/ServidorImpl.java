package grpc.servidor;

import com.proto.saludo.Holamundo.SaludoRequest;
import com.proto.saludo.Holamundo.SaludoResponse;
import com.proto.saludo.SaludoServiceGrpc;

import io.grpc.stub.StreamObserver;


public class ServidorImpl extends SaludoServiceGrpc.SaludoServiceImplBase{
    
    @Override 
    public void saludo(SaludoRequest request, StreamObserver<SaludoResponse> responseObserver){
        SaludoResponse repuesta = SaludoResponse.newBuilder().setResultado("Hola" + request.getNombre()).build();
    
        responseObserver.onNext(repuesta);

        responseObserver.onCompleted();

    }

}

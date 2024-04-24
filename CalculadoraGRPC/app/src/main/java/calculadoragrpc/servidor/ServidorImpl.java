package calculadoragrpc.servidor;

import com.proto.calculadora.Calculadora.OperacionRequest;
import com.proto.calculadora.Calculadora.OperacionResponse;
import com.proto.calculadora.CalculadoraServiceGrpc;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends CalculadoraServiceGrpc.CalculadoraServiceImplBase {
    @Override 
    public void calcular(OperacionRequest request, StreamObserver<OperacionResponse> responseObserver) {
        int resultado = 0;
        switch (request.getOperador()) {
            case "+":
                resultado = request.getNumero1() + request.getNumero2();
                break;
            case "-":
                resultado = request.getNumero1() - request.getNumero2();
                break;
            case "*":
                resultado = request.getNumero1() * request.getNumero2();
                break;
            case "/":
                if (request.getNumero2() != 0) {
                    resultado = request.getNumero1() / request.getNumero2();
                } else {
                    responseObserver.onError(
                        new IllegalArgumentException("No se puede dividir por cero")
                    );
                    return;
                }
                break;
            default:
                responseObserver.onError(
                    new IllegalArgumentException("Operador no válido: " + request.getOperador())
                );
                return;
        }
        
        OperacionResponse respuesta = OperacionResponse.newBuilder().setResultado(resultado).build();
        responseObserver.onNext(respuesta);
        responseObserver.onCompleted();
    }
}

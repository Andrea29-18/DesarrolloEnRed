package calculado.grpc.cliente;

import com.proto.calculadora.CalculadoraServiceGrpc;
import com.proto.calculadora.OperacionRequest;
import com.proto.calculadora.OperacionResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import javax.swing.JOptionPane;

public class Cliente {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 8080;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, puerto)
                                                      .usePlaintext()
                                                      .build();

        try {
            CalculadoraServiceGrpc.CalculadoraServiceBlockingStub stub = CalculadoraServiceGrpc.newBlockingStub(channel);

            while (true) {
                String opt = JOptionPane.showInputDialog(
                        "Calculadora\n" +
                                "Suma..........(1)\n" +
                                "Resta.........(2)\n" +
                                "Multiplicación.(3)\n" +
                                "División......(4)\n\n" +
                                "Cancelar para salir");
                if (opt == null) {
                    break;
                }

                int a = Integer.parseInt(JOptionPane.showInputDialog("Ingrese a"));
                int b = Integer.parseInt(JOptionPane.showInputDialog("Ingrese b"));

                switch (opt) {
                    case "1":
                        OperacionRequest sumaRequest = OperacionRequest.newBuilder()
                                                                      .setNumero1(a)
                                                                      .setNumero2(b)
                                                                      .setOperador("+")
                                                                      .build();
                        OperacionResponse sumaResponse = stub.calcular(sumaRequest);
                        JOptionPane.showMessageDialog(null, a + "+" + b + "=" + sumaResponse.getResultado());
                        break;
                    case "2":
                        OperacionRequest restaRequest = OperacionRequest.newBuilder()
                                                                       .setNumero1(a)
                                                                       .setNumero2(b)
                                                                       .setOperador("-")
                                                                       .build();
                        OperacionResponse restaResponse = stub.calcular(restaRequest);
                        JOptionPane.showMessageDialog(null, a + "-" + b + "=" + restaResponse.getResultado());
                        break;
                    case "3":
                        OperacionRequest multiplicacionRequest = OperacionRequest.newBuilder()
                                                                                 .setNumero1(a)
                                                                                 .setNumero2(b)
                                                                                 .setOperador("*")
                                                                                 .build();
                        OperacionResponse multiplicacionResponse = stub.calcular(multiplicacionRequest);
                        JOptionPane.showMessageDialog(null, a + "*" + b + "=" + multiplicacionResponse.getResultado());
                        break;
                    case "4":
                        OperacionRequest divisionRequest = OperacionRequest.newBuilder()
                                                                           .setNumero1(a)
                                                                           .setNumero2(b)
                                                                           .setOperador("/")
                                                                           .build();
                        OperacionResponse divisionResponse = stub.calcular(divisionRequest);
                        JOptionPane.showMessageDialog(null, a + "/" + b + "=" + divisionResponse.getResultado());
                        break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            channel.shutdown();
        }
    }
}

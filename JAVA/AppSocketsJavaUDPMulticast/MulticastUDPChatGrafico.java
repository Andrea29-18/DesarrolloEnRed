import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class MulticastUDPChatGrafico extends JFrame {
    private JTextField mensajeField;
    private JTextArea chatArea;
    private DatagramSocket socket;
    private InetAddress grupo;
    private int puerto;
    private String nombre;

    public MulticastUDPChatGrafico(String nombre) {
        super("Chat Multicast");
        this.nombre = nombre;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        mensajeField = new JTextField();
        mensajeField.addActionListener(new EnviarListener());
        add(mensajeField, BorderLayout.NORTH);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

        try {
            puerto = 8080;
            grupo = InetAddress.getByName("224.0.0.0");
            socket = new DatagramSocket();

            new Thread(new RecibirMensajes()).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EnviarListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String mensaje = mensajeField.getText();
            if (!mensaje.isEmpty()) {
                try {
                    if ("Adios".equalsIgnoreCase(mensaje)) {
                        byte[] datos = (nombre + ": " + mensaje).getBytes();
                        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, puerto);
                        socket.send(paquete);
                        //socket.leaveGroup(grupo);
                        socket.close();
                        dispose(); 
                        System.exit(0); 
                    } else {
                        byte[] datos = (nombre + ": " + mensaje).getBytes();
                        DatagramPacket paquete = new DatagramPacket(datos, datos.length, grupo, puerto);
                        socket.send(paquete);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mensajeField.setText("");
            }
        }
    }

    private class RecibirMensajes implements Runnable {
        public void run() {
            try {
                byte[] buffer = new byte[1024];
                MulticastSocket multicastSocket = new MulticastSocket(puerto);
                multicastSocket.joinGroup(grupo);

                while (true) {
                    DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(paquete);
                    String mensajeRecibido = new String(paquete.getData(), 0, paquete.getLength());
                    chatArea.append(mensajeRecibido + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String nombre = JOptionPane.showInputDialog("Por favor, escribe tu nombre:");
        if (nombre != null && !nombre.isEmpty()) {
            new MulticastUDPChatGrafico(nombre);
        }
    }
}

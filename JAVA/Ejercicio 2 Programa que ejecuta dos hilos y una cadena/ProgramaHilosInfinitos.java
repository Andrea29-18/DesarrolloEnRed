public class ProgramaHilosInfinitos {
    public static void main(String[] args) {
        // Crear dos objetos de la clase MiHiloInfinito
        MiHiloInfinito hiloHola = new MiHiloInfinito("Hola");
        MiHiloInfinito hiloMundo = new MiHiloInfinito("Mundo");

        // Iniciar los hilos
        hiloHola.start();
        hiloMundo.start();
    }
}

class MiHiloInfinito extends Thread {
    private String mensaje;

    public MiHiloInfinito(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(mensaje);

            try {
                // Agregar un retardo de 200 milisegundos entre cada impresión
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

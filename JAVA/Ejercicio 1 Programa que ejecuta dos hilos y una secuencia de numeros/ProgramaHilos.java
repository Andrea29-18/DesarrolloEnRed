public class ProgramaHilos {
    public static void main(String[] args) {

        MiHilo hilo1 = new MiHilo("Hilo1", 1, 100);
        MiHilo hilo2 = new MiHilo("Hilo2", 100, 1);

        hilo1.start();
        hilo2.start();
        
        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Programa principal finalizado.");
    }
}

class MiHilo extends Thread {
    private int inicio;
    private int fin;

    public MiHilo(String nombre, int inicio, int fin) {
        super(nombre);
        this.inicio = inicio;
        this.fin = fin;
    }

    @Override
    public void run() {
        for (int i = inicio; (inicio < fin) ? i <= fin : i >= fin; i += (inicio < fin) ? 1 : -1) {
            System.out.println(Thread.currentThread().getName() + ": " + i);

            try {
                // Agregar un retardo de 100 milisegundos entre cada impresión
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " terminado.");
    }
}

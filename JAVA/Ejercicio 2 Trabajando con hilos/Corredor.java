public class Corredor implements Runnable{
    private String avance;
    private long latencia;
    private char c;

    public Corredor(char c){
        this.avance = "";
        this.latencia = (long)(Math.random() * 100);
        this.c = c;
    }

    @Override
    public void run(){
        for (int i = 0; i <70; i++){
            avance = avance + c;
            System.out.println(avance);
            try{
                Thread.sleep(latencia);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        avance = avance + " terminé!!! ";
        System.out.println(avance);
    }
}

namespace Ejercicio03;

public class Persona
{
    public String Nombre {get;set;}
    public int Edad {get;set;}
    public String Sexo {get;set;}

    public Persona (string nombre, int edad, string sexo){
        this.Nombre = nombre;
        this.Edad = edad;
        this.Sexo = sexo;
    }

    static void TareaDeFondo(Object? stateInfo){
        Console.WriteLine($"Hilo 1: Hola soy un hilo sin uso parámetros desde ThreadPool.");
        Thread.Sleep(1500);
    }

    static void TareaDeFondoParametro(Object? stateInfo){
        if(stateInfo == null)
            return;

        Thread currentThread = Thread.CurrentThread;
        Console.WriteLine("Es un thread background: {0}", currentThread.IsBackground);

        Persona data = (Persona)stateInfo;
        Console.WriteLine($"Hilo 2: Hola {data.Nombre}, tu edad es {data.Edad}.");
        Thread.Sleep(500);
    }

    static void Main(String [] args){
        int workers, ports;

        ThreadPool.GetMaxThreads(out workers, out ports);
        Console.WriteLine($"Máximos hilos de trabajo: {workers} ");
        Console.WriteLine($"Máximos puertos sin hilos: {ports} ");

        ThreadPool.GetMinThreads(out workers, out ports);
        Console.WriteLine($"Mínimos hilos de trabajo: {workers} ");
        Console.WriteLine($"Mínimos puertos (completition port) para hilos: {ports} ");

        ThreadPool.GetAvailableThreads(out workers, out ports);
        Console.WriteLine($"Hilos de trabajo disponibles: {workers} ");
        Console.WriteLine($"Hilos de puerto (completition port) disponibles: {ports} ");

        int processCount = Environment.ProcessorCount;
        Console.WriteLine($"No. de procesadores disponibles en el sistema: {processCount} ");
        Console.WriteLine($"---------------------------------");

        ThreadPool.QueueUserWorkItem(TareaDeFondo);
        ThreadPool.GetAvailableThreads(out workers, out ports);
        Console.WriteLine($"Hilos de trabajo disponibles después del hilo 1: {workers} ");

        Persona p = new Persona("Alejandra Puchtea",21,"Mujer");
        ThreadPool.QueueUserWorkItem(TareaDeFondoParametro, p);
        ThreadPool.GetAvailableThreads(out workers, out ports);
        Console.WriteLine($"Hilos de trabajo disponibles después del hilo 2: {workers}");

        Thread.Sleep(2000);
        ThreadPool.GetAvailableThreads(out workers, out ports);
        Console.WriteLine($"Hilos de trabajo disponibles al final: {workers} ");
        Console.ReadKey();
    }
}
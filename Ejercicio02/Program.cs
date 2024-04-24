namespace Ejercicio02;

using System.Threading;


class Program
{
   static void Main()
    {
        Thread currentThread = Thread.CurrentThread;
        currentThread.Name = "HiloPrincipal";
        currentThread.Priority = ThreadPriority.Lowest;
        currentThread.IsBackground = false;

        Console.WriteLine("Thread id {0}", currentThread.ManagedThreadId);
        Console.WriteLine("Thread name {0}", currentThread.Name);
        Console.WriteLine("es un thread background: {0}", currentThread.IsBackground);
        Console.WriteLine("Priority {0}", currentThread.Priority);
        Console.WriteLine("Culture: {0}", currentThread.CurrentUICulture.Name);
        Console.WriteLine();

        CancellationTokenSource cts = new CancellationTokenSource();
        Thread workerThread = new Thread(new ParameterizedThreadStart(Print));
        workerThread.Name = "Hilo de Print";
        workerThread.Start(cts);

        for (int i = 0; i < 10; i++)
        {
            Console.WriteLine($"Principal thread: {i}");
            Thread.Sleep(200);
        }
        if (workerThread.IsAlive)
            cts.Cancel();
    }

    static void Print(object? obj)
    {
        if (obj == null || !(obj is CancellationTokenSource))
            return;

        CancellationTokenSource cts = (CancellationTokenSource)obj;

        Thread currentThread = Thread.CurrentThread;
        currentThread.Priority = ThreadPriority.Highest;
        currentThread.IsBackground = false;

        Console.WriteLine("Thread id {0}", currentThread.ManagedThreadId);
        Console.WriteLine("Thread name {0}", currentThread.Name);
        Console.WriteLine("es un thread background: {0}", currentThread.IsBackground);
        Console.WriteLine("Priority {0}", currentThread.Priority);
        Console.WriteLine("Culture: {0}", currentThread.CurrentUICulture.Name);
        Console.WriteLine();

        for (int i = 11; i < 20; i++)
        {
            if (cts.Token.IsCancellationRequested)
            {
                Console.WriteLine("En la iteracion {0}, la cancelacion ha sido solicitada...", i);
                break;
            }
            Console.WriteLine($"Print thread: {i}");
            Thread.Sleep(1000);
        }
    }
}

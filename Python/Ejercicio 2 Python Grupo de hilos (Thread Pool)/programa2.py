import concurrent.futures

def hilo1():
    print("Hilo 1 corriendo.")

    for i in range(0, 6):
        print(i)

def hilo2():
    print("Hilo 2 corriendo.")

    for i in range(6, 12):
        print(i)

pool = concurrent.futures.ThreadPoolExecutor(max_workers=2)

pool.submit(hilo1)
pool.submit(hilo2)

# Esta función termina el pool apropiadamente
pool.shutdown(wait=True)
print("El hilo Main termina aquí.")

import http.client

# Definir los datos de inicio de sesión
username = "alejandra"
password = "1234567890"

# Definir la dirección de IP y el puerto del socket
ip = "192.168.56.108"
puerto = "8080"

# Definir los encabezados que se enviaran
headers = {"Conten-type" : "application/x-www-form-urlencoded"}

# Definir los datos que se enciaran al servidor
data = "username=" + username + "&password" + password

# Crear una conexión con el socket
conn = http.client.HTTPConnection(ip, puerto)

# Enviar los datos como si fueran un inicio de sesión
conn.request("POST", "/login", body=data, headers=headers)

# Obtener la repuesta del servidor
response = conn.getresponse()

# Verificar si la solicitud se completo correctamente
if response.status == 200:
    print("Inicio de sesión exitosamente!")
else:
    print("Error en el incio de sesión", response.reason)

# Cierra la conexion
conn.close()
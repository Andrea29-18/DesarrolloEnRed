const grpc = require("@grpc/grpc-js");
const protoLoader = require("@grpc/proto-loader");
const dotenv = require('dotenv')
const PROTO_PATH = "./proto/audio.proto";
const portAudio = require('naudiodon');

//Configuración .env
dotenv.config()
//Implementación proto para JS
const packageDefinition = protoLoader.loadSync(PROTO_PATH);
const audioProto = grpc.loadPackageDefinition(packageDefinition);

//Cliente gRPC
const stub = new audioProto.AudioService(`localhost:${process.env.SERVER_PORT}`, grpc.credentials.createInsecure())
//Reproduce stream mientras descarga
nombre_archivo = 'anyma.wav'
streamAudio(stub, nombre_archivo);

//Función recibe stream y reproduce mientras descarga
function streamAudio(stub, nombre_archivo){
    //Crea reproducto audio con Writable Stream
    var ao = new portAudio.AudioIO({
        outOptions: {
            channelCount: 2,
            sampleFormat: portAudio.SampleFormat16Bit,
            sampleRate: 48000
        }
    });
    ao.start();

    console.log(`\nReproduciendo el archivo: ${nombre_archivo}`)
    //Usando stub, llamada a stream RPC
    stub.downloadAudio({
        nombre: nombre_archivo
    }).on('data',(DataChunkResponse) => {
        process.stdout.write('.')
        ao.write(DataChunkResponse.data)
    }).on('end', function(){
        console.log('\nRecepción de datos correcta.')
    })
}
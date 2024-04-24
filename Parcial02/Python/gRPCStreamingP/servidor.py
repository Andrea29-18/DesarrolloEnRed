from concurrent import futures

import grpc
import audio_pb2
import audio_pb2_grpc

class AudioStreamerServicer(audio_pb2_grpc.AudioServiceServicer):
    def downloadAudio(self, request, context):
        print("\nEnviando el archivo: {0}".format(request.nombre))

        chunk_size = 1024
        with open("recursos/{0}".format(request.nombre), "rb") as content_file:
            while chunk_bytes := content_file.read(chunk_size):
                yield audio_pb2.DataChunkResponse(chunk_bytes)
                print(".", end="", flush=True)

def servidor():
    servicer = AudioStreamerServicer()
    server =grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    puerto = "8080"

    audio_pb2_grpc.add_AudioServiceServicer_to_server(servicer, server)
    server.add_insecure_port("[::]:" + puerto)
    server.start()
    print("Servidor gRPC en ejecución en el puerto: " + puerto)

    try:
        server.wait_for_termination()
    except KeyboardInterrupt:
        pass
    finally:
        server.stop(0)

if __name__ == "__main__":
    servidor()
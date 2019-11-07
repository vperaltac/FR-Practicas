import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {




        public static void main(String[] args) {
            // Puerto de escucha
            int port=8989;
            ServerSocket socketServicio;
            Socket socketConexion = null;
            try {
                // Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
                socketServicio = new ServerSocket(port);

                do {

                    // Aceptamos una nueva conexión con accept()
                    socketConexion = socketServicio.accept();

                    // Creamos un objeto de la clase ProcesadorYodafy, pasándole como
                    // argumento el nuevo socket, para que realice el procesamiento
                    // Este esquema permite que se puedan usar hebras más fácilmente.
                  //  ProcesadorYodafy procesador=new ProcesadorYodafy(socketConexion);
                   // procesador.start();
                    Juego juego=new Juego(socketConexion);
                    juego.start();

                } while (true);

            } catch (IOException e) {
                System.err.println("Error al escuchar en el puerto "+port);
            }

        }

    }

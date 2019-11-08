import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        // Nombre del host donde se ejecuta el servidor:
        String host="localhost";
        // Puerto en el que espera el servidor:
        int port=8989;
        String codigo;
        // Socket para la conexión TCP
        Socket socketServicio=null;

        try {
            // Creamos un socket que se conecte a "hist" y "port":
            socketServicio = new Socket(host,port);

            PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(),true);
            BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));

            System.out.println("Introduce tu nick: ");
            Scanner teclado=new Scanner(System.in);

            String entrada="105 ";
                entrada+=teclado.nextLine();

            outPrinter.println(entrada);
            outPrinter.flush();

            // Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
            // rellenar. El método "read(...)" devolverá el número de bytes leídos.
            String recibida = "";
            recibida = inReader.readLine();
            codigo=recibida.substring(0,3);

            while(!codigo.equals("203")){
                switch (codigo){
                    case "200":
                        System.out.println("Elige una opcion e introduzca su código númerico(exclusivamente): \n" +
                                "100.-tirar dado\n");
                        entrada=teclado.nextLine();

                        outPrinter.println(entrada);
                        outPrinter.flush();
                        break;

                    case "201":
                        recibida=recibida.substring(4,recibida.length());
                        System.out.println(recibida);
                        entrada="101 ";
                        entrada+=teclado.nextLine();
                        outPrinter.println(entrada);
                        outPrinter.flush();
                    break;

                    case "202":
                        System.out.println("Elige una opcion e introduzca su código númerico(exclusivamente): \n" +
                                "100.-tirar dado\n" +
                                "102.-ver Puntuacion\n" +
                                "103.-recibir reglas del juego\n" +
                                "104.-terminar Partida");

                        entrada=teclado.nextLine();

                        outPrinter.println(entrada);
                        outPrinter.flush();
                        break;

                    case "204":
                        recibida=recibida.substring(4,recibida.length());
                        System.out.println(recibida);
                        System.out.println("Elige una opcion e introduzca su código númerico(exclusivamente): \n" +
                                "100.-tirar dado\n" +
                                "102.-ver Puntuacion\n" +
                                "103.-recibir reglas del juego\n" +
                                "104.-terminar Partida");

                        entrada=teclado.nextLine();

                        outPrinter.println(entrada);
                        outPrinter.flush();
                        break;

                    case "205":
                        recibida = recibida.substring(4,recibida.length());
                        System.out.println(recibida);

                        System.out.println("Elige una opcion e introduzca su código númerico(exclusivamente): \n" +
                                "100.-tirar dado\n" +
                                "102.-ver Puntuacion\n" +
                                "103.-recibir reglas del juego\n" +
                                "104.-terminar Partida");

                        entrada=teclado.nextLine();

                        outPrinter.println(entrada);
                        outPrinter.flush();
                        break;

                    default:
                        System.out.println("La opción introducida no existe.");
                }
                
                recibida = inReader.readLine();
                codigo=recibida.substring(0,3);
            }
        
            recibida=recibida.substring(4,recibida.length());
            System.out.println(recibida);

            // Una vez terminado el servicio, cerramos el socket (automáticamente se cierran
            // el inpuStream  y el outputStream)
            socketServicio.close();
            teclado.close();

            // Excepciones:
        } catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
        } catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
        }
    }
}
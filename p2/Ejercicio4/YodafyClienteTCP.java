//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class YodafyClienteTCP {

	public static void main(String[] args) {
		// Nombre del host donde se ejecuta el servidor:
		String host="localhost";
		// Puerto en el que espera el servidor:
		int port=8989;
		InetAddress direccion;
		DatagramPacket paquete;
		byte[] buferEnvio;
		byte[] buferRecepcion= new byte[256];
		int bytesLeidos=0;
		DatagramSocket socket;
		
		try {
			// Creamos un socket que se conecte a "hist" y "port":	
			socket = new DatagramSocket();
			direccion = InetAddress.getByName(host);

			// envío por UDP
			buferEnvio="Al monte del volcán debes ir sin demora".getBytes();
			paquete = new DatagramPacket(buferEnvio, buferEnvio.length,direccion,port);
			socket.send(paquete);

			// Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que intentará
			// rellenar. El método "read(...)" devolverá el número de bytes leídos.			
			paquete = new DatagramPacket(buferRecepcion, buferRecepcion.length);
			socket.receive(paquete);
			bytesLeidos = paquete.getLength();

			// Mostremos la cadena de caracteres recibidos:
			System.out.println("Recibido: ");
			String s = new String(buferRecepcion);
			System.out.println(s);
			
			// Una vez terminado el servicio, cerramos el socket
			socket.close();

		} catch (UnknownHostException e) {
			System.err.println("Error: Nombre de host no encontrado.");
		} catch (IOException e) {
			System.err.println("Error de entrada/salida al abrir el socket.");
		}
	}
}

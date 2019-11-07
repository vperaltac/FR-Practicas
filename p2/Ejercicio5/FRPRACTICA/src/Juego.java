import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;




    //
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente!
//
    public class Juego extends Thread {
        // Referencia a un socket para enviar/recibir las peticiones/respuestas
        private Socket socketServicio;
        private int puntuacion;
        private int num_preguntas;
        private String reglas;
        ArrayList<Pregunta> deportes = new ArrayList();
        ArrayList<Pregunta> politica = new ArrayList<>();
        ArrayList<Pregunta> geografia = new ArrayList<>();
        ArrayList<Pregunta> ciencia = new ArrayList<>();
        String nick;

        // Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
        public Juego(Socket socketServicio) {
            this.socketServicio = socketServicio;


            Pregunta p = new Pregunta("¿Cuantos mundiales ha ganado Michael Schumacher?", "7");
            deportes.add(p);
            p = new Pregunta("¿Cuantos mundiales de fútbol ha ganado España?", "1");
            deportes.add(p);
            p = new Pregunta("¿Cuantos mundiales de motoGP ha ganado Marc Márquez?", "6");
            deportes.add(p);
            p = new Pregunta("¿En que año ganó Fernando Alonso su primer mundial de F1", "2005");
            deportes.add(p);


            p = new Pregunta("¿En que año Tejero impulsó un golpe de estado?", "1981");
            politica.add(p);
            p = new Pregunta("¿En que año se aprobó la constitución española actual?", "1978");
            politica.add(p);
            p = new Pregunta("¿Cuantos diputados hay en el congreso español?", "350");
            politica.add(p);
            p = new Pregunta("¿Cual es el año de las ultimas elecciones nacionales en españa?", "2019");
            politica.add(p);

            p = new Pregunta("¿Altitud en metros del pico más alto de la península?", "3478");
            geografia.add(p);
            p = new Pregunta("¿Número de provincias de Andalucía?", "8");
            geografia.add(p);
            p = new Pregunta("¿Numero de provincias de la comunidad de Madrid?", "1");
            geografia.add(p);
            p = new Pregunta("¿Altitud en metros del pico más alto de España?", "3718");
            geografia.add(p);

            p = new Pregunta("¿Cuantos sentidos tiene el ser humano?", "5");
            ciencia.add(p);
            p = new Pregunta("¿Cuantos litros de sangre tiene el cuerpo humano?", "4");
            ciencia.add(p);
            p = new Pregunta("¿Cuantas capas tiene el modelo OSI?", "7");
            ciencia.add(p);
            p = new Pregunta("¿Cual es la velocidad de la luz en km/h?", "300000");
            ciencia.add(p);

            nick = "";
            puntuacion = 0;
            num_preguntas = 16;


            reglas = " ";
        }

        //Devuelve un entero aleatorio con un valor que representa a una temática con preguntas disponibles.
        public int tirarDado() {
            ArrayList<Integer> lista = new ArrayList<Integer>();

            if (deportes.size() > 0) lista.add(1);
            if (politica.size() > 0) lista.add(2);
            if (geografia.size() > 0) lista.add(3);
            if (ciencia.size() > 0) lista.add(4);

            int aleatorio = (int) (Math.random() * lista.size()+1);

            return aleatorio;

        }

    //Método que devuelve un par (pregunta,respuesta correcta) de un tema determinado por el dado.
        public Pregunta obtenerPregunta(int dado) {
            Pregunta p=new Pregunta("","");
            System.out.println("dado:"+dado+"\n");
            switch (dado) {
                case 1:
                    p=deportes.get(0);
                    deportes.remove(p);
                break;
                case 2:
                    p= politica.get(0);
                    politica.remove(p);

                    break;
                case 3:
                    p= geografia.get(0);
                    geografia.remove(p);
                break;
                case 4:
                    p= ciencia.get(0);
                    ciencia.remove(p);
                break;
            }
            return p;
        }

        // Aquí es donde se realiza el procesamiento realmente:
        @Override
        public void run() {
            try {
                // Obtiene los flujos de escritura/lectura
                PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
                BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
                String codigo;

                //Leemos el nick del usuario
                String recibida = inReader.readLine();
                codigo = recibida.substring(0, 3);
                nick = recibida.substring(4, recibida.length() - 1);

               //Inicializamos o creamos variables necesarias como dado o pregunta
                int dado;
                Pregunta p=new Pregunta("","");

                //Respondemos al cliente que el juego ha sido iniciado
                String respuesta="200 Juego iniciado";
                outPrinter.println(respuesta);
                outPrinter.flush();

                //Leemos petición de servicio del cliente y extraemos el codigo para identificarla posteriormente
                recibida = inReader.readLine();
                codigo = recibida.substring(0, 3);
                System.out.println(codigo);


                while (recibida != "104") {

                    switch (codigo) {
                        case "100":
                            dado = tirarDado();
                            p = obtenerPregunta(dado);
                            num_preguntas--;
                            respuesta = "201 " + p.getPregunta();
                            outPrinter.println(respuesta);
                            outPrinter.flush();
                            break;

                        case "101":
                            recibida = recibida.substring(4, recibida.length());
                            if (recibida.equals(p.respuesta)) {
                                puntuacion++;
                                respuesta = "202 respuesta correcta";
                            } else {
                                puntuacion--;
                                respuesta = "202 respuesta falsa";

                            }

                            if (puntuacion < 0) {
                                respuesta = "203 Juego terminado, SU PUNTUACIÓN ES NEGATIVA";
                                codigo="104";
                            } else {
                                if (num_preguntas < 1) {
                                    respuesta = "203 Juego terminado, Su puntuacion es de: " + puntuacion;
                                    codigo="104";
                                }
                            }

                            outPrinter.println(respuesta);
                            outPrinter.flush();
                            break;

                        case "102":
                            respuesta = "204 tu puntuacion es de: " + puntuacion;
                            outPrinter.println(respuesta);
                            outPrinter.flush();
                            break;

                        case "103":
                            respuesta = "205 " + reglas;
                            outPrinter.println(respuesta);
                            outPrinter.flush();
                            break;

                        case "104":
                            respuesta = "203 Juego terminado, Su puntuacion es de: " + puntuacion;
                            outPrinter.println(respuesta);
                            outPrinter.flush();
                            codigo="104";
                            break;



                    }
                    if(codigo!="104"){
                        recibida = inReader.readLine();
                        System.out.println(recibida);

                        codigo = recibida.substring(0, 3);
                    }
                }


            } catch (IOException e) {
                System.err.println("Error al obtener los flujso de entrada/salida.");
            }

        }

            }






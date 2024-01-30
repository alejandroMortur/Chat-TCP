package clienteChat;

//imports lectura entrada y salida
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class HiloclienteUIChat implements Runnable{

    private UIChat UIChat = null;
    private static boolean enChat = true;

    private MulticastSocket redBroadcast = null;

    private static String MULTICAST_ADDRESS;
    private static int MULTICAST_PORT;

    private static  byte[] buffer = new byte[8096];

    public HiloclienteUIChat(UIChat UIChat, MulticastSocket redBroadcast,String ipMulticast,int multicastPort){

        this.UIChat = UIChat;
        this.redBroadcast = redBroadcast;
        this.MULTICAST_ADDRESS = ipMulticast;
        this.MULTICAST_PORT = multicastPort;

    }

    @Override
    public void run() {

        try {

            boolean mensajeRecibido = false;

            String texto =UIChat.getNombre()+" esta online    ";

            // Crear el DatagramPacket con los datos a enviar
            buffer = texto.getBytes();
            DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);

            redBroadcast.send(paqueteRebote);

            while (enChat){

                //recibir información
                paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);
                redBroadcast.receive(paqueteRebote);

                if (!mensajeRecibido) {

                    int length = paqueteRebote.getLength(); // Obtener el tamaño real del mensaje recibido
                    String mensaje = new String(paqueteRebote.getData(), 0, paqueteRebote.getLength(), StandardCharsets.UTF_8);

                    System.out.println("Información recibida de: " + paqueteRebote.getAddress() + ", mensaje: " + mensaje + " \n");

                    if (mensaje.contains("online")){

                        // Encontrar el índice del primer espacio después del nombre de usuario
                        int indiceEspacioDespuesDeUsuario = mensaje.indexOf(' ', mensaje.indexOf(':') + 2);

                        // Extraer el nombre de usuario utilizando substring
                        String nombreUsuario = mensaje.substring(0, indiceEspacioDespuesDeUsuario);

                    }else if(mensaje.contains("offline")){

                        // Encontrar el índice del primer espacio después del nombre de usuario
                        int indiceEspacioDespuesDeUsuario = mensaje.indexOf(' ', mensaje.indexOf(':') + 2);

                        // Extraer el nombre de usuario utilizando substring
                        String nombreUsuario = mensaje.substring(0, indiceEspacioDespuesDeUsuario);

                    }

                    UIChat.añadirTexto(mensaje);

                    mensajeRecibido = true;

                }else {

                    mensajeRecibido = false;

                }

            }

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

    // Método para detener el hilo
    public static void detener() {

        enChat = false;

    }

}

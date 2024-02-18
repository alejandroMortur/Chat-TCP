package clienteChat;

//imports lectura entrada y salida
import java.io.IOException;
import java.nio.charset.StandardCharsets;

//import net
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class HiloclienteUIChat implements Runnable{

    //variables gestoras interfaz UI
    private UIChat UIChat = null;
    private static boolean enChat = true;
    private MulticastSocket redBroadcast = null;

    //btención datos grupo multicast
    private static String MULTICAST_ADDRESS;
    private static int MULTICAST_PORT;

    //Seteo buffer 
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

            //variables lcoales gestor de mensajes recibidos (booleano) y seteo texto inicial
            boolean mensajeRecibido = false;
            String texto =UIChat.getNombre()+" esta online    ";

            //crea el DatagramPacket con los datos a enviar
            buffer = texto.getBytes();
            DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);

            //envia el datagrama
            redBroadcast.send(paqueteRebote);

            while (enChat){

                //recibe información
                paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);
                redBroadcast.receive(paqueteRebote);

                //si el mensaje se considera como no recibido anteriormente
                if (!mensajeRecibido) {

                    String mensaje = new String(paqueteRebote.getData(), 0, paqueteRebote.getLength(), StandardCharsets.UTF_8);

                    System.out.println("Información recibida de: " + paqueteRebote.getAddress() + ", mensaje: " + mensaje + " \n");

                    //añade mensaje a la interfaz del dialogo chat
                    UIChat.añadirTexto(mensaje);

                    //setea el booleano de recibido el mensaje a true, para considerar el mensaje ya recibido
                    mensajeRecibido = true;

                }else {

                    //setea el booleano de recibido el mensaje a false
                    mensajeRecibido = false;

                }

            }

        } catch (IOException e) {

            //tratamiento errores
            System.out.println("Error: "+e);

        }

    }

    // Método para detener el hilo
    public static void detener() {

        enChat = false;

    }

}

package clienteChat;

//imports lectura entrada y salida
import java.io.IOException;
import java.net.*;


public class HiloclienteUIChat implements Runnable{

    private UIChat UIChat = null;
    private static boolean enChat = true;
    private static byte[] buffer = new byte[1024];

    private MulticastSocket redBroadcast = null;

    private static String MULTICAST_ADDRESS;
    private static int MULTICAST_PORT;

    public HiloclienteUIChat(UIChat UIChat, MulticastSocket redBroadcast,String ipMulticast,int multicastPort){

        this.UIChat = UIChat;
        this.redBroadcast = redBroadcast;
        this.MULTICAST_ADDRESS = ipMulticast;
        this.MULTICAST_PORT = multicastPort;

    }

    @Override
    public void run() {

        try {

            byte[] buffer = new byte[4086];
            boolean mensajeRecibido = false;

            while (enChat){

                //recibir información
                DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);
                redBroadcast.receive(paqueteRebote);

                if (!mensajeRecibido) {

                    String mensaje = new String(paqueteRebote.getData(), 0, paqueteRebote.getLength());
                    System.out.println("Información recibida de: " + paqueteRebote.getAddress() + ", mensaje: " + mensaje + " \n");

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

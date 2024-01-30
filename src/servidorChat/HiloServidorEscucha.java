package servidorChat;

//imports io
import java.io.*;

//imports net
import java.net.*;

//import tratamiento strings
import java.nio.charset.StandardCharsets;

public class HiloServidorEscucha implements Runnable {

    private static final String MULTICAST_ADDRESS = "239.0.0.1";
    private static final int MULTICAST_PORT = 12345;

    @Override
    public void run() {

        try {

            // Crear un nuevo socket multicast y unirse al grupo multicast
            MulticastSocket redBroadcast = new MulticastSocket(MULTICAST_PORT);
            InetAddress grupo = InetAddress.getByName(MULTICAST_ADDRESS);
            redBroadcast.joinGroup(grupo);

            System.out.println("Servidor de chat iniciado. Esperando mensajes multicast...");

            byte[] buffer = new byte[8096];
            boolean mensajeRecibido = false;

            while (true) {
                // Recibir información multicast
                DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
                redBroadcast.receive(paquete);

                if (!mensajeRecibido) {

                    int length = paquete.getLength(); // Obtener el tamaño real del mensaje recibido
                    String mensaje = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);

                    if (mensaje.contains("offline")) {

                        System.out.println("\n-------------------------------\n");
                        System.out.println(mensaje);
                        System.out.println("\n-------------------------------\n");

                    }
                    if (mensaje.contains("online")){

                        System.out.println("\n-------------------------------\n");
                        System.out.println(mensaje);
                        System.out.println("\n-------------------------------\n");

                    }

                        System.out.println("Información recibida de: " + paquete.getAddress() + ", mensaje: " + mensaje + " \n");

                        // Rebotar información multicast
                        buffer = mensaje.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), MULTICAST_PORT);
                        redBroadcast.send(paqueteRebote);



                    mensajeRecibido = true; // Marcar que se ha recibido un mensaje

                } else {

                    mensajeRecibido = false;

                }
            }

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

}

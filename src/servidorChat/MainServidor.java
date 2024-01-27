package servidorChat;

//imports io
import java.io.IOException;

//import sockets
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;

public class MainServidor {

    private static final int PuertoServer = 6001;
    private static final String MULTICAST_ADDRESS = "239.0.0.1";
    private static final int MULTICAST_PORT = 12345;

    public static void main(String args[]) {

        try {
            ServerSocket serverSocket = new ServerSocket(PuertoServer);

            System.out.println("\n------------------------------------------\n");
            System.out.println("Servidor TCP esperando conexiones en el puerto: " + PuertoServer);
            System.out.println("\n------------------------------------------\n");

            // Crear el socket multicast
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            MulticastSocket multicastSocket = new MulticastSocket();

            // Unirse al grupo multicast
            multicastSocket.joinGroup(group);

            while (true) {

                // Esperar a que un cliente se conecte
                Socket socketCliente = serverSocket.accept();

                // Crear un nuevo hilo para manejar la conexión con el cliente
                Thread hiloCliente = new Thread(new HiloServidor(socketCliente, multicastSocket, group, MULTICAST_PORT));
                hiloCliente.start();

            }

        } catch (IOException e) {

            System.out.println("Error: "+e);
        }

    }

}

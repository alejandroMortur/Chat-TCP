package servidorChat;

//imports io
import java.io.IOException;

//import sockets
import java.net.ServerSocket;
import java.net.Socket;

public class MainServidor {

    private static final int PuertoServer = 6001;

    public static void main(String args[]){

        try {
            ServerSocket serverSocket = new ServerSocket(PuertoServer);

            System.out.println("\n------------------------------------------\n");
            System.out.println("Servidor TCP esperando conexiones en el puerto: " + PuertoServer);
            System.out.println("\n------------------------------------------\n");

            while (true) {
                // Esperar a que un cliente se conecte
                Socket clienteSocket = serverSocket.accept();
                System.out.println("\n------------------------------------------\n");
                System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress().getHostAddress());
                System.out.println("\n------------------------------------------\n");

                // Crear un hilo para manejar la conexión con el cliente
                Thread hiloCliente = new Thread(new HiloServidor(clienteSocket));
                hiloCliente.start();

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}

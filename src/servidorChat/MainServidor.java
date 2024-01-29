package servidorChat;

//import io
import java.io.IOException;

//impor net
import java.net.*;


public class MainServidor {

    private static final int PuertoServer = 6001;

    public static void main(String args[]) {

        try {

            // Esperar a que un cliente se conecte
            ServerSocket serverSocket = new ServerSocket(PuertoServer);
            System.out.println("\n------------------------------------------\n");
            System.out.println("Servidor TCP esperando conexiones en el puerto: " + PuertoServer);
            System.out.println("\n------------------------------------------\n");

            Thread hiloClienteEscuchar = new Thread(new HiloServidorEscucha());
            hiloClienteEscuchar.start();

            while (true) {

                Socket socketCliente = serverSocket.accept();
                // Crear un nuevo hilo para manejar la conexión con el cliente
                Thread hiloCliente = new Thread(new HiloServidorValidarUsuarios(socketCliente));
                hiloCliente.start();

            }

        } catch (IOException e) {

            System.out.println("Error en el servidor: " + e);

        }

    }

}


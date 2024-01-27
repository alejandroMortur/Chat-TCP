package servidorChat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;

public class HiloServidor implements Runnable {

    private Socket socketCliente;
    private MulticastSocket multicastSocket;
    private InetAddress group;
    private int puertoMulticast;
    private static String archivo = "./src/servidorChat/usuarios.txt";

    public HiloServidor(Socket socket, MulticastSocket multicastSocket, InetAddress group, int puertoMulticast) {

        this.socketCliente = socket;
        this.multicastSocket = multicastSocket;
        this.group = group;
        this.puertoMulticast = puertoMulticast;

    }

    @Override
    public void run() {
        try {

            BufferedReader lector = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream(), true);

            // Autenticación del cliente (puedes modificar esta parte según tus necesidades)
            String nombreCliente = lector.readLine();
            String contraseñaCliente = lector.readLine();

            if (nombreCliente != null && buscarCredencialesEnArchivo(archivo, nombreCliente,contraseñaCliente) &&
                    contraseñaCliente != null) {

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nUsuario autenticado: " + nombreCliente);
                System.out.println("\nContraseña correcta: " + contraseñaCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Autenticación exitosa");
                // Aquí puedes implementar la lógica para manejar el chat entre los usuarios autenticados

                escritor.write(50);

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nUsuario conectado: " + nombreCliente + " correctamente");
                System.out.println("\n---------------------------------------------------------\n");

                String mensaje;
                while ((mensaje = lector.readLine()) != null) {

                    enviarMensajeMulticast(mensaje, multicastSocket, group, puertoMulticast);

                }

            } else {

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nError de autenticación para el usuario: " + nombreCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Error de autenticación");
            }

            // Cerrar la conexión con el cliente
            socketCliente.close();

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

    public static boolean buscarCredencialesEnArchivo(String nombreArchivo, String nombreUsuario, String contraseñaUsuario) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                // Dividir la línea en usuario y contraseña
                String[] partes = linea.split(" ");

                if (partes.length == 2 && partes[0].equals(nombreUsuario) && partes[1].equals(contraseñaUsuario)) {

                    return true;

                }

            }

        }

        return false;

    }
    public static void enviarMensajeMulticast(String mensaje, MulticastSocket multicastSocket, InetAddress group, int puerto) {

        try {

            byte[] data = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, group, puerto);
            multicastSocket.send(packet);

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

}

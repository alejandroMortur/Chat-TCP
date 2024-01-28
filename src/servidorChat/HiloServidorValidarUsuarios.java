package servidorChat;

//imports lectura y io
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;

//import sockets
import java.net.Socket;

public class HiloServidorValidarUsuarios implements Runnable {

    private Socket socketCliente;
    private byte[] buffer = new byte[1024];
    private static String nombreCliente = "";
    private static String archivo = "./src/servidorChat/usuarios.txt";

    public HiloServidorValidarUsuarios(Socket socket) {

        this.socketCliente = socket;

    }

    @Override
    public void run() {
        try {

            BufferedReader lector = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream(), true);

            // Autenticación del cliente (puedes modificar esta parte según tus necesidades)
            nombreCliente = lector.readLine();
            String contraseñaCliente = lector.readLine();

            if (nombreCliente != null && buscarCredencialesEnArchivo(archivo, nombreCliente,contraseñaCliente) &&
                    contraseñaCliente != null) {

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nUsuario autenticado: " + nombreCliente);
                System.out.println("\nContraseña correcta: " + contraseñaCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Autenticación exitosa");

                escritor.write(50);

                String mensaje = lector.readLine();
                if (mensaje.startsWith("CONNECT")) {

                    String nombreUsuario = mensaje.split(" ")[1];

                    System.out.println("\n---------------------------------------------------------\n");
                    System.out.println(mensaje+" correctamente");
                    System.out.println("\n---------------------------------------------------------\n");
                    // Realizar acciones para confirmar la conexión del usuario
                }

                // Cerrar la conexión con el cliente
                socketCliente.close();

            } else {

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nError de autenticación para el usuario: " + nombreCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Error de autenticación");
            }

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

}

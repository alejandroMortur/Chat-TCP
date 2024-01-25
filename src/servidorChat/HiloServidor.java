package servidorChat;

//imports IO
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileReader;

//imports socket
import java.net.Socket;


public class HiloServidor implements Runnable {

    private String archivo = "./src/servidorChat/usuarios.txt";
    private String nombreCliente = "";
    private String contraseñaCliente = "";
    private final Socket clienteSocket;
    private BufferedReader lector;
    private PrintWriter escritor;

    public HiloServidor(Socket clienteSocket) {

        this.clienteSocket = clienteSocket;

        try {

            lector = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            escritor = new PrintWriter(clienteSocket.getOutputStream(), true);

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void run() {
        try {

            // Autenticación del cliente (puedes modificar esta parte según tus necesidades)
            nombreCliente = lector.readLine();
            contraseñaCliente = lector.readLine();

            if (nombreCliente != null && buscarPalabraEnArchivo(archivo, nombreCliente)) {

                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nUsuario encontrado: "+nombreCliente);

                // Enviar respuesta positiva al cliente
                if(contraseñaCliente != null && buscarPalabraEnArchivo(archivo,contraseñaCliente)){

                    System.out.println("\nContraseña encontrada: "+contraseñaCliente);
                    System.out.println("\n-------------------------------------------------------\n");

                    escritor.println(50);
                    System.out.println("\n---------------------------------------------------------------------\n");
                    System.out.println("EL usuario: "+nombreCliente+" se ha registrado correctamente con la contraseña: "+contraseñaCliente);
                    System.out.println("\n---------------------------------------------------------------------\n");

                }

            } else {

                System.out.println("\n-------------------------------------------------------------------------\n");
                System.out.println("EL usuario: "+nombreCliente+" con la contraseña: "+contraseñaCliente+" no se ha encontrado en el servidor");
                System.out.println("\n-------------------------------------------------------------------------\n");

                // Enviar respuesta negativa al cliente
                escritor.println(0);

                return;

            }

            // Bucle para escuchar al cliente y manejar su desconexión
            while (true) {


            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                clienteSocket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    public static boolean buscarPalabraEnArchivo(String nombreArchivo, String palabra) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                if (linea.contains(palabra)) {

                    return true;

                }

            }

        }

        return false;

    }

}

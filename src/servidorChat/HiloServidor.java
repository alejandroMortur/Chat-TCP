package servidorChat;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HiloServidor implements Runnable {

    private String archivo = "./src/servidorChat/usuarios.txt";
    private String nombreCliente = "";
    private String contraseñaCliente = "";
    private Socket clienteSocket;
    private BufferedReader lector;
    private PrintWriter escritor;
    private static List<HiloServidor> clientesConectados = new ArrayList<>();

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

                System.out.println("usuario encontrado");

                // Enviar respuesta positiva al cliente
                if(contraseñaCliente != null && buscarPalabraEnArchivo(archivo,contraseñaCliente)){

                    System.out.println("contraseña encontrada");

                    escritor.println(50);
                    System.out.println("\n---------------------------------------------------------------------\n");
                    System.out.println("EL usuario: "+nombreCliente+" se ha registrado correctamente con la contraseña: "+contraseñaCliente);
                    System.out.println("\n---------------------------------------------------------------------\n");
                    broadcast(nombreCliente + " se ha unido al chat.");

                }

            } else {

                System.out.println("\n---------------------------------------------\n");
                System.out.println("EL usuario: "+nombreCliente+" con la contraseña: "+contraseñaCliente+" no se ha encontrado en el servidor");
                System.out.println("\n---------------------------------------------\n");

                // Enviar respuesta negativa al cliente
                escritor.println(0);

                return;

            }

            // Agregar el hilo del cliente a la lista
            clientesConectados.add(this);

            // Bucle para escuchar al cliente y manejar su desconexión
            while (true) {
                String mensaje = lector.readLine();
                if (mensaje == null) {

                    // El cliente se ha desconectado, salir del bucle
                    break;

                }

                // Leer mensajes del cliente y reenviarlos a todos los clientes
                broadcast(nombreCliente + ": " + mensaje);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            // Eliminar el hilo del cliente de la lista y cerrar la conexión
            clientesConectados.remove(this);
            broadcast(nombreCliente + " se ha desconectado.");

            try {

                clienteSocket.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

    private void broadcast(String mensaje) {
        // Reenviar el mensaje a todos los clientes conectados
        for (HiloServidor cliente : clientesConectados) {

            cliente.escritor.println(mensaje);

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

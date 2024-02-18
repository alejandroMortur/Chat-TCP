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

    //variables locales gestión credenciales (Fichero, nombre cliente y socket)
    private Socket socketCliente;
    private static String nombreCliente = "";
    private static String archivo = "./src/servidorChat/usuarios.txt";

    //cnstructor
    public HiloServidorValidarUsuarios(Socket socket) {

        this.socketCliente = socket;

    }

    @Override
    public void run() {
        try {

            //Componentes necesarios para gestionar la lectura correcta del fichero de credenciales
            BufferedReader lector = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
            PrintWriter escritor = new PrintWriter(socketCliente.getOutputStream(), true);

            // Autenticación del cliente (puedes modificar esta parte según tus necesidades)
            nombreCliente = lector.readLine();
            String contraseñaCliente = lector.readLine();

            //condición if necesaria para gestionar que las credenciales son correctas y no estan vacias
            if (nombreCliente != null && buscarCredencialesEnArchivo(archivo, nombreCliente,contraseñaCliente) &&
                    contraseñaCliente != null) {

                //mensaje de confirmación cliente conectado
                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nUsuario autenticado: " + nombreCliente);
                System.out.println("\nContraseña correcta: " + contraseñaCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Autenticación exitosa");

                //envio codigo conexión correcta
                escritor.write(50);

                //lectura de mensaje del cliente (este confirma que recibio el entero con otro mensaje con la muletilla "Connect")
                String mensaje = lector.readLine();

                //Si el mensaje recibido lleva esa  muletilla
                if (mensaje.startsWith("CONNECT")) {

                    //saca el nobmre de toda la cadena del mensaje
                    String nombreUsuario = mensaje.split(" ")[1];

                    //mensaje confirmación conexión al grupo multicast exitosa
                    System.out.println("\n---------------------------------------------------------\n");
                    System.out.println(nombreUsuario+" correctamente");
                    System.out.println("\n---------------------------------------------------------\n");

                }

                // Cerrar la conexión con el cliente
                socketCliente.close();

            } else {

                //en caso de que la autenticación sea incorrecta , mensaje terminal
                System.out.println("\n---------------------------------------------------------\n");
                System.out.println("\nError de autenticación para el usuario: " + nombreCliente);
                System.out.println("\n---------------------------------------------------------\n");

                escritor.println("Error de autenticación");
            }

        } catch (IOException e) {

            //tratamiento de errores
            System.out.println("Error: "+e);

        }

    }

    /*
     * Metodo encargado de gestionar si las strins recibidas estan en la misma 
     * linea del fichero (de estarlo se considera que el usuario aportado es correcto)
     * devuelve un booleano en cada caso a modo de confirmación
     */
    public static boolean buscarCredencialesEnArchivo(String nombreArchivo, String nombreUsuario, String contraseñaUsuario) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {

            //lee el fichero linea a linea para tratar de encontrar en la misma linea las credenciales
            String linea;
            while ((linea = br.readLine()) != null) {

                // Dividir la línea en usuario y contraseña
                String[] partes = linea.split(" ");

                //de estar en la misma linea las 2 partes
                if (partes.length == 2 && partes[0].equals(nombreUsuario) && partes[1].equals(contraseñaUsuario)) {

                    return true;//en caso de doble coincidencia true
                }

            }

        }

        return false;//en caso de no haber doble coindicencia false;

    }

}

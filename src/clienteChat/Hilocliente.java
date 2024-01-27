package clienteChat;

//imports lectura entrada y salida
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import sockets
import java.net.Socket;

public class Hilocliente implements Runnable{

    private UIChat UIChat = null;
    private Socket clienteSocket = null;
    private boolean enChat = true;

    public Hilocliente(UIChat UIChat, Socket clienteSocket){

        this.UIChat = UIChat;
        this.clienteSocket = clienteSocket;

    }

    @Override
    public void run() {
        try {

            // Obtener el flujo de entrada del cliente
            InputStream input = clienteSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Bucle para recibir mensajes continuamente
            while (enChat){

                // Leer mensaje del servidor
                String mensaje = reader.readLine();
                // Verificar si el mensaje no es nulo y no está vacío

                if (mensaje != null && !mensaje.isEmpty()) {

                    // Mostrar mensaje en la interfaz de chat
                    UIChat.añadirTexto(mensaje);

                }

            }

            // Cerrar el socket cliente
            clienteSocket.close();

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

    // Método para detener el hilo
    public void detener() {

        enChat = false;

    }

}

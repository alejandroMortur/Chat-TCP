package clienteChat;

//imports IO
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//imports swing
import javax.swing.JOptionPane;
import javax.swing.JFrame;

//import sockets
import java.net.*;

public class Main {

    private static final String ipServer = "127.0.0.1";

    private static final int puertoServer = 6001;
    private static UIRegistro UIRegistro = null;
    private static UIChat UI = null;
    private static final String ipMulticast = "239.0.0.1";
    private static final int multicastPort = 12345;

    public static void main(String[] args) {

        try {

            // Crear el socket TCP y obtener los streams de entrada y salida
            Socket clienteSocket = new Socket(ipServer, puertoServer);
            InputStream input = clienteSocket.getInputStream();
            OutputStream output = clienteSocket.getOutputStream();

            // Interfaz de registro de usuario
            UIRegistro = new UIRegistro(clienteSocket, input, output);
            UIRegistro.setContentPane(UIRegistro.panel_Registro);
            UIRegistro.setTitle("Entrada chat");
            UIRegistro.setResizable(false);
            UIRegistro.setSize(400, 500);
            UIRegistro.setVisible(true);
            UIRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Error al conectarse al servidor\n\nReinicie el programa y intentelo de nuevo", "Error de conexión", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Error: " + e);

        }

    }

    // Método para lanzar la interfaz de chat
    public static void lanzarChat(String nombre) {
        try {

            // Crear un nuevo socket multicast y unirse al grupo multicast
            MulticastSocket redBroadcast = new MulticastSocket(multicastPort);
            InetAddress grupo = InetAddress.getByName(ipMulticast);
            redBroadcast.joinGroup(grupo);

            // Cerrar la ventana de registro
            UIRegistro.dispose();

            // Inicializar la interfaz de chat
            UI = new UIChat(nombre, redBroadcast);
            UI.setContentPane(UI.panel_chat);

            UI.setTitle("Chat online");
            UI.setResizable(false);
            UI.setSize(840, 720);
            UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            UI.setVisible(true);

            HiloclienteUIChat hiloClienteUIChat = new HiloclienteUIChat(UI, redBroadcast,ipMulticast ,multicastPort);
            Thread hilo = new Thread(hiloClienteUIChat);
            hilo.start();



        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Error al unirse al chat: " + e.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error al unirse al chat: " + e.getMessage());

        }

    }

    public static void detenerhilo(){

        HiloclienteUIChat.detener();

    }

}

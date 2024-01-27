package clienteChat;

//imports IO
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//imports swing
import javax.swing.JOptionPane;
import javax.swing.JFrame;

//import sockets
import java.net.Socket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;

public class Main {

    private static final String ipServer = "127.0.0.1";

    private static final String ipMulticast = "239.0.0.1";
    private static int multicastPort = 12345;
    private static final int puertoServer = 6001;
    private static UIRegistro UIRegistro = null;
    private static UIChat UI = null;

    public static void main(String[] args) {

        try {

            // Crear el socket TCP y obtener los streams de entrada y salida
            Socket clienteSocket = new Socket(ipServer, puertoServer);
            InputStream input = clienteSocket.getInputStream();
            OutputStream output = clienteSocket.getOutputStream();

            // Crear el socket multicast y unirse al grupo
            InetAddress group = InetAddress.getByName(ipMulticast);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(group);

            // Interfaz de registro de usuario
            UIRegistro = new UIRegistro(clienteSocket, input, output);
            UIRegistro.setContentPane(UIRegistro.panel_Registro);
            UIRegistro.setTitle("Entrada chat");
            UIRegistro.setSize(400, 400);
            UIRegistro.setVisible(true);
            UIRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, "Error al conectarse al servidor\n\nReinicie el programa y intentelo de nuevo", "Error de conexión", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Error: " + e);

        }

    }

    // Método para lanzar la interfaz de chat
    public static void lanzarChat(Socket clienteSocket, InputStream input, OutputStream output, MulticastSocket multicastSocket, InetAddress group, int multicastPort,String nombre) {

        UIRegistro.dispose(); // Cerrar ventana de registro
        UI = new UIChat(multicastSocket, group, multicastPort,nombre); // Inicializar la interfaz de chat
        UI.setContentPane(UI.panel_chat);
        UI.setTitle("Chat online");
        UI.setSize(700, 600);
        UI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        UI.setVisible(true);

    }

    public static void enviarMensajeMulticast(String mensaje, MulticastSocket multicastSocket, InetAddress group, int multicastPort) {
        try {
            byte[] data = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, group, multicastPort);
            multicastSocket.send(packet);
        } catch (IOException e) {
            System.out.println("Error al enviar mensaje multicast: " + e);
        }
    }

}

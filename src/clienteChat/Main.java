package clienteChat;

//imports IO
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
            InetAddress group = InetAddress.getByName("239.0.0.1");
            int multicastPort = 12345;
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

        // Hilo para recibir mensajes multicast
        Thread thread = new Thread(() -> {
            try {

                byte[] buffer = new byte[1024];
                while (true) {

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    multicastSocket.receive(packet);
                    String mensaje = new String(packet.getData(), 0, packet.getLength());
                    UI.añadirTexto(mensaje); // Agregar mensaje al chat

                }

            } catch (IOException e) {

                System.out.println("Error: "+e);

            }

        });

        thread.start();

    }
}

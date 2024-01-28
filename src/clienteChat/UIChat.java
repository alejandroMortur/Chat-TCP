package clienteChat;

//imports swing
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

//import eventos
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//imports io y red
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Objects;

public class UIChat extends JFrame {

    public JPanel panel_chat;
    private JTextField entrada_texto;
    private JButton boton_enviar;
    private JTextPane panel_texto;
    private JList<String> listado_usuarios;
    private final DefaultListModel<String> modelo_lista_usuarios;
    private JLabel etiqueta_conectado;
    private JLabel etiqueta_usuario;
    private String nombre = "";
    private String texto = "";
    private MulticastSocket redBroadcast = null;
    private static final String ipMulticast = "239.0.0.1";
    private static final int multicastPort = 12345;

    public UIChat(String nombre, MulticastSocket redBroadcast){

        this.nombre = nombre;
        this.redBroadcast = redBroadcast;

        modelo_lista_usuarios = new DefaultListModel<>();
        listado_usuarios.setModel(modelo_lista_usuarios);

        añadirTextoListadoUsuario(nombre);

        etiqueta_usuario.setText("Usuario: " + nombre);

        setEventos();

    }

    public void setEventos() {

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                try {

                    System.out.println("La ventana se está cerrando...");

                    texto ="El usuario: " +nombre+" se ha desconectado ";

                    // Crear el DatagramPacket con los datos a enviar
                    byte [] buffer = texto.getBytes();
                    DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipMulticast), multicastPort);

                    redBroadcast.send(paqueteRebote);

                    //cese hilo
                    Main.detenerhilo();

                    //desconexion socket multiple
                    redBroadcast.close();

                } catch (IOException ex) {

                    System.out.println("Error: "+ex);

                }

            }

        });

        boton_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                texto = entrada_texto.getText();
                if(!Objects.equals(texto, "") && texto != null){

                    try {

                        texto = nombre +": "+ texto;

                        // Crear el DatagramPacket con los datos a enviar
                        byte [] buffer = texto.getBytes();
                        DatagramPacket paqueteRebote = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(ipMulticast), multicastPort);

                        // Enviar el paquete a través del socket multicast
                        redBroadcast.send(paqueteRebote);

                        // Mostrar mensaje de éxito en la consola
                        System.out.println("Mensaje: " + texto + " enviado correctamente al servidor \n");

                        // Limpiar el campo de entrada
                        entrada_texto.setText("");

                    } catch (IOException ex) {

                        // Manejar errores de E/S
                        System.out.println("Error al enviar el mensaje: " + ex.getMessage());

                    }

                } else {

                    // Manejar caso en que el texto esté vacío
                    System.out.println("El texto está vacío");

                }

            }

        });

    }

    public void añadirTexto(String texto) {

        StyledDocument doc = panel_texto.getStyledDocument();
        try {

            doc.insertString(doc.getLength(), texto + "\n", null);

        } catch (BadLocationException e) {

            System.out.println("Error al añadir texto: " + e);

        }

    }

    public void añadirTextoListadoUsuario(String texto) {

        DefaultListModel modelo = (DefaultListModel) listado_usuarios.getModel();
        modelo.addElement("\n" + nombre);

    }

}

package clienteChat;

//imports swing
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

//imports listas
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

//imports eventos
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//imports lectura entrada y salida
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UIChat extends JFrame {

    private final MulticastSocket multicastSocket;
    private InetAddress group;
    private final int multicastPort;
    public  JPanel panel_chat;
    private JTextField entrada_texto;
    private JButton boton_enviar;
    private JTextPane panel_texto;
    private JList<String> listado_usuarios;
    private final DefaultListModel<String> modelo_lista_usuarios;
    private JLabel etiqueta_conectado;
    private JLabel etiqueta_usuario;
    private String nombre = "";
    private DefaultListModel<String> modelo;

    public UIChat(MulticastSocket multicastSocket, InetAddress group, int multicastPort, String nombre) {

        this.multicastSocket = multicastSocket;
        this.group = group;
        this.multicastPort = multicastPort;
        this.nombre = nombre;

        // Inicializar el modelo de lista de usuarios
        modelo_lista_usuarios = new DefaultListModel<>();
        listado_usuarios.setModel(modelo_lista_usuarios); // Inicializar listado_usuarios

        añadirTextoListadoUsuario(nombre);

        // Inicializar el componente etiqueta_usuario con el nombre de usuario
        etiqueta_usuario.setText("Usuario: " + nombre);

        // Llamar al método para configurar eventos
        setEventos();

    }


    //metodo que setea los eventos de la interfaz
    public void setEventos() {

        System.out.println("\n---------------------------------------------\n");
        System.out.println("INTERFAZ CHAT LANZADA");
        System.out.println("\n---------------------------------------------\n");

        //evento cerrar ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                int option = JOptionPane.showConfirmDialog(
                        UIChat.this,
                        "¿Estás seguro de que quieres cerrar la aplicación?",
                        "Confirmar cierre",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {

                    System.out.println("Cerrando la aplicación...");
                    System.exit(0); // Cierra la aplicación

                }

            }
        });

        //evento click boton
        boton_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String entrada_teclado = entrada_texto.getText();

                añadirTexto(entrada_teclado);

            }

        });

    }

    //-------------------------------------------------------
    //metodo para añadir texto en la interfaz de mensajes
    public void añadirTexto(String texto) {

        StyledDocument doc = panel_texto.getStyledDocument();

        try {

            doc.insertString(doc.getLength(),nombre+": " + texto + "\n", null);

        } catch (BadLocationException e) {

            System.out.println("Error: "+e);

        }

    }
    // Método para enviar un mensaje al grupo multicast
    public void enviarMensajeMulticast(String mensaje) {

        try {

            byte[] data = mensaje.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, group, multicastPort);
            multicastSocket.send(packet);

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

    // Método que se llama cuando se hace clic en el botón de enviar
    public void botonEnviarActionPerformed(ActionEvent e) {

        if(entrada_texto.getText() != null && entrada_texto.getText() != ""){

            String mensaje = entrada_texto.getText();
            añadirTexto(mensaje); // Agregar el mensaje al panel de texto
            enviarMensajeMulticast(mensaje); // Enviar el mensaje al grupo multicast
            entrada_texto.setText(""); // Limpiar el campo de entrada

        }

    }

    //metodo añadir texto en el panel de usuarios
    public void añadirTextoListadoUsuario(String texto) {

        DefaultListModel modelo = (DefaultListModel) listado_usuarios.getModel();

        modelo.addElement("\n"+nombre+"\n");

    }

}

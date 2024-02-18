package clienteChat;

//imports swing
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class UIChat extends JFrame {

    //variables gestoras elementos UI
    public JPanel panel_chat;
    private JTextField entrada_texto;
    private JButton boton_enviar;
    private JTextPane panel_texto;
    private JLabel etiqueta_usuario;
    private String nombre = "";
    private String texto = "";

    //variables locales
    private MulticastSocket redBroadcast = null;
    private static final String ipMulticast = "239.0.0.1";
    private static final int multicastPort = 12345;

    public String getNombre(){

        return nombre;

    }

    //Constructor
    public UIChat(String nombre, MulticastSocket redBroadcast){

        this.nombre = nombre;
        this.redBroadcast = redBroadcast;

        //Setea autoscroll pare evitar perder texto cuandl el dialogo se llena
        panel_texto.setAutoscrolls(true);

        //Setea la etiequeta con el nombre del usuario que ha iniciado sesión
        etiqueta_usuario.setText("Usuario: " + nombre);

        //Setea los eventos
        setEventos();

    }

    //metodo gestor de los eventos de la interfaz
    public void setEventos() {

        //Creación evento cierre de ventana por parte del usuario
        this.addWindowListener(new WindowAdapter() {
            @Override

            public void windowClosing(WindowEvent e) {

                try {

                    //mensaje de consola confirmación cierre
                    System.out.println("La ventana se está cerrando...");

                    String mensajeDesconectado = nombre + " esta offline   ";

                    // Convertir el mensaje a bytes utilizando UTF-8
                    byte[] bufferMensaje = mensajeDesconectado.getBytes(StandardCharsets.UTF_8);

                    // Crear el DatagramPacket con los datos a enviar
                    DatagramPacket paqueteRebote = new DatagramPacket(bufferMensaje, bufferMensaje.length, InetAddress.getByName(ipMulticast), multicastPort);

                    redBroadcast.send(paqueteRebote);

                    //cese hilo
                    Main.detenerhilo();

                    //desconexion socket multiple
                    redBroadcast.close();

                } catch (IOException ex) {

                    //tratamiento de errores
                    System.out.println("Error: "+ex);

                }

            }

        });

        //creación evento boton de enviar UI
        boton_enviar.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                //saca el texto del textbox de la interfaz
                texto = entrada_texto.getText();

                if(!Objects.equals(texto, "") && texto != null){

                    try {

                        //prepara el texto para enviarlo por el multicast
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

    //metodo encargado de añadir el texto al dialogo del chat siempre que reciba un mensaje nuevo
    public void añadirTexto(String texto) {

        StyledDocument doc = panel_texto.getStyledDocument();

        try {

            //añade el texto a la interfaz
            doc.insertString(doc.getLength(), texto + "\n", null);

        } catch (BadLocationException e) {

            System.out.println("Error al añadir texto: " + e);

        }

    }

}

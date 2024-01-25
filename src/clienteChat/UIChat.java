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
import java.io.InputStream;
import java.io.OutputStream;

public class UIChat extends JFrame {

    public JPanel panel_chat;

    private InputStream input = null;
    private OutputStream output = null;
    private JTextField entrada_texto;
    private JButton boton_enviar;
    private JTextPane panel_texto;
    private JList<String> listado_usuarios;
    private DefaultListModel<String> modelo_lista_usuarios;
    private JLabel etiqueta_conectado;
    private JLabel etiqueta_usuario;

    public UIChat(InputStream input, OutputStream output) {

        this.input = input;
        this.output = output;

        modelo_lista_usuarios = new DefaultListModel<>();
        listado_usuarios = new JList<>(modelo_lista_usuarios);

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

                // envío de datos al servidor
                try {

                    output.write(entrada_teclado.getBytes());

                } catch (IOException ex) {

                    throw new RuntimeException(ex);

                }

                añadirTexto(entrada_teclado);
            }
        });
    }

    //-------------------------------------------------------
    //metodo para añadir texto en la interfaz de mensajes
    public void añadirTexto(String texto) {
        StyledDocument doc = panel_texto.getStyledDocument();

        try {

            doc.insertString(doc.getLength(), texto + "\n", null);

        } catch (BadLocationException e) {

            e.printStackTrace();

        }

    }

    //metodo añadir texto en el panel de usuarios
    public void añadirTextoListadoUsuario(String texto) {

        // Añadir el texto al modelo de lista
        modelo_lista_usuarios.addElement(texto);

    }

}

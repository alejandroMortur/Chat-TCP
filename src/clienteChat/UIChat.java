package clienteChat;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UIChat extends JFrame {

    private final MulticastSocket multicastSocket;
    private InetAddress group;
    private final int multicastPort;
    public JPanel panel_chat;
    protected JTextField entrada_texto;
    protected JButton boton_enviar;
    private JTextPane panel_texto;
    private JList<String> listado_usuarios;
    private final DefaultListModel<String> modelo_lista_usuarios;
    private JLabel etiqueta_conectado;
    private JLabel etiqueta_usuario;
    private String nombre = "";

    public UIChat(MulticastSocket multicastSocket, InetAddress group, int multicastPort, String nombre) {

        this.multicastSocket = multicastSocket;
        this.group = group;
        this.multicastPort = multicastPort;
        this.nombre = nombre;

        modelo_lista_usuarios = new DefaultListModel<>();
        listado_usuarios.setModel(modelo_lista_usuarios);

        añadirTextoListadoUsuario(nombre);

        etiqueta_usuario.setText("Usuario: " + nombre);

        setEventos();

    }

    public void setEventos() {
        boton_enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                botonEnviarActionPerformed(e);

            }

        });

    }

    public void añadirTexto(String texto) {

        StyledDocument doc = panel_texto.getStyledDocument();
        try {

            doc.insertString(doc.getLength(), nombre + ": " + texto + "\n", null);

        } catch (BadLocationException e) {

            System.out.println("Error al añadir texto: " + e);

        }

    }

    public void botonEnviarActionPerformed(ActionEvent e) {

        String mensaje = entrada_texto.getText();
        if(mensaje != "" && mensaje != null){

            entrada_texto.setText(""); // Limpiar el campo de entrada

        }

    }

    public void añadirTextoListadoUsuario(String texto) {

        DefaultListModel modelo = (DefaultListModel) listado_usuarios.getModel();
        modelo.addElement("\n" + nombre + "\n");

    }

}

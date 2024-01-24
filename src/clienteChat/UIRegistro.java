package clienteChat;

//imports clase swing
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

//imports eventos
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//imports lectura entrada y salida
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//imports Socket
import java.net.Socket;

public class UIRegistro extends JFrame {
    public JPanel panel_Registro;
    private JButton pulsa_registro;
    private JLabel etiqueta_nombre;
    private JTextField campo_nombre;
    private JLabel etiqueta_contraseña;
    private JTextField campo_contraseña;

    //-------variables locales:---------
    private String ipServer = null;
    private int puertoServer = 0;

    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private byte[] buffer = new byte[1024];
    private int intentos = 3;

    public UIRegistro(Socket clienteSocket,InputStream input,OutputStream output) {

        this.clienteSocket = clienteSocket;
        this.input = input;
        this.output = output;

            pulsa_registro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String nombre = campo_nombre.getText();
                    String contraseña = campo_contraseña.getText();

                    try {

                        if (intentos >= 0) {

                            //envio de datos al servidor
                            output.write(nombre.getBytes());
                            output.write(contraseña.getBytes());

                            //tras eso espera la respuesta del servidor
                            int respuesta = input.read(buffer);

                            if (respuesta == 200) {


                                Main.lanzarChat(clienteSocket,input,output);

                            } else {

                                --intentos;
                                JOptionPane.showMessageDialog(pulsa_registro, "Error no esta registrada la contraseña o el usuario en el servidor");

                            }

                        } else {

                            JOptionPane.showMessageDialog(pulsa_registro, "Error ha alcanzado el maximo de intentos");

                        }

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                }

            });

    }

}

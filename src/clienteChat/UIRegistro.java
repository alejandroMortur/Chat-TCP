package clienteChat;

//imports clase swing
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
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
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;

public class UIRegistro extends JFrame {
    public JPanel panel_Registro;
    private JButton pulsa_registro;
    private JLabel etiqueta_nombre;
    private JTextField campo_nombre;
    private JLabel etiqueta_contraseña;
    private JTextField campo_contraseña;
    private JLabel etiqueta_inicio_sesion;

    //-------variables locales:---------
    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private int intentos = 3;

    public UIRegistro(Socket clienteSocket, InputStream input, OutputStream output) {
        this.clienteSocket = clienteSocket;
        this.input = input;
        this.output = output;

        pulsa_registro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre = campo_nombre.getText();
                String contraseña = campo_contraseña.getText();

                if (comprobarEntrada(nombre) && comprobarEntrada(contraseña)) {

                    try {

                        if (intentos >= 0) {
                            // Envío de datos al servidor con un separador
                            output.write((nombre + "\n" + contraseña + "\n").getBytes());

                            // Espera la respuesta del servidor
                            int respuesta = input.read();

                            System.out.println(respuesta);

                            if (respuesta == 65) {

                                //limpieza de campos
                                campo_contraseña.setText("");
                                campo_nombre.setText("");

                                System.out.println("\n---------------------------------------------\n");
                                System.out.println("CREDENCIALES ACEPTADAS POR EL SERVIDOR");
                                System.out.println("\n---------------------------------------------\n");

                                // Lanzar el chat
                                lanzarChat(nombre);

                            } else {

                                --intentos;
                                JOptionPane.showMessageDialog(pulsa_registro, "Error: Usuario o contraseña incorrectos");
                                System.out.println("\n---------------------------------------------\n");
                                System.out.println("CREDENCIALES NO ACEPTADAS POR EL SERVIDOR");
                                System.out.println("\n---------------------------------------------\n");

                            }

                        } else {

                            JOptionPane.showMessageDialog(pulsa_registro, "Error: Ha alcanzado el máximo de intentos");
                            System.out.println("\n---------------------------------------------\n");
                            System.out.println("BLOQUEO INTERFAZ LOGIN POR EXCESO DE INTENTOS");
                            System.out.println("\n---------------------------------------------\n");
                            //limpieza y bloqueo de campos
                            campo_contraseña.setEnabled(false);
                            campo_nombre.setEnabled(false);
                            campo_contraseña.setText("");
                            campo_nombre.setText("");

                        }

                    } catch (IOException ex) {

                        System.out.println("Error: " + e);

                    }

                } else {

                    JOptionPane.showMessageDialog(pulsa_registro, "Error: El nombre de usuario y contraseña deben tener una letra mayuscula o no deben estar vacios los campos");
                    //limpiado de campos
                    campo_contraseña.setText("");
                    campo_nombre.setText("");

                }

            }

        });

    }

    //comprobar si la entrada no está vacía y tiene al menos una letra mayúscula
    public boolean comprobarEntrada(String entrada) {

        if (!entrada.isBlank()) {

            for (char caracter : entrada.toCharArray()) {

                if (Character.isUpperCase(caracter)) {

                    return true; // La cadena tiene al menos una letra mayúscula

                }

            }

            return false; // La cadena no tiene letras mayúsculas

        } else {

            return false; // La cadena está vacía

        }

    }

    // Método para lanzar la ventana de chat
    public void lanzarChat(String nombre) {

        try {

            InetAddress group = InetAddress.getByName("239.0.0.1");
            int multicastPort = 12345;
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(group);
            Main.lanzarChat(clienteSocket, input, output, multicastSocket, group, multicastPort,nombre);

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

}

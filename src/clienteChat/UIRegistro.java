package clienteChat;

//imports clase swing
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
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

public class UIRegistro extends JFrame {

    //variables gestoras UI de registro del usuario
    public JPanel panel_Registro;
    private JButton pulsa_registro;
    private JLabel etiqueta_nombre;
    private JTextField campo_nombre;
    private JLabel etiqueta_contraseña;
    private JLabel etiqueta_inicio_sesion;
    private JCheckBox mostrarContraseña;
    private JPasswordField campo_contraseña;

    //-------variables locales:---------
    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private int intentos = 3;

    public UIRegistro(Socket clienteSocket, InputStream input, OutputStream output) {

        //Aprovechado de elemento bloque main
        this.clienteSocket = clienteSocket;
        this.input = input;
        this.output = output;

        // Creación evento  para quitar o poner * en el campo contraseña
        mostrarContraseña.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                // Verificar si la casilla de verificación está marcada
                if (mostrarContraseña.isSelected()) {

                    // Si está marcada, mostrar el campo de contraseña como un JTextField
                    campo_contraseña.setEchoChar((char) 0); // Mostrar caracteres

                } else {

                    // Si no está marcada, mostrar el campo de contraseña como un JPasswordField
                    campo_contraseña.setEchoChar('*'); // Ocultar caracteres

                }

            }

        });

        //evento gestor encargado de enviar los datos al servidor cuando el usuario da click en el boton de autenticar
        pulsa_registro.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {

                String nombre = campo_nombre.getText();
                String contraseña = campo_contraseña.getText();

                //comprueba si las entradas no estan vacias
                if (comprobarEntrada(nombre) && comprobarEntrada(contraseña)) {

                    try {

                        //gestiona hasta un total de 3 intentos antes de bloquear el programa por fallo credenciales 3 -> 2 -> 1 -> 0
                        if (intentos >= 0) {

                            // Envío de datos al servidor con un separador
                            output.write((nombre + "\n" + contraseña + "\n").getBytes());

                            // Espera la respuesta del servidor
                            int respuesta = input.read();

                            System.out.println(respuesta);

                            //si la respuesta del servidor es el 65 , las credenciasles con correctas
                            if (respuesta == 65) {

                                //limpieza de campos
                                campo_contraseña.setText("");
                                campo_nombre.setText("");

                                //mensaje de confirmación por terminal
                                System.out.println("\n---------------------------------------------\n");
                                System.out.println("CREDENCIALES ACEPTADAS POR EL SERVIDOR");
                                System.out.println("\n---------------------------------------------\n");

                                //enviar mensaje confirmación conexión
                                String mensaje = "CONNECT " + nombre;
                                output.write(mensaje.getBytes());

                                // Lanzar el chat del usuario
                                lanzarChat(nombre);

                                //Cierra la conexión TCP por desuso
                                clienteSocket.close();

                            } else {

                                //en caso de fallar el intento , se resta un turno al intento
                                --intentos;

                                //Se muestra mensaje de error
                                JOptionPane.showMessageDialog(pulsa_registro, "Error: Usuario o contraseña incorrectos");

                                //Confirmación por terminal
                                System.out.println("\n---------------------------------------------\n");
                                System.out.println("CREDENCIALES NO ACEPTADAS POR EL SERVIDOR");
                                System.out.println("\n---------------------------------------------\n");

                            }

                        } else {

                            //En caso de fallar todos los intentos , mensaje de error
                            JOptionPane.showMessageDialog(pulsa_registro, "Error: Ha alcanzado el máximo de intentos");

                            //Confirmación maximo intentos por terminal
                            System.out.println("\n---------------------------------------------\n");
                            System.out.println("BLOQUEO INTERFAZ LOGIN POR EXCESO DE INTENTOS");
                            System.out.println("\n---------------------------------------------\n");

                            //limpieza y bloqueo de campos
                            campo_contraseña.setEnabled(false);
                            campo_nombre.setEnabled(false);
                            campo_contraseña.setText("");
                            campo_nombre.setText("");

                            //Cierre socket con el servidor
                            clienteSocket.close();

                        }

                    } catch (IOException ex) {

                        //tratamiento de errores
                        System.out.println("Error: " + e);

                    }

                } else {

                    //en caso de que el nombre o la contraseña no cumplan los estandares basicos (una mayuscula como minimo), mensaje de error
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

            //se une al grupo multicast del servidor
            InetAddress group = InetAddress.getByName("239.0.0.1");
            int multicastPort = 12345;

            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(group);

            //lanza el sistema del chat
            Main.lanzarChat(nombre);

        } catch (IOException e) {

            //tratamiento de errores
            System.out.println("Error: "+e);

        }

    }

}

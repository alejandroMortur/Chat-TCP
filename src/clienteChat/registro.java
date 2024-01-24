package clienteChat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class registro extends JFrame {
    public JPanel panel_Registro;
    private JButton pulsa_registro;
    private JLabel etiqueta_nombre;
    private JTextField campo_nombre;
    private JLabel etiqueta_contraseña;
    private JTextField campo_contraseña;

    //variables locales:
    private String ipServer = "127.0.0.1";
    private int puertoServer  = 6001;

    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private byte[] buffer = new byte[1024];
    private int intentos = 3;

    public  registro() {

        try {

            clienteSocket = new Socket(ipServer, puertoServer);
            input = clienteSocket.getInputStream();
            output = clienteSocket.getOutputStream();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }
        pulsa_registro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre = campo_nombre.getText();
                String contraseña = campo_contraseña.getText();

                try {

                    if(intentos >= 0){

                        //envio de datos al servidor
                        output.write(nombre.getBytes());
                        output.write(contraseña.getBytes());

                        //tras eso espera la respuesta del servidor
                        int respuesta = input.read(buffer);

                        if(respuesta == 200){

                            Hilocliente hilo = new Hilocliente(clienteSocket,input,output);
                            Thread hiloLanzable = new Thread(hilo);

                            hiloLanzable.start();

                        }else{

                            --intentos;
                            JOptionPane.showMessageDialog(pulsa_registro, "Error no esta registrada la contraseña o el usuario en el servidor");

                        }

                    }else{

                        JOptionPane.showMessageDialog(pulsa_registro, "Error ha alcanzado el maximo de intentos");

                    }

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }

        });

    }

}

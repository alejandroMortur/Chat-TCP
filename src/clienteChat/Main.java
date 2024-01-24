package clienteChat;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    private static final String ipServer = "127.0.0.1";
    private static final int puertoServer  = 6001;

    public static void main(String[] args) {
        try {

            Socket clienteSocket = new Socket(ipServer, puertoServer);
            InputStream input = clienteSocket.getInputStream();
            OutputStream output = clienteSocket.getOutputStream();

            UIRegistro h = new UIRegistro(clienteSocket ,input,output);

            h.setContentPane(h.panel_Registro);
            h.setTitle("Entrada chat");
            h.setSize(300,400);
            h.setVisible(true);
            h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }catch (IOException e) {

            JOptionPane.showMessageDialog(
                    null,                // Componente padre (null para un cuadro de diálogo independiente)
                    "Error al conectarse al servidor\n\nReinicie el programa y intentelo de nuevo",  // Mensaje a mostrar
                    "Error de conexión",       // Título del cuadro de diálogo
                    JOptionPane.INFORMATION_MESSAGE   // Tipo de mensaje (puede ser ERROR_MESSAGE, WARNING_MESSAGE, etc.)
            );

        }

    }

    public static void  lanzarChat(Socket clienteSocket,InputStream input,OutputStream output){

        UIChat UI = new UIChat();

        Hilocliente hilo = new Hilocliente(clienteSocket, input, output,UI);
        Thread hiloLanzable = new Thread(hilo);

        hiloLanzable.start();

        UI.setContentPane(UI.panel_chat);
        UI.setTitle("Entrada chat");
        UI.setSize(300,400);
        UI.setVisible(true);
        UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
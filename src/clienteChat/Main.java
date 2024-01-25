package clienteChat;

//imports swing
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//imports socket y entradas/salidas
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Main {

    private static final String ipServer = "127.0.0.1";
    private static final int puertoServer  = 6001;
    private static UIRegistro UIRegistro = null;
    private static UIChat UI = null;

    public static void main(String[] args) {
        try {

            //socket y entradas/salidas
            Socket clienteSocket = new Socket(ipServer, puertoServer);
            InputStream input = clienteSocket.getInputStream();
            OutputStream output = clienteSocket.getOutputStream();

            //interfaz registro usuario
            UIRegistro = new UIRegistro(clienteSocket ,input,output);

            UIRegistro.setContentPane(UIRegistro.panel_Registro);
            UIRegistro.setTitle("Entrada chat");
            UIRegistro.setSize(400,400);
            UIRegistro.setVisible(true);
            UIRegistro.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }catch (IOException e) {

            JOptionPane.showMessageDialog(

                    null,                // Componente padre (null para un cuadro de diálogo independiente)
                    "Error al conectarse al servidor\n\nReinicie el programa y intentelo de nuevo",  // Mensaje a mostrar
                    "Error de conexión",       // Título del cuadro de diálogo
                    JOptionPane.INFORMATION_MESSAGE   // Tipo de mensaje (puede ser ERROR_MESSAGE, WARNING_MESSAGE, etc.)

            );

        }

    }

    //metodo lanzar componentes chat (ya registrado)
    public static void  lanzarChat(Socket clienteSocket,InputStream input,OutputStream output){

        //Cierre ventana registro
        UIRegistro.dispose();

        //lanzar interfaz chat
        UI = new UIChat(input,output);

        //lanzar hilo gestor mensajes
        Hilocliente hilo = new Hilocliente(clienteSocket, input, output,UI);
        Thread hiloLanzable = new Thread(hilo);

        //empezar hilo
        hiloLanzable.start();

        //Seteo interfaz
        UI.setContentPane(UI.panel_chat);
        UI.setTitle("Chat conectado");
        UI.setSize(700,600);
        UI.setVisible(true);
        UI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        try {

            //esperar a fin hilo para fin programa
            hiloLanzable.join();

        } catch (InterruptedException e) {

            throw new RuntimeException(e);

        }

    }

}
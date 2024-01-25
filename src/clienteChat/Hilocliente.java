package clienteChat;

//imports lectura entrada y salida
import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

//import sockets
import java.net.Socket;

public class Hilocliente implements Runnable{

    //buffer de lectura socket
    private byte[] buffer = new byte[1024];
    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;

    private UIChat UIChat = null;
    private boolean enChat = true;

    //Setter variable booleana
    public void setEnChat(){

        enChat = false;

    }

    public Hilocliente(Socket clienteSocket,InputStream input,OutputStream output,UIChat UIChat){

        this.clienteSocket = clienteSocket;
        this.input = input;
        this.output = output;
        this.UIChat = UIChat;

    }

    /**
     * Runs this operation.
     */
    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            while (enChat){

                try {

                    int respuesta = input.read(buffer);

                    //si el servidor envia un 200 (información usuario nuevo)
                    if (respuesta == 200) {

                        String entrada = in.readLine();
                        UIChat.añadirTextoListadoUsuario(entrada);

                        //si el servidor envia un 202 (información mensaje nuevo)
                    }else if( respuesta == 202){

                        String entrada = in.readLine();
                        UIChat.añadirTexto(entrada);

                    }else{

                        //mostrar mensaje de error
                        JOptionPane.showMessageDialog(

                                null,                // Componente padre (null para un cuadro de diálogo independiente)
                                "Error en la comunicación con el servidor\n\n",  // Mensaje a mostrar
                                "Codigo de error - 408 (time out)",       // Título del cuadro de diálogo
                                JOptionPane.INFORMATION_MESSAGE   // Tipo de mensaje (puede ser ERROR_MESSAGE, WARNING_MESSAGE, etc.)

                        );

                    }

                } catch (IOException e) {

                    System.out.println("Error: "+e);

                }

            }

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

}

package clienteChat;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Hilocliente implements Runnable{

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

        while (enChat){




            
        }

    }

}

package clienteChat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Hilocliente implements Runnable{

    private Socket clienteSocket = null;
    private InputStream input = null;
    private OutputStream output = null;
    private boolean enChat = true;

    public Hilocliente(Socket cliente,InputStream input,OutputStream output){

        clienteSocket = cliente;
        this.input = input;
        this.output = output;

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

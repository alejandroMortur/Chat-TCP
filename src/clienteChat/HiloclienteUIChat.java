package clienteChat;

//imports lectura entrada y salida

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class HiloclienteUIChat implements Runnable{

    private UIChat UIChat = null;
    private static boolean enChat = true;
    private static final String ipMulticast = "239.0.0.1";
    private static int multicastPort = 12345;

    public HiloclienteUIChat(UIChat UIChat){

        this.UIChat = UIChat;

    }

    @Override
    public void run() {

        try {

            InetAddress group = InetAddress.getByName(ipMulticast);
            MulticastSocket multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.joinGroup(group);

            while (enChat){


            }

        } catch (IOException e) {

            System.out.println("Error: "+e);

        }

    }

    // Método para detener el hilo
    public static void detener() {

        enChat = false;

    }

}

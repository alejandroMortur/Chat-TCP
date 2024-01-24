package clienteChat;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {

        registro h = new registro();

        h.setContentPane(h.panel_Registro);
        h.setTitle("Entrada chat");
        h.setSize(300,400);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
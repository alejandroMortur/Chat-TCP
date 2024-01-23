import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        Formulario h = new Formulario();

        h.setContentPane(h.panel_principal);
        h.setTitle("saludo a todos");
        h.setSize(300,400);
        h.setVisible(true);
        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}
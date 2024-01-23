import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Formulario  extends  JFrame{

    public JPanel panel_principal;
    private JTextField campo_nombre;
    private JLabel etiqueta_nombre;
    private JButton pulsa_registro;

        public Formulario() {
            pulsa_registro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JOptionPane.showMessageDialog(pulsa_registro, "!hola " + campo_nombre.getText());

                }

            });

        }
    }

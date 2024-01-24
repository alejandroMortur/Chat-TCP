import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Formulario  extends  JFrame{

    public JPanel panel_principal;
    private JTextField campo_contraseña;
    private JLabel etiqueta_nombre;
    private JButton pulsa_registro;
    private JLabel etiqueta_contraseña;
    private JTextField campo_nombre;

    public Formulario() {
            pulsa_registro.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    String nombre = campo_nombre.getText();
                    String contraseña = campo_contraseña.getText();

                    JOptionPane.showMessageDialog(pulsa_registro, "Datos obtenidos: Nombre: "+nombre+", Contraseña: "+contraseña);

                }

            });

        }
    }

package biblioteca;

import biblioteca.view.LoginFrame;
import javax.swing.SwingUtilities;

public class BibliotecaEtapa3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true); 
        });
    }
}
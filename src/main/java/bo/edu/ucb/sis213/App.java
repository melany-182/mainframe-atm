package bo.edu.ucb.sis213;

import java.awt.EventQueue;
import bo.edu.ucb.sis213.ui.Login;

public class App {
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}

package bo.edu.ucb.sis213.ui;

import javax.swing.*;
import bo.edu.ucb.sis213.bbdd.Conexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
	private JTextField txtUsuario;
	private JPasswordField txtPIN;
	private JButton btnIngresar;

	private int intentos = 3;

	public Login() {
		setTitle("Login");
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label_0 = new JLabel("Bienvenido(a) al Cajero Automático");
		label_0.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		label_0.setBounds(44, 46, 360, 25);
		label_0.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label_0);

		JLabel label_1 = new JLabel("Usuario:");
		label_1.setBounds(120, 115, 52, 30);
		getContentPane().add(label_1);

		txtUsuario = new JTextField(20);
		txtUsuario.setBounds(200, 115, 130, 30);
		getContentPane().add(txtUsuario);

		JLabel label_2 = new JLabel("PIN:");
		label_2.setBounds(120, 170, 25, 30);
		getContentPane().add(label_2);

		txtPIN = new JPasswordField(20); // TODO: restringir input a INT
		txtPIN.setBounds(200, 170, 130, 30);
		getContentPane().add(txtPIN);

		btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(177, 245, 95, 30);
		getContentPane().add(btnIngresar);

		btnIngresar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				validarUsuario();
			}
		});
	}

	private void validarUsuario() {
		String usuario = txtUsuario.getText();
		String pin = new String(txtPIN.getPassword());
		int pinBD = 0;
		Conexion con = new Conexion();
		Connection connection = null;

		try {
			connection = (Connection) con.getConnection();
		} catch (SQLException e) {
			System.err.println("No se puede conectar a Base de Datos");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			String query = "SELECT * FROM usuarios WHERE alias = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, usuario);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				pinBD = resultSet.getInt("pin");
				if (!pin.equals(String.valueOf(pinBD))) {
					intentos--;
					if (intentos <= 0) {
						;
						JOptionPane.showMessageDialog(this, "PIN incorrecto. Ha excedido el número de intentos.",
								"Número de intentos excedido", JOptionPane.INFORMATION_MESSAGE);
						System.exit(0);
					} else {
						JOptionPane.showMessageDialog(this, "PIN incorrecto. Le queda(n) " + intentos + " intento(s).",
								"Credenciales incorrectos", JOptionPane.INFORMATION_MESSAGE);
						txtPIN.setText("");
					}
				} else {
					int id = resultSet.getInt("id");
					MenuPrincipal frame = new MenuPrincipal(id);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					this.dispose();
				}
			} else {
				JOptionPane.showMessageDialog(this, "El usuario ingresado no existe.", "Usuario no encontrado",
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

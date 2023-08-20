package bo.edu.ucb.sis213.ui;

import javax.swing.*;
import bo.edu.ucb.sis213.bbdd.Conexion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MenuPrincipal extends JFrame {
	private JButton btnConsultarSaldo;
	private JButton btnDepositar;
	private JButton btnRetirar;
	private JButton btnCambiarPIN;
	private JButton btnSalir;

	private int usuarioId;

	public MenuPrincipal(int id) {
		usuarioId = id;

		setTitle("Menú Principal");
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		btnConsultarSaldo = new JButton("Consultar Saldo");
		btnConsultarSaldo.setBounds(154, 46, 143, 30);
		getContentPane().add(btnConsultarSaldo);

		btnDepositar = new JButton("Realizar Depósito");
		btnDepositar.setBounds(149, 96, 152, 30);
		getContentPane().add(btnDepositar);

		btnRetirar = new JButton("Realizar Retiro");
		btnRetirar.setBounds(160, 146, 131, 30);
		getContentPane().add(btnRetirar);

		btnCambiarPIN = new JButton("Cambiar PIN");
		btnCambiarPIN.setBounds(165, 196, 121, 30);
		getContentPane().add(btnCambiarPIN);

		btnSalir = new JButton("Salir");
		btnSalir.setBounds(188, 246, 75, 30);
		getContentPane().add(btnSalir);

		btnConsultarSaldo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				consultarSaldo();
			}
		});

		btnDepositar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarADeposito();
			}
		});

		btnRetirar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarARetiro();
			}
		});

		btnCambiarPIN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarACambioPIN();
			}
		});

		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarALogin();
			}
		});
	}

	private void consultarSaldo() {
		Conexion con = new Conexion();
		Connection connection = null;
		double saldo = 0;

		try {
			connection = (Connection) con.getConnection();
		} catch (SQLException e) {
			System.err.println("No se puede conectar a Base de Datos");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			String query = "SELECT * FROM usuarios WHERE id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, usuarioId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				saldo = resultSet.getDouble("saldo");
			} else {
				throw new SQLException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JOptionPane.showMessageDialog(this, "Su saldo actual es: $" + String.format("%.2f", saldo) + ".",
				"Consulta de saldo", JOptionPane.INFORMATION_MESSAGE);
	}

	private void llamarADeposito() {
		Deposito frame = new Deposito(usuarioId);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void llamarARetiro() {
		Retiro frame = new Retiro(usuarioId);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void llamarACambioPIN() {
		CambioPIN frame = new CambioPIN(usuarioId);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void llamarALogin() {
		Login frame = new Login();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}
}

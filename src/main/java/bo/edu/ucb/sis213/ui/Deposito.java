package bo.edu.ucb.sis213.ui;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import bo.edu.ucb.sis213.bbdd.Conexion;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Deposito extends JFrame {
	private JTextField txtCantidad;
	private JButton btnCancelar;
	private JButton btnDepositar;

	private int usuarioId;

	public Deposito(int id) {
		usuarioId = id;

		setTitle("Realizar Depósito");
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label_0 = new JLabel("Depósito");
		label_0.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		label_0.setBounds(44, 46, 360, 25);
		label_0.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label_0);

		JLabel label_1 = new JLabel("Ingrese la cantidad a depositar:");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(120, 110, 210, 30);
		getContentPane().add(label_1);

		JLabel label_2 = new JLabel("$");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(152, 146, 32, 30);
		getContentPane().add(label_2);

		txtCantidad = new JTextField(20);
		((AbstractDocument) txtCantidad.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (isValid(fb.getDocument().getText(0, fb.getDocument().getLength()) + string)) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (isValid(fb.getDocument().getText(0, fb.getDocument().getLength()).substring(0, offset) + text
						+ fb.getDocument().getText(0, fb.getDocument().getLength()).substring(offset + length))) {
					super.replace(fb, offset, length, text, attrs);
				}
			}

			private boolean isValid(String text) {
				if (text.isEmpty())
					return true;
				if (text.equals("."))
					return true;
				try {
					Double.parseDouble(text);
					return true;
				} catch (NumberFormatException e) {
					return false;
				}
			}
		});
		txtCantidad.setHorizontalAlignment(SwingConstants.CENTER);
		txtCantidad.setBounds(175, 146, 100, 30);
		getContentPane().add(txtCantidad);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(100, 245, 100, 30);
		getContentPane().add(btnCancelar);

		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarAMenuPrincipal();
			}
		});

		btnDepositar = new JButton("Depositar");
		btnDepositar.setBounds(250, 245, 100, 30);
		getContentPane().add(btnDepositar);

		btnDepositar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarDeposito();
			}
		});
	}

	private void llamarAMenuPrincipal() {
		MenuPrincipal frame = new MenuPrincipal(usuarioId);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void realizarDeposito() {
		double cantidad = Double.parseDouble(txtCantidad.getText());
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
			if (cantidad <= 0) {
				JOptionPane.showMessageDialog(this, "La cantidad ingresada no es válida.", "Cantidad inválida",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				// actualización de saldo
				String query = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
				PreparedStatement preparedStatement = connection.prepareStatement(query);
				preparedStatement.setDouble(1, cantidad);
				preparedStatement.setInt(2, usuarioId);
				preparedStatement.executeUpdate();
				preparedStatement.close();

				// registro en histórico
				query = "INSERT INTO historico(usuario_id, tipo_operacion, cantidad) VALUES (?, 'depósito', ?)";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, usuarioId);
				preparedStatement.setDouble(2, cantidad);
				preparedStatement.executeUpdate();
				preparedStatement.close();

				query = "SELECT * FROM usuarios WHERE id = ?";
				preparedStatement = connection.prepareStatement(query);
				preparedStatement.setInt(1, usuarioId);
				ResultSet resultSet = preparedStatement.executeQuery();

				if (resultSet.next()) {
					saldo = resultSet.getDouble("saldo");
				} else {
					throw new SQLException();
				}

				txtCantidad.setText("");
				JOptionPane.showMessageDialog(this,
						"Depósito realizado con éxito. Su nuevo saldo es: $" + String.format("%.2f", saldo) + ".",
						"Depósito exitoso", JOptionPane.INFORMATION_MESSAGE);
				MenuPrincipal frame = new MenuPrincipal(usuarioId);
				frame.setVisible(true);
				frame.setLocationRelativeTo(null);
				this.dispose();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

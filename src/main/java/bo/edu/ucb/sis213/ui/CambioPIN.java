package bo.edu.ucb.sis213.ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import bo.edu.ucb.sis213.bbdd.Conexion;
import javax.swing.JPasswordField;

public class CambioPIN extends JFrame {
	private JButton btnCancelar;
	private JButton btnCambiarPIN;

	private int usuarioId;
	private JPasswordField passwordField_0;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;

	public CambioPIN(int id) {
		usuarioId = id;

		setTitle("Cambiar PIN");
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label_0 = new JLabel("Cambio de PIN");
		label_0.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		label_0.setBounds(44, 46, 360, 25);
		label_0.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label_0);

		JLabel label_1 = new JLabel("Ingrese su PIN actual:");
		label_1.setBounds(80, 106, 150, 30);
		getContentPane().add(label_1);

		passwordField_0 = new JPasswordField();
		((AbstractDocument) passwordField_0.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (string.matches("\\d*")) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text.matches("\\d*")) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		passwordField_0.setBounds(255, 106, 115, 30);
		getContentPane().add(passwordField_0);

		JLabel label_2 = new JLabel("Ingrese su nuevo PIN:");
		label_2.setBounds(80, 146, 150, 30);
		getContentPane().add(label_2);

		passwordField_1 = new JPasswordField();
		((AbstractDocument) passwordField_1.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (string.matches("\\d*")) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text.matches("\\d*")) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		passwordField_1.setBounds(255, 146, 115, 30);
		getContentPane().add(passwordField_1);

		JLabel label_3 = new JLabel("Confirme su nuevo PIN:");
		label_3.setBounds(80, 186, 150, 30);
		getContentPane().add(label_3);

		passwordField_2 = new JPasswordField();
		((AbstractDocument) passwordField_2.getDocument()).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
					throws BadLocationException {
				if (string.matches("\\d*")) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
					throws BadLocationException {
				if (text.matches("\\d*")) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		});
		passwordField_2.setBounds(255, 186, 115, 30);
		getContentPane().add(passwordField_2);

		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(100, 245, 100, 30);
		getContentPane().add(btnCancelar);

		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				llamarAMenuPrincipal();
			}
		});

		btnCambiarPIN = new JButton("Cambiar PIN");
		btnCambiarPIN.setBounds(245, 245, 105, 30);
		getContentPane().add(btnCambiarPIN);

		btnCambiarPIN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cambiarPIN();
			}
		});
	}

	private void llamarAMenuPrincipal() {
		MenuPrincipal frame = new MenuPrincipal(usuarioId);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void cambiarPIN() {
		String pinActual = new String(passwordField_0.getPassword());
		String nuevoPin = new String(passwordField_1.getPassword());
		String confirmacionNuevoPin = new String(passwordField_2.getPassword());
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
			String query = "SELECT * FROM usuarios WHERE id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, usuarioId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				pinBD = resultSet.getInt("pin");
			} else {
				throw new SQLException();
			}

			if (pinActual.equals(String.valueOf(pinBD))) {
				if (nuevoPin.equals(confirmacionNuevoPin)) {
					// actualización de pin
					query = "UPDATE usuarios SET pin = ? WHERE id = ?";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, Integer.valueOf(nuevoPin));
					preparedStatement.setInt(2, usuarioId);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					// registro en histórico
					query = "INSERT INTO historico(usuario_id, tipo_operacion, cantidad) VALUES (?, 'cambio de pin', ?)";
					preparedStatement = connection.prepareStatement(query);
					preparedStatement.setInt(1, usuarioId);
					preparedStatement.setDouble(2, 0);
					preparedStatement.executeUpdate();
					preparedStatement.close();

					JOptionPane.showMessageDialog(this, "PIN actualizado con éxito.", "Cambio de PIN exitoso",
							JOptionPane.INFORMATION_MESSAGE);
					MenuPrincipal frame = new MenuPrincipal(usuarioId);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Error al cambiar el PIN. Los PINs no coinciden.",
							"Cambio de PIN fallido", JOptionPane.INFORMATION_MESSAGE);
					passwordField_0.setText("");
					passwordField_1.setText("");
					passwordField_2.setText("");
				}
			} else {
				JOptionPane.showMessageDialog(this, "Error al cambiar el PIN. PIN incorrecto.", "Cambio de PIN fallido",
						JOptionPane.INFORMATION_MESSAGE);
				passwordField_0.setText("");
				passwordField_1.setText("");
				passwordField_2.setText("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

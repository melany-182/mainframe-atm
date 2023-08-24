package bo.edu.ucb.sis213.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import bo.edu.ucb.sis213.bl.AppBl;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
	private JTextField txtUsuario;
	private JPasswordField txtPIN;
	private JButton btnIngresar;

	private AppBl appBl = new AppBl();

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

		txtPIN = new JPasswordField(20);
		((AbstractDocument) txtPIN.getDocument()).setDocumentFilter(new DocumentFilter() {
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

		try {
			appBl.validarUsuario(usuario, pin);
			MenuPrincipal frame = new MenuPrincipal(appBl);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			this.dispose();
		} catch (Exception e) {
			// TODO: gestionar JOptionPane's
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			txtPIN.setText("");
		}
		/*
			JOptionPane.showMessageDialog(this, "PIN incorrecto. Ha excedido el número de intentos.",
				"Número de intentos excedido", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
					
			JOptionPane.showMessageDialog(this, "PIN incorrecto. Le queda(n) " + intentos + " intento(s).",
				"Credenciales incorrectos", JOptionPane.INFORMATION_MESSAGE);
			txtPIN.setText("");

			JOptionPane.showMessageDialog(this, "El usuario ingresado no existe.", "Usuario no encontrado",
				JOptionPane.INFORMATION_MESSAGE);
			txtPIN.setText("");
		*/
	}
}

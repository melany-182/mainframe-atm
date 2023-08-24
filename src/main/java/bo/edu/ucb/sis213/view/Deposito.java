package bo.edu.ucb.sis213.view;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import bo.edu.ucb.sis213.bl.AppBl;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Deposito extends JFrame {
	private JTextField txtCantidad;
	private JButton btnCancelar;
	private JButton btnDepositar;

	private AppBl appBl;

	public Deposito(AppBl appBl) {
		this.appBl = appBl;

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
		MenuPrincipal frame = new MenuPrincipal(appBl);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void realizarDeposito() {
		BigDecimal cantidad = new BigDecimal(txtCantidad.getText());

		try {
			appBl.realizarDeposito(cantidad);
			txtCantidad.setText("");
			JOptionPane.showMessageDialog(this,
					"Depósito realizado con éxito. Su nuevo saldo es: $" + String.format("%.2f", appBl.getSaldo()) + ".",
					"Depósito exitoso", JOptionPane.INFORMATION_MESSAGE);
					
			MenuPrincipal frame = new MenuPrincipal(appBl);
			frame.setVisible(true);
			frame.setLocationRelativeTo(null);
			this.dispose();
		} catch (Exception e) {
			// TODO: gestionar JOptionPane's
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
		/*
			JOptionPane.showMessageDialog(this, "La cantidad ingresada no es válida.", "Cantidad inválida",
				JOptionPane.INFORMATION_MESSAGE);
		*/
	}
}

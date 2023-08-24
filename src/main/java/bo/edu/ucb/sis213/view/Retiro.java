package bo.edu.ucb.sis213.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import bo.edu.ucb.sis213.bl.AppBl;

public class Retiro extends JFrame {
	private JTextField txtCantidad;
	private JButton btnCancelar;
	private JButton btnRetirar;

	private AppBl appBl;

	public Retiro(AppBl appBl) {
		this.appBl = appBl;

		setTitle("Realizar Retiro");
		setBounds(100, 100, 450, 350);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JLabel label_0 = new JLabel("Retiro");
		label_0.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		label_0.setBounds(44, 46, 360, 25);
		label_0.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label_0);

		JLabel label_1 = new JLabel("Ingrese la cantidad a retirar:");
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

		btnRetirar = new JButton("Retirar");
		btnRetirar.setBounds(250, 245, 100, 30);
		getContentPane().add(btnRetirar);

		btnRetirar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				realizarRetiro();
			}
		});
	}

	private void llamarAMenuPrincipal() {
		MenuPrincipal frame = new MenuPrincipal(appBl);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void realizarRetiro() {
		BigDecimal cantidad = new BigDecimal(txtCantidad.getText());
		
		try {
			appBl.realizarRetiro(cantidad);
			txtCantidad.setText("");
			JOptionPane.showMessageDialog(this,
					"Retiro realizado con éxito. Su nuevo saldo es: $" + String.format("%.2f", appBl.getSaldo()) + ".",
					"Retiro exitoso", JOptionPane.INFORMATION_MESSAGE);
					
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

			JOptionPane.showMessageDialog(this, "Error al realizar el retiro. Saldo insuficiente.",
				"Retiro fallido", JOptionPane.INFORMATION_MESSAGE);
		*/
	}
}

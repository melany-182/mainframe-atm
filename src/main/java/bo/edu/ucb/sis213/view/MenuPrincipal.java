package bo.edu.ucb.sis213.view;

import javax.swing.*;
import bo.edu.ucb.sis213.bl.AppBl;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class MenuPrincipal extends JFrame {
	private JButton btnConsultarSaldo;
	private JButton btnDepositar;
	private JButton btnRetirar;
	private JButton btnCambiarPIN;
	private JButton btnSalir;

	private AppBl appBl;

	public MenuPrincipal(AppBl appBl) {
		this.appBl = appBl;

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
		BigDecimal saldoAMostrar = appBl.getSaldo();
		JOptionPane.showMessageDialog(this, "Su saldo actual es: $" + String.format("%.2f", saldoAMostrar) + ".",
				"Consulta de saldo", JOptionPane.INFORMATION_MESSAGE);
	}

	private void llamarADeposito() {
		Deposito frame = new Deposito(appBl);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void llamarARetiro() {
		Retiro frame = new Retiro(appBl);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		this.dispose();
	}

	private void llamarACambioPIN() {
		CambioPIN frame = new CambioPIN(appBl);
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

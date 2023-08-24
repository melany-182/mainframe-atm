package bo.edu.ucb.sis213.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bo.edu.ucb.sis213.util.ATMException;

public class UsuarioDao {
    private ConexionMySQL con;
    private Connection connection;

    public UsuarioDao() {
        con = new ConexionMySQL();
		try {
			connection = (Connection) con.getConnection();
		} catch (SQLException e) {
			System.err.println("No se puede conectar a Base de Datos");
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    public int getUsuarioId(String nombreDeUsuario) {
        int usuarioId = -1;
        try {
            String query = "SELECT id FROM usuarios WHERE alias = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombreDeUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
            } else {
                throw new ATMException("El usuario ingresado no existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarioId;
    }

    public String getNombre(String nombreDeUsuario) {
        String nombre = "";
        try {
            String query = "SELECT nombre FROM usuarios WHERE alias = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombreDeUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nombre = resultSet.getString("nombre");
            } else {
                throw new ATMException("El usuario ingresado no existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nombre;
    }

    public String getPin(String nombreDeUsuario) {
        String pin = "";
        try {
            String query = "SELECT pin FROM usuarios WHERE alias = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombreDeUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                pin = resultSet.getString("pin");
            } else {
                throw new ATMException("El usuario ingresado no existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pin;
    }

    public BigDecimal getSaldo(String nombreDeUsuario) {
        BigDecimal saldo = new BigDecimal(-1.0);
        try {
            String query = "SELECT saldo FROM usuarios WHERE alias = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombreDeUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                saldo = resultSet.getBigDecimal("saldo");
            } else {
                throw new ATMException("El usuario ingresado no existe.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldo.setScale(2, RoundingMode.HALF_DOWN);
    }

    public void actualizarSaldo(int usuarioId, BigDecimal nuevoSaldo) {
        try {
            String query = "UPDATE usuarios SET saldo = ? WHERE id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setBigDecimal(1, nuevoSaldo);
			preparedStatement.setInt(2, usuarioId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarPIN(int usuarioId, String nuevoPin) {
        try {
            String query = "UPDATE usuarios SET pin = ? WHERE id = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, nuevoPin);
			preparedStatement.setInt(2, usuarioId);
			preparedStatement.executeUpdate();
			preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

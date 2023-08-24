package bo.edu.ucb.sis213.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoricoDao {
    private ConexionMySQL con;
    private Connection connection;

    public HistoricoDao() {
        con = new ConexionMySQL();
		try {
			connection = (Connection) con.getConnection();
		} catch (SQLException e) {
			System.err.println("No se puede conectar a Base de Datos");
			e.printStackTrace();
			System.exit(1);
		}
    }

    public void registrarTransaccion(int usuarioId, String tipoOperacion, BigDecimal cantidad) {
        try {
            String query = "INSERT INTO historico(usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, usuarioId);
            preparedStatement.setString(2, tipoOperacion);
            preparedStatement.setBigDecimal(3, cantidad);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

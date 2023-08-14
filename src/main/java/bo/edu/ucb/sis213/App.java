package bo.edu.ucb.sis213;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private static int usuarioId;
    private static int pinActual;
    private static double saldo;
    private static Connection connection;

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }
        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public static void main(String[] args) {
        Scanner scanner1 = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        int intentos = 3;
        System.out.println("Bienvenido al Cajero Automático.");

        // Connection connection = null;
        try {
            connection = getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            System.out.print("Ingrese su ID de usuario: ");
            int idIngresado = scanner1.nextInt();
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner2.nextInt();
            if (validarPIN(idIngresado, pinIngresado)) {
                pinActual = pinIngresado;
                usuarioId = idIngresado;
                mostrarMenu();
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("Credenciales incorrectos. Le queda(n) " + intentos + " intento(s).\n");
                } else {
                    System.out.println("Credenciales incorrectos. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
        // scanner1.close();
        // scanner2.close();
    }

    public static boolean validarPIN(int id, int pin) {
        // String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        String query = "SELECT * FROM usuarios WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int pinBD = resultSet.getInt("pin");
                if (pin != pinBD) {
                    return false;
                }
                else {
                    saldo = resultSet.getDouble("saldo");
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo();
                    break;
                case 2:
                    realizarDeposito();
                    break;
                case 3:
                    realizarRetiro();
                    break;
                case 4:
                    cambiarPIN();
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            // scanner.close(); // ¿cómo cerrar el scanner? *** en todos los métodos
        }
    }

    public static void consultarSaldo() { // TODO: consultar saldo en BD
        System.out.println("Su saldo actual es: $" + saldo);
    }

    public static void realizarDeposito() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else {
            saldo += cantidad;
            String query1 = "UPDATE usuarios SET saldo = ? WHERE id = " + usuarioId + " AND pin = " + pinActual;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setDouble(1, saldo);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String query2 = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setInt(1, usuarioId);
                preparedStatement.setString(2, "depósito");
                preparedStatement.setDouble(3, cantidad);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
        // scanner.close();
    }

    public static void realizarRetiro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();

        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            saldo -= cantidad;
            String query1 = "UPDATE usuarios SET saldo = ? WHERE id = " + usuarioId + " AND pin = " + pinActual;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query1);
                preparedStatement.setDouble(1, saldo);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String query2 = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setInt(1, usuarioId);
                preparedStatement.setString(2, "retiro");
                preparedStatement.setDouble(3, cantidad);
                preparedStatement.executeUpdate();
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
        // scanner.close();
    }

    public static void cambiarPIN() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su PIN actual: ");
        int pinIngresado = scanner.nextInt();

        if (pinIngresado == pinActual) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                pinActual = nuevoPin;
                String query1 = "UPDATE usuarios SET pin = ? WHERE id = " + usuarioId;
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query1);
                    preparedStatement.setInt(1, pinActual);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String query2 = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query2);
                    preparedStatement.setInt(1, usuarioId);
                    preparedStatement.setString(2, "cambio de PIN");
                    preparedStatement.setDouble(3, 0);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("PIN actualizado con éxito.");
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
        // scanner.close();
    }
}

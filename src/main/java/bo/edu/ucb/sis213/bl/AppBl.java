package bo.edu.ucb.sis213.bl;

import java.math.BigDecimal;
import bo.edu.ucb.sis213.dao.HistoricoDao;
import bo.edu.ucb.sis213.dao.UsuarioDao;
import bo.edu.ucb.sis213.util.ATMException;

public class AppBl {
    private int usuarioId;
    private String nombre;
    private String pin;
    private BigDecimal saldo;
    private String nombreDeUsuario;
    private UsuarioDao usuarioDao;
    private HistoricoDao historicoDao;
    private int intentos;
    
    public AppBl() {
        usuarioDao = new UsuarioDao();
        historicoDao = new HistoricoDao();
        intentos = 3;
    }

    public int getUsuarioId() {
		return usuarioId;
	}

    public String getNombre() {
        return nombre;
    }

    public String getPin() {
        return pin;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public int getIntentos() {
        return intentos;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public void validarUsuario(String nombreDeUsuario, String pin) throws ATMException {
        String pinBD = "";
        try {
            pinBD = usuarioDao.getPin(nombreDeUsuario);
        }
        catch (Exception e) {
            throw new ATMException("El usuario ingresado no existe.");
        }

        try {
            if (!(pin.equals(pinBD))) {
                intentos--;
			    if (intentos <= 0) {
                    throw new ATMException("PIN incorrecto. Ha excedido el número de intentos.", 0); // TODO: hacer que el ATM se bloquee
			    } else {
                    throw new ATMException("PIN incorrecto. Le queda(n) " + intentos + " intento(s).");
			    }
		    }
            this.usuarioId = usuarioDao.getUsuarioId(nombreDeUsuario);
            this.nombre = usuarioDao.getNombre(nombreDeUsuario);
            this.nombreDeUsuario = nombreDeUsuario;
            this.pin = usuarioDao.getPin(nombreDeUsuario);
            this.saldo = usuarioDao.getSaldo(nombreDeUsuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarSaldo(String tipoOperacion, BigDecimal cantidad) {
        if (tipoOperacion.equals("depósito")) {
            this.saldo = saldo.add(cantidad);
        }
        else if (tipoOperacion.equals("retiro")) {
            this.saldo = saldo.subtract(cantidad);
        }
        usuarioDao.actualizarSaldo(usuarioId, saldo);
        historicoDao.registrarTransaccion(usuarioId, tipoOperacion, cantidad);
    }

    public void realizarDeposito(BigDecimal cantidad) throws ATMException {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ATMException("La cantidad ingresada no es válida.");
        }
        else {
            actualizarSaldo("depósito", cantidad);
        }
    }

    public void realizarRetiro(BigDecimal cantidad) throws ATMException {
        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ATMException("La cantidad ingresada no es válida.");
        }
        else {
            if (cantidad.compareTo(saldo) > 0) {
                throw new ATMException("Error al realizar el retiro. Saldo insuficiente.");
            }
            else {
                actualizarSaldo("retiro", cantidad);
            }
        }
    }

    public void cambiarPIN(String pinActual, String nuevoPin, String confirmacionNuevoPin) throws ATMException {
        if (pinActual.equals(pin)) {
            if (nuevoPin.equals(confirmacionNuevoPin)) {
                usuarioDao.actualizarPIN(usuarioId, nuevoPin);
                historicoDao.registrarTransaccion(usuarioId, "cambio de PIN", BigDecimal.ZERO);
            }
            else {
                throw new ATMException("Error al cambiar el PIN. Los PINs no coinciden.");
            }
        } else {
            throw new ATMException("Error al cambiar el PIN. PIN incorrecto.");
        }
    }
}

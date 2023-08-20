package bo.edu.ucb.sis213.modelos;

// TODO: este modelo no se utiliza todavía
public class Usuario {
    private int usuarioId;
    private String nombre;
    private int pin;
    private double saldo;
    private String nombreDeUsuario;

    public Usuario(int usuarioId, String nombre, int pin, double saldo, String nombreDeUsuario) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.pin = pin;
        this.saldo = saldo;
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPin() {
        return pin;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    @Override
    public String toString() {
        return "Usuario{" + "usuarioId=" + usuarioId + ", nombre=" + nombre + ", pin=" + pin + ", saldo=" + saldo + ", nombreDeUsuario=" + nombreDeUsuario + '}';
    }
}

package bo.edu.ucb.sis213.modelos;

// TODO: este modelo no se utiliza todavía
public class Transaccion {
	private int id;
	private int usuarioId;
	private String tipoOperacion;
	private double cantidad;
	private java.util.Date fecha;

	public Transaccion(int id, int usuarioId, String tipoOperacion, double cantidad, java.util.Date fecha) {
		this.id = id;
		this.usuarioId = usuarioId;
		this.tipoOperacion = tipoOperacion;
		this.cantidad = cantidad;
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public int getUsuarioId() {
		return usuarioId;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public double getCantidad() {
		return cantidad;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Transaccion{" + "id=" + id + ", usuarioId=" + usuarioId + ", tipoOperacion=" + tipoOperacion
				+ ", cantidad=" + cantidad + ", fecha=" + fecha + '}';
	}
}

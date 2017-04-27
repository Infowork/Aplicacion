package javabean;

public class Localizacion {

	private String dni;
	private double latitud,longitud;
	public Localizacion(String dni, double latitud, double longitud) {
		super();
		this.dni = dni;
		this.latitud = latitud;
		this.longitud = longitud;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
	@Override
	public String toString() {
		return dni+"|"+latitud+"|"+longitud;
	}
	
}

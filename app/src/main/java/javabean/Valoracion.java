package javabean;

import java.io.Serializable;

public class Valoracion implements Serializable{

	private String dni,texto;
	private int puntuacion;
	public Valoracion(String dni, String texto, int puntuacion) {
		super();
		this.dni = dni;
		this.texto = texto;
		this.puntuacion = puntuacion;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	@Override
	public String toString() {
		return dni + "|" + texto + "|" + puntuacion;
	}
	
}

package javabean;

import java.io.Serializable;

public class Adicionales implements Serializable{

	private String dni,titulo,descripcion;

	public Adicionales(String dni, String titulo, String descripcion) {
		super();
		this.dni = dni;
		this.titulo = titulo;
		this.descripcion = descripcion;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return dni + "|" + titulo + "|" + descripcion;
	}
	

	
	
}

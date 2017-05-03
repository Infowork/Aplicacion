package javabean;

import java.io.Serializable;

public class Datos implements Serializable{
	
	private String dni;
	private String empresa;
	private String nombre,empleo;
	private int telefono;
	private String email,contrasena;
	private boolean urgente;
	public Datos(String dni, String empresa, String nombre, int telefono, String email, String contrasena,
			boolean urgente, String empleo) {
		super();
		this.dni = dni;
		this.empresa = empresa;
		this.nombre = nombre;
		this.telefono = telefono;
		this.email = email;
		this.contrasena = contrasena;
		this.urgente = urgente;
		this.empleo=empleo;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getTelefono() {
		return telefono;
	}
	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContrasena() {
		return contrasena;
	}
	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
	public boolean isUrgente() {
		return urgente;
	}
	public void setUrgente(boolean urgente) {
		this.urgente = urgente;
	}
	
	
	public String getEmpleo() {
		return empleo;
	}
	public void setEmpleo(String empleo) {
		this.empleo = empleo;
	}
	@Override
	public String toString() {
		return dni+"|"+empresa+"|"+nombre+"|"+telefono+"|"+email+"|"+contrasena+"|"+urgente+"|"+empleo;
	}
	
	

}

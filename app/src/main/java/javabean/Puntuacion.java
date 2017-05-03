package javabean;

import java.io.Serializable;
import java.util.ArrayList;

public class Puntuacion implements Serializable{
	
	private String dni;
	private ArrayList<String> review;
	private double puntuacion;
	public Puntuacion(String dni, ArrayList<String> review, double puntuacion) {
		super();
		this.dni = dni;
		this.review = review;
		this.puntuacion = puntuacion;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public ArrayList<String> getReview() {
		return review;
	}
	public void setReview(ArrayList<String> review) {
		this.review = review;
	}
	public double getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(double puntuacion) {
		this.puntuacion = puntuacion;
	}
	@Override
	public String toString() {
		return  dni + "|" + review + "|" + puntuacion;
	}
	
	
	

}

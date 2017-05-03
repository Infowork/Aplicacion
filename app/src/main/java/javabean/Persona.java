package javabean;

import java.io.Serializable;

public class Persona implements Serializable{

	private Datos datos;
	private Localizacion loc;
	private Adicionales ad;
	private Puntuacion punt;
	public Persona(Datos datos, Localizacion loc, Adicionales ad, Puntuacion punt) {
		super();
		this.datos = datos;
		this.loc = loc;
		this.ad = ad;
		this.punt = punt;
	}
	public Datos getDatos() {
		return datos;
	}
	public void setDatos(Datos datos) {
		this.datos = datos;
	}
	public Localizacion getLoc() {
		return loc;
	}
	public void setLoc(Localizacion loc) {
		this.loc = loc;
	}
	public Adicionales getAd() {
		return ad;
	}
	public void setAd(Adicionales ad) {
		this.ad = ad;
	}
	public Puntuacion getPunt() {
		return punt;
	}
	public void setPunt(Puntuacion punt) {
		this.punt = punt;
	}
	@Override
	public String toString() {
		return "Persona [datos=" + datos + ", loc=" + loc + ", ad=" + ad + ", punt=" + punt + "]";
	}
	
	
}

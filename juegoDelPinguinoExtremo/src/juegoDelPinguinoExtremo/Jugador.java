package juegoDelPinguinoExtremo;
import java.util.Scanner;
public abstract class Jugador {
	
private int posicion;
private String nombre;
private String color;
private Inventario inv;
private boolean turnoTerminado;

	public Jugador(String nombre, String color) {
		
		this.nombre = nombre;
		this.color = color;
		this.posicion = 0;
	}
	
	public String getNombre() {
		
		return nombre;
		
	}
	
	public void setNombre(String nombre) {
		
		this.nombre = nombre;
		
	}
	
	public String getColor() {
		
		return color;
		
	}
	
	public void setColor(String color) {
		
		this.color = color;
		
	}
	
	public int getPos() {
		
		return posicion;
		
	}
	
	public void setPos(int posicion) {
	
		this.posicion = posicion;
	}
	
	public void moverPosicion(int nDado) {
		
		this.posicion += nDado; //Sistema de avance del jugador
		
	}

}

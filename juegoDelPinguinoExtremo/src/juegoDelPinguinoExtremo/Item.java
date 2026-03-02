package juegoDelPinguinoExtremo;

public abstract class Item {
	
	private String nombre;
	
	
	public Item(String nombre) {
		
		this.nombre = nombre;
		
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	public String getNombre() {
		return nombre;
	}
}

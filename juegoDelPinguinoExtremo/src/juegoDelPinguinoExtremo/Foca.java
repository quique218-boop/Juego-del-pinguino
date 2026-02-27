package juegoDelPinguinoExtremo;

public class Foca extends Jugador{
	
	private boolean soborno;
	
	public Foca(boolean soborno, String nombre, String color) {
		
		super(nombre, color);
		
		this.soborno = false;
		
	}
	
	public boolean getSoborno() {
		
		return this.soborno;
		
	}
	
	public void setSoborno(boolean soborno) {
		
		this.soborno = soborno;
		
	}
	
	public void aplastarJugador(Pinguino otro) { //Esto es robar la mitat del inventario
		
		int mitad = otro.getInventario() / 2; //Dividimos el inventario a la mitad
		
		otro.setInventario(otro.getInventario() - mitad); //Le restamos la mitad del inventario al jugador
		
	}
	
	public void golpearJugador(Pinguino otro) {
		
		
	}
	
	public void esSobornado() {
		
		
	}

}

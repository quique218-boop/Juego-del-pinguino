package juegoDelPinguinoExtremo;

public abstract class Casilla {

	private int posicion;
	
	public Casilla() {}
	
	public abstract void realizarAccion(Tablero tablero, Jugador jugador);
	
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	
	public int getPosicion() {
		return this.posicion;
	}
}

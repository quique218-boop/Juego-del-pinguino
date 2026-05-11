package modelo;

public abstract class Casilla {

	private int posicion;
	
	public Casilla() {}
	
	public abstract void realizarAccion(Tablero tablero, Jugador jugador);
	
	//Modificamos y obtenemos la posicion del jugadorb  en el tablero
	
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	
	public int getPosicion() {
		return this.posicion;
	}
}

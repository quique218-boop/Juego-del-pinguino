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
	
	public void aplastarJugador(Pinguino jugador) { //Esto es robar la mitat del inventario
		
		int mitad = jugador.getInventario() / 2; //Dividimos el inventario a la mitad
		
		jugador.setInventario(jugador.getInventario() - mitad); //Le restamos la mitad del inventario al jugador
		
	}
	
	public void golpearJugador(Pinguino jugador, Tablero tablero) {
		
		boolean encontrado = false;
		
		if(this.getPos() != jugador.getPos()) {
			
			return; //Si no estan en la misma casilla no pasa nada
			
		}else {
			
			System.out.println("La foca golpea al pinguino fuertemente");
			
			

			for (int i = jugador.getPos() - 1; i > 0 && encontrado == false; i--) {

				if (tablero.getCasilla(i) instanceof Agujero) {
					
					jugador.setPos(i);
					
					encontrado = true;
					
				}
			}
			 
		}
		
	}
	
	public void esSobornado() {
		
		System.out.println("La foca come el pez");
		
		for(int i = 0; i < 2; i++) {
			
			System.out.println("Foca bloqueada");
			
		}
		
		
	}

}

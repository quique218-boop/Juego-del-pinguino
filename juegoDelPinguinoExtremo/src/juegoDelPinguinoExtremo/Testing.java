package juegoDelPinguinoExtremo;

import java.util.ArrayList;
import java.util.Random;

public class Testing {

	public static void main(String[] args) {

		ArrayList<Casilla> c1 = new ArrayList<>();
				
		Tablero t1 = new Tablero();
				
		t1.siguienteTurno();

		int casillaAnterior = 0;
		
		for (int i = 0; i < 50; i++) {
			
			switch(casillaAnterior = Randomizador(casillaAnterior)) {
			
			case 0:
				c1.add(new Normal());
				break;
				
			case 1:
				c1.add(new Oso());
				break;
				
			case 2:
				c1.add(new Agujero());
				break;
				
			case 3:
				c1.add(new Evento());
				break;
				
			case 4:
				c1.add(new Trineo());
				break;
				
			case 5:
				c1.add(new SueloQuebradizo());
				break;
			
			}
		}
		
		t1.inicializarCasillas(c1);
		
		for (int i = 0; i < 50; i++) {
			System.out.println(t1.getCasilla(i));
		}
		
		
	}
	
	public static int Randomizador(int casillaAnterior) {
		
		Random ran = new Random();
		
		int casillaActual = ran.nextInt(6);

		for (int i = 0; i < 3; i++) {
			if(casillaActual != 0) casillaActual = ran.nextInt(6);
		}
		
		
		while(casillaActual == casillaAnterior && casillaActual != 0) casillaActual = ran.nextInt(5) + 1;
		
		return casillaActual;

	}

}

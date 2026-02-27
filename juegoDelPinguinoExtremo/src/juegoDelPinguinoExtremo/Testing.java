package juegoDelPinguinoExtremo;

import java.util.ArrayList;
import java.util.Random;

public class Testing {

	public static void main(String[] args) {

		ArrayList<Casilla> c1 = new ArrayList<>();

		ArrayList<Jugador> jugadores = new ArrayList<>();

		/*jugadores.add(new Pinguino("Pedro", "Blanco"));
		jugadores.add(new Pinguino("Pablo", "Rojo"));
		jugadores.add(new Pinguino("Tres", "Azul"));
		jugadores.add(new Pinguino("Cuatro", "Negro"));*/

		Tablero t1 = new Tablero();

		RellenarTablero(t1);

		t1.inicializarJugadores(jugadores);

		int contador = 0;

		int agujero = 0;

		for (int i = 0; i < t1.getArrayListCasilla().size() && contador != 2; i++) {

			if (t1.getCasilla(i) instanceof Agujero) {
				contador++;
			}

			if (contador == 2)
				agujero = i;
		}

		jugadores.get(0).setPos(agujero);

		System.out.println(
				t1.getCasilla(jugadores.get(0).getPos()) + "Jugador esta en posicion " + jugadores.get(0).getPos());

		for (int i = jugadores.get(0).getPos() - 1; i > 0; i--) {

			if (t1.getCasilla(i) instanceof Agujero) {
				System.out.println(t1.getCasilla(i) + " Encontrado en i = " + i);
			}
		}
	}

	public static int Randomizador(int casillaAnterior) {

		Random ran = new Random();

		int casillaActual = ran.nextInt(6);

		for (int i = 0; i < 3; i++) {
			if (casillaActual != 0)
				casillaActual = ran.nextInt(6);
		}

		while (casillaActual == casillaAnterior && casillaActual != 0)
			casillaActual = ran.nextInt(5) + 1;

		return casillaActual;

	}

	public static void RellenarTablero(Tablero t1) {

		ArrayList<Casilla> c1 = new ArrayList<>();

		int casillaAnterior = 0;

		for (int i = 0; i < 50; i++) {

			switch (casillaAnterior = Randomizador(casillaAnterior)) {

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
	}

	public static void asignarPosicionCasillas(Tablero t1) {

		for (int i = 0; i < t1.getArrayListCasilla().size(); i++) {
			t1.getCasilla(i).setPosicion(i);
		}
	}
}

package vista;

import controlador.*;
import modelo.*;
import javafx.fxml.FXML;

import java.util.Random;
import java.util.Scanner;

public class Play {

	private GestorTablero gestorTablero;

	@FXML
	private void initialize() {

		this.gestorTablero = new GestorTablero();
	}

	@FXML
	private void lanzar() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();
		Dado dado = (Dado) pinguino.getInventario().getDado().getFirst();

		int resultado = gestorTablero.tirarDado(pinguino, dado);

	}

	@FXML
	private void usarBola() {

		Scanner scanb = new Scanner(System.in);

		Random random = new Random();

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if (inventario.getBolas().isEmpty()) { // Si la lista no es vacia

			System.out.println("No tienes este objeto");

		}else {

			pinguino.usarItem(inventario.getBolas().get(0)); // El jugador coge la primera bola de nieve de la lista

			System.out.println("A que jugador quieres lanzar-le la bola de nieve?");

			String el = scanb.nextLine();

			gestorTablero.getPartida().getArrayListJugador();

			Pinguino pinguino2 = null;

			for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

				if (jugador.getNombre().equalsIgnoreCase(el)) {

					pinguino2 = (Pinguino) jugador;

				} else {

					System.out.println("No existe");

				}

			}

			int skill = random.nextInt() + 1;

			if (skill <= 5) {

				System.out.println("Has fallado");

			}else {

				System.out.println("Has acertado");

				pinguino2.moverPosicion(-1);

			}

		}

	}

	@FXML
	private void usarPez() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		Foca foca = null;

		for (Jugador jugador : gestorTablero.getPartida().getArrayListJugador()) {

			if (jugador.getNombre().equalsIgnoreCase("foca")) {

				foca = (Foca) jugador;

			}
		}

		if (inventario.getPez().isEmpty()) { // Si la lista no es vacia

			System.out.println("No tienes este objeto");

		}else {

			Casilla casillaActual = gestorTablero.getPartida().getCasilla(pinguino.getPos()); //Casilla donde se encuentra

			if(casillaActual instanceof Oso) {

				pinguino.usarItem(pinguino.getInventario().getPez().getFirst());

			}else if(pinguino.getPos() == foca.getPos()) { //Si estamos en la misma casilla de foca

				System.out.println("Quieres usar un pez para salvar tu inventario?");

				//TODO debido a que queremos que se haga con ventanas emergentes de botones


				// En caso de que si se usa pinguino.usarItem(pinguino.getInventario().getPez().getFirst());

				//En caso de que no se pierde mitad de inventario

				foca.golpearJugador(pinguino, gestorTablero.getPartida()); //Te empuja al anterior agujero

			}

		}

	}

	@FXML
	private void usarDadoR() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if(inventario.getDado().size() < 0) {

			System.out.println("No tienes ningún dado especial");

		}else {

			for(Dado d : pinguino.getInventario().getDado()) {

				if(d.getNombre().equalsIgnoreCase("DadoR")) {

					d.tirarDado(); //Tiramos el primer dado rápido que encontramos

				}else if(d.getNombre().equalsIgnoreCase("Dado Lento")) {

					d.tirarDado();

				}
			}
		}
	}

	@FXML
	private void usarDadoL() {

		Pinguino pinguino = (Pinguino) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if(inventario.getDado().size() < 0) {

			System.out.println("No tienes ningún dado especial");

		}else {

			for(Dado d : pinguino.getInventario().getDado()) {

				if(d.getNombre().equalsIgnoreCase("DadoL")) {

					d.tirarDado();

				}
			}
		}

	}
	
	private void Pausa() {
		
		
	}

}

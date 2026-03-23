package Error_capa8;

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

			return;

		}else {

			Casilla casillaActual = gestorTablero.getPartida().getCasilla(pinguino.getPos()); //Casilla donde se encuentra

			if(casillaActual instanceof Oso) {

				pinguino.usarItem(pinguino.getInventario().getPez().getFirst());

			} else if(pinguino.getPos() == foca.getPos()) { //Si estamos en la misma casilla de foca

				pinguino.usarItem(pinguino.getInventario().getPez().getFirst());
				
				foca.esSobornado();
				
			} else if(pinguino.getPos() == foca.getPos() && pinguino.getInventario().getPez().isEmpty()) {
					
				foca.golpearJugador(pinguino, gestorTablero.getPartida()); //Te empuja al anterior agujero
					
				}

			}

		}

	@FXML
	private void usarDadoR() {

		Jugador pinguino = (Jugador) gestorTablero.getPartida().getJugadorActual();

		Inventario inventario = pinguino.getInventario();

		if(inventario.getDado().size() < 0) {

			System.out.println("No tienes ningún dado especial");

		}else {

			for(Dado d : pinguino.getInventario().getDado()) {

				if(d instanceof DadoRapido) {

					d.tirarDado(); //Tiramos el primer dado rápido que encontramos

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

				if(d instanceof DadoLento) {

					d.tirarDado();

				}
			}
		}

	}
	
	private void Pausa() {
		
		
	}

}

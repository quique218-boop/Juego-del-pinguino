package juegoDelPinguinoExtremo;

import java.util.ArrayList;

public class Pinguino extends Jugador {

	private Inventario inventario = super.getInventario();

	public Pinguino(String nombre, String color, Inventario inventario) {

		super(nombre, color, inventario);

	}

	public void gestionarBatalla(Pinguino jugador) {

		if (jugador == null) {

			return;

		}

		if (this.getPos() != jugador.getPos()) {

			return;
		}

		int misBolas = this.contarBolasdeNieve();

		int susBolas = jugador.contarBolasdeNieve();

		if (misBolas > susBolas) {

			jugador.moverPosicion(-1);

			this.consumirBoladeNieve();

		} else if (susBolas > misBolas) {

			this.moverPosicion(-1);

			jugador.consumirBoladeNieve();

		} else {

			// En caso de que empaten los dos pierden todas sus bolas de nieve

			this.ponerBolasdeNieveACero();

			jugador.ponerBolasdeNieveACero();

		}

	}

	private int contarBolasdeNieve() {

		if (inventario == null)
			return 0;

		return inventario.getBolas().size();
	}

	private void consumirBoladeNieve() {

		if (inventario == null)
			return;

		if (inventario.getBolas().size() > 0)
			inventario.getBolas().removeFirst();
	}

	public void ponerBolasdeNieveACero() {

		if (inventario == null)
			return;

		while (inventario.getBolas().size() != 0) {
			inventario.getBolas().removeFirst();
		}

	}

	public void usarItem(Item item) {

		if (item == null || inventario == null)
			return;

		// TODO funcion para items??

		quitarItem(item);

	}

	public void anadirItem(Item item) {

		if (item == null)
			return;

		if (inventario == null)
			inventario = new Inventario(new ArrayList<Dado>(), new ArrayList<Pez>(), new ArrayList<BolaDeNieve>());

		if (item instanceof BolaDeNieve)
			inventario.addListaBolas((BolaDeNieve) item);

		else if (item instanceof Pez)
			inventario.addListaPez((Pez) item);

		else if (item instanceof Dado_lento)
			inventario.addListaDado((Dado_lento) item);

		else if (item instanceof Dado_rapido)
			inventario.addListaDado((Dado_rapido) item);

	}

	public void quitarItem(Item item) {

		if (item instanceof BolaDeNieve)
			inventario.getBolas().removeFirst();

		else if (item instanceof Pez)
			inventario.getPez().removeFirst();

		else if (item instanceof Dado_lento)
			inventario.getDado().remove(item);

		else if (item instanceof Dado_rapido)
			inventario.getDado().remove(item);

	}
}
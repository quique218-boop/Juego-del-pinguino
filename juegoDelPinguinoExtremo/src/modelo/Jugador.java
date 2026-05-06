package modelo;

import java.util.ArrayList;

public abstract class Jugador {

	private int posicion;
	private String nombre;
	private String color;
	private boolean turnoTerminado;
	private Inventario inventario;
	private int deudaTurnos;
	private int partidasTotales;
	private int turnoEnArray;
	private String pelea;

	public Jugador(String nombre, String color, Inventario inventario) {

		this.nombre = nombre;
		this.color = color;
		this.posicion = 0;
		this.inventario = inventario;
		this.turnoEnArray = -1;
	}

	public Jugador(int posicion, String nombre, String color, Inventario inventario, int deudaTurnos,
			int partidasTotales, int turnoEnArray) {

		this.nombre = nombre;
		this.color = color;
		this.posicion = posicion;
		this.inventario = inventario;
		this.turnoEnArray = turnoEnArray;
	}

	public Jugador() {

		this.nombre = "prueba";
		this.color = "rojo puton";
		this.posicion = 67;
		this.inventario = new Inventario();
	}

	public int getPartidasTotales() {

		return partidasTotales;
	}

	public String getNombre() {

		return nombre;

	}

	public void setNombre(String nombre) {

		this.nombre = nombre;

	}

	public String getColor() {

		return color;

	}

	public void setColor(String color) {

		this.color = color;

	}

	public boolean getTurnoTerminado() {

		return this.turnoTerminado;

	}

	public void setTurnoTerminado(boolean turnoTerminado) {

		this.turnoTerminado = turnoTerminado;

	}

	public int getPos() {

		return posicion;

	}

	public void setPos(int posicion) {

		this.posicion = posicion;
	}

	public void moverPosicion(int nDado) {

		this.posicion += nDado;

		if (this.posicion >= 50)
			this.posicion = 49;
		else if (this.posicion < 0)
			this.posicion = 0;
	}

	public Inventario getInventario() {

		return inventario;

	}

	public void setInventario(Inventario inventario) {

		this.inventario = inventario;

	}

	public int getDeudaTurnos() {
		return this.deudaTurnos;
	}

	public void setDeudaTurnos(int deudaTurnos) {
		this.deudaTurnos = deudaTurnos;
	}

	public void reducirDeudaTurnos() {
		this.deudaTurnos -= 1;
	}

	public void terminarTurnoSiHayDeuda() {

		if (this.deudaTurnos > 0)
			this.turnoTerminado = true;

	}

	public int getTurnoEnArray() {

		return this.turnoEnArray;

	}

	public void setTurnoEnArray(int turnoEnArray) {

		this.turnoEnArray = turnoEnArray;

	}

	public int contarBolasdeNieve() {

		if (inventario == null)
			return 0;

		return inventario.getBolas().size();
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

		else if (item instanceof DadoLento)
			inventario.addListaDado((DadoLento) item);

		else if (item instanceof DadoRapido)
			inventario.addListaDado((DadoRapido) item);

	}

	public void quitarItem(Item item) {

		if (item instanceof BolaDeNieve)
			inventario.getBolas().removeFirst();

		else if (item instanceof Pez)
			inventario.getPez().removeFirst();

		else if (item instanceof DadoLento)
			inventario.getDado().remove(item);

		else if (item instanceof DadoRapido)
			inventario.getDado().remove(item);

	}

	public Jugador devolverSiNombreCoincide(String nombre) {

		if (nombre.equals(this.nombre))
			return this;

		return null;
	}

	public void resultadoGuerra(Jugador rival, Boolean victoria) {

		if (victoria == null) {
			this.pelea = this.nombre + " ha empatado con " + rival.nombre;
			return;
		}
		
		this.pelea = (victoria) ? this.nombre + " ha ganado una pelea contra " + rival.getNombre()
				: this.nombre + " ha perdido una pelea contra " + rival.getNombre();
	}

	public String getPelea() {

		String pelea = this.pelea;

		this.pelea = null;

		return pelea;
	}
}

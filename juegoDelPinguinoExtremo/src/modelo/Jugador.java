package modelo;

public abstract class Jugador {

	private int posicion;
	private String nombre;
	private String color;
	private boolean turnoTerminado;
	private Inventario inventario;
	private int deudaTurnos;

	public Jugador(String nombre, String color, Inventario inventario) {

		this.nombre = nombre;
		this.color = color;
		this.posicion = 0;
		this.inventario = inventario;
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

		this.posicion += nDado; // Sistema de avance del jugador

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
	
	public void terminarTurnoSiHayDeuda() {
		
		if(this.deudaTurnos > 0) this.turnoTerminado = true;
		
	}

}

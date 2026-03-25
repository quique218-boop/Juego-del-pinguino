package controlador;

import modelo.*;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GestorBBDD {

	public static Connection con;

	private static String urlBBDD;
	private static String username;
	private static String password;

	private static int count;

	public static void guardarBBDD(Tablero t1) {
		con = BBDD.conectarBaseDatos();

		int n_jug_actual;
		int id_tablero = -1;

		int turnos_tablero = t1.getTurnos();

		Jugador jugador_actual = t1.getJugadorActual();

		for(int i = 0; i < t1.getArrayListJugador().size(); i++) {

			if(jugador_actual == t1.getArrayListJugador().get(i)){

				n_jug_actual = i;
			}

		}

		ArrayList<Casilla> casillas = t1.getArrayListCasilla();

		int n_peces = t1.getJugador(0).getInventario().getPez().size();

		int n_bolas = t1.getJugador(0).getInventario().getBolas().size();

		int n_dadoR = 0;
		int n_dadoL = 0;

		for (Dado dado : t1.getJugador(0).getInventario().getDado()) {

			if (dado instanceof DadoRapido) {

				n_dadoR++;

			} else if (dado instanceof DadoLento) {

				n_dadoL++;

			}
		}

		Jugador j1 = t1.getJugador(0);

		Jugador j2 = t1.getJugador(1);

		Jugador j3 = t1.getJugador(2);

		Jugador j4 = t1.getJugador(3);

		Jugador j5 = t1.getJugador(4);

		Jugador foca = t1.getJugador(5); //Al insertarlo en SQL se tendria que poner 1

		int pos_j1 = t1.getJugador(0).getPos();

		int pos_j2 = t1.getJugador(1).getPos();

		int pos_j3 = t1.getJugador(2).getPos();

		int pos_j4 = t1.getJugador(3).getPos();

		int pos_j5 = t1.getJugador(4).getPos();

		int pos_foca = t1.getJugador(5).getPos();

		int turno_j1 = 0;

		int turno_j2 = 1;
		
		int turno_j3 = 2;
		
		int turno_j4 = 3;
		
		int turno_foca = 4;
		
		BBDD.insert(con, "INSERT INTO testing (Num)\n" + "VALUES (2)");

		BBDD.cerrar(con);
	}

	public static Tablero cargarTablero(int id) {
		con = BBDD.conectarBaseDatos();

		System.out.println("Select");
		ArrayList<String> cols = new ArrayList<>();
		cols.add("NACTOR");
		cols.add("NOMBRE");
		cols.add("FECHAN");
		procesamientoSelect(con, "SELECT * FROM ACTOR\n" + "WHERE \"NACTOR\" = " + id, cols);

		BBDD.cerrar(con);

		return null;
	}

	public static void procesamientoSelect(Connection con, String sql, ArrayList<String> columnas) {

		// Ejecuta el SELECT usando la plantilla BBDD.
		// Devuelve una lista de filas.
		ArrayList<LinkedHashMap<String, String>> filas = BBDD.select(con, sql);

		// Si no hay resultados, informamos al usuario.
		if (filas.isEmpty()) {
			System.out.println("No se ha encontrado nada");
		} else {

			// Recorremos cada fila del resultado del SELECT
			for (LinkedHashMap<String, String> fila : filas) {

				// Para cada fila recorremos las columnas que queremos leer
				for (String col : columnas) {

					// Obtenemos el valor de la columna actual
					String valor = fila.get(col);

					// Si no existe la columna o es null avisamos
					if (valor == null) {
						System.out.println("Aviso: la columna '" + col + "' no existe en el SELECT o no tiene valor.");
					} else {
						procesarValor(col, valor);
					}
				}
			}
		}
	}

	/*
	 * IMPORTANTE: En un proyecto real usaríamos un objeto directamente en vez de
	 * variables globales, pero aquí lo hacemos así para simplificar el ejemplo.
	 */
	public static void procesarValor(String col, String valor) {

		if (col.equals("NUMIDS")) {
			count = Integer.parseInt(valor) + 1;
		}
	}
}
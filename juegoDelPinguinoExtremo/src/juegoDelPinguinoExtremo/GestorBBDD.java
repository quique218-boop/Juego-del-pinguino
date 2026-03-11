package juegoDelPinguinoExtremo;

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

		int TABLERO_TURNOS = t1.getTurnos();
		int TABLERO_JUGADORACTUAL = t1.getjugadorActual();
		ArrayList<Casilla> TABLERO_CASILLAS = t1.getArrayListCasilla();

		int INVENTARIO_NUM_PECES1 = t1.getJugador(0).getInventario().getPez().size();
		int INVENTARIO_NUM_PECES2 = t1.getJugador(1).getInventario().getPez().size();
		int INVENTARIO_NUM_PECES3 = t1.getJugador(2).getInventario().getPez().size();
		int INVENTARIO_NUM_PECES4 = t1.getJugador(3).getInventario().getPez().size();
		int INVENTARIO_NUM_PECES_FOCA = t1.getJugador(4).getInventario().getPez().size();

		int INVENTARIO_NUM_BOLAS1 = t1.getJugador(0).getInventario().getBolas().size();
		int INVENTARIO_NUM_BOLAS2 = t1.getJugador(1).getInventario().getBolas().size();
		int INVENTARIO_NUM_BOLAS3 = t1.getJugador(2).getInventario().getBolas().size();
		int INVENTARIO_NUM_BOLAS4 = t1.getJugador(3).getInventario().getBolas().size();
		int INVENTARIO_NUM_BOLAS_FOCA = t1.getJugador(4).getInventario().getBolas().size();

		int INVENTARIO_NUM_DADOR1;
		int INVENTARIO_NUM_DADOR2;
		int INVENTARIO_NUM_DADOR3;
		int INVENTARIO_NUM_DADOR4;
		int INVENTARIO_NUM_DADOR_FOCA;

		int INVENTARIO_NUM_DADOL1;
		int INVENTARIO_NUM_DADOL2;
		int INVENTARIO_NUM_DADOL3;
		int INVENTARIO_NUM_DADOL4;
		int INVENTARIO_NUM_DADOL_FOCA;
		
		for (int i = 0; i < t1.getArrayListJugador().size(); i++) {

			int numdadoR = 0;
			int numdadoL = 0;

			for (Dado dado : t1.getJugador(i).getInventario().getDado()) {

				if (dado instanceof Dado_lento) {
					
					numdadoL++;

				} else if (dado instanceof Dado_rapido) {

					numdadoR++;
					
				}

			}

			switch (i) {

			case 0: {
				INVENTARIO_NUM_DADOR1 = numdadoR;
				INVENTARIO_NUM_DADOL1 = numdadoL;
				break;
			}

			case 1: {
				INVENTARIO_NUM_DADOR2 = numdadoR;
				INVENTARIO_NUM_DADOL2 = numdadoL;
				break;
			}

			case 2: {
				INVENTARIO_NUM_DADOR3 = numdadoR;
				INVENTARIO_NUM_DADOL3 = numdadoL;
				break;
			}

			case 3: {
				INVENTARIO_NUM_DADOR4 = numdadoR;
				INVENTARIO_NUM_DADOL4 = numdadoL;
				break;
			}
			case 4: {
				INVENTARIO_NUM_DADOR_FOCA = numdadoR;
				INVENTARIO_NUM_DADOL_FOCA = numdadoL;
				break;
			}
			}
		}

		ArrayList<String> cols = new ArrayList<>();

		cols.add("NUMIDS");

		procesamientoSelect(con, "SELECT COUNT(*) AS NUMIDS FROM TABLEROS", cols);

		BBDD.insert(con, "INSERT INTO TABLEROS (ID, USERNAME, PASSWORD)\n" + "VALUES (" + count + ", \'" + username
				+ "\', \'" + password + "\')");

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
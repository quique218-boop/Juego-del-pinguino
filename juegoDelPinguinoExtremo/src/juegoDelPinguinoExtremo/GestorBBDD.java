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

	public static void guardarBBDD(String username, String password) {
		con = BBDD.conectarBaseDatos();
		ArrayList<String> cols = new ArrayList<>();

		cols.add("NUMIDS");

		procesamientoSelect(con, "SELECT COUNT(*) AS NUMIDS FROM TABLEROS", cols);

		BBDD.insert(con, "INSERT INTO TABLEROS (ID, USERNAME, PASSWORD)\n" + "VALUES (" + count + ", \'" + username + "\', \'"
				+ password + "\')");

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
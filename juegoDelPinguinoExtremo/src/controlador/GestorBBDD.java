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

	public static void guardar(Tablero t1) {
		
		con = BBDD.conectarBaseDatos();
//Iniciamos INSERT de la tabla TABLERO

		int turnos_tablero = t1.getTurnos();

		Jugador jugador_actual = t1.getJugadorActual();

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
		
		int id_tablero = obtenerIdTablero(con);
		
		ArrayList<Jugador> jugadores = t1.getArrayListJugador();
		
		int posActual = 0;
		
		for(int i = 0; i < jugadores.size(); i++) {
			
			if(jugadores.get(i) == t1.getJugadorActual()) {
				
				posActual = i;
				
			}
			
		}
		
		ArrayList<String> casillasBBDD = new ArrayList<>();
		
		for(int i = 0; i < casillas.size(); i++) {
			
			if(casillas.get(i) instanceof Agujero) {
				
				casillasBBDD.add("Agujero");
				
			}
			
			else if(casillas.get(i) instanceof Evento) {
				
				casillasBBDD.add("Evento");
				
			}
			
			else if(casillas.get(i) instanceof SueloQuebradizo) {
				
				casillasBBDD.add("SueloQuebradizo");
				
			}
			
			else if(casillas.get(i) instanceof Trineo) {
				
				casillasBBDD.add("Trineo");
				
			}
			
			else if(casillas.get(i) instanceof Normal) {
				
				casillasBBDD.add("Normal");
				
			}
			
			else if(casillas.get(i) instanceof Oso) {
				
				casillasBBDD.add("Oso");
				
			}
			
		}
		
		String varray = "";
		
		for(int i = 0; i < casillasBBDD.size()-1; i++) {
			
			 varray+= " '"+casillasBBDD.get(i)+"',";
			
		}
		
		varray += " '"+casillasBBDD.get(49)+"'";
		
		String sqlTablero = "INSERT INTO TABLERO VALUES(seq_tablero.NEXTVAL, "
		+turnos_tablero+", "+posActual+", ARRAY_CASILLAS("+varray+"), SYSDATE, 1)";
		
		System.out.println(sqlTablero);

		
		BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
		           new String[]{"TOTAL"});

		BBDD.insert(con, sqlTablero); ///Termina el insert de la tabla TABLERO
		
		
		//Hacemos el insert de jugador
		for(int i = 0; i < t1.getArrayListJugador().size(); i++) {
			
			if(t1.getArrayListJugador().get(i) instanceof Foca) {
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 1, " + t1.getArrayListJugador().get(i).getPos() + ", "+i+
						", seq_tablero.CURRVAL)";
				
				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				           new String[]{"TOTAL"});
				
				BBDD.insert(con, sqlJugador);
				
			}
			else {
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 0, " + t1.getArrayListJugador().get(i).getPos() + ", "+i+
						", seq_tablero.CURRVAL)";
				
				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				           new String[]{"TOTAL"});
				
				BBDD.insert(con, sqlJugador);
				
			}
		}
		
		//Terminamos el insert de jugador
		
		
		
		/*Jugador foca;
		
		for(int i = 0; i < t1.getArrayListJugador().size(); i++) {
			
			if(t1.getJugador(i) instanceof Foca) {
				
				foca = t1.getJugador(i);
				
			}
		}*/

		/*Jugador j1 = t1.getJugador(0);

		Jugador j2 = t1.getJugador(1);

		Jugador j3 = t1.getJugador(2);

		Jugador j4 = t1.getJugador(3);

		Jugador j5 = t1.getJugador(4);

		
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
		*/
		
		
		
		  /*  String sql = "SELECT MAX(id_tablero) AS max_id FROM TABLERO";

		    ArrayList<LinkedHashMap<String, String>> resultado = BBDD.select(con, sql);

		    
		
		
		BBDD.insert(con, "INSERT INTO testing (Num)\n" + "VALUES (2)");

		BBDD.cerrar(con);*/
		
		
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
	
	public static int obtenerIdTablero(Connection con) {
	    String sql = "SELECT MAX(id_tablero) AS max_id FROM TABLERO";

	    ArrayList<LinkedHashMap<String, String>> resultado = BBDD.select(con, sql);

	    if (resultado.size() > 0 && resultado.get(0).get("MAX_ID") != null) {
	        return Integer.parseInt(resultado.get(0).get("MAX_ID")) + 1;
	    } else {
	        return 1; 
	    }
	}
}
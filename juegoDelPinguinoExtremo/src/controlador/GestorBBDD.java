package controlador;

import modelo.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GestorBBDD {

	public static Connection con;
	
	private static int count;

	
	public GestorBBDD() {
		
	}

	
	public void guardar(Tablero t1) {
		
		con = BBDD.conectarBaseDatos();

		//Iniciamos INSERT de la tabla TABLERO

		int turnos_tablero = t1.getTurnos();

		Jugador jugador_actual = t1.getJugadorActual();

		ArrayList<Casilla> casillas = t1.getArrayListCasilla();
		
		
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
						", seq_tablero.CURRVAL, '" + t1.getArrayListJugador().get(i).getNombre() +"', '" +  t1.getArrayListJugador().get(i).getColor() + 
						"', " +  t1.getArrayListJugador().get(i).getDeudaTurnos() + ", " +   t1.getArrayListJugador().get(i).getPartidasTotales() + ")";
				
				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				           new String[]{"TOTAL"});
				
				System.out.println(sqlJugador);
				
				BBDD.insert(con, sqlJugador);
				
				
			}
			
			else {
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 0, " + t1.getArrayListJugador().get(i).getPos() + ", "+i+
						", seq_tablero.CURRVAL, '" + t1.getArrayListJugador().get(i).getNombre() +"', '" +  t1.getArrayListJugador().get(i).getColor() + 
						"', " +  t1.getArrayListJugador().get(i).getDeudaTurnos() + ", " +   t1.getArrayListJugador().get(i).getPartidasTotales() + ")";		
				
				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				           new String[]{"TOTAL"});
				
				System.out.println(sqlJugador);
				
				BBDD.insert(con, sqlJugador);
				
			}
			
			//Hacemos el insert de inventario a la vez que el de jugador
			
			int n_peces = t1.getJugador(i).getInventario().getPez().size();

			int n_bolas = t1.getJugador(i).getInventario().getBolas().size();

			int n_dadoR = 0;
			
			int n_dadoL = 0;

			for (Dado dado : t1.getJugador(i).getInventario().getDado()) {

				if (dado instanceof DadoRapido) {

					n_dadoR++;

				} else if (dado instanceof DadoLento) {

					n_dadoL++;

				}
			}
			
			String sqlInventario = "INSERT INTO INVENTARIO VALUES(seq_inventario.NEXTVAL, "+ n_peces + ",  "+ n_bolas + ", "+ n_dadoR +
					", "+ n_dadoL +", seq_jugador.CURRVAL)";
			
			BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
			           new String[]{"TOTAL"});
			
			BBDD.insert(con, sqlInventario);
		}
	}

	public Tablero  cargarTablero(int indice) {
		con = BBDD.conectarBaseDatos();
		
		//HACEMOS EL SELECT QUE NECESITAMOS DE LA TABLA TABLERO
		
		ArrayList<LinkedHashMap<String, String>> partida =
			    BBDD.select(con, "SELECT ID_TABLERO, TURNOS, JUGADOR_ACTUAL, FECHA_INICIO FROM TABLERO WHERE ID_TABLERO = "+indice);
		
		LinkedHashMap<String, String> fila = partida.get(0);

		int idTablero = Integer.parseInt(fila.get("ID_TABLERO"));
		
		int turnos = Integer.parseInt(fila.get("TURNOS"));
		
		int jugadorActual = Integer.parseInt(fila.get("JUGADOR_ACTUAL"));
		
		String fecha = fila.get("FECHA_INICIO");
         
        ArrayList <Casilla> casilla = new ArrayList <>();
		
		try (Statement st = con.createStatement();
				
			     ResultSet rs = st.executeQuery("SELECT CASILLAS FROM TABLERO WHERE ID_TABLERO = " + indice)) {

			    if (rs.next()) {
			    	
			        Array array = rs.getArray("CASILLAS");
			        
			        String[] casillas = (String[]) array.getArray();
			        
			       
			        for (int i = 0; i < casillas.length; i++) {
			        	
			        	if(casillas[i] == "Normal") {
							
							casilla.add(new Normal());
							
						}
						
						else if(casillas[i] == "Evento") {
							
							casilla.add(new Evento());
							
						}
						
						else if(casillas[i] == "SueloQuebradizo") {
							
							casilla.add(new SueloQuebradizo());
							
						}
						
						else if(casillas[i] == "Trineo") {
							
							casilla.add(new Trineo());
							
						}
						
						else if(casillas[i] == "Oso") {
							
							casilla.add(new Oso());
							
						}
			        }
			    }

			} catch (SQLException e) {
			    e.printStackTrace();
			}
		//TERMINAMOS EL SELECT DE TABLERO
		
		//Empezamos el SELECT de jugadores
		
		ArrayList<Jugador>jugadores = new ArrayList<>();
		
		ArrayList<LinkedHashMap<String, String>> jugador =
			    BBDD.select(con, "SELECT * FROM JUGADOR WHERE ID_TABLERO = " + indice + " ORDER BY TURNO ASC");
		
		for(LinkedHashMap<String, String> entrada : jugador) {
			
			int idJugador = Integer.parseInt(entrada.get("ID_JUGADOR"));
			
			ArrayList<LinkedHashMap<String, String>> inventario =
				   
					BBDD.select(con, "SELECT NUM_PECES, NUM_BOLAS, NUM_DADOR, NUM_DADOL FROM INVENTARIO WHERE ID_JUGADOR = " + idJugador);
			
			
			//Realizamos para cada jugador un SELECT de su inventario
			
			LinkedHashMap<String, String> Objeto = inventario.get(0);

			int num_peces = Integer.parseInt(fila.get("NUM_PECES"));
			
			ArrayList<Pez> peces = new ArrayList<>();
			
			for(int i = 0; i < num_peces; i++) {
				
				peces.add(new Pez());
				
			}
			
			int num_bolas = Integer.parseInt(fila.get("NUM_BOLAS"));
			
			ArrayList<BolaDeNieve> bolas = new ArrayList<>();
			
			for(int i = 0; i < num_bolas; i++) {
				
				bolas.add(new BolaDeNieve());
				
			}
			
			int num_dador = Integer.parseInt(fila.get("NUM_DADOR"));
			
			ArrayList<Dado> dados = new ArrayList<>();
			
			for(int i = 0; i < num_dador; i++) {
				
				dados.add(new DadoRapido());
				
			}
			
			int num_dadoL = Integer.parseInt(fila.get("NUM_DADOL"));
			
			for(int i = 0; i < num_dadoL; i++) {
				
				dados.add(new DadoLento());
				
			}
			
			Inventario inventarios = new Inventario(dados, peces, bolas);
			
			//terminamos el SELECT de inventario
			
			int foca = Integer.parseInt(entrada.get("FOCA"));
			
			if(foca == 1) {
				int posicion = Integer.parseInt(entrada.get("POSICION"));
				int turno = Integer.parseInt(entrada.get("TURNO"));
				String nombre = entrada.get("NOMBRE");
				String color = entrada.get("COLOR");
				int turnoPerdido = Integer.parseInt(entrada.get("TURNOSPERDIDOS"));
				int partidasJugadas = Integer.parseInt(entrada.get("PARTIDASJUGADAS"));
				
				Foca nuevo = new Foca(posicion, nombre, color, inventarios, turnoPerdido, partidasJugadas, turno);
				
				jugadores.add(nuevo);

			}
			
			else {
				
				int posicion = Integer.parseInt(entrada.get("POSICION"));
				int turno = Integer.parseInt(entrada.get("TURNO"));
				String nombre = entrada.get("NOMBRE");
				String color = entrada.get("COLOR");
				int turnoPerdido = Integer.parseInt(entrada.get("TURNOSPERDIDOS"));
				int partidasJugadas = Integer.parseInt(entrada.get("PARTIDASJUGADAS"));
				
				Pinguino nuevo = new Pinguino(posicion, nombre, color, inventarios, turnoPerdido, partidasJugadas, turno);
				
				jugadores.add(nuevo);
				
			}

		}
		
		//Terminamos el SELECT de Jugador; Ya tenemos el ArrayList DE Jugadores con sus respectivos inventarios

		
		Jugador jugActual = jugadores.get(jugadorActual);
		
		
		Tablero	tablero = new Tablero(jugadores, casilla, fecha, turnos, jugActual);
		
		
		return tablero;
			
	}

	public  void procesamientoSelect(Connection con, String sql, ArrayList<String> columnas) {

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
	public void procesarValor(String col, String valor) {

		if (col.equals("NUMIDS")) {
			count = Integer.parseInt(valor) + 1;
		}
	}
	

}
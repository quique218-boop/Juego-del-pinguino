package controlador;

import modelo.*;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GestorBBDD {

	public static Connection con;

	public GestorBBDD() {
		con = BBDD.conectarBaseDatos();
	}

	public void guardar(Tablero t1, int slot) { //Guardamos una partida completa

		if (existeSlot(slot)) { // Si ya existe una partida en ese slot, se borra antes de guardar la nueva.

			borrarPartida(slot);

		}

		// Obtenemos los datos principales del tablero

		int turnos_tablero = t1.getTurnos();

		ArrayList<Casilla> casillas = t1.getArrayListCasilla();

		ArrayList<Jugador> jugadores = t1.getArrayListJugador();

		//Busca la posición del jugador actual dentro del ArrayList de jugadores.
		
		int posActual = 0;

		for (int i = 0; i < jugadores.size(); i++) {

			if (jugadores.get(i) == t1.getJugadorActual()) {

				posActual = i;

			}
		}

		//Convierte cada tipo de casilla en texto para poder guardarlo en la base de datos. 
		
		ArrayList<String> casillasBBDD = new ArrayList<>();

		for (int i = 0; i < casillas.size(); i++) {

			switch (casillas.get(i)) {

			case Agujero a -> casillasBBDD.add("Agujero");

			case Evento e -> casillasBBDD.add("Evento");

			case SueloQuebradizo s -> casillasBBDD.add("SueloQuebradizo");

			case Trineo t -> casillasBBDD.add("Trineo");

			case Normal n -> casillasBBDD.add("Normal");

			case Oso o -> casillasBBDD.add("Oso");

			default -> throw new IllegalArgumentException("Unexpected value: " + casillas.get(i));

			}
		}

		//Construye el array de casillas en formato SQL para insertarlo en Oracle.
		String varray = "";

		for (int i = 0; i < casillasBBDD.size() - 1; i++) {

			varray += " '" + casillasBBDD.get(i) + "',";

		}

		varray += " '" + casillasBBDD.get(49) + "'";

		// Crea la sentencia SQL para insertar los datos del tablero.
		
		String sqlTablero = "INSERT INTO TABLERO VALUES(" + slot + ", " + turnos_tablero + ", " + posActual
				+ ", ARRAY_CASILLAS(" + varray + "), SYSDATE)";

		System.out.println(sqlTablero);

		//Muestra informacion de prueba y ejecuta el INSERT del tablero
		BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO", new String[] { "TOTAL" });

		BBDD.insert(con, sqlTablero); // Termina el insert de la tabla TABLERO

		// Hacemos el insert de jugador

		//Recorre a todos los jugadores para guardarlos en la tabla jugador
		for (int i = 0; i < t1.getArrayListJugador().size(); i++) {

			if (t1.getArrayListJugador().get(i) instanceof Foca) { //Si el jugador es foca se guarda como FOCA = 1

				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 1, "
						+ t1.getArrayListJugador().get(i).getPos() + ", " + i + ", " + slot + ", '"
						+ t1.getArrayListJugador().get(i).getNombre() + "', '"
						+ t1.getArrayListJugador().get(i).getColor() + "', "
						+ t1.getArrayListJugador().get(i).getDeudaTurnos() + ", 1,  0)";

				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO", new String[] { "TOTAL" });

				System.out.println(sqlJugador);

				BBDD.insert(con, sqlJugador);

			}

			else {
				
				//Si el jugador es un Pingüino, se guarda con FOCA = 0, usuario y puntuación.
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 0, "
						+ t1.getArrayListJugador().get(i).getPos() + ", " + i + ", " + slot + ", '"
						+ t1.getArrayListJugador().get(i).getNombre() + "', '"
						+ t1.getArrayListJugador().get(i).getColor() + "', "
						+ t1.getArrayListJugador().get(i).getDeudaTurnos() + ", "
						+ obtenerIdUsuario(((Pinguino) t1.getArrayListJugador().get(i)).getUsuario()) + ", "
						+ ((Pinguino) t1.getArrayListJugador().get(i)).getPuntuacion() + ")";

				// BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				// new String[]{"TOTAL"});

				System.out.println(sqlJugador);

				BBDD.insert(con, sqlJugador);

			}

			// Hacemos el insert de inventario a la vez que el de jugador

			int n_peces = t1.getJugador(i).getInventario().getPez().size();

			int n_bolas = t1.getJugador(i).getInventario().getBolas().size();

			int n_dadoR = 0;

			int n_dadoL = 0;

			for (Dado dado : t1.getJugador(i).getInventario().getDado()) { //Diferenciamos si es dado rapido o lento

				if (dado instanceof DadoRapido) {

					n_dadoR++;

				} else if (dado instanceof DadoLento) {

					n_dadoL++;

				}
			}
			//Inserta el inventario asociado al último jugador insertado a partir de CURRVAL
			String sqlInventario = "INSERT INTO INVENTARIO VALUES(seq_inventario.NEXTVAL, " + n_peces + ",  " + n_bolas
					+ ", " + n_dadoR + ", " + n_dadoL + ", seq_jugador.CURRVAL)";

			BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO", new String[] { "TOTAL" });
			System.out.println(sqlInventario);

			BBDD.insert(con, sqlInventario);
		}

	}

	public Tablero cargarTablero(int indice) { //Carga una partida guardada desde la base de datos y reconstruye el objeto Tablero.

		// Obtiene los datos principales del tablero guardado

		ArrayList<LinkedHashMap<String, String>> partida = BBDD.select(con,
				"SELECT ID_TABLERO, TURNOS, JUGADOR_ACTUAL, FECHA_INICIO FROM TABLERO WHERE ID_TABLERO = " + indice);

		LinkedHashMap<String, String> fila = partida.get(0);

		int idTablero = Integer.parseInt(fila.get("ID_TABLERO"));

		int turnos = Integer.parseInt(fila.get("TURNOS"));

		int jugadorActual = Integer.parseInt(fila.get("JUGADOR_ACTUAL"));

		String fecha = fila.get("FECHA_INICIO");

		ArrayList<Casilla> casilla = new ArrayList<>(); //Se reconstruyen las casillas del tablero

		//Recuperamos el array de casillas desde Oracle
		
		try (Statement st = con.createStatement(); 

				ResultSet rs = st.executeQuery("SELECT CASILLAS FROM TABLERO WHERE ID_TABLERO = " + indice)) {

			if (rs.next()) {

				Array array = rs.getArray("CASILLAS");

				ArrayList<String> casillas = new ArrayList<>();

				Object[] casillaProv = (Object[]) array.getArray();

				for (Object c : casillaProv) {
					String tipo = c.toString();
					casillas.add(tipo);
				}

				for (int i = 0; i < casillas.size(); i++) { // Crea objetos Casilla según el texto guardado en la base de datos.

					switch (casillas.get(i)) {

					case "Normal":
						casilla.add(new Normal());
						break;

					case "Evento":
						casilla.add(new Evento());
						break;

					case "Agujero":
						casilla.add(new Agujero());
						break;

					case "SueloQuebradizo":
						casilla.add(new SueloQuebradizo());
						break;

					case "Trineo":
						casilla.add(new Trineo());
						break;

					case "Oso":
						casilla.add(new Oso());
						break;

					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// TERMINAMOS EL SELECT DE TABLERO

		// Empezamos el SELECT de jugadores

		ArrayList<Jugador> jugadores = new ArrayList<>(); //Lista donde se reconstruiran los jugadores

		ArrayList<LinkedHashMap<String, String>> jugador = BBDD.select(con,
				"SELECT * FROM JUGADOR WHERE ID_TABLERO = " + indice + " ORDER BY TURNO ASC"); //Recuperamos todos los jugadores del tablero, ordenados por turno

		for (LinkedHashMap<String, String> entrada : jugador) { //Recorremos cada jugador recuperado

			int idJugador = Integer.parseInt(entrada.get("ID_JUGADOR"));

			//Recuperamos el inventario
			
			ArrayList<LinkedHashMap<String, String>> inventario =

					BBDD.select(con,
							"SELECT NUM_PECES, NUM_BOLAS, NUM_DADOR, NUM_DADOL FROM INVENTARIO WHERE ID_JUGADOR = "
									+ idJugador);

			// Realizamos para cada jugador un SELECT de su inventario

			LinkedHashMap<String, String> Objeto = inventario.get(0);

			int num_peces = Integer.parseInt(Objeto.get("NUM_PECES"));

			ArrayList<Pez> peces = new ArrayList<>();

			for (int i = 0; i < num_peces; i++) {

				peces.add(new Pez());

			}

			int num_bolas = Integer.parseInt(Objeto.get("NUM_BOLAS"));

			ArrayList<BolaDeNieve> bolas = new ArrayList<>();

			for (int i = 0; i < num_bolas; i++) {

				bolas.add(new BolaDeNieve());

			}

			int num_dador = Integer.parseInt(Objeto.get("NUM_DADOR"));

			ArrayList<Dado> dados = new ArrayList<>();

			for (int i = 0; i < num_dador; i++) {

				dados.add(new DadoRapido());

			}

			int num_dadoL = Integer.parseInt(Objeto.get("NUM_DADOL"));

			for (int i = 0; i < num_dadoL; i++) {

				dados.add(new DadoLento());

			}

			Inventario inventarios = new Inventario(dados, peces, bolas); //Creamos inventario completo del jugador

			

			int foca = Integer.parseInt(entrada.get("FOCA")); //Comprobamos si el jugador es foca o pinguino

			if (foca == 1) { //Si es foca
				int posicion = Integer.parseInt(entrada.get("POSICION"));
				int turno = Integer.parseInt(entrada.get("TURNO"));
				String nombre = entrada.get("NOMBRE");
				String color = entrada.get("COLOR");
				int turnoPerdido = Integer.parseInt(entrada.get("TURNOSPERDIDOS"));

				Foca nuevo = new Foca(posicion, nombre, color, inventarios, turnoPerdido, turno);

				jugadores.add(nuevo);

			}

			else { //Si es pinguino

				int posicion = Integer.parseInt(entrada.get("POSICION"));
				int turno = Integer.parseInt(entrada.get("TURNO"));
				String nombre = entrada.get("NOMBRE");
				String color = entrada.get("COLOR");
				int turnoPerdido = Integer.parseInt(entrada.get("TURNOSPERDIDOS"));
				int puntuacion = Integer.parseInt(entrada.get("PUNTUACION"));
				int id_usuario = Integer.parseInt(entrada.get("ID_USUARIO"));

				Usuario usuario = obtenerUsuario(id_usuario);

				Pinguino nuevo = new Pinguino(posicion, nombre, color, inventarios, turnoPerdido, turno, puntuacion,
						usuario);

				jugadores.add(nuevo);

			}

		}

		// Terminamos el SELECT de Jugador; Ya tenemos el ArrayList DE Jugadores con sus
		// respectivos inventarios

		Jugador jugActual = jugadores.get(jugadorActual);

		Tablero tablero = new Tablero(jugadores, casilla, fecha, turnos, jugActual, idTablero); //Creamos tablero final cargado

		return tablero;

	}
	
	public static void borrarPartida(int slot) { 

		//Borramos una partida completa: primero inventarios, luego jugadores y finalmente tablero.
		
		String deleteInventario = "DELETE FROM INVENTARIO " + "WHERE id_inventario IN ("
				+ "SELECT id_inventario FROM JUGADOR " + "WHERE id_tablero = " + slot + ")";

		String deleteJugadores = "DELETE FROM JUGADOR WHERE id_tablero = " + slot;

		String deleteTablero = "DELETE FROM TABLERO WHERE id_tablero = " + slot;

		BBDD.delete(con, deleteInventario);
		BBDD.delete(con, deleteJugadores);
		BBDD.delete(con, deleteTablero);
	}

	public boolean validarUsuario(Usuario usuario, String pass) { //Comprobamos si un usuario existe

		String hash = Seguridad.hashPassword(pass);

		String sql = "SELECT EXISTE('" + usuario.getNombre() + "', '" + hash + "') AS RES FROM dual";

		ArrayList<LinkedHashMap<String, String>> res = BBDD.select(con, sql);

		if (res.get(0).get("RES").toUpperCase().equals("S")) {

			return true;

		}

		else {
			return false;

		}

	}

	public boolean crearUsuario(Usuario u, String pass) { //Creamos un nuevo usuario en la base de datos
		
		String sql = "INSERT INTO USUARIO VALUES (SEQ_USUARIO.NEXTVAL, ?, ?, 0, 0, 0)"; //Escribimos la consulta

		try (PreparedStatement ps = con.prepareStatement(sql)) { //Preparamos la consulta

			ps.setString(1, u.getNombre()); //El primer ? se sustituye por el nombre de usuario
			ps.setString(2, Seguridad.hashPassword(pass)); //el segundo ? se sustituye por la contraselña
			ps.executeUpdate(); //Ejecuta la consulta Select preparada en el PreparedStatement
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public int obtenerIdUsuario(Usuario usuario) { //Obtenemos el ID de un usuario a partir de su nombre

		String sql = "SELECT ID_USUARIO " + "FROM USUARIO " + "WHERE NOMBRE = ? ";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, usuario.getNombre()); //El ? se sustituye por el nombre de usuario

			ResultSet rs = ps.executeQuery();

			if (rs.next()) { //Intenta colocarse en la primera fila del resultado si existe fila devuelve true
				return rs.getInt("ID_USUARIO");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; // no encontrado
	}

	public Usuario obtenerUsuario(int idUsuario) { //Obtenemos a un usuario a partir de su ID

		String sql = "SELECT NOMBRE FROM USUARIO WHERE ID_USUARIO = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, idUsuario);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String nombre = rs.getString("NOMBRE");

				return new Usuario(nombre);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null; // no encontrado
	}

	public boolean existeSlot(int slot) { //Comprobamos si ya existe una partida guardada en un slot concreto

		String sql = "SELECT COUNT(*) FROM TABLERO WHERE ID_TABLERO = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, slot);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// FUNCIONES PL/SQL PARA LAS ESTADÍSTICAS

	public int partidasGanadas(int id) { //Devolvemos el número de partidas ganadas por un usuario

		int ganadas = 0;

		String sql = "SELECT PARTIDASGANADAS " + "FROM USUARIO " + "WHERE ID_USUARIO = " + id;

		try {

			ArrayList<LinkedHashMap<String, String>> res = BBDD.select(con, sql);

			if (!res.isEmpty()) {
				ganadas = Integer.parseInt(res.get(0).get("PARTIDASGANADAS"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ganadas;

	}

	public int partidasJugadas(int id) { //Devuelve el número de partidas jugadas por un usuario

		int jugadas = 0;

		String sql = "SELECT PARTIDASJUGADAS " + "FROM USUARIO " + "WHERE ID_USUARIO = " + id;

		try {

			ArrayList<LinkedHashMap<String, String>> res = BBDD.select(con, sql);

			if (!res.isEmpty()) {
				jugadas = Integer.parseInt(res.get(0).get("PARTIDASJUGADAS"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jugadas;
	}

	public int recordUsuario(int id) { //Devuelve la puntuacion total de un usuario

		int record = 0;

		String sql = "SELECT PUNTUACIONTOTAL " + "FROM USUARIO " + "WHERE ID_USUARIO = " + id;

		try {

			ArrayList<LinkedHashMap<String, String>> res = BBDD.select(con, sql);

			if (!res.isEmpty()) {
				record = Integer.parseInt(res.get(0).get("PUNTUACIONTOTAL"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return record;

	}

	public double obtenerMediaPuntuacion() { //Llamamos a una función PL/SQL para que calcule la media

		double media = 0;

		try {

			CallableStatement stmt = con.prepareCall("{ ? = call MEDIAPUNTUACION() }");

			stmt.registerOutParameter(1, java.sql.Types.NUMERIC);

			stmt.execute();

			media = stmt.getDouble(1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return media;

	}

	public String obtenerRankingTexto() { //Obtenemos el ranking de usuarios desde una función que devuelve un cursor.

		StringBuilder resultado = new StringBuilder();

		try {

			CallableStatement stmt = con.prepareCall("{ ? = call RANKING() }");
			stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

			stmt.execute();

			ResultSet rs = ((oracle.jdbc.OracleCallableStatement) stmt).getCursor(1);
			int posicion = 1;

			while (rs.next()) {
				String nombre = rs.getString("NOMBRE");
				int puntos = rs.getInt("PUNTUACIONTOTAL");

				resultado.append(posicion).append(". ").append(nombre).append(" - ").append(puntos).append(" pts\n");

				posicion++;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "Error al cargar ranking";
		}

		return resultado.toString();
	}

	public int obtenerRecordGlobal() { //Obtenemos el record global de partidas ganadas a partir de una funcion PL/SQL

		int record = 0;

		try {

			CallableStatement cs = con.prepareCall("{ ? = call RECORD_GANADAS() }");

			cs.registerOutParameter(1, java.sql.Types.INTEGER);

			cs.execute();

			record = cs.getInt(1);

			cs.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return record;
	}

	public String obtenerJugadoresRecord() { //Obtenemos los jugadores que tienen el récord global

		String texto = "";

		try {

			CallableStatement cs = con.prepareCall("{ ? = call JUGADORES_RECORD() }");

			cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

			cs.execute();

			ResultSet rs = (ResultSet) cs.getObject(1);

			while (rs.next()) {

				texto += "🏆 " + rs.getString("NOMBRE") + "\n";
			}

			rs.close();
			cs.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return texto;
	}

	public String obtenerJugadoresSuperiorMedia() { //Obtenemos los jugadores que stán por encima de la media de partidas ganadas

		String texto = "";

		try {

			CallableStatement cs = con.prepareCall("{ ? = call JUGADORES_SUPERIOR_MEDIA() }");

			cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

			cs.execute();

			ResultSet rs = (ResultSet) cs.getObject(1);

			while (rs.next()) {

				texto += "⭐ " + rs.getString("NOMBRE") + " (" + rs.getInt("PARTIDASGANADAS") + ")\n";
			}

			rs.close();
			cs.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return texto;
	}

	public int obtenerPosicionRanking(int idUsuario) { //A partir de una ID obtenemos la posición de un usuario dentro del ranking

		int posicion = -1;

		try {

			CallableStatement cs = con.prepareCall("{ ? = call POSICION_RANKING(?) }");

			cs.registerOutParameter(1, java.sql.Types.INTEGER);

			cs.setInt(2, idUsuario);

			cs.execute();

			posicion = cs.getInt(1);

			cs.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return posicion;
	}

	public double obtenerPorcentajeInferior(int ganadas) { //Calcula qué porcentaje de usuarios tiene menos partidas ganadas que el valor indicado.

		double porcentaje = 0;

		try {

			CallableStatement cs = con.prepareCall("{ ? = call PORCENTAJE_INFERIOR(?) }");

			cs.registerOutParameter(1, java.sql.Types.DOUBLE);

			cs.setInt(2, ganadas);

			cs.execute();

			porcentaje = cs.getDouble(1);

			cs.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return porcentaje;
	}

	public void sumarPartidaJugada(String nombreUsuario) { //Sumamos una partida jugada al usuario indicado

		try {

			String sql = "UPDATE USUARIO " + "SET PARTIDASJUGADAS = " + "PARTIDASJUGADAS + 1 " + "WHERE NOMBRE = ?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, nombreUsuario);

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sumarPartidaGanada(String nombreUsuario) { //Sumamos una partida ganada al usuario indicado

		try {

			String sql = "UPDATE USUARIO " + "SET PARTIDASGANADAS = " + "PARTIDASGANADAS + 1 " + "WHERE NOMBRE = ?";

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, nombreUsuario);

			ps.executeUpdate();

			ps.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sumarPuntuacion(String nombre, int puntos) { //Sumamos la puntuacion total de los usuarios

		String sql = "UPDATE USUARIO " + "SET PUNTUACIONTOTAL = PUNTUACIONTOTAL + ? " + "WHERE NOMBRE = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, puntos);
			ps.setString(2, nombre);

			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double mediaPartidasGanadas() { //Calculamos la media de partidas ganadas llamando a PL/SQL

		double media = 0;

		try {

			CallableStatement stmt = con.prepareCall("{ ? = call MEDIA_PARTIDAS_GANADAS() }");

			stmt.registerOutParameter(1, java.sql.Types.NUMERIC);

			stmt.execute();

			media = stmt.getDouble(1);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return media;
	}

	public String obtenerInfoSlot(int slot) { //Obtenemos información resumida de un slot

		String sql = """
				    SELECT turnos, fecha_inicio
				    FROM TABLERO
				    WHERE ID_TABLERO = ?
				""";

		try {

			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, slot);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {

				int turno = rs.getInt("turnos");

				Timestamp fecha = rs.getTimestamp("fecha_inicio");

				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

				return "Turno " + turno + "\n" + sdf.format(fecha);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

}
package controlador;

import modelo.*;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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

	
	public static void guardar(Tablero t1, int slot) {
		
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
		
		String varray = "";
		
		for(int i = 0; i < casillasBBDD.size()-1; i++) {
			
			 varray+= " '"+casillasBBDD.get(i)+"',";
			
		}
		
		varray += " '"+casillasBBDD.get(49)+"'";
		
		String sqlTablero = "INSERT INTO TABLERO VALUES("+ slot+", "
		+turnos_tablero+", "+posActual+", ARRAY_CASILLAS("+varray+"), SYSDATE, 1)";
		
		System.out.println(sqlTablero);

		
		BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
		           new String[]{"TOTAL"});

		BBDD.insert(con, sqlTablero); //Termina el insert de la tabla TABLERO
		
		
		//Hacemos el insert de jugador
		
		for(int i = 0; i < t1.getArrayListJugador().size(); i++) {
			
			if(t1.getArrayListJugador().get(i) instanceof Foca) {
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 1, " + t1.getArrayListJugador().get(i).getPos() + ", "+i+
						", "+slot+", '" + t1.getArrayListJugador().get(i).getNombre() +"', '" +  t1.getArrayListJugador().get(i).getColor() + 
						"', " +  t1.getArrayListJugador().get(i).getDeudaTurnos() + ", 0,  0)";
				
				BBDD.print(con, "SELECT COUNT(*) AS TOTAL FROM TABLERO",
				           new String[]{"TOTAL"});
				
				System.out.println(sqlJugador);
				
				BBDD.insert(con, sqlJugador);
				
				
			}
			
			else {
				
				String sqlJugador = "INSERT INTO JUGADOR VALUES(seq_jugador.NEXTVAL, 0, " + t1.getArrayListJugador().get(i).getPos() + ", "+i+
						", "+slot+", '" + t1.getArrayListJugador().get(i).getNombre() +"', '" +  t1.getArrayListJugador().get(i).getColor() + 
						"', " +  t1.getArrayListJugador().get(i).getDeudaTurnos() + ", " + obtenerIdUsuario(((Pinguino)t1.getArrayListJugador().get(i)).getUsuario()) + ", " +
						((Pinguino)t1.getArrayListJugador().get(i)).getPuntuacion() +")";		
				
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
	
	

	public static Tablero cargarTablero(int indice) {
		
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
			        
			        ArrayList<String> casillas = new ArrayList<>(); 
			        
			        Object[] casillaProv = (Object[]) array.getArray();

			        for (Object c : casillaProv) {
			            String tipo = c.toString();
			            casillas.add(tipo);
			        }
			        
			       
			        for (int i = 0; i < casillas.size(); i++) {
			        	
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

			int num_peces = Integer.parseInt(Objeto.get("NUM_PECES"));
			
			ArrayList<Pez> peces = new ArrayList<>();
			
			for(int i = 0; i < num_peces; i++) {
				
				peces.add(new Pez());
				
			}
			
			int num_bolas = Integer.parseInt(Objeto.get("NUM_BOLAS"));
			
			ArrayList<BolaDeNieve> bolas = new ArrayList<>();
			
			for(int i = 0; i < num_bolas; i++) {
				
				bolas.add(new BolaDeNieve());
				
			}
			
			int num_dador = Integer.parseInt(Objeto.get("NUM_DADOR"));
			
			ArrayList<Dado> dados = new ArrayList<>();
			
			for(int i = 0; i < num_dador; i++) {
				
				dados.add(new DadoRapido());
				
			}
			
			int num_dadoL = Integer.parseInt(Objeto.get("NUM_DADOL"));
			
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
				
				Foca nuevo = new Foca(posicion, nombre, color, inventarios, turnoPerdido, turno);
				
				jugadores.add(nuevo);

			}
			
			else {
				
				int posicion = Integer.parseInt(entrada.get("POSICION"));
				int turno = Integer.parseInt(entrada.get("TURNO"));
				String nombre = entrada.get("NOMBRE");
				String color = entrada.get("COLOR");
				int turnoPerdido = Integer.parseInt(entrada.get("TURNOSPERDIDOS"));
				int puntuacion = Integer.parseInt(entrada.get("PUNTUACION"));
				int id_usuario = Integer.parseInt(entrada.get("ID_USUARIO"));
				
				Usuario usuario = obtenerUsuario(id_usuario);
				
				Pinguino nuevo = new Pinguino(posicion, nombre, color, inventarios, turnoPerdido, turno, puntuacion, usuario);
				
				jugadores.add(nuevo);
				
			}

		}
		
		//Terminamos el SELECT de Jugador; Ya tenemos el ArrayList DE Jugadores con sus respectivos inventarios

		
		Jugador jugActual = jugadores.get(jugadorActual);
		
		
		Tablero	tablero = new Tablero(jugadores, casilla, fecha, turnos, jugActual, idTablero);
		
		
		return tablero;
			
	}
	
	public void actualizarTablero(Tablero tablero, int id) {
		
		
		con = BBDD.conectarBaseDatos();

		BBDD.update(con, "DELETE FROM INVENTARIO WHERE ID_JUGADOR IN (SELECT ID_JUGADOR FROM JUGADOR WHERE ID_TABLERO = " + id + ")");
	    
	    BBDD.update(con, "DELETE FROM JUGADOR WHERE ID_TABLERO = " + id);
	    
	    BBDD.update(con, "DELETE FROM TABLERO WHERE ID_TABLERO = " + id);


	    guardar(tablero, id);

	    BBDD.cerrar(con);
			
	}
	
	public static boolean validarUsuario(Usuario usuario) {
	    
	    Connection con = BBDD.conectarBaseDatos();
	    
	    String sql = "SELECT EXISTE('" + usuario.getNombre() + "', '" + usuario.getContraseña() + "') AS RES FROM dual";
	    
	    ArrayList<LinkedHashMap<String, String>> res = BBDD.select(con, sql);
	    
	    BBDD.cerrar(con);
	    
	    if (res.get(0).get("RES").toUpperCase().equals("S")) {
	    	
	        return true;
	        
	    }
	    
	    else {
	    	return false;
	    
	    }
	    
	}
	
	public static boolean crearUsuario(Usuario u) {
	    String sql = "INSERT INTO USUARIO (ID_USUARIO, NOMBRE, CONTRASEÑA) VALUES (SEQ_USUARIO.NEXTVAL, ?, ?)";

	    try (Connection con = BBDD.conectarBaseDatos();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, u.getNombre());
	        ps.setString(2, u.getContraseña());

	        ps.executeUpdate();
	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static int obtenerIdUsuario(Usuario usuario) {

	    String sql = "SELECT ID_USUARIO " +
	                 "FROM USUARIO " +
	                 "WHERE NOMBRE = ? ";

	    try (Connection con = BBDD.conectarBaseDatos();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, usuario.getNombre());

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("ID_USUARIO");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return -1; // no encontrado
	}
	
	public static Usuario obtenerUsuario(int idUsuario) {

	    String sql = "SELECT NOMBRE, CONTRASENA FROM USUARIO WHERE ID_USUARIO = ?";

	    try (Connection con = BBDD.conectarBaseDatos();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, idUsuario);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            String nombre = rs.getString("NOMBRE");
	            String contrasena = rs.getString("CONTRASENA");

	            return new Usuario(nombre, contrasena);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return null; // no encontrado
	}
	
	public static boolean existeSlot(int slot) {

	    String sql = "SELECT COUNT(*) FROM TABLERO WHERE SLOT = ?";

	    try (Connection con = BBDD.conectarBaseDatos();
	         PreparedStatement ps = con.prepareStatement(sql)) {

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
	
	
	//FUNCIONES PL/SQL PARA LAS ESTADÍSTICAS
	
	public static int partidasGanadas(int id) {
		
		int ganadas = 0;

	    String sql =
	        "SELECT PARTIDASGANADAS " +
	        "FROM USUARIO " +
	        "WHERE ID_USUARIO = " + id;

	    try (Connection con = BBDD.conectarBaseDatos()) {

	        ArrayList<LinkedHashMap<String, String>> res =
	                BBDD.select(con, sql);

	        if (!res.isEmpty()) {
	            ganadas = Integer.parseInt(
	                    res.get(0).get("PARTIDASGANADAS")
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return ganadas;
		
	}
	
	public static int partidasJugadas(int id) {
		
		int jugadas = 0;

	    String sql =
	        "SELECT PARTIDASJUGADAS " +
	        "FROM USUARIO " +
	        "WHERE ID_USUARIO = " + id;

	    try (Connection con = BBDD.conectarBaseDatos()) {

	        ArrayList<LinkedHashMap<String, String>> res =
	                BBDD.select(con, sql);

	        if (!res.isEmpty()) {
	            jugadas = Integer.parseInt(
	                    res.get(0).get("PARTIDASJUGADAS")
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return jugadas;
	}
		
	

	public static int recordUsuario(int id) {
		
		int record = 0;

	    String sql =
	        "SELECT PUNTUACIONTOTAL " +
	        "FROM USUARIO " +
	        "WHERE ID_USUARIO = " + id;

	    try (Connection con = BBDD.conectarBaseDatos()) {

	        ArrayList<LinkedHashMap<String, String>> res =
	                BBDD.select(con, sql);

	        if (!res.isEmpty()) {
	            record = Integer.parseInt(
	                    res.get(0).get("PUNTUACIONTOTAL")
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return record;
	
	}
	
	public static double obtenerMediaPuntuacion() {
		
		double media = 0;

	    try (Connection conn = BBDD.conectarBaseDatos()) {

	        CallableStatement stmt = conn.prepareCall("{ ? = call MEDIAPUNTUACION() }");

	        stmt.registerOutParameter(1, java.sql.Types.NUMERIC);

	        stmt.execute();

	        media = stmt.getDouble(1);

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return media;
		
	}
	
	
	public static String obtenerRankingTexto() {
	    
		StringBuilder resultado = new StringBuilder();

	    try (Connection conn = BBDD.conectarBaseDatos()) {

	        CallableStatement stmt = conn.prepareCall("{ ? = call RANKING() }");
	        stmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

	        stmt.execute();

	        ResultSet rs = ((oracle.jdbc.OracleCallableStatement) stmt).getCursor(1);
	        int posicion = 1;

	        while (rs.next()) {
	            String nombre = rs.getString("NOMBRE");
	            int puntos = rs.getInt("PUNTUACIONTOTAL");

	            resultado.append(posicion)
	                     .append(". ")
	                     .append(nombre)
	                     .append(" - ")
	                     .append(puntos)
	                     .append(" pts\n");
	            

	            posicion++;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error al cargar ranking";
	    }

	    return resultado.toString();
	}
	

}
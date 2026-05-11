package modelo;

public class Usuario {
	
	private String nombre;
	private String contraseña;
	
	// Crea un usuario con nombre y contraseña.
	
	public Usuario(String nombre, String contraseña) {
		
		this.nombre = nombre;
		this.contraseña = contraseña;
	}
	
	public String getNombre(){
		
		return nombre;
		
	}
	
	public String getContraseña() {
		
		return contraseña;
			
	}

}

package modelo;

public class Usuario {
	
	private String nombre;
	
	public Usuario(String nombre) {
		
		this.nombre = nombre; //Recibe un nombre y lo guarda en el atributo
		
	}
	
	public String getNombre(){
		
		return nombre; //Devuelve el nombre
		
	}

}

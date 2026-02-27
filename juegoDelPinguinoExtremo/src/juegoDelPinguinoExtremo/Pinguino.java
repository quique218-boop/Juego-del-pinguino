package juegoDelPinguinoExtremo;

public class Pinguino extends Jugador {
	
	private Inventario inventario;
	
	public Pinguino(Inventario inventario, String nombre, String color) {
		
		super(nombre, color);
		
		this.inventario = inventario;
		
		
	}
	
	public Inventario getInventario() {
		
		return inventario;
		
	}
	
	public void setInventario(Inventario inventario) {
		
		this.inventario = inventario;
		
	}
	
	public void gestionarBatalla(Pinguino jugador) {
		
		if(jugador == null) {
			
			return;
			
		}
			
			if(this.getPos() != jugador.getPos()) {
				
				return;					
		}
		
		int misBolas = contarBolasdeNieve();
		
		int susBolas = jugador.contarBolasdeNieve();
		
		if(misBolas > susBolas) {
			
			jugador.moverPosicion(-1);
			
			this.consumirBoladeNieve();
			
		}else if(susBolas > misBolas) {
			
			this.moverPosicion(-1);
			
			jugador.consumirBoladeNieve();
		
		}else {
			
			//En caso de que empaten los dos pierden todas sus bolas de nieve
			
			ponerBolasdeNieveACero();
			
			jugador.ponerBolasdeNieveACero();
			
		}
		
	}
	
	private int contarBolasdeNieve() {

	    if (inventario == null || inventario.getLista() == null) {
	    	
	        return 0;
	        
	    }

	    for (Item it : inventario.getLista()) {

	        if (it != null && it.getNombre() != null) {

	            String nombre = it.getNombre().trim().toLowerCase();

	            if (nombre.equals("bola de nieve") || nombre.equals("bolas de nieve")) {
	            	
	                return it.getCantidad();
	                
	            }
	        }
	    }

	    return 0;
	}
	
	private void consumirBoladeNieve() {

	    if (inventario != null && inventario.getLista() != null) {

	        for (int i = 0; i < inventario.getLista().size(); i++) {

	            Item it = inventario.getLista().get(i);

	            if (it != null && it.getNombre() != null) {

	                String nombre = it.getNombre().trim().toLowerCase();

	                if (nombre.equals("bola de nieve") || nombre.equals("bolas de nieve")) {

	                    int cantidad = it.getCantidad();

	                    if (cantidad > 0) {
	                        it.setCantidad(cantidad - 1);
	                    }

	                    if (it.getCantidad() == 0) {
	                        inventario.getLista().remove(i);
	                    }

	                    return;
	                }
	            }
	        }
	    }
	}
	
	public void ponerBolasdeNieveACero() {
		
		if(inventario == null || inventario.getLista() == null) {
			
			return;
			
		}
		
		for(int i = 0; i < inventario.getLista().size(); i++) {
			
			Item it = inventario.getLista().get(i);
			
			if(it != null && it.getNombre() != null) {
				
				String nombre = it.getNombre().trim().toLowerCase();
				
				if(nombre.equals("bola de nieve") || nombre.equals("bolas de nieve")) {
					
					it.setCantidad(0);
					
					return;
					
				}
			}
		}
	}
	
	public void usarItem(Item i) {
		
		if(i == null || inventario == null || inventario.getLista() == null) {
			
			return;
			
		}
		
		for(int index = 0; index < inventario.getLista().size(); index++) {
			
			Item it = inventario.getLista().get(index);
			
			if (it != null && it.getNombre() != null && i.getNombre() != null && it.getNombre().equalsIgnoreCase(i.getNombre())) {
				
				int cantidad = it.getCantidad();
				
				if(cantidad > 0) {
					
					it.setCantidad(cantidad -1);
				
				}
				
				if(it.getCantidad() == 0) {
					
					inventario.getLista().remove(index);
					
				}
				
				return;
			}
		}
	}
	
	public void anadirItem(Item i) {
		
		if(i == null) {
			
			return;
			
		}
		
		if(inventario == null) {
			
			inventario = new Inventario();
			
		}
		
		inventario.getLista().add(i);
		
	}
	
	public void quitarItem(Item i) {
		
		if(i == null || inventario == null || inventario.getLista() == null) {
			
			return;
		}
		
		inventario.getLista().remove(i);
		
	}
		
}
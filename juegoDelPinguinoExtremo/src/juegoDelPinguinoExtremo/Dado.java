package juegoDelPinguinoExtremo;

public class Dado extends Item {
	
	private int max;
	
	private int min;
	
	public Dado(String nombre, int cantidad, int max,int min) {
		
		super(nombre, cantidad);
		 
		this.max = max;
		 
		this.min = min;
	
	}
	
	public int getMax() {
		return max;
	}
	
	public int getMin() {
		return min;
	}
	
	public void setMax(int max) {
		this.max = max;
	}
	
	public void setMin(int min) {
		this.min = min;
	}
	
	
	

}

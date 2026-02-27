package juegoDelPinguinoExtremo;

public class Dado extends Item {
	
	private int max;
	
	private int min;
	
	public Dado(String nombre) {
		
		super(nombre);
		 
		this.max = 12;
		 
		this.min = 1;
	
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

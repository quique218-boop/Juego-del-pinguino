package juegoDelPinguinoExtremo;

import java.util.Random;

public class Dado extends Item {
	Random rand = new Random();
	
	private int max;
	
	private int min;
	
	public Dado(String nombre) {
		
		super(nombre);
		 
		this.max = 10;
		 
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
	
	public int tirarDado() {
		return rand.nextInt((max - min) + 1) + min;
		
	}
	
	
	

}

package juegoDelPinguinoExtremo;

import java.util.ArrayList;

public class Inventario {
	
	private ArrayList<Dado> listaDado = new ArrayList<>();
	
	private ArrayList<Pez> listaPez = new ArrayList<>();
	
	private ArrayList<BolaDeNieve> listaBolas = new ArrayList<>();
	
	public Inventario(ArrayList<Dado> listaDado, ArrayList<Pez> listaPez, ArrayList<BolaDeNieve> listaBolas) {
		
		this.listaDado = listaDado;
		this.listaPez = listaPez;
	    this.listaBolas = listaBolas;
		
	}
	
	public void setListaDado(ArrayList<Dado> listaDado) {
		this.listaDado = listaDado;
	}
	
	public void setListaPez(ArrayList<Pez> listaPez) {
		this.listaPez = listaPez;
	}
	
	public void setListaBolas(ArrayList<BolaDeNieve> listaBolas) {
		this.listaBolas = listaBolas;
	}
	
	public void addListaDado(Dado item) {
		listaDado.add(item);
	}
	
	public void addListaPez(Pez item) {
		listaPez.add(item);
	}
	
	public void addListaBola(BolaDeNieve item) {
		listaBolas.add(item);
	}
	
	public ArrayList<Dado> getDado(){
		return listaDado;
	}
	
	public ArrayList<Pez> getPez(){
		return listaPez;
	}
	
	public ArrayList<BolaDeNieve> getBolas(){
		return listaBolas;
	}
	
	public void RobarInventario() {
		
		int totalDado = listaDado.size(); //Cogemos el tamaño de la lista de dados
		
		int mDado = totalDado / 2; //Dividimos la lista a la mitad
		
		while(mDado > 0) { 
			
			if(!listaDado.isEmpty()) { //Si la lista no es vacia
				
				listaDado.removeFirst(); //Borra el primero
				
				mDado--;
			}
		}
		
		int totalPez = listaPez.size();
		
		int mPez = totalPez / 2;
		
		while(mPez > 0) {
			
			if(!listaPez.isEmpty()) {
				
				listaPez.removeFirst();
				
				mPez--;
			}
		}
		
		int totalBolas = listaBolas.size();
		
		int mBolas = totalBolas / 2;
		
		while(mBolas > 0) {
			
			if(!listaBolas.isEmpty()) {
				
				listaBolas.removeFirst();
				
				mBolas--;
				
			}
		}
	}

}

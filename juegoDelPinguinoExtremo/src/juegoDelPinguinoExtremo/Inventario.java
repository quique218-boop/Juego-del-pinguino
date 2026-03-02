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
	
	public boolean addListaDado(Dado item) {
		if(listaDado.size()<3) {
			listaDado.add(item);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean addListaPez(Pez item) {
		if(listaPez.size()<2) {
			listaPez.add(item);
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean addListaBolas(BolaDeNieve item) {
		if(listaBolas.size()>3) {
			listaBolas.add(item);
			return true;
		}
		else {
			return false;
		}
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
			}
		}
		
		int totalPez = listaPez.size();
		
		int mPez = totalPez / 2;
		
		while(mPez > 0) {
			
			if(!listaPez.isEmpty()) {
				
				listaPez.removeFirst();
				
			}
		}
		
		int totalBolas = listaBolas.size();
		
		int mBolas = totalBolas / 2;
		
		while(mBolas > 0) {
			
			if(!listaBolas.isEmpty()) {
				
				listaBolas.removeFirst();
				
			}
		}
	}

}

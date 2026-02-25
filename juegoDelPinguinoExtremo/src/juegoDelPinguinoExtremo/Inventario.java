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
	
	public void addLista(BolaDeNieve item) {
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
	
	

}

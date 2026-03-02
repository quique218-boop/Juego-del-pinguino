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

	public ArrayList<Dado> getDado() {
		return listaDado;
	}

	public ArrayList<Pez> getPez() {
		return listaPez;
	}

	public ArrayList<BolaDeNieve> getBolas() {
		return listaBolas;
	}

	public void RobarInventario() {

		int mDado = listaDado.size() / 2; // Cogemos el tamaño de la lista de dados y dividimos entre dos

		int mPez = listaPez.size() / 2; 

		int mBolas = listaBolas.size() / 2;

		while (mDado > 0 || mPez > 0 || mBolas > 0) {

			if (!listaDado.isEmpty() && mDado > 0) {

				listaDado.removeFirst(); // Borra el primero

				mDado--;
			}

			if (!listaPez.isEmpty() && mPez > 0) {

				listaPez.removeFirst();

				mPez--;
			}

			if (!listaBolas.isEmpty() && mBolas > 0) {

				listaBolas.removeFirst();

				mBolas--;

			}
		}
	}
}

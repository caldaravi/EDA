// DNI 74007071 ALDARAVI COLL, CARLOS

import java.util.*;

public class Lista {
	private NodoL pr;
	private NodoL ul;
	private int tamLista;//NEW para llevar control del tamanyo de la lista
	
	public Lista(){//done
		pr = null;
		ul = null;
		tamLista = 0;
	}
	
	public boolean esVacia(){ //done
		return pr==null;
	}
	
	public void insertaCabeza(ArrayList<Coordenadas> v){ //done
		NodoL nuevo = new NodoL(v);
		
		if(v!=null){
			if(pr==null){
				pr = new NodoL(v);
				ul = pr;
				tamLista++;
			}
			else{
				nuevo.cambiaNext(pr);
				pr = nuevo;
				tamLista++;
			}
		}
	}
	
	public void insertaCola(ArrayList<Coordenadas> v){ //done
		NodoL nuevo = new NodoL(v);
		
		if(v!=null){
			if(pr==null){
				pr = new NodoL(v);
				ul = pr;
				tamLista++;
			}
			else{
				ul.cambiaNext(nuevo);
				ul = nuevo;
				tamLista++;
			}
		}
	}
	
	public void inserta(ArrayList<Coordenadas> v, int i) throws IndexOutOfBoundsException{ //TODO
		
	}
	
	public boolean borraCabeza(){ //done
		boolean done = false;
		
		if(!esVacia()){
			if(pr == ul){
				pr = null;
				ul = null;
				done = true;
				tamLista--;
			}
			else{
				pr = pr.getNext();
				done = true;
				tamLista--;
			}
		}
		
		return done;
	}
	
	public boolean borraCola(){ //done
		boolean done = false;
		
		NodoL aux = null;
		NodoL tmp = null;

		tmp = pr;
		
		if(!esVacia()){
			if(pr == ul){
				//si solo hay un elemento es cabeza y cola y se borran ambos
				pr = null;
				ul = null;
				done = true;
				tamLista--;
			}
			else{
				//recorro con dos nodos la lista para que cuando uno llegue al ultimo
				//nodo el otro se quede con el antepenultimo y asi borrar el ultimo
				while(tmp.getNext()!=null){
					aux = tmp;
					tmp=tmp.getNext();
				}
				//una vez borrado el ultimo nodo hago que el penultimo sea el ultimo
				aux.cambiaNext(null);
				ul = aux;
				done = true;
				tamLista--;
			}
		}
		
		return done;
	}
	
	public void borra(int i) throws IndexOutOfBoundsException{ //TODO
		
	}
	
	public boolean borra(ArrayList<Coordenadas> v){ //done
		boolean done = false;
		
		if(v!=null && !esVacia()){
			if(pr == ul){
				if(pr.comparaCamino(v)){
					pr = null;
					ul = null;
					done = true;
					tamLista--;
				}
			}
			else{
				if(pr.comparaCamino(v)){
					pr = pr.getNext();
					done = true;
					tamLista--;
				}
				else{
					NodoL del = pr;
					NodoL atr = null;
					
					while(!del.comparaCamino(v) || del.getNext()!=null){
						atr = del;
						del = del.getNext();
					}
					atr.cambiaNext(del.getNext());
					done = true;
					tamLista--;
				}
			}
		}
		return done;
	}
	
	public void escribeLista(){ //done
		if(!esVacia()){
			NodoL aux = pr;
			
			int pos = 1;
			
			for(int i=0; i<tamLista; i++){
				if(!aux.getCamino().isEmpty()){
					System.out.print("camino " + pos + ": ");
					aux.escribeCamino();
					aux = aux.getNext();
					pos++;
				}
			}
		}
	}
	
	public int enLista(ArrayList<Coordenadas> v){ //done
		int pos =-1;
		
		NodoL aux = pr;
		
		if(v!=null && !esVacia()){
			for(int i=0; i<tamLista && pos==-1; i++){
				if(aux.comparaCamino(v)){
					pos = i;
				}
				aux = aux.getNext();
			}
		}
		return pos;
	}
	
	public ArrayList<Coordenadas> getCamino(int pos) throws IndexOutOfBoundsException{ //TODO
		ArrayList<Coordenadas> camino = null;
		return camino;
	}
	
	public int getTamanyoLista(){
		
		return tamLista;
	}
	
	private class NodoL{// done
		private ArrayList<Coordenadas> camino;
		private NodoL next;
		
		public NodoL(ArrayList<Coordenadas> v){ //done
			camino = v;
			next = null;
		}
		
		public NodoL(ArrayList<Coordenadas> v, NodoL n){ //done			
			camino = v;
			next = n;
		}
		
		public void cambiaNext(NodoL n){ //done
			next = n;
		}
		
		public NodoL getNext(){ //done
			return next;
		}
		
		public ArrayList<Coordenadas> getCamino(){ //done
			return camino;
		}
		
		public void escribeCamino(){ //done
			if(!camino.isEmpty()){
				for(int i=0; i<camino.size(); i++){
					System.out.print("(" + camino.get(i).getFila() + "," + camino.get(i).getColumna() + ")");
				}
				System.out.println();
			}
		}
		
		public boolean comparaCamino(ArrayList<Coordenadas> v){ //done
			boolean iguales = false;
			
			if(this.camino.isEmpty() == true && v.isEmpty() == true){
				iguales = true;
			}
			else{
				if(this.camino.isEmpty() == false && v.isEmpty() == false && this.camino.size() == v.size()){
					iguales = true;
					for(int i=0; i<v.size(); i++){
						if(this.camino.get(i).getFila() != v.get(i).getFila() || this.camino.get(i).getColumna() != v.get(i).getColumna())
							iguales = false;
					}
				}
			}
			return iguales;
		}
	}
}

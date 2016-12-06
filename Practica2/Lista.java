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
	
	public void inserta(ArrayList<Coordenadas> v, int i) throws IndexOutOfBoundsException{ //done
		if(v!=null){
			if(!esVacia() && i>=0 && i<this.getTamanyoLista()){ 
				NodoL nuevo = new NodoL(v);
				NodoL aux = pr;
	
					if(i==0){
						insertaCabeza(v);
					}
					else{
						int j = 1;
						while(j<i){
							aux = aux.getNext();
							j++;
						}
						nuevo.cambiaNext(aux.getNext());
						aux.cambiaNext(nuevo);
						tamLista++;
					}
			}
			else{
				throw new IndexOutOfBoundsException(Integer.toString(i));
			}
		}
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
	
	public void borra(int pos) throws IndexOutOfBoundsException{ //done
		//lo primero que hago es comprobar que la lista no esta vacia y que 
		//pos esta en la lista
		if(!esVacia() && pos>=0 && pos<tamLista){
			//si el entero que me pasan es el primero o el ultimo de la lista
			//llamo a borraCabeza o borraCola respectivamente
			if(pos == 0){
				borraCabeza();
			}
			else{
				if(pos == tamLista-1){
					borraCola();
				}
				else{
					//creo dos nodos auxiliares, uno para llegar a la posicion a borrar
					//y el otro para quedarse detras y ser el que apunte al siguiente de pos
					NodoL aux1 = pr.getNext();
					NodoL aux2 = pr;
					int i = 1;//inicializo i a 1 porque ya he comprobado 0
					while(i<pos){
						//busco la posicion que me pasan avanzando con los dos nodos
						//uno detras del otro
						aux2 = aux1;
						aux1 = aux1.getNext();
						i++;
					}
					//una vez encontrada la posicion, al nodo que queda detras le ponemos next 
					//al next de aux1
					aux2.cambiaNext(aux1.getNext());
					tamLista--;
				}
			}
		}
		//si no lo esta lanzo y propago la excepcion
		else{
			throw new IndexOutOfBoundsException(Integer.toString(pos));
		}
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
					
					while(!del.comparaCamino(v) && del.getNext()!=null){
						atr = del;
						del = del.getNext();
					}
					if(del.comparaCamino(v)){
						atr.cambiaNext(del.getNext());
						done = true;
						tamLista--;
					}
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
				System.out.print("camino " + pos + ": ");
				aux.escribeCamino();
				aux = aux.getNext();
				pos++;
			}
		}
	}
	
	public int enLista(ArrayList<Coordenadas> v){ //done
		int pos =-1;
		
		NodoL aux = pr;//creo un nodo aux para recorrer la lista
		
		//compruebo que v sea distinto de null y que hay nodos en lista
		if(v!=null && !esVacia()){
			//recorro la lista hasta el final, si encuentro el camino en 
			//la lista me quedo con su posicion y salgo del for
			for(int i=0; i<tamLista && pos==-1; i++){
				if(aux.comparaCamino(v)){
					pos = i;
				}
				aux = aux.getNext();
			}
		}
		return pos;
	}
	
	public ArrayList<Coordenadas> getCamino(int pos) throws IndexOutOfBoundsException{ //done
		ArrayList<Coordenadas> camino = null;
		
		if(!esVacia() && pos>=0 && pos<tamLista){
			if(pos == 0){
				camino = pr.getCamino();
			}
			else{
				if(pos == tamLista-1){
					camino = ul.getCamino();
				}
				else{
					NodoL aux = pr;
					int i = 0;
					while(i<pos){
						aux = aux.getNext();
						i++;
					}
					camino = aux.getCamino();
				}
			}
		}
		else{
			throw new IndexOutOfBoundsException(Integer.toString(pos));
		}
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

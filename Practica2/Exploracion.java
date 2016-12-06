// DNI 74007071 ALDARAVI COLL, CARLOS

import java.util.*;

public class Exploracion {
	//voy a declarar variables estaticas para hacer un main limpio y corto, llamando a metodos para jugar
	
	static boolean win = false;//la usare para ponerla a true cuando salga del bosque
	static boolean trampa = false;//true cuando hemos pisado una trampa
	static boolean buscandoComienzo = false; //lo pongo a true cuando hay que buscar el comienzo para el segundo bucle
	
	static int x; // filas
	static int y; // columnas
	static int destinos; //destinos en el mapa
	
	static ArrayList<Coordenadas> vacio = new ArrayList<Coordenadas>();// creo un arraylist vacio de coordenadas
	
	static Plano bosque = null;// donde guardare el mapa
	
	static exPath comienzo = null; //donde guardare el comienzo
	static exPath actual = null; //el exPath que usare para saber en la casilla que estoy cuando voy avanzando
	
	static Stack<exPath> pila = null;//la pila
	
	static Casilla[][] plano = null;//matriz del mapa para trabajar mas comodo
	
	static Lista caminos = null;//los caminos que voy recorriendo los anyado a la lista
	
	public static void main(String[]args){
		if(args.length==3){
			x = Integer.parseInt(args[1]); //guardando filas
			y = Integer.parseInt(args[2]); //guardando columnas
			
			bosque = new Plano(x,y); //creo el mapa
			bosque.leePlano(args[0]); //leo el archivo
			
			//guardo el comienzo en un exPath y meto las coordenadas al camino
			comienzo = new exPath(bosque.getComienzo().getCoordenadas().getFila(),bosque.getComienzo().getCoordenadas().getColumna(), vacio);
			
			pila = new Stack<exPath>(); //creo la pila
			
			plano = bosque.getPl();//me traigo la matriz del plano para trabajar mas comodo
			
			caminos = new Lista();//creo una lista para guardar los caminos que he usado
			
			destinos = bosque.getDestinos().size();
			
			//COMIENZO DE EXPLORACION
			
			pila.push(comienzo);//almaceno el comienzo en la pila
			//mientras que la pila no este vacia o no ganemos la partida
			exploraTodosLosDestinos();
			
			//FIN DE LA EXPLORACION
		
			if(!win){
				buscandoComienzo = true;
				//si cuando terminamos la exploracion no hemos ganado ni perdido, hay que poner todas las casillas
				//visitadas a libres y volver al comienzo
				visitadasAlibres();//pasamos todas las casillas visitadas del mapa a libres
				//desapilamos toda la pila al estar en la ultima casilla libre
				while(!pila.isEmpty()){
					pila.pop();
				}
				exPath ultimaCasillaLibre = new exPath(actual.getCoordenadas(), vacio);//vuelvo a crear un camino nuevo desde donde estoy
				pila.push(ultimaCasillaLibre);//por ultimo apilamos en la pila el esta casilla que es la nueva salida
				//esta vez exploramos buscando el comienzo
				exploraComienzo();
				
			}
			
			if(!win)
				//si no se ha conseguido poner el valor de win a true hay que mostrar el mensaje de partida perdida
				System.out.println("HAS PERDIDO EL JUEGO");
			//hay que mostrar los caminos realizados para ganar
			caminos.escribeLista();
		}
	}
	
	private static void exploraTodosLosDestinos(){
		/*NEW desde este metodo hare la exploracion del mapa
		Lo primero que voy a hacer es diferenciar la exploracion a traves del booleano trampa,
		dependera el sentido de la exploracion de esta variable
		*/
		while(!pila.isEmpty() && !win){
			actual = pila.pop(); //desapilamos y guardamos las coordenadas en actual

			//vemos cual es el tipo de la casilla actual
			switch(bosque.consultaCasilla(actual.getCoordenadas())){
				case 'l': //libre
					if(comienzoSinDestinos()){
						//si estoy en el comienzo y no me quedan destinos por visitar gano la partida
						win = true;
						break;
					}
					else{
						//cuando se visita una casilla, se le cambia el valor de 'l' a 'v'
						plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');
						if(!trampa)
							explora();
						else
							exploraConTrampa();
					}
					break;
					
				case 'd'://destino
					bosque.borraDestino(actual.getCoordenadas());//borro el destino de mi ArrayList de destinos
					destinos--;//quitamos un destino del mapa
					plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('l'); //cambio 'd' por 'l'
					visitadasAlibres();//pasamos todas las casillas visitadas del mapa a libres
					//desapilamos toda la pila al estar en un destino
					while(!pila.isEmpty()){
						pila.pop();
					}
					caminos.insertaCola(actual.getCamino());//insertamos el camino recorrido a la cola de la lista
					exPath destino = new exPath(actual.getCoordenadas(), vacio);//vuelvo a crear un camino nuevo desde donde estoy
					pila.push(destino);//por ultimo apilamos en la pila el destino que es la nueva salida
					
					break;
				case 'p'://puerta
					//si es una puerta, tenemos mas casos posibles dentro
					switch(plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].getPropiedad()){
						case 'B'://bloqueda, retrocedemos una posicion
							//la tomamos como un obstaculo
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('o');
							actual.setUltimasCoordenadas();//retrocedo una casilla
							break;
						case 'P'://se abre la puerta y podemos seguir
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');//cambiamos a visitada
							if(!trampa)
								explora();
							else
								exploraConTrampa();
							break;
						case 'S'://salto al siguiente destino no visitado mas cercano
							if(destinos>0){
								Coordenadas sigDestino = destinoCercano();//guardo las coordenadas del destino mas cercano
								actual = new exPath(sigDestino, actual.getCamino());//salto al destino
								pila.push(actual);//apilo
							}
							else{
								//si no quedan destinos por visitar se considera el comienzo como un destino
								actual = new exPath(comienzo.getCoordenadas(), actual.getCamino());//anyado las coordenadas del comienzo al camino
								caminos.insertaCola(actual.getCamino());//encolo el camino actual
								pila.push(actual);
								win = true;//y gano la partida
							}
							break;
						case 'T'://trampa, desactivamos, la ponemos como tipo P y volvemos al comienzo
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setPropiedad('P');
							actual = new exPath(comienzo.getCoordenadas(), actual.getCamino());//se insertan las coordenadas del comienzo al camino
							caminos.insertaCola(actual.getCamino());//se encola el camino actual para empezar otro desde el comienzo
							visitadasAlibres();//vuelvo a poner a libres todas las visitadas
							//se desapila la pila
							while(!pila.isEmpty()){
								pila.pop();
							}
							actual = new exPath(comienzo.getCoordenadas(), vacio);//creo un nuevo camino desde el comienzo
							pila.push(actual);//volvemos al comienzo
							trampa = !trampa; //invertimos el valor de trampa
							break;
						case 'X'://aqui se gana la partida
							
							caminos.insertaCola(actual.getCamino()); //encolamos el camino 
							win = true;//y ganamos
					}
					break;
				case 'c': //comienzo
					plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');//visitada
					if(!trampa)
						explora();
					else
						exploraConTrampa();
					
					break;
			}
		}//FIN WHILE
	}
	
	private static void exploraComienzo(){
		while(!pila.isEmpty() && !win){
			actual = pila.pop(); //desapilamos y guardamos las coordenadas en actual

			//vemos cual es el tipo de la casilla actual
			switch(bosque.consultaCasilla(actual.getCoordenadas())){
				case 'l': //libre
					if(comienzoSinDestinos()){
						//si estoy en el comienzo y no me quedan destinos por visitar gano la partida
						win = true;
						break;
					}
					else{
						//cuando se visita una casilla, se le cambia el valor de 'l' a 'v'
						plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');
						if(!trampa)
							explora();
						else
							exploraConTrampa();
					}
					break;
					
				case 'd'://destino
					bosque.borraDestino(actual.getCoordenadas());//borro el destino de mi ArrayList de destinos
					destinos--;//quitamos un destino del mapa
					plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('l'); //cambio 'd' por 'l'
					visitadasAlibres();//pasamos todas las casillas visitadas del mapa a libres
					//desapilamos toda la pila al estar en un destino
					while(!pila.isEmpty()){
						pila.pop();
					}
					caminos.insertaCola(actual.getCamino());//insertamos el camino recorrido a la cola de la lista
					exPath destino = new exPath(actual.getCoordenadas(), vacio);//vuelvo a crear un camino nuevo desde donde estoy
					pila.push(destino);//por ultimo apilamos en la pila el destino que es la nueva salida
					
					break;
				case 'p'://puerta
					//si es una puerta, tenemos mas casos posibles dentro
					switch(plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].getPropiedad()){
						case 'B'://bloqueda, retrocedemos una posicion
							//la tomamos como un obstaculo
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('o');
							actual.setUltimasCoordenadas();//retrocedo una casilla
							break;
						case 'P'://se abre la puerta y podemos seguir
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');//cambiamos a visitada
							if(!trampa)
								explora();
							else
								exploraConTrampa();
							break;
						case 'S'://salto al siguiente destino no visitado mas cercano
							if(destinos>0){
								Coordenadas sigDestino = destinoCercano();//guardo las coordenadas del destino mas cercano
								actual = new exPath(sigDestino, actual.getCamino());//salto al destino
								pila.push(actual);//apilo
							}
							else{
								//si no quedan destinos por visitar se considera el comienzo como un destino
								actual = new exPath(comienzo.getCoordenadas(), actual.getCamino());//anyado las coordenadas del comienzo al camino
								caminos.insertaCola(actual.getCamino());//encolo el camino actual
								pila.push(actual);
								win = true;//y gano la partida
							}
							break;
						case 'T'://trampa, desactivamos, la ponemos como tipo P y volvemos al comienzo
							plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setPropiedad('P');
							actual = new exPath(comienzo.getCoordenadas(), actual.getCamino());//se insertan las coordenadas del comienzo al camino
							caminos.insertaCola(actual.getCamino());//se encola el camino actual para empezar otro desde el comienzo
							visitadasAlibres();//vuelvo a poner a libres todas las visitadas
							//se desapila la pila
							while(!pila.isEmpty()){
								pila.pop();
							}
							actual = new exPath(comienzo.getCoordenadas(), vacio);//creo un nuevo camino desde el comienzo
							pila.push(actual);//volvemos al comienzo
							trampa = !trampa; //invertimos el valor de trampa
							break;
						case 'X'://aqui se gana la partida
							
							caminos.insertaCola(actual.getCamino()); //encolamos el camino 
							win = true;//y ganamos
					}
					break;
				case 'c': //comienzo
					plano[actual.getCoordenadas().getFila()][actual.getCoordenadas().getColumna()].setTipo('v');//visitada
					if(!trampa)
						explora();
					else
						exploraConTrampa();
					
					break;
			}
		}//FIN WHILE
	}
	
	private static boolean comienzoSinDestinos() {
		//NEW metodo de comprobacion para cuando se visita la casilla de comienzo saber si quedan destinos
		//en el caso de no quedar se encola el camino actual y se gana la partida
		boolean si = false;
		 
		
		if(actual.getCoordenadas().compareTo(comienzo.getCoordenadas())==0){
			if(!buscandoComienzo){
				if(bosque.getDestinos().isEmpty()){
					caminos.insertaCola(actual.getCamino()); //encolamos el camino
					si = true;
				}
			}
			//si estamos buscando el comienzo nos da igual que queden destinos o no, una vez se alcanza el comienzo se gana la partida
			//a este punto se llega cuando se ha vaciado la pila y se ha salido del primer bucle
			else{
				caminos.insertaCola(actual.getCamino()); //encolamos el camino
				si = true;
			}
		}
		return si;
	}

	private static void explora(){
		//NEW exploramos el plano en el sentido norte, sur, este, oeste
		//creo 4 coordenadas con las 4 direcciones
		
		Coordenadas norte = new Coordenadas(actual.getCoordenadas().getFila()-1, actual.getCoordenadas().getColumna());
		Coordenadas sur = new Coordenadas(actual.getCoordenadas().getFila()+1, actual.getCoordenadas().getColumna());
		Coordenadas este = new Coordenadas(actual.getCoordenadas().getFila(), actual.getCoordenadas().getColumna()+1);
		Coordenadas oeste = new Coordenadas(actual.getCoordenadas().getFila(), actual.getCoordenadas().getColumna()-1);
		
		exPath nuevo = null;
		
		//el sentido de la exploracion aqui es: sur, norte, oeste, este
		
		//compruebo que direccion esta libre y sin visitar y la apilo
		if(bosque.estaEnElPlano(norte) && !esObstaculo(norte) && !estaVisitada(norte)){
			nuevo = new exPath(norte, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(sur) && !esObstaculo(sur) && !estaVisitada(sur)){
			nuevo = new exPath(sur, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(este) && !esObstaculo(este) && !estaVisitada(este)){
			nuevo = new exPath(este, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(oeste) && !esObstaculo(oeste) && !estaVisitada(oeste)){
			nuevo = new exPath(oeste, actual.getCamino());
			pila.push(nuevo);
		}
	}
	
	private static void exploraConTrampa(){
		//NEW mismo meotdo que explora pero con otro sentido de explorar lo usare si he caido en una trampa
		
		Coordenadas norte = new Coordenadas(actual.getCoordenadas().getFila()-1, actual.getCoordenadas().getColumna());
		Coordenadas sur = new Coordenadas(actual.getCoordenadas().getFila()+1, actual.getCoordenadas().getColumna());
		Coordenadas este = new Coordenadas(actual.getCoordenadas().getFila(), actual.getCoordenadas().getColumna()+1);
		Coordenadas oeste = new Coordenadas(actual.getCoordenadas().getFila(), actual.getCoordenadas().getColumna()-1);
		
		exPath nuevo = null;
		
		//el sentido de la exploracion aqui es: sur, norte, oeste, este
		
		if(bosque.estaEnElPlano(sur) && !esObstaculo(sur) && !estaVisitada(sur)){
			nuevo = new exPath(sur, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(norte) && !esObstaculo(norte) && !estaVisitada(norte)){
			nuevo = new exPath(norte, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(oeste) && !esObstaculo(oeste) && !estaVisitada(oeste)){
			nuevo = new exPath(oeste, actual.getCamino());
			pila.push(nuevo);
		}
		if(bosque.estaEnElPlano(este) && !esObstaculo(este) && !estaVisitada(este)){
			nuevo = new exPath(este, actual.getCamino());
			pila.push(nuevo);
		}
	}
	
	private static void visitadasAlibres(){
		//NEW metodo para pasar las casillas visitadas a libres
		Casilla[][] pl = bosque.getPl();
		
		for(int i=0; i<pl.length; i++){
			for(int j=0; j<pl[0].length; j++){
				if(pl[i][j].getTipo()=='v'){
					pl[i][j].setTipo('l');
				}
			}
		}
	}
	
	private static Coordenadas destinoCercano(){//revisar
		//NEW metodo en el que nos quedamos con las coordenadas del destino mas cercano
		int suma;
		int menorDistancia = Integer.MAX_VALUE;//inicializo el valor de la distancia al maximo
		
		ArrayList<Coordenadas> cercanas = new ArrayList<Coordenadas>();
		Coordenadas destinoCercano = null;
		
		for(int i=0; i<bosque.getDestinos().size(); i++){
			suma = 0;
			for(int j=0; j<bosque.getDestinos().size(); j++){
				suma = Math.abs(actual.getCoordenadas().getFila()-bosque.getDestinos().get(i).getCoordenadas().getFila()) 
						+ Math.abs(actual.getCoordenadas().getColumna()-bosque.getDestinos().get(i).getCoordenadas().getColumna());
			}
			//me quedo con el destino mas cercano
			if(suma<menorDistancia){
				cercanas.clear();
				cercanas.add(bosque.getDestinos().get(i).getCoordenadas());
				menorDistancia = suma;
			}
			else{
				//en el caso de tener mas de un destino a la misma distancia
				//me quedo con ellos
				if(suma == menorDistancia){
					cercanas.add(bosque.getDestinos().get(i).getCoordenadas());
				}
			}
		}
		//ordeno los destinos sobreescribiendo a traves de la clase coordenadas el metodo
		//compareTo de Collections para ordenar por filas si hay destinos a la misma distancia
		Collections.sort(cercanas); 
		//devuelvo el destino mas cercano
		if(!cercanas.isEmpty())
			destinoCercano = new Coordenadas(cercanas.get(0).getFila(),cercanas.get(0).getColumna());
		return destinoCercano;
	}
	
 	private static boolean esObstaculo(Coordenadas c){
		//NEW para que se mas rapido comprobar un obstaculo
		boolean si = false;
		if(bosque.getPl()[c.getFila()][c.getColumna()].getTipo() == 'o'){
			si = true;
		}
		return si;
	}
	
	private static boolean estaVisitada(Coordenadas c){
		//NEW para que sea mas rapido comprobar una casilla que esta visitada
		boolean si = false;
		if(bosque.getPl()[c.getFila()][c.getColumna()].getTipo() == 'v'){
			si = true;
		}
		return si;
	}
}
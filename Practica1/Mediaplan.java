// DNI 74007071 ALDARAVI COLL, CARLOS

import java.util.*;

/*
 * El coste en el mejor de los casos para el formato 'a' seria un coste constante pues si no hay destinos
 * simplemente imprimimos por pantalla "NO HAY COMIENZO"
 * 
 * El coste en el peor de los casos seria (i*j*k + k) + (i*j*k + k) <---- Esto seria solo para el coste de encontrar los destinos
 * y las casillas que no sean obstaculos, ahora el coste dentro del formato 'a' seria (i+j+x)(para la parte 1) + (i+j+x).
 * 
 * Total: (i*j*k + k) + (i*j*k + k) + (i+j+x) + (i+j+x) 
 * 
 * Lo que hace que el coste asintotico para el formato 'a' sea n^3 para el peor de los casos.
 * 
 */

public class Mediaplan {
	public static void main(String []args){
		if(args.length==4){
				int x = Integer.parseInt(args[1]);//guardamos las filas
				int y = Integer.parseInt(args[2]);//guardamos las columnas
				
				char formato = args[3].charAt(0);//guardamos el formato de salida
				
				Plano m = new Plano(x,y);//creamos el plano

				m.leePlano(args[0]);//creamos el mapa
				
				Casilla [][] pl = m.getPl();//me traigo la matriz privada de plano para trabajar mas comodo
								
				Coordenadas [] destino = null;//aqui guardo los destinos que existan en el mapa
				Coordenadas [] libres = null;//aqui guardo todas las casillas distintas de 'o' del mapa
				
				int numDestinos = 0;//para contabilizat los destinos
				int numLibres = 0;//para contabilizar las casillas distintas de 'o'
				
				//desde aqui-->
				//guardamos las coordenadas de los destinos que hay en el mapa
				//aqui para su uso en el formato 'a' y 'b'
				for(int i=0; i<x; i++){
					for(int j=0; j<y; j++){
						if(pl[i][j]!=null && pl[i][j].getTipo()=='d'){
							if(pl[i][j].getCoordenadas()!=null){
								if(destino==null){
									destino = new Coordenadas[1];
									destino[0] = pl[i][j].getCoordenadas();
								}
								else{
									Coordenadas [] tmpDes = new Coordenadas[destino.length];
									for(int k=0; k<tmpDes.length; k++){
										tmpDes[k] = new Coordenadas(destino[k].getFila(),destino[k].getColumna());
									}
									destino = new Coordenadas[tmpDes.length+1];
									for(int k=0; k<tmpDes.length; k++){
										destino[k] = new Coordenadas(tmpDes[k].getFila(),tmpDes[k].getColumna());
									}
									destino[destino.length-1] = pl[i][j].getCoordenadas();
								}
								numDestinos++;
							}
						}
					}
				}//hasta aqui<---
				//desde aqui-->
				//guardamos las coordenadas de todas las casillas que 
				//no sean obstaculos que hay en el mapa para hacer el apartado 2 del formato 'a' y 'b'
				for(int i=0; i<x; i++){
					for(int j=0; j<y; j++){
						if(pl[i][j]!=null && pl[i][j].getTipo()!='o'){
							if(pl[i][j].getCoordenadas()!=null){
								if(libres==null){
									libres = new Coordenadas[1];
									libres[0] = pl[i][j].getCoordenadas();
								}
								else{
									Coordenadas [] tmpDes = new Coordenadas[libres.length];
									for(int k=0; k<tmpDes.length; k++){
										tmpDes[k] = new Coordenadas(libres[k].getFila(),libres[k].getColumna());
									}
									libres = new Coordenadas[tmpDes.length+1];
									for(int k=0; k<tmpDes.length; k++){
										libres[k] = new Coordenadas(tmpDes[k].getFila(),tmpDes[k].getColumna());
									}
									libres[libres.length-1] = pl[i][j].getCoordenadas();
								}
								numLibres++;
							}
						}
					}
				}//hasta aqui<---
				int suma;
				int pos = 0;
				int menorDistancia = Integer.MAX_VALUE;//iniciamos la menor distancia al maximo valor de int
				int numMenorDis = 0;//variable para guardar el numero de coordenadas a la minima distancia
				int [] posMenores = new int[x*y];//esta variable la usare para guardar la distancia cuando haya mas de una menor
//FORMATO 'a'
				if(formato=='a'){
		//PARTE 1	
					if(numDestinos!=0){
						for(int i=0; i<numDestinos; i++){
							suma = 0;
							for(int j=0; j<numDestinos; j++){
								if(destino[i].getFila()!=destino[j].getFila() || destino[i].getColumna()!=destino[j].getColumna())
									suma += Math.abs(destino[i].getFila() - destino[j].getFila()) + Math.abs(destino[i].getColumna() - destino[j].getColumna());
							}
							if(suma<menorDistancia){
								menorDistancia = suma;
								pos = i;
								numMenorDis = 1;
								posMenores = new int[x*y];
								//cada vez que pase por aqui lo creo de nuevo 
								//para poner todo a 0 porque solo habra un menor
							}
							else{
								if(suma==menorDistancia){
									//si entramos aqui es porque hay minimo dos valores menores iguales
									//por lo que guardo en un array de int en la misma posicion 
									//donde lo he encontrado, su valor.
									posMenores[i] = suma;
									numMenorDis++;
								}
							}
						}
						if(numMenorDis==1){
							System.out.println("1:"+menorDistancia+"-("+destino[pos].getFila()+","+destino[pos].getColumna()+")");
						}
						else{
							if(numMenorDis>1){
								System.out.print("1:"+menorDistancia+"-("+destino[pos].getFila()+","+destino[pos].getColumna()+")");
								for(int i=0; i<posMenores.length; i++){
									if(posMenores[i]!=0){
										System.out.print("("+destino[i].getFila()+","+destino[i].getColumna()+")");
									}
								}
							System.out.println();
							}
						}
					}
					else{
						System.out.println("NO HAY COMIENZO");
					}
		//PARTE 2
					if(numDestinos!=0){
						pos = 0;
						menorDistancia = Integer.MAX_VALUE;//iniciamos la menor distancia al maximo valor de int
						numMenorDis = 0;//variable para guardar el numero de coordenadas a la minima distancia
						
						posMenores = new int[x*y];//vuelvo a poner todo a 0 para hacer una nueva cuenta
						
						for(int i=0; i<numLibres; i++){
							suma = 0;
							for(int j=0; j<numDestinos; j++){
								if(libres[i].getFila()!=destino[j].getFila() || libres[i].getColumna()!=destino[j].getColumna())
									suma += Math.abs(libres[i].getFila() - destino[j].getFila()) + Math.abs(libres[i].getColumna() - destino[j].getColumna());
							}
							if(suma<menorDistancia){
								menorDistancia = suma;
								pos = i;
								numMenorDis = 1;
								posMenores = new int[x*y];
								//cada vez que pase por aqui lo creo de nuevo 
								//para poner todo a 0 porque solo habra un menor
							}
							else{
								if(suma==menorDistancia){
									//si entramos aqui es porque hay minimo dos valores menores iguales
									//por lo que guardo en un array de int en la misma posicion 
									//donde lo he encontrado, su valor.
									posMenores[i] = suma;
									numMenorDis++;
								}
							}
						}
						if(numMenorDis==1){
							System.out.println("2:"+menorDistancia+"-("+libres[pos].getFila()+","+libres[pos].getColumna()+")");
						}
						else{
							if(numMenorDis>1){
								System.out.print("2:"+menorDistancia+"-("+libres[pos].getFila()+","+libres[pos].getColumna()+")");
								for(int i=0; i<posMenores.length; i++){
									if(posMenores[i]!=0){
										System.out.print("("+libres[i].getFila()+","+libres[i].getColumna()+")");
									}
								}
								System.out.println();
							}
						}
					}
					else{
						System.out.println("NO HAY COMIENZO");
					}
				}
				
//FORMATO 'b'
				else{
					if(formato=='b'){
		//PARTE 1
						if(numDestinos!=0){
							ArrayList<CoordenadasConDistancia> c = new ArrayList<CoordenadasConDistancia>();
	
							for(int i=0; i<numDestinos; i++){
								suma = 0;
								for(int j=0; j<numDestinos; j++){
									if(destino[i].getFila()!=destino[j].getFila() || destino[i].getColumna()!=destino[j].getColumna())
										suma += Math.abs(destino[i].getFila() - destino[j].getFila()) + Math.abs(destino[i].getColumna() - destino[j].getColumna());
								}
								c.add(new CoordenadasConDistancia(destino[i], suma));
							}
							Collections.sort(c, new Comparator<CoordenadasConDistancia>(){
								@Override
								//sobreescribo el metodo para poder ordenar el arraylist por las distancias
								public int compare(CoordenadasConDistancia c1, CoordenadasConDistancia c2){
									return new Integer(c1.getDistancia()).compareTo(c2.getDistancia());
								}
							});
	
							if(!c.isEmpty()){
								System.out.print("1:"+c.get(0));
							}
							else{
								System.out.println("NO HAY COMIENZO");
							}
							
							if(c.size()>1){
								for(int i=1; i<c.size(); i++){
									if(c.get(i).getDistancia()==c.get(i-1).getDistancia()){
										System.out.print("("+c.get(i).getCoordenadas().getFila()+","+c.get(i).getCoordenadas().getColumna()+")");
									}
									else{
										System.out.println();
										System.out.print("1:"+c.get(i));
									}
								}
							}
						}
						else{
							System.out.println("NO HAY COMIENZO");
						}
		//PARTE 2		
						if(numDestinos!=0){
							ArrayList<CoordenadasConDistancia> l = new ArrayList<CoordenadasConDistancia>();
	
							for(int i=0; i<numLibres; i++){
								suma = 0;
								for(int j=0; j<numDestinos; j++){
									if(libres[i].getFila()!=destino[j].getFila() || libres[i].getColumna()!=destino[j].getColumna())
										suma += Math.abs(libres[i].getFila() - destino[j].getFila()) + Math.abs(libres[i].getColumna() - destino[j].getColumna());
								}
								l.add(new CoordenadasConDistancia(libres[i], suma));
							}
							Collections.sort(l, new Comparator<CoordenadasConDistancia>(){
								@Override
								//sobreescribo el metodo para poder ordenar el arraylist por las distancias
								public int compare(CoordenadasConDistancia l1, CoordenadasConDistancia l2){
									return new Integer(l1.getDistancia()).compareTo(l2.getDistancia());
								}
							});
							if(!l.isEmpty()){
								System.out.println();
								System.out.print("2:"+l.get(0));
							}
							if(l.size()>1){
								for(int i=1; i<l.size(); i++){
									if(l.get(i).getDistancia()==l.get(i-1).getDistancia()){
										System.out.print("("+l.get(i).getCoordenadas().getFila()+","+l.get(i).getCoordenadas().getColumna()+")");
									}
									else{
										System.out.println();
										System.out.print("2:"+l.get(i));		
									}
								}
								System.out.println();
							}
						}
						else{
							System.out.println("NO HAY COMIENZO");
						}
					}
				}
			}
	}
	
	private static class CoordenadasConDistancia{
		//NEW creo esta clase para hacer el formato 'b'
		//usare esta clase para crear un arraylist
		//guardando objetos que tienen coordenadas
		//asociadas a su distancia a los destinos
		private Coordenadas c;
		private int dis;
		
		public CoordenadasConDistancia(Coordenadas c, int distancia){
			this.c = new Coordenadas(c.getFila(),c.getColumna());
			dis = distancia;
		}
		public Coordenadas getCoordenadas(){
			return c;
		}
		public int getDistancia(){
			return dis;
		}
		public String toString(){
			return this.getDistancia() + "-(" + this.getCoordenadas().getFila()+","+this.getCoordenadas().getColumna() + ")";
		}
	}
}
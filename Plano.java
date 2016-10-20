// DNI 74007071 ALDARAVI COLL, CARLOS

import java.io.*;
import java.util.*;

public class Plano {
	private Casilla[][] pl;
	private Coordenadas dimension;//dimension de la matriz
	private Casilla comienzo;//NEW para guardar las coordenadas del comienzo
	private ArrayList<Casilla> destinos;//NEW variable dinamica para guardar los destinos del mapa
	private ArrayList<Casilla> libres;//NEW variable dinamica para guardar las casillas libres
	private ArrayList<Casilla> puertas;//NEW variable dinamica para guardar las puertas del mapa
	private ArrayList<Casilla> obstaculos;//NEW variable dinamica para guardar las casillas libres
	
	public Plano(int i, int j){//hecho
		//si alguno negativo inicializar en 3
		
		if(i<0)
			i = 3;
		if(j<0)
			j = 3;
		dimension = new Coordenadas(i,j);
		pl = new Casilla[i][j];
		
		destinos = new ArrayList<Casilla>();
		libres = new ArrayList<Casilla>();
		puertas = new ArrayList<Casilla>();
		obstaculos = new ArrayList<Casilla>();
	}
	
	public boolean setCasilla(Casilla x){//hecho
		//anyade la casilla al plano si la posicion esta libre
		
		boolean done = false;
		
		//comprobamos que la casilla x es valida y que sus coordenadas
		//estan dentro del plano y si las coordenadas en el plano
		//no tienen una casilla ya, se la asignamos
		if(x!=null && pl!=null && x.getCoordenadas()!=null){
			if(estaEnElPlano(x.getCoordenadas())){
				if(pl[x.getCoordenadas().getFila()][x.getCoordenadas().getColumna()]==null){
					pl[x.getCoordenadas().getFila()][x.getCoordenadas().getColumna()] = x;
					done = true;
					switch(x.getTipo()){
						case 'l': libres.add(x); break;
						case 'o': obstaculos.add(x); break;
						case 'p': puertas.add(x); break;
						case 'd': destinos.add(x); break;
						case 'c': comienzo = x; break;
					}
				}
			}
		}
		return done;
	}
	
	public void leePlano(String f/*nombre de fichero*/){//hecho
		//abrimos, leemos y cerramos fichero
		
		String linea;//para guardar cada linea
		String[] tmp = null;//para guarda cada caracter de la linea separado

		int filas = 0;//variable para llevar la cuenta de las filas del fichero
		
		FileReader fr=null;
		BufferedReader bf=null;
		
		try{//intentamos leer el fichero
			fr=new FileReader(f); //abro el fichero para lectura
			bf=new BufferedReader(fr); //para leer por lineas
			
			linea=bf.readLine(); //primera linea del fichero
			
			if(linea!=null && linea.equals("<MAPA>")){
			//procesamiento y lectura de todo el fichero
				
				linea = bf.readLine();
				if(linea!=null){
					tmp = linea.split("");
					linea = creaMapa(linea, tmp, filas, bf);
				}
				//una vez creado el mapa leemos el resto de fichero para crear el resto
				while(linea!=null){
					if(linea.equals("<DESTINO>")){
						linea = creaDestino(linea, tmp, filas, bf);
					}
					else{
						if(linea.equals("<COMIENZO>")){
							linea = creaComienzo(linea, tmp, filas, bf);
						}
						else{
							if(linea.equals("<PUERTA>")){
								linea = creaPuerta(linea, tmp, filas, bf);
							}
							else{
								if(linea.equals(""))
									break;
							}
						}
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		try{
			if(fr!=null)
				fr.close();
			if(bf!=null)
				bf.close();
		}catch(IOException ex){
			System.out.println(ex);
		}
	}
	
	private String creaMapa(String linea, String[] tmp, int filas, BufferedReader bf){
		//NEW desde aqui creamos el mapa(sin puertas,comienzos ni destinos)
		try{
			if(tmp.length!=dimension.getColumna()){
				//compruebo la longitud de la primera linea
				//por si es necesario redimensionar las columnas
				
				redimensionaColumnas(tmp.length);
			}
			
			while(linea!=null){
				if((linea.equals("<DESTINO>") || linea.equals("<COMIENZO>") || linea.equals("<PUERTA>")) && filas<dimension.getFila()){
					//si el valor de la linea es destino o comienzo
					//ya hemos terminado de crear el mapa
					break;
				}
				else{
					if(linea!=null)
						tmp = linea.split("");
					
					if(filas<dimension.getFila()){
						//voy creando casillas en el plano
						//y llevando la cuenta de las lineas de mapa que hay
						bucle1:
						for(int i=0; i<dimension.getFila(); i++){
							for(int j=0; j<dimension.getColumna(); j++){
								if(linea==null || linea.equals("<DESTINO>") || linea.equals("<COMIENZO>") || linea.equals("<PUERTA>"))
									break bucle1;
								if(pl[i][j]==null){
									if(tmp.length<dimension.getColumna() && j>=tmp.length && j<dimension.getColumna()){
										pl[i][j] = new Casilla('l');
										pl[i][j].setCoordenadas(i, j, this);
										libres.add(pl[i][j]);
									}
									else{
										pl[i][j] = new Casilla(tmp[j].charAt(0));
										pl[i][j].setCoordenadas(i, j, this);
										switch(pl[i][j].getTipo()){
											case 'l': libres.add(pl[i][j]); break;
											case 'o': obstaculos.add(pl[i][j]); break;
										}
									}
								}
							}
							linea=bf.readLine();
							if(linea!=null)
								tmp = linea.split("");
							filas++;
						}
					}
					else
						break;
				}
			}
			
			while(linea!=null){
				if((linea.equals("<DESTINO>") || linea.equals("<COMIENZO>") || linea.equalsIgnoreCase("") || linea.equals("<PUERTA>")) && filas==dimension.getFila())
					break;
				if(filas>=dimension.getFila()){
					redimensionaFilas();
					//Una vez redimensionado
					//Introduzco el valor de la ultima fila
					
					for(int j=0; j<pl[0].length; j++){
						if(tmp.length<dimension.getColumna() && j>=tmp.length && j<dimension.getColumna()){
							pl[dimension.getFila()-1][j] = new Casilla('l');
							pl[dimension.getFila()-1][j].setCoordenadas(dimension.getFila()-1, j, this);
							libres.add(pl[dimension.getFila()-1][j]);
						}
						else{
							pl[dimension.getFila()-1][j] = new Casilla(tmp[j].charAt(0));
							pl[dimension.getFila()-1][j].setCoordenadas(dimension.getFila()-1, j, this);
							switch(pl[dimension.getFila()-1][j].getTipo()){
								case 'l': libres.add(pl[dimension.getFila()-1][j]); break;
								case 'o': obstaculos.add(pl[dimension.getFila()-1][j]); break;
							}
						}
					}
					
					linea=bf.readLine();
					if(linea!=null)
						tmp = linea.split("");
					filas++;
				}
				else{
					for(int j=0; j<dimension.getColumna(); j++){
						if(pl[filas][j]==null){
							pl[filas][j] = new Casilla('l');
							pl[filas][j].setCoordenadas(filas, j, this);
							libres.add(pl[filas][j]);
						}
					}
					filas++;
				}
			}
			if(filas<this.getDimension().getFila()){
				while(filas<this.getDimension().getFila()){
					for(int i=filas; i<getDimension().getFila(); i++){
						for(int j=0; j<getDimension().getColumna(); j++){
							pl[i][j] = new Casilla('l');
							pl[i][j].setCoordenadas(i, j, this);
							libres.add(pl[i][j]);
						}
						filas++;
					}
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		return linea;
	}

	private String creaDestino(String linea, String[] tmp, int filas, BufferedReader bf){
		//NEW desde aqui creamos unicamente los destinos
		try{
			int x = -1,y = -1;//variables para guardar las coordenadas
			
			String nombre;
			
			boolean coordCorrectas = false;
			
			nombre = linea=bf.readLine();//guardo el nombre del destino
			
			if(nombre!=null && linea!=null){
				//compruebo que hay 2 lineas entre etiquetas
	
				linea=bf.readLine();
				if(linea!=null)
					tmp = linea.split(" ");
				
				if(tmp.length==2){
					//compruebo que en la segunda linea hay 2 numeros
					if(isNumeric(tmp[0]) && isNumeric(tmp[1])){
						x = Integer.parseInt(tmp[0]);
						y = Integer.parseInt(tmp[1]);
						coordCorrectas = true;
					}
				}
				if(coordCorrectas){
					//compruebo si las coordenadas estan en el plano
					if(x>=0 && x<dimension.getFila()){
						if(y>=0 && y<dimension.getColumna()){
							if(pl[x][y]!=null && pl[x][y].getTipo()=='l'){
								//en este fin despues de comprobar que la casilla esta libre
								//le cambio el tipo a destino y le pongo el nombre
								pl[x][y].setTipo('d');
								pl[x][y].setNombre(nombre);
								destinos.add(pl[x][y]);
								libres.remove((Casilla)pl[x][y]);
							}
						}
					}	
				}
				while(linea!=null && !linea.equals("<DESTINO>") && !linea.equals("<COMIENZO>") && !linea.equals("<PUERTA>")){
					linea=bf.readLine();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		
		return linea;
	}
	
	private String creaPuerta(String linea, String[] tmp, int filas, BufferedReader bf){
		//NEW desde aqui creamos las puertas
		try{
			int x = -1,y = -1;//variables para guardar las coordenadas
			int numPuerta = -1;//para guardar el numero de puerta
			int etiquetas = -1; //para contar el numero de lineas entre etiquetas
			
			char propPuerta = 'F';//para guardar la propiedad de la puerta
			
			boolean numeroOK = false;//true si hay 3 elementos entre etiquetas
			boolean coordenadasOK = false;//true si las coordenadas son validas
			boolean propiedadOK = false;//true si la propiedad es valida
			
			linea=bf.readLine();
			
			//comprobando que hay 3 lineas entre etiquetas
			if(linea!=null){
				if(isNumeric(linea)){
					numPuerta = Integer.parseInt(linea);
					if(numPuerta >= 0){
						numeroOK = true;
						etiquetas++;
					}
				}
				linea=bf.readLine();
				if(linea!=null)
					tmp = linea.split(" ");
				if(tmp.length==2){
					//compruebo que en la segunda linea hay 2 numeros
					if(isNumeric(tmp[0]) && isNumeric(tmp[1])){
						x = Integer.parseInt(tmp[0]);
						y = Integer.parseInt(tmp[1]);
						coordenadasOK = true;
						etiquetas++;
					}
				}
				linea=bf.readLine();
				if(linea!=null && linea.length()==1){
					propPuerta = linea.charAt(0);
					if(propPuerta!='F'){
						propiedadOK = true;
						etiquetas++;
					}
					
				}
			}
				//avanzamos hasta final de archivo o siguiente etiqueta
			while(linea!=null && !linea.equals("<DESTINO>") && !linea.equals("<COMIENZO>") && !linea.equals("<PUERTA>")){
				linea=bf.readLine();
				etiquetas++;
			}
			if(coordenadasOK && etiquetas==3){
				//compruebo si las coordenadas estan en el plano
				if(x>=0 && x<dimension.getFila()){
					if(y>=0 && y<dimension.getColumna()){
						if(pl[x][y]!=null && pl[x][y].getTipo()=='l'){
							//en este fin despues de comprobar que la casilla esta libre
							//le cambio el tipo a destino y le pongo el nombre
							if(numeroOK && propiedadOK){
								pl[x][y].setPuerta(numPuerta, propPuerta);
								pl[x][y].setTipo('p');
								puertas.add(pl[x][y]);
								libres.remove((Casilla)pl[x][y]);
							}
						}
					}
				}	
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		return linea;
	}
	
	private String creaComienzo(String linea, String[] tmp, int filas, BufferedReader bf) {
		//NEW desde aqui creamos unicamente el comienzo
		try{
			int x = -1,y = -1;//variables para guardar las coordenadas
					
			String nombre;
			
			boolean coordCorrectas = false;
					
			nombre = linea=bf.readLine();//guardo el nombre del destino
					
			if(nombre!=null && linea!=null){
				//compruebo que hay 2 lineas entre etiquetas
						
				linea=bf.readLine();
				if(linea!=null)
					tmp = linea.split(" ");
						
				if(tmp.length==2){
					//compruebo que en la segunda linea hay 2 numeros
					if(isNumeric(tmp[0]) && isNumeric(tmp[1])){
						x = Integer.parseInt(tmp[0]);
						y = Integer.parseInt(tmp[1]);
						coordCorrectas = true;
					}
				}
				if(coordCorrectas && comienzo == null){
					//compruebo si las coordenadas estan en el plano
					if(x>=0 && x<dimension.getFila()){
						if(y>=0 && y<dimension.getColumna()){
							if(pl[x][y]!=null && pl[x][y].getTipo()=='l'){
								//en este fin despues de comprobar que la casilla esta libre
								//le cambio el tipo a destino y le pongo el nombre
								pl[x][y].setTipo('c');
								pl[x][y].setNombre(nombre);
								comienzo = pl[x][y];
								libres.remove((Casilla)pl[x][y]);
							}
						}
					}	
				}
				//avanzamos hasta final de archivo o siguiente etiqueta
				while(linea!=null && !linea.equals("<DESTINO>") && !linea.equals("<COMIENZO>") && !linea.equals("<PUERTA>")){
					linea=bf.readLine();
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
		return linea;
	}

	private void redimensionaColumnas(int j) {
		//NEW desde aqui redimensionaremos las columnas
		//tengo que guardar antes de redimensionar si habia 
		//alguna casilla ya en el plano
		
		Casilla[][] tmp = null;
		
		if(dimension.getColumna()>0){
			tmp = new Casilla[dimension.getFila()][dimension.getColumna()];
			for(int i=0; i<dimension.getFila(); i++){
				for(int y=0; y<dimension.getColumna(); y++){
					if(pl[i][y]!=null){
						tmp[i][y] = pl[i][y];
					}
				}
			}
		}
		
		dimension.setColumna(j);
		pl = new Casilla[dimension.getFila()][dimension.getColumna()];
		
		if(tmp!=null){
			for(int i=0; i<tmp.length; i++){
				for(int y=0; y<tmp[0].length; y++){
					if(tmp[i][y]!=null){
						pl[i][y] = tmp[i][y];
					}
				}
			}
		}
	}
	
	private static boolean isNumeric(String cadena){
		//NEW comprobacion de que las cordenadas pasadas por el archivo sean numeros
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}

	private void redimensionaFilas(){
		//NEW llegados a este punto, hay que redimensionar las filas del plano
		
		Casilla[][] plTmp = null;
		
		if(pl.length>0 && pl[0].length > 0){
			plTmp = new Casilla[pl.length][pl[0].length];
		}
		
		//creo un plano temporal para volcar el contenido actual de this
		if(plTmp!=null){
			for(int i=0; i<plTmp.length; i++){
				for(int j=0; j<plTmp[0].length; j++){
					if(pl[i][j] != null){
						plTmp[i][j] = pl[i][j];
					}
				}
			}
		}
		//redimensiono this
		pl = new Casilla[dimension.getFila()+1][dimension.getColumna()];
		dimension.setFila(pl.length);
		//vuelvo a introducir en this su valor anterior
		//pero ahora tiene una fila mas
		if(plTmp!=null){
			for(int i=0; i<plTmp.length; i++){
				for(int j=0; j<plTmp[0].length; j++){
					if(plTmp[i][j]!=null){
						pl[i][j] = plTmp[i][j];
					}
				}
			}
		}
	}
	
	public char consultaCasilla(Coordenadas x){//hecho
		//devuelve 'F' si incorrecto
		
		char tipo = 'F';
		if(x!=null){
			if(estaEnElPlano(x) && pl[x.getFila()][x.getColumna()]!=null){
				tipo = pl[x.getFila()][x.getColumna()].getTipo();
			}
		}
		return tipo;
	}
	
	public char[] consultaVecinas(Coordenadas x){//hecho
		//null si coordenadas invalidas o vacias
		//rellenar con el tipo de las vecinas, 
		//empezando arriba y en sentido del reloj
		
		char[] tipoVecinas = null;
		char[] tmp = null;
		
		int pos = 0;
		
		if(x!=null){
			int fila = x.getFila();
			int columna = x.getColumna();
			
			//compruebo que las coordenadas estan dentro del plano
			//y que existe la casilla en dichas coordenadas
			if(estaEnElPlano(x) && pl[fila][columna]!=null){

//compruebo las 8 vecinas 
   //una a una:
	/*arriba*/	
				if(estaEnElPlano(fila-1, columna)){
					if(pl[fila-1][columna]==null){
						tipoVecinas = new char[1];
						tipoVecinas[0] = 'F';
						pos++;
					}
					else{
						tipoVecinas = new char[1];
						tipoVecinas[0] = pl[fila-1][columna].getTipo();
						pos++;
					}
				}
/*arriba-derecha*/	
				if(estaEnElPlano(fila-1, columna+1)){
					if(pl[fila-1][columna+1]==null){
						tmp = new char[1];
						
						tmp[0] = tipoVecinas[0];
						
						tipoVecinas = new char[2];
						
						tipoVecinas[0] = tmp[0];

						tipoVecinas[pos] = 'F';
						pos++;
					}
					else{
						tmp = new char[1];
						
						tmp[0] = tipoVecinas[0];
						
						tipoVecinas = new char[2];
						
						tipoVecinas[0] = tmp[0];

						tipoVecinas[pos] = pl[fila-1][columna+1].getTipo();
						pos++;
					}
				}
	/*derecha*/	
				if(estaEnElPlano(fila, columna+1)){
					/*int l=0;
					if(tipoVecinas!=null)
						l=tipoVecinas.length+1;
					else
						l=1;
					char[] temp=new char[l];
					int k=0;
					for(;k<temp.length-1;k++){
						temp[k]=tipoVecinas[k];
					}
					tipoVecinas=temp;
					pos=k;*/
					if(pos==0){
						if(pl[fila][columna+1]==null){
							tipoVecinas = new char[1];
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tipoVecinas = new char[1];
							tipoVecinas[pos] = pl[fila][columna+1].getTipo();
							pos++;
						}
					}
					else{
						if(pos==2){
							if(pl[fila][columna+1]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila][columna+1].getTipo();
								pos++;
							}
						}
					}
				}
/*abajo-derecha*/	
				if(estaEnElPlano(fila+1, columna+1)){
					if(pos==1){
						if(pl[fila+1][columna+1]==null){
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = pl[fila+1][columna+1].getTipo();
							pos++;
						}
					}
					else{
						if(pos==3){
							if(pl[fila+1][columna+1]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[4];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[4];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila+1][columna+1].getTipo();
								pos++;
							}
						}
					}
				}
	/*abajo*/	
				if(estaEnElPlano(fila+1, columna)){
					if(pos==0){
						if(pl[fila+1][columna]==null){
							tipoVecinas = new char[1];
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tipoVecinas = new char[1];
							tipoVecinas[pos] = pl[fila+1][columna].getTipo();
							pos++;
						}
					}
					else{
						if(pos==1){
							if(pl[fila+1][columna]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[2];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[2];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila+1][columna].getTipo();
								pos++;
							}
						}
						else{
							if(pos==2){
								if(pl[fila+1][columna]==null){
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[3];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = 'F';
									pos++;
								}
								else{
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[3];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = pl[fila+1][columna].getTipo();
									pos++;
								}
							}
							else{
								if(pos==4){
									if(pl[fila+1][columna]==null){
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[5];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = 'F';
										pos++;
									}
									else{
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[5];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = pl[fila+1][columna].getTipo();
										pos++;
									}
								}
							}
						}
					}
				}
/*abajo-izquierda*/	
				if(estaEnElPlano(fila+1, columna-1)){
					if(pos==1){
						if(pl[fila+1][columna-1]==null){
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = pl[fila+1][columna-1].getTipo();
							pos++;
						}
					}
					else{
						if(pos==2){
							if(pl[fila+1][columna-1]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila+1][columna-1].getTipo();
								pos++;
							}
						}
						else{
							if(pos==3){
								if(pl[fila+1][columna-1]==null){
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[4];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = 'F';
									pos++;
								}
								else{
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[4];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = pl[fila+1][columna-1].getTipo();
									pos++;
								}
							}
							else{
								if(pos==5){
									if(pl[fila+1][columna-1]==null){
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[6];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = 'F';
										pos++;
									}
									else{
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[6];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = pl[fila+1][columna-1].getTipo();
										pos++;
									}
								}
							}
						}
					}
				}
	/*izquierda*/	
				if(estaEnElPlano(fila, columna-1)){
					if(pos==1){
						if(pl[fila][columna-1]==null){
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[2];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = pl[fila][columna-1].getTipo();
							pos++;
						}
					}
					else{
						if(pos==2){
							if(pl[fila][columna-1]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[3];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila][columna-1].getTipo();
								pos++;
							}
						}
						else{
							if(pos==3){
								if(pl[fila][columna-1]==null){
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[4];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = 'F';
									pos++;
								}
								else{
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[4];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = pl[fila][columna-1].getTipo();
									pos++;
								}
							}
							else{
								if(pos==4){
									if(pl[fila][columna-1]==null){
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[5];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = 'F';
										pos++;
									}
									else{
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[5];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = pl[fila][columna-1].getTipo();
										pos++;
									}
								}
								else{
									if(pos==6){
										if(pl[fila][columna-1]==null){
											tmp = new char[pos];
											for(int i=0; i<pos; i++)
												tmp[i] = tipoVecinas[i];
											
											tipoVecinas = new char[7];
											for(int i=0; i<pos; i++)
												tipoVecinas[i] = tmp[i];
											
											tipoVecinas[pos] = 'F';
											pos++;
										}
										else{
											tmp = new char[pos];
											for(int i=0; i<pos; i++)
												tmp[i] = tipoVecinas[i];
											
											tipoVecinas = new char[7];
											for(int i=0; i<pos; i++)
												tipoVecinas[i] = tmp[i];
											
											tipoVecinas[pos] = pl[fila][columna-1].getTipo();
											pos++;
										}
									}
								}
							}
						}
					}
				}
/*arriba-izquierda*/	
				if(estaEnElPlano(fila-1, columna-1)){
					if(pos==2){
						if(pl[fila-1][columna-1]==null){
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[3];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = 'F';
							pos++;
						}
						else{
							tmp = new char[pos];
							for(int i=0; i<pos; i++)
								tmp[i] = tipoVecinas[i];
							
							tipoVecinas = new char[3];
							for(int i=0; i<pos; i++)
								tipoVecinas[i] = tmp[i];
							
							tipoVecinas[pos] = pl[fila-1][columna-1].getTipo();
							pos++;
						}
					}
					else{
						if(pos==3){
							if(pl[fila-1][columna-1]==null){
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[4];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = 'F';
								pos++;
							}
							else{
								tmp = new char[pos];
								for(int i=0; i<pos; i++)
									tmp[i] = tipoVecinas[i];
								
								tipoVecinas = new char[4];
								for(int i=0; i<pos; i++)
									tipoVecinas[i] = tmp[i];
								
								tipoVecinas[pos] = pl[fila-1][columna-1].getTipo();
								pos++;
							}
						}
						else{
							if(pos==4){
								if(pl[fila-1][columna-1]==null){
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[5];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = 'F';
									pos++;
								}
								else{
									tmp = new char[pos];
									for(int i=0; i<pos; i++)
										tmp[i] = tipoVecinas[i];
									
									tipoVecinas = new char[5];
									for(int i=0; i<pos; i++)
										tipoVecinas[i] = tmp[i];
									
									tipoVecinas[pos] = pl[fila-1][columna-1].getTipo();
									pos++;
								}
							}
							else{
								if(pos==7){
									if(pl[fila-1][columna-1]==null){
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[8];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = 'F';
										pos++;
									}
									else{
										tmp = new char[pos];
										for(int i=0; i<pos; i++)
											tmp[i] = tipoVecinas[i];
										
										tipoVecinas = new char[8];
										for(int i=0; i<pos; i++)
											tipoVecinas[i] = tmp[i];
										
										tipoVecinas[pos] = pl[fila-1][columna-1].getTipo();
										pos++;
									}
								}
							}
						}
					}
				}
			}
		}
		return tipoVecinas;
	}
	
	public void muestraPlano(){//hecho
		int filas = dimension.getFila();
		int columnas = dimension.getColumna();
		
		for(int i=0; i<filas; i++){
			for(int j=0; j<columnas; j++){
				if(pl[i][j]!=null){
					switch(pl[i][j].getTipo()){
						case 'l': System.out.print("l");
								break;
						case 'o': System.out.print("o");
								break;
						case 'd': System.out.print("d");
								break;
						case 'p': System.out.print("p"); //POR QUE NO ENTRA?
								break;
						case 'c': System.out.print("c");
								break;
					}
				}
				else{
					System.out.print("F");
				}
			}
			System.out.println();
		}
	}
	
	public boolean equals(Plano compara){//hecho
		//metodo para comparar si un plano es igual a otro
		boolean si = false;
		
		if(compara!=null && this!=null){
			//despues de ver si existe el plano, compruebo que tiene dimension
			if(compara.dimension!=null && this.dimension!=null){
				//comparo que ambos planos tienen la misma dimension
				if(compara.dimension.getFila() == this.dimension.getFila() 
						&& compara.dimension.getColumna() == this.dimension.getColumna())
					si = true;
			}
		}
		if(si){
			//si ambos planos tienen la misma dimension, comparo las casillas una a una
			for(int i=0; i<compara.dimension.getFila(); i++){
				for(int j=0; j<compara.dimension.getColumna(); j++){
					if(compara.pl[i][j]!=null && this.pl[i][j]!=null){
						if(!compara.pl[i][j].equals(this.pl[i][j]))	
							si=false;
					}
					else{
						if(compara.pl[i][j]==null && this.pl[i][j]!=null){
							si=false;
						}
						else{
							if(compara.pl[i][j]!=null && this.pl[i][j]==null){
								si = false;
							}
						}
					}
				}
			}
		}
		return si;
	}
	
	public Coordenadas getDimension(){
		//NEW. metodo para obtener la dimension del plano
		return dimension;
	}
	
	public boolean estaEnElPlano(int i, int j){
		//NEW. comprobacion de que las coordenadas de una casilla se correspondan 
		//con una posicion valida del tablero
		boolean si = false;
		
		if(i >= 0 && i < dimension.getFila()){
			if(j >= 0 && j < dimension.getColumna()){
				si = true;
			}
		}
		
		return si;
	}
	
	public boolean estaEnElPlano(Coordenadas x){
		//NEW. SOBRECARGA del metodo estaEnElPlano para poder comprobar
		//lo mismo pero pasando un objeto coordenadas y no dos enteros
		boolean si = false;
		
		if(x!=null){
			if(x.getFila() >= 0 && x.getFila() < dimension.getFila()){
				if(x.getColumna() >= 0 && x.getColumna() < dimension.getColumna()){
					si = true;
				}
			}
		}
		return si;
	}
	
	public Casilla[][] getPl(){
		//NEW para poder usar el plano en mediaplan
		return pl;
	}

	public ArrayList<Casilla> getLibres(){
		//NEW para devolver las casillas libres
		return libres;
	}
	
	public ArrayList<Casilla> getDestinos(){
		//NEW para devolver los destinos
		return destinos;
	}
	
	public ArrayList<Casilla> getPuertas(){
		//NEW para devolver las puertas;
		return puertas;
	}
	
	public ArrayList<Casilla> getObstaculos(){
		//NEW para devolver las puertas;
		return obstaculos;
	}
	
	public Casilla getComienzo(){
		//NEW para devolver la casilla de comienzo
		return comienzo;
	}
}
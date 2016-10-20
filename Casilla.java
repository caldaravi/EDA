// DNI 74007071 ALDARAVI COLL, CARLOS

public class Casilla {
	private Coordenadas propias;//NEW para saber las coordenadas de la casilla
	private String nombre;//NEW para que saber el nombre en caso de ser destino o comienzo
	private char tipo; //NEW para guardar el tipo de casilla
	private int numPuerta;//NEW en caso de ser una puerta para guardar el numero
	private char propPuerta;//NEW en caso de ser una puerta para guardar su propiedad
	
	public Casilla(char a){//hecho
		//el caracter indica si es libre('l'),obstaculo('o'), 
		//puerta ('p'), destino ('d') o comienzo ('c').
		
		tipo = a;
	}
	
	public boolean setCoordenadas(int i, int j, Plano m){//hecho
		//crea objeto tipo Coordenadas
		
		boolean creada = false;
		
		if(m!=null && propias==null){
			//compruebo que existe plano y que aun no hay casilla
			if(i>=0 && i<m.getDimension().getFila()){
				if(j>=0 && j<m.getDimension().getColumna()){
					//asigno las coordenadas a esta casilla
					propias = new Coordenadas(i,j);
					creada = true;
				}
			}
		}
		return creada;
	}
	
	public void setNombre(String n){//hecho
		// nombre de comienzo o destino
		if(n!=null && (tipo=='d' || tipo=='c')){
			nombre = new String(n);
		}
	}
	
	public void setPuerta(int y, char p){//hecho
		numPuerta = y;
		propPuerta = p;
	}
	
	public boolean esComienzo(){//hecho
		boolean si = false;
		
		if(tipo=='c')
			si = true;
		
		return si;
	}
	
	public boolean esDestino(){//hecho
		boolean si = false;
		
		if(tipo=='d')
			si = true;
		
		return si;
	}
	
	public boolean esObstaculo(){//hecho
		boolean si = false;
		
		if(tipo=='o')
			si = true;
		
		return si;
	}
	
	public boolean esLibre(){//hecho
		boolean si = false;
		
		if(tipo=='l')
			si = true;
		
		return si;
	}
	
	public boolean esPuerta(){// hecho
		boolean si = false;
		
		if(tipo=='p')
			si = true;
		
		return si;
	}
	
	public String getNombre(){//hecho
		//null si no es un comienzo o destino
		
		String s = null;
		
		if(tipo=='d' || tipo=='c')
			s = new String(nombre);
		
		return s;
	}
	
	public int getNumero(){//hecho
		//si no es puerta devuelve -1
		
		int puerta = -1;
		
		if(esPuerta())
			puerta = numPuerta;
		
		return puerta;
	}
	
	public char getPropiedad(){//hecho
		//propiedad de puerta o 'F' si no es puerta
		
		char prop = 'F';
		
		if(esPuerta())
			prop = propPuerta;
		
		return prop;
	}
	
	public void escribeInfo(){//hecho
		//muestra por pantalla informacion de casilla
		
		if(propias!=null){
			if(tipo=='d' || tipo=='c'){
				System.out.println(nombre + ":(" + propias.getFila()
				+ "," + propias.getColumna() + ")");
			}
			else{
				if(tipo=='o'){
					System.out.println("obstaculo:(" + propias.getFila()
					+ "," + propias.getColumna() + ")");
				}
				else{
					if(tipo=='l'){
						System.out.println("libre:(" + propias.getFila()
						+ "," + propias.getColumna() + ")");
					}
					else{
						System.out.println(numPuerta + ":(" + propias.getFila()
						+ "," + propias.getColumna() + "):" + propPuerta);
					}
				}
			}
		}
	}
	
	public Coordenadas getCoordenadas(){//hecho
		return propias;
	}
	
	public boolean equals(Casilla c){//hecho
		//true si ambas casillas son iguales
		
		boolean si = false;
		
		if(c!=null){
			//despues de ver si existe la casilla, compruebo que tiene coordenadas
			if(propias!=null && c.getCoordenadas()!=null){
				if(propias.getColumna() == c.getCoordenadas().getColumna()
						&& propias.getFila() == c.getCoordenadas().getFila()){
					if(this.getTipo() == c.getTipo()){
						si = true;
						if(getTipo()=='d' || getTipo()=='c'){
							if(!this.getNombre().equals(c.getNombre())){
								si = false;
							}
						}
						else{
							if(getTipo()=='d'){
								if(this.getNumero()!= c.getNumero() || this.getPropiedad()!=c.getPropiedad()){
									si = false;
								}
							}
						}
					}
				}
			}
			else{
				if(propias==null && c.getCoordenadas()==null){
					if(this.getTipo() == c.getTipo())
						si = true;
				}
			}
		}
		return si;
	}
	
	public char getTipo(){
		//NEW. metodo para obtener el tipo de casilla
		return tipo;
	}
	
	public void setTipo(char a){
		//NEW para cambiar el tipo de casilla cuando se ha creado el mapa
		tipo = a;
	}
}

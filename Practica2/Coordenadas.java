// DNI 74007071 ALDARAVI COLL, CARLOS

public class Coordenadas implements Comparable<Coordenadas>{
	private int fila;
	private int columna;
	
	public int compareTo(Coordenadas c){
		//0 iguales
		//1 this mayor
		//-1 el parametro mayor
		int pos;
		if(this.fila>c.getFila()){
			pos = 1;
		}
		else{
			if(this.fila<c.getFila()){
				pos = -1;
			}
			else{
				if(this.columna>c.getColumna()){
					pos = 1;
				}
				else{
					if(this.columna<c.getColumna()){
						pos = -1;
					}
					else{
						pos = 0;
					}
				}
			}
		}
		return pos;
	}
	
	public Coordenadas(int i, int j){
		fila = i;
		columna = j;
	}
	
	public int getFila(){
		return fila;
	}
	
	public int getColumna(){
		return columna;
	}
	public void setFila(int i){
		//NEW para redimensionar las filas del tablero
		//en caso de necesitarlo al leer el archivo
		
		if(i>=0)
			fila = i;
	}
	
	public void setColumna(int j){
		//NEW para redimensionar las columnas del tablero
		//en caso de necesitarlo al leer la primera linea
		//despues de <MAPA> del archivo
		
		if(j>=0)
			columna = j;
	}
}

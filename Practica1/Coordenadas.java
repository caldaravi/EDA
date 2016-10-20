// DNI 74007071 ALDARAVI COLL, CARLOS

public class Coordenadas {
	private int fila;
	private int columna;
	
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

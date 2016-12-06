// DNI 74007071 ALDARAVI COLL, CARLOS

import java.util.*;

public class exPath {
	private Coordenadas coor;
	private ArrayList<Coordenadas> camino;
	
	public exPath(int x, int y, ArrayList<Coordenadas> v){
		coor = new Coordenadas(x,y);
		
		camino = new ArrayList<Coordenadas>();
		
		if(!v.isEmpty()){
			for(int i=0; i<v.size();i++){
				camino.add(v.get(i));
			}
		}
		camino.add(coor);
	}
	
	public exPath(Coordenadas c, ArrayList<Coordenadas> v){
		//NEW para comodidad de pasar coordenadas en vez de dos enteros
		coor = new Coordenadas(c.getFila(),c.getColumna());
		
		camino = new ArrayList<Coordenadas>();
		
		if(!v.isEmpty()){
			for(int i=0; i<v.size();i++){
				camino.add(v.get(i));
			}
		}
		camino.add(coor);
	}
	
	public Coordenadas getCoordenadas(){
		return coor;
	}
	
	public ArrayList<Coordenadas> getCamino(){
		return camino;
	}
	
	public void setUltimasCoordenadas(){
		//NEW desde aqui borraremos las ultimas coordenadas del camino 
		//y pondremos las nuevas ultimas como las coordenadas de la ultima posicion que se visito
		camino.remove(camino.size()-1);
		coor = camino.get(camino.size()-1);
	}
}

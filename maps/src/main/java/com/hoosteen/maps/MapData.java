package com.hoosteen.maps;

import java.util.HashMap;

import javax.swing.JComponent;

import com.hoosteen.maps.MapTable.MapSection;

public class MapData {
	
	double centerLat;
	double centerLng;
	
	JComponent parent;
	
	public MapData(double centerLat, double centerLng, JComponent parent){
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.parent = parent;
	}
	
	HashMap<Integer, MapTable> data = new HashMap<Integer, MapTable>();
	
	public MapSection get(int zoom, int x, int y){
		
		MapTable table = data.get(zoom);
		
		if(table == null){
			data.put(zoom, (table = new MapTable(zoom, centerLat, centerLng, parent)));
		}
		
		return table.get(x, y);
		
	}
}
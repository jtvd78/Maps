package com.hoosteen.maps;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.JComponent;

public class MapTable {
	
	HashMap<Integer, HashMap<Integer, MapSection>> sections = new HashMap<Integer, HashMap<Integer, MapSection>>();

	int zoom;
	double centerLat;
	double centerLng;
	
	JComponent parent;
	
	public MapTable(int zoom, double centerLat, double centerLng, JComponent parent){
		this.zoom = zoom;
		this.centerLat = centerLat;
		this.centerLng = centerLng;
		this.parent = parent;
	}	
	
	class MapSection{
		
		BufferedImage mapImage;
		double mapCenterLat;
		double mapCenterLng;
		
		int x;
		int y;
		
		public MapSection(double mapCenterLat, double mapCenterLng, int x, int y){
			this.mapCenterLat = mapCenterLat;
			this.mapCenterLng = mapCenterLng;
			this.x = x;
			this.y = y;
			mapImage = MapMaker.getMapImage(mapCenterLat, mapCenterLng, zoom, (x == 0 && y == 0) ? true : false);
		}		
	}
	
	
	public MapSection get(int x, int y){
		
		HashMap<Integer, MapSection> col = sections.get(x);
		
		//There is no row
		if(col == null){
			
			load(x, y);
			return null;
		}
		
		MapSection section = col.get(y);
		
		if(section == null){
			load(x, y);
			return null;
		}		
		
		return section;
	}
	
	
	private void load(final int x, final int y){
		
		Runnable r = new Runnable(){
			public void run() {
				
				//Load the image col
				HashMap<Integer, MapSection> col = sections.get(x);
				
				//There is no col
				if(col == null){
					sections.put(x, new HashMap<Integer, MapSection>());
				}
				
				boolean hasImage = col.containsKey(y);
				
				if(!hasImage){
					
					double centerX = toPxLng(centerLng, zoom);
					double centerY = toPxLat(centerLat, zoom);
					
					double outLng = toLngOffset(centerX, x, zoom);
					double outLat = toLatOffset(centerY, y, zoom);
					
					MapSection newImage = new MapSection(outLat , outLng,x,y);
					col.put(y, newImage);
					sectionLoaded();
				}							
			}		
		};	
		
		new Thread(r).start();		
	}
	
	public static double toPxLat(double lat, int zoomLevel){	
		
		double insideTan = Math.PI/4.0 + Math.toRadians(lat)/2.0;
		double tan = Math.tan(insideTan);
		double ln = Math.log(tan)/Math.log(Math.E);
		
		double out = (Math.PI - ln )*(MapMaker.mapWidth/(2.0*Math.PI))*getZoomScale(zoomLevel);
		
		return out;
	}	
	
	public static double toPxLng(double lng, int zoomLevel){
		return (lng + 180.0)*(MapMaker.mapWidth/360.0)*getZoomScale(zoomLevel);
	}
	
	public static double toLatOffset(double centerY, double offset, int zoomLevel){
		
		double offsetY = centerY + MapMaker.mapHeight*offset;
		double zoomStuff = (offsetY)/getZoomScale(zoomLevel) * 2.0*Math.PI / MapMaker.mapHeight;		
		double tan = Math.atan(Math.pow(Math.E, (-1.0 * zoomStuff + Math.PI)));
		double half = tan - Math.PI/4.0;
		double rad =  half*2.0;
		double out = Math.toDegrees(rad);		
		
		return out;
	}
	
	public static double toLngOffset(double centerX, double offset, int zoomLevel){
		
		
		return (centerX + MapMaker.mapWidth*offset)/getZoomScale(zoomLevel) * 360.0 / MapMaker.mapWidth - 180;
	}	
	
	public static double toLat(double centerY, int zoomLevel){
		return toLatOffset(centerY, 0, zoomLevel);
	}
	
	public static double toLng(double centerX, int zoomLevel){
		return toLngOffset(centerX, 0, zoomLevel);
	}
	
	private static double getZoomScale(int zoomLevel){
		return Math.pow(2, zoomLevel);
	}
	
	private void sectionLoaded(){
		parent.repaint();
	}
}
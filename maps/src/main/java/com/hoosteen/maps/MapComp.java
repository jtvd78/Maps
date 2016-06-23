package com.hoosteen.maps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;

import com.hoosteen.graphics.GraphicsWrapper;
import com.hoosteen.graphics.Rect;
import com.hoosteen.maps.MapTable.MapSection;

public class MapComp extends JComponent{
	
	int zoom = 0;
	
	int xOffset = 0;
	int yOffset = 0;
	
	double baseLat;
	double baseLng;
	
	MapTools tools = new MapTools();
	MapData data;
	
	public MapComp(String address){
		double[] center = tools.getCoordinates(address);
		baseLat = center[0];
		baseLng = center[1];
		init();
	}
	
	public MapComp(double lat, double lng){
		baseLat = lat;
		baseLng = lng;
		init();
	}
	
	private void init(){
		
		data = new MapData(baseLat, baseLng, this);
		
		
		
		Listener l = new Listener();
		addMouseMotionListener(l);
		addMouseWheelListener(l);
	}
	
	public void paintComponent(Graphics g){
		
		GraphicsWrapper gw = new GraphicsWrapper(g);
		
		g.setColor(Color.black);
		g.fillRect(0,0,getWidth(),getHeight());
		
		g.setColor(Color.red);
		
		int spaceLeft = getWidth()/2 + xOffset;
		int spaceRight = getWidth()/2 - xOffset;
		
		int spaceUp = getHeight()/2 + yOffset;
		int spaceDown = getHeight()/2 - yOffset;		
		
		int startX = -(int) Math.floor(spaceLeft/MapMaker.mapWidth)-1;
		int endX = (int) Math.ceil(spaceRight/MapMaker.mapWidth)+1;
		
		int startY = -(int) Math.floor(spaceUp/MapMaker.mapHeight)-1;
		int endY = (int) Math.ceil(spaceDown/MapMaker.mapHeight)+1;
		
		
		
		for(int x = startX; x <= endX; x++){
			for (int y = startY; y <= endY; y++){		
				
				
				int xPos = getWidth()/2 - MapMaker.mapWidth/2 + x * MapMaker.mapWidth + xOffset;
				int yPos = getHeight()/2 - MapMaker.mapHeight/2 + y * MapMaker.mapHeight + yOffset;
				
				Rect r = new Rect(xPos, yPos, MapMaker.mapWidth, MapMaker.mapHeight);
				
				
				MapSection sectionToDraw = data.get(zoom, x, y);
				
				
				if(sectionToDraw == null){
					gw.drawCenteredString("Loading Image", r);
				}else{
					g.drawImage(sectionToDraw.mapImage, xPos, yPos, null);
				//	gw.drawMultiLineCenteredString(x + " : " + y + "\n" + sectionToDraw.mapCenterLat + " : " + sectionToDraw.mapCenterLng, r);
				}
			}
			
		}
		
		
		double centerMapX = MapTable.toPxLng(baseLng, zoom);
		double centerMapY = MapTable.toPxLat(baseLat, zoom);
		
		double xFromCenter = getWidth()/2 + xOffset - px;
		double yFromCenter = getHeight()/2 + yOffset -py;		
		
		double mapXatMouse = (centerMapX-xFromCenter);
		double mapYatMouse = (centerMapY-yFromCenter);
		
		int boxWidth = 250;
		int boxHeight = 75;
		int radius = 25;
		
		//Transparent black
		g.setColor(new Color(0,0,0,128));	
		g.fillRoundRect(0, 0, boxWidth, boxHeight, radius,radius);
		
		Rect box = new Rect(0,0,boxWidth,boxHeight);
		
		double lat = MapTable.toLat(mapYatMouse, zoom);
		double lng = MapTable.toLng(mapXatMouse, zoom);
		
		g.setColor(Color.RED);
		gw.drawMultiLineCenteredString("Zoom: " + zoom + "\nLat: " +  String.format("%.2f",lat)+ "\nLng: " + String.format("%.2f",lng), box);
	}
	
	int px;
	int py;
	
	class Listener implements MouseMotionListener, MouseWheelListener{

		public void mouseDragged(MouseEvent e) {

			int xChange = e.getX() - px;
			int yChange = e.getY() - py;
			
			xOffset += xChange;
			yOffset += yChange;
			
			
			px = e.getX();
			py = e.getY();
			
			repaint();
		}

		public void mouseMoved(MouseEvent e) {
			px = e.getX();
			py = e.getY();
			repaint();
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			if(e.getWheelRotation() > 0){
				zoom --;
			}else if(e.getWheelRotation() < 0){
				zoom ++;
			}
			repaint();
		}
		
	}
}
package com.hoosteen.maps;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public final class MapMaker {
	
	//Max size available for free
	public static final int mapWidth = 256;
	public static final int mapHeight = 256;
	
	private static final String baseURLString = "http://maps.googleapis.com/maps/api/staticmap?";

	/*
	 * 
	 * https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY&zoom=13&size=600x300&maptype=roadmap
&markers=color:blue%7Clabel:S%7C40.702147,-74.015794&markers=color:green%7Clabel:G%7C40.711614,-74.012318
&markers=color:red%7Clabel:C%7C40.718217,-73.998284
&key=YOUR_API_KEY
	 * 
	 */
	
	//Make it so this class cannot be instantiated 
	private MapMaker(){
		
	}
	
	private static String buildURL(String center, int zoom, boolean marker){
		
		String[] mapTypes = {"roadmap", "satellite", "terrain","hybrid"};
		
		
		if(zoom < 0 || zoom > 20){
			throw new IllegalArgumentException("Unsupported zoom level: " + zoom);
		}
		
		//Replace spaces with the space character thingy
		center = center.replace(" ", "%20");
		
		
		String urlString = baseURLString + "center=" + center +
				"&zoom=" + zoom + "&size=" + 256 + "x" + 256 + 
				"&maptype=" + mapTypes[0];
		
		if(marker){
			urlString +="&markers="+center;
		}
		
		
		urlString+="&key=" + MapTools.APIKey;
		
		return urlString;
	}
	
	private static BufferedImage BLANKIMAGE = new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_ARGB);
	
	public static BufferedImage getMapImage(double lat, double lng, int zoom, boolean marker){
		
		double max = 85.051129;
		
		if(lng < -180 || lng > 180 || lat < -max || lat > max){
			return BLANKIMAGE;
		}
		
		
		
		return getMapImage(lat+","+lng, zoom, marker);
	}
	
	public static BufferedImage getMapImage(String center, int zoom, boolean marker){
		
		String imageURL = buildURL(center, zoom, marker);
		
		BufferedImage out = null;
		try {
			 out = ImageIO.read(new URL(imageURL));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return out;		
	}	
}
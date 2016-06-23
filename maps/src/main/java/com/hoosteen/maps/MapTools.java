package com.hoosteen.maps;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class MapTools {
	
	
	GeoApiContext context;
	static String APIKey = "AIzaSyAxtu9Ptjei1m-ihn8f8RCwZzKZmyL6UGA";
	
	public MapTools(){
		context = new GeoApiContext().setApiKey(APIKey);
	}
    
    public double[] getCoordinates(String address){    	
    	GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(context,address).await();
			return new double[]{results[0].geometry.location.lat, results[0].geometry.location.lng};
		} catch (Exception e) {
			e.printStackTrace();
			return new double[]{0.0,0.0};
		}    			
    }    
}
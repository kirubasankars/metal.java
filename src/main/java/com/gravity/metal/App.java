package com.gravity.metal;

import com.google.gson.Gson;
/**
 * Hello world!
 *
 */
public class App 
{
    private static Gson gson;

	public static void main( String[] args )
    {
    	setGson(new Gson());
    	
    	Metal m = new Metal();
    	m.set("id", 1);
    	m.set("lines.@0.id", 90);    	
        System.out.println( m.get("id") );
        System.out.println( m.get("lines.@0.id") );
        System.out.println( m.get("lines.@0.$parent.id") );
        System.out.println( m.json(true) );
        
    }

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		App.gson = gson;
	}
}

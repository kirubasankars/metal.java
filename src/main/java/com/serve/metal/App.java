package com.serve.metal;

import com.google.gson.Gson;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	Gson gson = new Gson();
    	
    	Metal m = new Metal();
    	m.set("student.name", "Dev");
    	m.set("student.marks.@2.j", 90);
    	m.set("student.marks.@1", 20);
        System.out.println( m.get("student.name") );
        System.out.println( m.get("student.marks.@1") );
       // System.out.println( gson.toJson(m.raw()) );
        
    }
}

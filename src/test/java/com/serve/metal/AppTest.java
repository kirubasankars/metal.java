package com.serve.metal;

import com.gravity.metal.Metal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AppTest 
    extends TestCase
{    
    public AppTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    public void testJson() {
    	Metal m = new Metal();
    	m.set("rows.@0.order_id", 1);
    	m.set("rows.@0.order_date", null);
    	System.out.println(m.json());
    	assertEquals(m.json(), "{\"rows\":[{\"order_id\":1,\"order_date\":null}]}");
    }
    
    public void ttestSet()
    {
    	Metal m = new Metal();
    	
    	m.set("order_id", 1);
    	m.set("order_date", "2012/1/1");
    	
    	m.set("lines.@0.line_id", 1);
    	m.set("lines.@0.qty", 1);
    	
    	m.set("lines.@0.product.id", 1);
    	m.set("lines.@0.product.name", "product");
    	    	
    	m.set("lines.@1.line_id", 2);
    	m.set("lines.@1.qty", 2);
    	
    	m.set("lines.@1.product.id", 2);
    	m.set("lines.@1.product.name", "product 2");
    	
        assertTrue( Integer.parseInt(m.get("order_id").toString()) == 1 );        
        assertTrue( m.get("order_date").toString().equals("2012/1/1") );
        
        assertTrue( Integer.parseInt(m.get("$length").toString()) == 3 );        
        assertNull( m.get("$parent") );
        assertFalse( m.isArray() );
        
        assertNotNull( m.get("lines") );        
        assertTrue( Integer.parseInt(m.get("lines.$length").toString()) == 2 );
        assertTrue( m.get("lines.$parent") == m );
        
        Metal line1 = (Metal)m.get("lines.@0");        
        assertTrue( Integer.parseInt(line1.get("line_id").toString()) == 1 );
        assertTrue( Integer.parseInt(line1.get("qty").toString()) == 1 );
        assertTrue( Integer.parseInt(line1.get("$length").toString()) == 3 );
        assertTrue( m == line1.get("$parent"));
        
        Metal line2 = (Metal)m.get("lines.@1");
        
        assertTrue( Integer.parseInt(line2.get("line_id").toString()) == 2 );
        assertTrue( Integer.parseInt(line2.get("qty").toString()) == 2 );
        assertTrue( Integer.parseInt(line2.get("$length").toString()) == 3 );
        assertTrue( m == line2.get("$parent"));
        
        assertTrue( m.get("lines.@0.product.name").toString() == "product" );
        assertTrue( m.get("lines.@1.product.name").toString() == "product 2" );
                
        assertTrue( Integer.parseInt(m.get("lines.@1.$parent.order_id").toString()) == 1);       
        assertTrue( Integer.parseInt(m.get("lines.@1.product.$parent.line_id").toString()) == 2);
        
        assertTrue( Integer.parseInt(m.get("lines.@1.product.id").toString()) == 2 );
        
        assertTrue(m.json().equals("{\"order_date\":\"2012/1/1\",\"lines\":[{\"product\":{\"name\":\"product\",\"id\":1},\"qty\":1,\"line_id\":1},{\"product\":{\"name\":\"product 2\",\"id\":2},\"qty\":2,\"line_id\":2}],\"order_id\":1}"));
    }
    
    public void ttestSetArray()
    {
    	Metal m = new Metal();    	
    	m.parse("{\"order_date\":\"2012/1/1\",\"lines\":[{\"product\":{\"name\":\"product\",\"id\":1},\"qty\":1,\"line_id\":1},{\"product\":{\"name\":\"product 2\",\"id\":2},\"qty\":2,\"line_id\":2}],\"order_id\":1}");
        	    	
        assertTrue( Double.parseDouble(m.get("order_id").toString()) == 1 );              
        
        assertTrue( m.get("order_date").toString().equals("2012/1/1") );
                        
        assertTrue( Double.parseDouble(m.get("$length").toString()) == 3.0 );        
        assertNull( m.get("$parent") );
        assertFalse( m.isArray() );
        
        assertNotNull( m.get("lines") );        
        assertTrue( Double.parseDouble(m.get("lines.$length").toString()) == 2.0 );
        assertTrue( m.get("lines.$parent") == m );
        
        Metal line1 = (Metal)m.get("lines.@0");        
        assertTrue( Double.parseDouble(line1.get("line_id").toString()) == 1.0 );
        assertTrue( Double.parseDouble(line1.get("qty").toString()) == 1.0 );
        assertTrue( Double.parseDouble(line1.get("$length").toString()) == 3.0 );
        assertTrue( m == line1.get("$parent"));
        
        Metal line2 = (Metal)m.get("lines.@1");
        
        assertTrue( Double.parseDouble(line2.get("line_id").toString()) == 2.0 );
        assertTrue( Double.parseDouble(line2.get("qty").toString()) == 2.0 );
        assertTrue( Double.parseDouble(line2.get("$length").toString()) == 3.0 );
        assertTrue( m == line2.get("$parent"));
        
        assertTrue( m.get("lines.@0.product.name").toString().equals("product") );
        assertTrue( m.get("lines.@1.product.name").toString().equals("product 2") );
                        
        assertTrue( Double.parseDouble(m.get("lines.@1.$parent.order_id").toString()) == 1.0);
                
        assertTrue( Double.parseDouble(m.get("lines.@1.product.$parent.line_id").toString()) == 2.0);
        
        assertTrue( Double.parseDouble(m.get("lines.@1.product.id").toString()) == 2.0 );         
                
  //      System.out.println(m.json());
        
        assertTrue(m.json().equals("{\"order_date\":\"2012/1/1\",\"lines\":[{\"product\":{\"name\":\"product\",\"id\":1.0},\"qty\":1.0,\"line_id\":1.0},{\"product\":{\"name\":\"product 2\",\"id\":2.0},\"qty\":2.0,\"line_id\":2.0}],\"order_id\":1.0}"));
    	
    }
}

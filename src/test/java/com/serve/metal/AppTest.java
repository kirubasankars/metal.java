package com.serve.metal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testSet()
    {
    	Metal m = new Metal();
    	m.set("student.name", "dev");
    	    	
        assertTrue( m.get("student.name") == "dev" );        
        assertTrue( Integer.parseInt(m.get("student.$length").toString()) == 1 );
        assertTrue( Integer.parseInt(m.get("$length").toString()) == 1 );
    }
    
    public void testSetArray()
    {
    	Metal m = new Metal();
    	m.set("student.marks.@0", 10);
    	m.set("student.marks.@1", 20);
    	
        assertTrue( Integer.parseInt(m.get("student.marks.@0").toString()) == 10 );
        assertTrue( Integer.parseInt(m.get("student.marks.$length").toString()) == 2 );
        assertTrue( Integer.parseInt(m.get("student.$length").toString()) == 1 );
        assertTrue( Integer.parseInt(m.get("$length").toString()) == 1 );
    }
}

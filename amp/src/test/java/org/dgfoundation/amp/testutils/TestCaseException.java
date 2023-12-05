package org.dgfoundation.amp.testutils;

public class TestCaseException extends RuntimeException{
    
    public TestCaseException(String msg)
    {
        super(msg);
    }
    
    private TestCaseException()
    {
        
    }
}

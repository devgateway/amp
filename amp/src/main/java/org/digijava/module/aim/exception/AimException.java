package org.digijava.module.aim.exception;

import org.digijava.kernel.exception.DgException;

public class AimException extends DgException 
{
    public AimException() 
    {
    }

    public AimException(String message) 
    {
        super(message);
    }

    public AimException(String message, Throwable cause) 
    {
        super(message, cause);
    }

    public AimException(Throwable cause) 
    {
        super(cause);
    }
}

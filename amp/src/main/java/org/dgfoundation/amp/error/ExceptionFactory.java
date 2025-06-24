package org.dgfoundation.amp.error;

public class ExceptionFactory {
    public static AMPException newAMPException(Throwable cause){
        if (cause instanceof AMPException){
            AMPException ae = (AMPException)cause;
            return new AMPException(ae);
        }
        else
            if (cause instanceof AMPUncheckedException){
                AMPUncheckedException au = (AMPUncheckedException)cause;
                return new AMPException(au);
            }
            else{
                return new AMPException(cause);
            }
    }
    
    public static AMPException newAMPException(int level, boolean continuable, Throwable cause){
        if (cause instanceof AMPException){
            AMPException ae = (AMPException)cause;
            return new AMPException(level, continuable, ae);
        }
        else
            if (cause instanceof AMPUncheckedException){
                AMPUncheckedException au = (AMPUncheckedException)cause;
                return new AMPException(level, continuable, au);
            }
            else{
                return new AMPException(level, continuable, cause);
            }
    }
    
    public static AMPUncheckedException newAMPUncheckedException(Throwable cause){
        if (cause instanceof AMPException){
            AMPException ae = (AMPException)cause;
            return new AMPUncheckedException(ae);
        }
        else
            if (cause instanceof AMPUncheckedException){
                AMPUncheckedException au = (AMPUncheckedException)cause;
                return new AMPUncheckedException(au);
            }
            else{
                return new AMPUncheckedException(cause);
            }
    }
    
    public static AMPUncheckedException newAMPUncheckedException(int level, boolean continuable, Throwable cause){
        if (cause instanceof AMPException){
            AMPException ae = (AMPException)cause;
            return new AMPUncheckedException(level, continuable, ae);
        }
        else
            if (cause instanceof AMPUncheckedException){
                AMPUncheckedException au = (AMPUncheckedException)cause;
                return new AMPUncheckedException(level, continuable, au);
            }
            else{
                return new AMPUncheckedException(level, continuable, cause);
            }
    }
    
}

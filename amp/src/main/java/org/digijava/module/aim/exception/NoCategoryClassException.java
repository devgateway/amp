/**
 * 
 */
package org.digijava.module.aim.exception;

/**
 * @author Alex Gartner
 *
 */
public class NoCategoryClassException extends Exception {
    public NoCategoryClassException (String msg) {
        super(msg);
    }
    public NoCategoryClassException () {
        super();
    }
}

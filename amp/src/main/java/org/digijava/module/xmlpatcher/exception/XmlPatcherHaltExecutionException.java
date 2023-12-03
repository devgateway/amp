package org.digijava.module.xmlpatcher.exception;

/**
 * Thrown when a patch failed to apply and failOnError was set. Used to halt the execution of all following patches.
 * @author Octavian Ciubotaru
 */
public class XmlPatcherHaltExecutionException extends RuntimeException {

    public XmlPatcherHaltExecutionException(String s) {
        super(s);
    }
}

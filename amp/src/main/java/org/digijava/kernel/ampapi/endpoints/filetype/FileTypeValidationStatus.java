package org.digijava.kernel.ampapi.endpoints.filetype;

/**
 * Possible file type validation statuses
 * *  <li>{@link #ALLOWED}</li>
 * *  <li>{@link #NOT_ALLOWED}</li>
 * *  <li>{@link #CONTENT_EXTENSION_MISMATCH}</li>
 * 
 * @author Viorel Chihai
 *
 */
public enum FileTypeValidationStatus {
    
    ALLOWED(),
    
    NOT_ALLOWED(),
    
    CONTENT_EXTENSION_MISMATCH(),
    
    INTERNAL_ERROR();
}

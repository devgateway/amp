/**
 * 
 */
package org.digijava.module.aim.exception.dynlocation;

import org.digijava.kernel.exception.DgException;

/**
 * @author Alex Gartner
 *
 */
public class DuplicateLocationCodeException extends DgException {

    private String codeType;
    private String locationLevel;

    /**
     * 
     * @param message
     * @param codeType iso/iso3/code
     * @param locationLevel country / region / district /zone 
     */
    public DuplicateLocationCodeException(String message, String codeType, String locationLevel) {
        super(message);
        this.codeType           = codeType;
        this.locationLevel  = locationLevel;
    }

    public String getCodeType() {
        return codeType;
    }

    public String getLocationLevel() {
        return locationLevel;
    }

    
}

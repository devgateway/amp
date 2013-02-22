package org.digijava.module.gis.util;

public class GenericIdGetter implements IdGetter {
    public Long getId (Object obj) {
        Long retVal = null;
        if (obj != null) {
            retVal = (Long) obj;
        }
        return retVal;
    }
}

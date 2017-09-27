package org.dgfoundation.amp.onepager.util;

import java.io.Serializable;

public class AmpIndicatorTypeHelper implements Serializable {
    private String name;
    private String id;
    
    public AmpIndicatorTypeHelper(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
}

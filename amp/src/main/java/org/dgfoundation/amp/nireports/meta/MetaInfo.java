package org.dgfoundation.amp.nireports.meta;

import org.dgfoundation.amp.nireports.ImmutablePair;

/**
 * <b> Immutable</b> class describing a piece of metadata.
 * The enclosed value should be immutable (or at least <b>never</b> changed), or you will be in a world of pain
 * 
 * @author Dolghier Constantin
 * 
 */
public class MetaInfo extends ImmutablePair<String, Object> {
    
    public MetaInfo(String category, Object value) {
        super(category, value);
    }
    
    public String getCategory() {
        return this.k;
    }
    
    public Object getValue() {
        return this.v;
    }
}

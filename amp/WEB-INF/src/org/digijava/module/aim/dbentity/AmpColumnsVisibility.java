/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpColumnsVisibility implements Serializable {
    /** Comment for <code>serialVersionUID</code> */
    private static final long serialVersionUID = -4428239331282837794L;
    private AmpColumns ampColumn;
    private AmpFieldsVisibility ampfield;
    private AmpFeaturesVisibility parent;
    
    public AmpColumns getAmpColumn() {
        return ampColumn;
    }
    public void setAmpColumn(AmpColumns ampColumn) {
        this.ampColumn = ampColumn;
    }
    public AmpFieldsVisibility getAmpfield() {
        return ampfield;
    }
    public void setAmpfield(AmpFieldsVisibility ampfield) {
        this.ampfield = ampfield;
    }
    public AmpFeaturesVisibility getParent() {
        return parent;
    }
    public void setParent(AmpFeaturesVisibility parent) {
        this.parent = parent;
    }
        
}

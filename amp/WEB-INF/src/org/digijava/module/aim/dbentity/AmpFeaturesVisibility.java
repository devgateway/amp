/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;

import java.io.Serializable;

public class AmpFeaturesVisibility extends AmpObjectVisibility implements Serializable {

    private static final long serialVersionUID = 7004856623866175824L;


    public AmpObjectVisibility getParent() {
        return (AmpModulesVisibility)parent;
    }

    public String getVisible() {
        return templates.contains(parent.getParent())?"true":"false";
    }
    
    public AmpTemplatesVisibility getTemplate() {
        return parent.getTemplate();
    } 
    
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
        for(AmpFeaturesVisibility x:aObjVis.getFeatures())
        {
            if (x.getId().compareTo(id) == 0) 
                return true;            
        }
        return false;
    }
    @Override
    public Class getPermissibleCategory() {
        return AmpFeaturesVisibility.class;
        
    }
    
    
    
}

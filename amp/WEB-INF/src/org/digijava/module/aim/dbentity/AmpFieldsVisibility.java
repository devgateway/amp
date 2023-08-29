/*
* AMP FEATURE TEMPLATES
*/
/**
 * @author dan
 */
package org.digijava.module.aim.dbentity;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;

import java.io.Serializable;
import java.util.Iterator;

public class AmpFieldsVisibility extends AmpObjectVisibility implements Serializable{
    
        
    private static final long serialVersionUID = 1255296454545642749L;
    
    public String getVisible() {
        return templates.contains(parent.getParent().getParent())?"true":"false";
    }

    public AmpTemplatesVisibility getTemplate() {
        return parent.getTemplate();
    } 
    
    public boolean isVisibleTemplateObj(AmpTemplatesVisibility aObjVis){
        for(Iterator it=aObjVis.getFields().iterator();it.hasNext();)
        {
            AmpFieldsVisibility x=(AmpFieldsVisibility) it.next();
            if(x.getId().compareTo(id)==0) return true;
            
        }
        return false;
    }
    
    public boolean isFieldActive(AmpTreeVisibility atv)
    {
        AmpTemplatesVisibility currentTemplate = (AmpTemplatesVisibility) atv.getRoot();
        
        return currentTemplate.fieldExists(this.getName());
    }

    
    @Override
    public Class getPermissibleCategory() {
        return AmpFieldsVisibility.class;
    }
    
    public String getClusterIdentifier() {
        return name;
    }

//    public String toString()
//    {
//        return String.format("%s: %s", super.getName(), this.getVisible());
//    }
            
}

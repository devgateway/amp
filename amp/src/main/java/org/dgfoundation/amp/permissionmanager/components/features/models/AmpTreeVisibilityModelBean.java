/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import org.dgfoundation.amp.visibility.AmpObjectVisibility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dan
 *
 */
public class AmpTreeVisibilityModelBean implements Serializable
{
    private Boolean checked=false;
    private String name;
    private List<Object> items;
    private AmpObjectVisibility ampObjectVisibility;
    
    
    public AmpObjectVisibility getAmpObjectVisibility() {
        return ampObjectVisibility;
    }
    
    public void setAmpObjectVisibility(AmpObjectVisibility aov) {
        this.ampObjectVisibility = aov;
    }

    public String getName() {
        if(name.contains("/"))
            return name.substring(name.lastIndexOf("/")+1, name.length());
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    /**
     * Creates the bean.
     * 
     * @param s
     *            String that will be suffix of each property.
     * @param ampObjectVisibility 
     */
    public AmpTreeVisibilityModelBean(String s, List<Object> items, AmpObjectVisibility ampObjectVisibility)
    {
        checked = false;
        this.items = items;
        this.name = s;
        this.ampObjectVisibility = ampObjectVisibility;
    }

    
    public AmpTreeVisibilityModelBean(){
        this.items=new ArrayList();
        checked = false;
    }
    
    public AmpTreeVisibilityModelBean(String name, AmpObjectVisibility ampObjectVisibility){
        checked = false;
        this.name = name;
        this.items=new ArrayList();
        this.ampObjectVisibility = ampObjectVisibility;
    }
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getName();
    }
}

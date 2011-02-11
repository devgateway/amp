/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

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

    public String getName() {
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
     */
    public AmpTreeVisibilityModelBean(String s, List<Object> items)
    {
        checked = false;
        this.items = items;
        this.name = s;
    }

    
    public AmpTreeVisibilityModelBean(){
    	this.items=new ArrayList();
    	checked = false;
    }
    
    public AmpTreeVisibilityModelBean(String name){
    	checked = false;
    	this.name = name;
    	this.items=new ArrayList();
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

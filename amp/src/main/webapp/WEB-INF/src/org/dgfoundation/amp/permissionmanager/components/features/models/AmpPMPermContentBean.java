/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;

import org.digijava.module.gateperm.core.GatePermission;


/**
 * @author dan
 *
 */
public class AmpPMPermContentBean implements Comparable<AmpPMPermContentBean>, Serializable{

    /**
     * 
     */
    public AmpPMPermContentBean() {
        // TODO Auto-generated constructor stub
    }

    String label    = "";
    Boolean view    = false;
    Boolean edit    = false;
    
    public AmpPMPermContentBean(GatePermission gate){
        
    }
    
    public AmpPMPermContentBean(String label,  Boolean view, Boolean edit) {
        super();
        this.label = label;
        this.edit = edit;
        this.view = view;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public Boolean getEdit() {
        return edit;
    }
    public void setEdit(Boolean edit) {
        this.edit = edit;
    }
    public Boolean getView() {
        return view;
    }
    public void setView(Boolean view) {
        this.view = view;
    }

    @Override
    public int compareTo(AmpPMPermContentBean o) {
        
        return this.label.compareTo(o.getLabel());
    }
    
    
}

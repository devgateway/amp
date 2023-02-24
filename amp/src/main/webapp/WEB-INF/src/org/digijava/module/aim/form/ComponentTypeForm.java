package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComponentType;

public class ComponentTypeForm extends ActionForm implements Serializable {
    private static final long serialVersionUID = 5155457392821477753L;
    Long id = null;
    String name = null;
    String code = null;

    Boolean enable;
    Boolean selectable;
    String check;
    ArrayList<AmpComponentType> componentTypesList;

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        this.id = null;
        this.name = null;
        this.code = null;
        this.enable = false;
        this.selectable = false;
        this.check=null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<AmpComponentType> getComponentTypesList() {
        return componentTypesList;
    }

    public void setComponentTypesList(ArrayList<AmpComponentType> componentTypesList) {
        this.componentTypesList = componentTypesList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    /**
     * @return the selectable
     */
    public Boolean getSelectable() {
        return selectable;
    }

    /**
     * @param selectable the selectable to set
     */
    public void setSelectable(Boolean selectable) {
        this.selectable = selectable;
    }
    

}

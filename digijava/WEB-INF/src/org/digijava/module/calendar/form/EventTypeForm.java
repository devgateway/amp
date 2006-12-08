package org.digijava.module.calendar.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.digijava.module.calendar.util.AmpDbUtil;
import java.util.Iterator;
import java.util.ArrayList;

public class EventTypeForm
    extends ActionForm {
    private List eventTypes;
    private String addName;
    private String addColor;
    private long deleteId;

    public List getEventTypes() {
        return eventTypes;
    }

    public AmpEventType getEventType(int index) {
        return (AmpEventType)eventTypes.get(index);
    }

    public String getAddName() {
        return addName;
    }

    public String getAddColor() {
        return addColor;
    }

    public long getDeleteId() {
        return deleteId;
    }

    public void setEventTypes(List eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setAddColor(String addColor) {
        this.addColor = addColor;
    }

    public void setAddName(String addName) {
        this.addName = addName;
    }

    public void setDeleteId(long deleteId) {
        this.deleteId = deleteId;
    }
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();

        if(this.getAddColor()==null || this.getAddColor().trim().equals("")){
            errors.add(null, new ActionError("error.calendar.emptyEventTypeName"));
        }

        if(this.getAddName()==null || this.getAddName().trim().equals("")){
            errors.add(null, new ActionError("error.calendar.emptyEventTypeColor"));
        }else {
            /*if (!this.getAddColor().matches("^[a-fA-F0-9]{6}$")) {
                errors.add(null, new ActionError("error.calendar.invalidEventTypeColor"));

            }*/
        }
        try{
            List eventTypes = new ArrayList(AmpDbUtil.getEventTypes());
            if(eventTypes != null) {
                Iterator etItr = eventTypes.iterator();
                while(etItr.hasNext()) {
                    AmpEventType et = (AmpEventType) etItr.next();
                    if(et.getColor().equalsIgnoreCase(getAddColor())) {
                        errors.add(null, new ActionError("error.calendar.colorAllredyExist"));
                        break;
                    }
                }
                etItr=null;

                etItr = eventTypes.iterator();
                while(etItr.hasNext()) {
                    AmpEventType et = (AmpEventType) etItr.next();
                    if(et.getName().equalsIgnoreCase(getAddName())) {
                        errors.add(null, new ActionError("error.calendar.nameAllredyExist"));
                        break;
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return errors.isEmpty() ? null : errors;
    }
}

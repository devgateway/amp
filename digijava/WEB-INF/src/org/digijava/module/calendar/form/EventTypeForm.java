package org.digijava.module.calendar.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;

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
            if (!this.getAddColor().matches("^[a-fA-F0-9]{6}$")) {
                errors.add(null, new ActionError("error.calendar.invalidEventTypeColor"));

            }

        }

        return errors.isEmpty() ? null : errors;
    }
}

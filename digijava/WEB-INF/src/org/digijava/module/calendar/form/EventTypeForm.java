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
    private String eventTypeName;
    private String eventTypeColor;
    private Long eventTypeId;

    public List getEventTypes() {
        return eventTypes;
    }

    public AmpEventType getEventType(int index) {
        return (AmpEventType)eventTypes.get(index);
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public String getEventTypeColor() {
        return eventTypeColor;
    }

    public Long getEventTypeId() {
        return eventTypeId;
    }

    public void setEventTypes(List eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setEventTypeColor(String eventTypeColor) {
        this.eventTypeColor = eventTypeColor;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public void setEventTypeId(Long eventTypeId) {
        this.eventTypeId = eventTypeId;
    }
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();

        if(this.getEventTypeColor()==null || this.getEventTypeColor().trim().equals("")){
            errors.add(null, new ActionError("error.calendar.emptyEventTypeName"));
        }

        if(this.getEventTypeName()==null || this.getEventTypeName().trim().equals("")){
            errors.add(null, new ActionError("error.calendar.emptyEventTypeColor"));
        }else{
            String color = this.getEventTypeColor();
            if(color != null) {
                if(color.length() != 0) {
                    try{
                        if(color.charAt(0) != '#') {
                            errors.add(null,new ActionError("error.calendar.missingCharInBegining"));
                        } else {
                            color = color.substring(1, color.length());
                        }

                        if(!color.matches("^[a-fA-F0-9]{6}$")) {
                            errors.add(null,new ActionError("error.calendar.invalidEventTypeColor"));
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }

        try {
            List eventTypes = new ArrayList(AmpDbUtil.getEventTypes());
            if (eventTypes != null) {
                Iterator etItr = eventTypes.iterator();
                while (etItr.hasNext()) {
                    AmpEventType et = (AmpEventType) etItr.next();
                    if (et.getColor().equalsIgnoreCase(this.getEventTypeColor())) {
                        if(!et.getId().equals(this.getEventTypeId())){
                            errors.add(null,
                                       new ActionError("error.calendar.colorAlreadyExist"));
                            break;
                        }
                    }
                }
                etItr = null;

                etItr = eventTypes.iterator();
                while (etItr.hasNext()) {
                    AmpEventType et = (AmpEventType) etItr.next();
                    if (et.getName().equalsIgnoreCase(this.getEventTypeName())) {
                        if(!et.getId().equals(this.getEventTypeId())){
                            errors.add(null,
                                       new ActionError("error.calendar.nameAlreadyExist"));
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return errors.isEmpty() ? null : errors;
    }
}

package org.digijava.module.calendar.action;

import org.digijava.module.calendar.util.AmpDbUtil;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.digijava.module.calendar.form.EventTypeForm;
import org.digijava.module.calendar.util.AmpUtil;
import java.util.Iterator;
import java.util.List;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.apache.struts.action.ActionErrors;

public class EventTypeAction
    extends DispatchAction {

    public ActionForward unspecified(ActionMapping mapping,
                                     ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws
        Exception {
        return showList(mapping, form, request, response);
    }

    public ActionForward showList(ActionMapping mapping,
                                  ActionForm form,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws
        Exception {
        EventTypeForm eventForm = (EventTypeForm) form;
        eventForm.setEventTypes(AmpDbUtil.getEventTypes());
        eventForm.setEventTypeColor(null);
        eventForm.setEventTypeName(null);
        return mapping.findForward("eventTypesPage");
    }

    public ActionForward delete(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws
        Exception {
        EventTypeForm eventForm = (EventTypeForm) form;
        AmpUtil.deleteEventType(eventForm.getEventTypeId());
        return showList(mapping, form, request, response);
    }

    public ActionForward addType(ActionMapping mapping,
                            ActionForm form,
                            HttpServletRequest request,
                            HttpServletResponse response) throws
    Exception {
    EventTypeForm eventForm = (EventTypeForm) form;
    ActionErrors errors = eventForm.validate(mapping, request);

    if (errors == null){
        AmpUtil.createEventType(eventForm.getEventTypeName(), eventForm.getEventTypeColor());
    } else {
        if(errors.size() == 0) {
            AmpUtil.createEventType(eventForm.getEventTypeName(), eventForm.getEventTypeColor());
        }else{
            saveErrors(request, errors);
        }
    }

    return showList(mapping, form, request, response);
}


    public ActionForward save(ActionMapping mapping,
                              ActionForm form,
                              HttpServletRequest request,
                              HttpServletResponse response) throws
        Exception {
        EventTypeForm eventForm = (EventTypeForm) form;
        List typesList = eventForm.getEventTypes();

        Iterator iterTypes = typesList.iterator();
        while(iterTypes.hasNext()) {
            AmpEventType typeBean = (AmpEventType) iterTypes.next();
            if(typeBean.getId().equals(eventForm.getEventTypeId())){
                eventForm.setEventTypeName(typeBean.getName());
                eventForm.setEventTypeColor(typeBean.getColor());
                break;
            }
        }


        ActionErrors errors = eventForm.validate(mapping, request);
        if (errors != null){
           if(errors.size() != 0) {
               saveErrors(request, errors);
           }
        }else{
            iterTypes=typesList.iterator();
            while(iterTypes.hasNext()) {
                AmpEventType typeBean = (AmpEventType) iterTypes.next();
                AmpEventType et=AmpDbUtil.getEventType(typeBean.getId());
                et.setName(typeBean.getName());
                et.setColor(typeBean.getColor());
                AmpDbUtil.updateEventType(et);
            }
        }
        return showList(mapping, form, request, response);
    }
}

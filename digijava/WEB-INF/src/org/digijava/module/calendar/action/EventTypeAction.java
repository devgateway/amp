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
        eventForm.setAddColor(null);
        eventForm.setAddName(null);
        return mapping.findForward("eventTypesPage");
    }

    public ActionForward delete(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws
        Exception {
        EventTypeForm eventForm = (EventTypeForm) form;
        AmpUtil.deleteEventType(new Long(eventForm.getDeleteId()));
        return showList(mapping, form, request, response);
    }

    public ActionForward addType(ActionMapping mapping,
                            ActionForm form,
                            HttpServletRequest request,
                            HttpServletResponse response) throws
    Exception {
    EventTypeForm eventForm = (EventTypeForm) form;
    ActionErrors errors = eventForm.validate(mapping, request);

    if (errors == null || errors.size() == 0) {
        AmpUtil.createEventType(eventForm.getAddName(), eventForm.getAddColor());
    } else {
        saveErrors(request, errors);
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
            AmpDbUtil.updateEventType(typeBean);
        }
        if(eventForm.getAddName() != null && eventForm.getAddColor() != null &&
           !eventForm.getAddName().equals("") && !eventForm.getAddColor().equals("")) {
            AmpUtil.createEventType(eventForm.getAddName(),eventForm.getAddColor());
        }
        return showList(mapping, form, request, response);
    }
}

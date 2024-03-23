package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.dbentity.AmpComponentsIndicators;
import org.digijava.module.aim.form.ComponentsForm;
import org.digijava.module.aim.util.ComponentsUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

public class AddComponentIndicator extends Action 
{
    private static Logger logger = Logger.getLogger(AddComponentIndicator.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception 
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("ampAdmin") == null) {
            return mapping.findForward("index");
        } else {
            String str = (String) session.getAttribute("ampAdmin");
            if (str.equals("no")) {
                return mapping.findForward("index");
            }
        }

        ComponentsForm compForm = (ComponentsForm) form;

        String event = request.getParameter("event");
        String idS = request.getParameter("id");
        
        logger.debug("event="+event+"     idS="+idS);
        
    if(event !=null){
        if(event.equalsIgnoreCase("add")){
            logger.debug("add");
            compForm.setCompIndicatorName(null);
            compForm.setCompIndicatorCode(null);
            compForm.setCompIndicatorDesc(null);
            compForm.setIndicatorId(null);
        }
        else if(event.equalsIgnoreCase("edit")){
            for (Object o : ComponentsUtil.getComponentIndicator(new Long(idS))) {
                AmpComponentsIndicators ampComInd = (AmpComponentsIndicators) o;
                compForm.setIndicatorId(ampComInd.getAmpCompIndId());
                compForm.setCompIndicatorName(ampComInd.getName());
                compForm.setCompIndicatorCode(ampComInd.getCode());
                compForm.setCompIndicatorDesc(ampComInd.getDescription());
            }
        }
        else if(event.equalsIgnoreCase("save"))
        {  
            logger.debug("save");
            boolean dupExist = ComponentsUtil.checkDuplicateNameCode(
                    compForm.getCompIndicatorName(), compForm.getCompIndicatorCode(),compForm.getIndicatorId());
            if (dupExist) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                        "error.aim.compAddIndicator.duplicateNameOrCode"));
                saveErrors(request, errors);
                compForm.setDuplicate("true");
                logger.debug("Duplicate Values::::::::::::::;;");
                return mapping.findForward("forward");
            }
            AmpComponentsIndicators ampCompInd = new AmpComponentsIndicators();
            if(idS==null || idS.equalsIgnoreCase("")  || idS.trim().length()==0){
            logger.debug("just born");
            ampCompInd.setName(compForm.getCompIndicatorName());
            ampCompInd.setCode(compForm.getCompIndicatorCode());
            ampCompInd.setDescription(compForm.getCompIndicatorDesc());
            }
            else{
            logger.debug("not new");
            ampCompInd.setAmpCompIndId(new Long(idS));
            ampCompInd.setName(compForm.getCompIndicatorName());
            ampCompInd.setCode(compForm.getCompIndicatorCode());
            ampCompInd.setDescription(compForm.getCompIndicatorDesc());
            }
            ComponentsUtil.saveComponentIndicator(ampCompInd);
            compForm.setDuplicate("save");
            return mapping.findForward("forward");
        }
        else if(event.equalsIgnoreCase("delete")){
            ComponentsUtil.delComponentIndicator(new Long(idS));
            return mapping.findForward("delete");
        }
    }   
        compForm.setDuplicate("false");
        return mapping.findForward("forward");
  }
}

/*
 * GetIndicatorValues.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateIndicatorValuesForm;
import org.digijava.module.aim.helper.ActivityIndicator;

public class GetIndicatorValues extends Action {
    
    private static Logger logger = Logger.getLogger(GetIndicatorValues.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        UpdateIndicatorValuesForm uIndValForm = (UpdateIndicatorValuesForm) form;
        Long indValId = new Long(-1);
        String temp = request.getParameter("indValId");
        if (temp != null) {
            try {
                indValId = new Long(Long.parseLong(temp));
                if (uIndValForm.getIndicators() != null) {
                    Iterator itr = uIndValForm.getIndicators().iterator();
                    while (itr.hasNext()) {
                        ActivityIndicator actInd = (ActivityIndicator) itr.next();
                        if (actInd.getIndicatorId().equals(indValId)) {
                            uIndValForm.setBaseVal(actInd.getBaseVal());
                            uIndValForm.setBaseValDate(actInd.getBaseValDate());
                            uIndValForm.setTargetVal(actInd.getTargetVal());
                            uIndValForm.setTargetValDate(actInd.getTargetValDate());
                            uIndValForm.setIndicatorId(actInd.getIndicatorId());
                            uIndValForm.setIndicatorValId(actInd.getIndicatorValId());
                            uIndValForm.setExpIndicatorId(actInd.getIndicatorId());
                            uIndValForm.setRevisedTargetVal(actInd.getRevisedTargetVal());
                            uIndValForm.setRevisedTargetValDate(actInd.getRevisedTargetValDate());
                            //uIndValForm.setActivityId(actInd.getActivityId());
                            break;
                        }
                    }
                }               
            } catch (NumberFormatException nfe) {
                logger.error("Trying to parse " + temp + " to long");
                logger.error(nfe.getMessage());
            }
        } else {
            uIndValForm.setExpIndicatorId(new Long(-1));
        }
        
        return mapping.findForward("forward");
    }
}

/*
 * GetActivityIndicators.java
 * Created : 21-Mar-2006
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.UpdateIndicatorValuesForm;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;

public class GetActivityIndicators extends Action {
    
    private static Logger logger = Logger.getLogger(GetActivityIndicators.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        UpdateIndicatorValuesForm uIndValForm = (UpdateIndicatorValuesForm) form;
        
        HttpSession session = request.getSession();
        
        uIndValForm.setActivityId(uIndValForm.getActivityId());
        
        uIndValForm.setIndicators(IndicatorUtil.getActivityIndicatorHelperBeans(
                uIndValForm.getActivityId()));
        
        uIndValForm.setIndicatorId(null);
        uIndValForm.setIndicatorValId(null);
        uIndValForm.setExpIndicatorId(new Long(-1));
        if("true".equalsIgnoreCase((String)session.getAttribute("forStep9"))){
            session.removeAttribute("forStep9");
            return mapping.findForward("AddIndicatorForStepNine");
        }
        return mapping.findForward("forward");
    }
}

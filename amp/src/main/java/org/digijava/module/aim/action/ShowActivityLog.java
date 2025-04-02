package org.digijava.module.aim.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpAuditLogger;
import org.digijava.module.aim.form.AuditLoggerManagerForm;
import org.digijava.module.aim.util.AuditLoggerUtil;
 
public class ShowActivityLog extends Action{
    
    private static Logger logger = Logger.getLogger(ShowActivityLog.class);
    
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response){
        AuditLoggerManagerForm vForm = (AuditLoggerManagerForm) form;
        String activityId  = request.getParameter("activityId");
        List<AmpAuditLogger> logs = AuditLoggerUtil.getActivityLogObjects(activityId);
        if(logs.size() > 5)
            logs = logs.subList(0, 5);
        vForm.setLogs(logs);
        return mapping.findForward("forward");
    }

}

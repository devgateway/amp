/*
 * Created on 18/04/2005
 * @author akashs
 *
 */
package org.digijava.module.aim.action ;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.form.PhysicalProgressForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class ViewComponentDescription extends Action
{
    private static Logger logger = Logger.getLogger(ViewComponentDescription.class) ;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
    HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
    {
        HttpSession session = request.getSession();
        TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
        PhysicalProgressForm formBean = (PhysicalProgressForm) form ; 

        if(teamMember==null)
        {
            formBean.setValidLogin(false);
        }
        else
        {
            DecimalFormat mf = new DecimalFormat("###,###,###,###,###");
            formBean.setValidLogin(true);
            Long cid = new Long(request.getParameter("cid"));
            logger.debug("CID is : " + cid.toString());
            AmpComponent progress=DbUtil.getAmpComponentDescription(cid);
            formBean.setTitle(progress.getTitle());
            formBean.setDescription(progress.getDescription());
        }
        return mapping.findForward("forward");
    }
}

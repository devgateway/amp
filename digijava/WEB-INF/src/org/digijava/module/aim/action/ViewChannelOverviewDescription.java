package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpSector ;
import org.digijava.module.aim.dbentity.AmpLevel ;
import org.digijava.module.aim.dbentity.AmpLocation ;
import org.digijava.module.aim.form.ChannelOverviewDescriptionForm ;
import org.digijava.module.aim.dbentity.AmpActivityInternalId ;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpFunding ;
import org.digijava.module.aim.dbentity.AmpModality ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.COverSubString ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import java.util.Collection ;
import java.util.Iterator ;
import java.util.ArrayList ;

public class ViewChannelOverviewDescription extends Action
{
	private static Logger logger = Logger.getLogger(ViewChannelOverview.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		Long id=new Long(request.getParameter("ampActivityId"));
		ChannelOverviewDescriptionForm formBean = (ChannelOverviewDescriptionForm) form ; 
		AmpActivity ampActivity = DbUtil.getProjectChannelOverview(id) ;
		

		if ( ampActivity != null )
		{
			formBean.setName(ampActivity.getName()) ;
			formBean.setDescription(ampActivity.getDescription()) ; 
			
		}
		return mapping.findForward("forward");
	}
}


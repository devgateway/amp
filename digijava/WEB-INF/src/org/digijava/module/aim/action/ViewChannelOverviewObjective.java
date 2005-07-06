package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.apache.struts.tiles.actions.TilesAction;
import org.apache.struts.tiles.ComponentContext;
import java.io.IOException;
import javax.servlet.ServletException;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpSector ;
import org.digijava.module.aim.dbentity.AmpLevel ;
import org.digijava.module.aim.dbentity.AmpLocation ;
import org.digijava.module.aim.form.ChannelOverviewObjectiveForm ;
import org.digijava.module.aim.dbentity.AmpActivityInternalId ;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpFunding ;
import org.digijava.module.aim.dbentity.AmpModality ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.COverSubString ;
import org.digijava.module.aim.helper.TeamMember ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.Collection ;
import java.util.Iterator ;
import java.util.ArrayList ;

public class ViewChannelOverviewObjective extends TilesAction
{
	private static Logger logger = Logger.getLogger(ViewChannelOverviewObjective.class) ;
	public ActionForward execute(ComponentContext context,
								  ActionMapping mapping, 
								  ActionForm form,
								  HttpServletRequest request, 
								  HttpServletResponse response) 
							   throws IOException,ServletException 
	{
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");
		Long id=new Long(request.getParameter("ampActivityId"));
		ChannelOverviewObjectiveForm formBean = (ChannelOverviewObjectiveForm) form ; 
		if(teamMember==null)
		{
			formBean.setValidLogin(false);
		}
		else
		{
			formBean.setValidLogin(true);
			AmpActivity ampActivity = DbUtil.getProjectChannelOverview(id) ;
			if(ampActivity!=null)
			{
				if(teamMember.getAppSettings().getPerspective()==null)
				formBean.setPerspective("Donor");
			else
				formBean.setPerspective(teamMember.getAppSettings().getPerspective());
				formBean.setName(ampActivity.getName()) ;
				formBean.setObjective(ampActivity.getObjective()) ; 
			}		
		}
		return null;
	}
}


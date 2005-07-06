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
import org.digijava.module.aim.form.KnowledgeForm ;
import org.digijava.module.aim.dbentity.AmpNotes ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.COverSubString ;
import org.digijava.module.aim.helper.TeamMember ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import javax.servlet.http.HttpSession ;
import java.util.Collection ;
import java.util.Iterator ;
import java.util.ArrayList ;

public class ViewKnowledgeNotes extends TilesAction
{
	private static Logger logger = Logger.getLogger(ViewKnowledgeNotes.class) ;
	public ActionForward execute(ComponentContext context,
								  ActionMapping mapping, 
								  ActionForm form,
								  HttpServletRequest request, 
								  HttpServletResponse response) 
							   throws IOException,ServletException 
	{
		HttpSession session = request.getSession();
		TeamMember teamMember=(TeamMember)session.getAttribute("currentMember");

		KnowledgeForm formBean = (KnowledgeForm) form ; 
		if(teamMember==null)
		{
			formBean.setValidLogin(false);
		}
		else
		{
			if(teamMember.getAppSettings().getPerspective()==null)
				formBean.setPerspective("Donor");
			else
				formBean.setPerspective(teamMember.getAppSettings().getPerspective());
			formBean.setValidLogin(true);
			Long id=new Long(request.getParameter("ampNotesId"));
			AmpNotes ampNotes = DbUtil.getAmpNotesDetails(id) ;
			formBean.setDescription(ampNotes.getDescription()) ; 
		}
		return null;
	}
}


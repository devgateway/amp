package org.digijava.module.aim.action ;

import org.apache.log4j.Logger ;
import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.digijava.module.aim.form.DonorDataImportForm ;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.helper.COverSubString ;
import org.digijava.module.aim.helper.AmpProject ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import java.util.Collection ;
import java.util.Iterator ;
import java.util.ArrayList ;

public class DonorDataImport extends Action
{
	private static Logger logger = Logger.getLogger(ViewChannelOverview.class) ;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		DonorDataImportForm formBean=(DonorDataImportForm) form;
		
		return mapping.findForward("forward");
	}
}


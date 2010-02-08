package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpStatus;
import org.digijava.module.aim.form.StatusListForm;
import org.digijava.module.aim.helper.AmpStatusItem;
import org.digijava.module.aim.util.DbUtil;

/**
 * 
 * @deprecated
 *
 * CategoryManager should be used instead
 */
public class StatusManager extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
	HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{

		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") == null) {
			return mapping.findForward("index");
		} else {
			String str = (String)session.getAttribute("ampAdmin");
			if (str.equals("no")) {
				return mapping.findForward("index");
			}
		}
			  
		StatusListForm formBean = (StatusListForm) form ; 
		
		ArrayList dbReturnSet= null ;
		
		Iterator iter = null ;
		
		dbReturnSet = DbUtil.getAmpStatusList();
		
		if (dbReturnSet != null)
		{
			iter = dbReturnSet.iterator() ;
		
					formBean.setStatusCollection(new ArrayList()) ;
					while ( iter.hasNext() )
					{
							AmpStatus ampStatus = (AmpStatus) iter.next() ;	
							AmpStatusItem statusItem = new AmpStatusItem();
							statusItem.setAmpStatusId(ampStatus.getAmpStatusId());
							statusItem.setName(ampStatus.getName())		;
							statusItem.setStatusCode(ampStatus.getStatusCode());	
							formBean.getStatusCollection().add(statusItem) ;
				
					}
		}
		
		
		return mapping.findForward("forward");
	}
}


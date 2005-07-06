package org.digijava.module.aim.action ;

import org.apache.struts.action.* ;
import org.digijava.module.aim.form.AmpActivityForm;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpSector ;
import org.digijava.module.aim.util.DbUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import javax.servlet.http.* ;

public class ShowSubSectorList extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws java.lang.Exception
	{
		Collection dbReturnSet=null;
		Iterator iter;
		AmpActivityForm formBean = (AmpActivityForm) form ; 
				
		/*dbReturnSet=DbUtil.getAmpSectors();
		iter = dbReturnSet.iterator() ;
		formBean.setSectors(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpSector ampSector = (AmpSector) iter.next() ;
			formBean.getSectors().add(ampSector);
		}*/

		dbReturnSet=DbUtil.getAmpSubSectors();
		iter = dbReturnSet.iterator() ;
		formBean.setSubSectors(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpSector ampSector = (AmpSector) iter.next() ;
			formBean.getSubSectors().add(ampSector);
		}

		return mapping.findForward("forward");
	}
}
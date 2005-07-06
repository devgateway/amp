package org.digijava.module.aim.action ;

import org.apache.struts.action.Action ;
import org.apache.struts.action.ActionForm ;
import org.apache.struts.action.ActionMapping ;
import org.apache.struts.action.ActionForward ;
import org.digijava.kernel.util.DgUtil ;
import org.digijava.module.aim.dbentity.AmpActivity ;
import org.digijava.module.aim.dbentity.AmpModality ;
import org.digijava.module.aim.dbentity.AmpStatus ;
import org.digijava.module.aim.dbentity.AmpLevel ;
import org.digijava.module.aim.dbentity.AmpLocation ;
import org.digijava.module.aim.dbentity.AmpSector ;
import org.digijava.module.aim.dbentity.AmpOrganisation ;
import org.digijava.module.aim.helper.Notes ;
import org.digijava.module.aim.form.AmpActivityForm ;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.COverSubString ;
import org.digijava.module.aim.util.DbUtil;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class ShowAddOrganisation extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response)
								 throws java.lang.Exception
	{
		AmpActivityForm formBean = (AmpActivityForm) form ; 
		Collection dbReturnSet=null;
		Iterator iter;
		
		dbReturnSet=DbUtil.getAmpOrganisations();
		iter = dbReturnSet.iterator() ;
		formBean.setFundingagency(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
			formBean.getFundingagency().add(ampOrganisation);
		}
		
		iter = dbReturnSet.iterator() ;
		formBean.setReportingagency(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
			formBean.getReportingagency().add(ampOrganisation);
		}

		iter = dbReturnSet.iterator() ;
		formBean.setImplagency(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
			formBean.getImplagency().add(ampOrganisation);
		}

		iter = dbReturnSet.iterator() ;
		formBean.setRelatedins(new ArrayList()) ;
		while ( iter.hasNext() )
		{
			AmpOrganisation ampOrganisation = (AmpOrganisation) iter.next() ;
			formBean.getRelatedins().add(ampOrganisation);
		}

		
	/*	AmpActivityForm.SectorList list = new AmpActivityForm.SectorList();
		list.setAmpSectorId(new Long(0));
		formBean.getSectorList().add(list);*/

		return mapping.findForward("forward");
	}
}



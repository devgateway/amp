package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AddMTEFProjection extends Action{

	private static Logger logger = Logger.getLogger(AddMTEFProjection.class);
	
	private List<MTEFProjection> mtefProjections=null;
	
	private String event;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getFunding().getEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		
		try {
			formBean.getFunding().setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false, request));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long index = formBean.getFunding().getTransIndexId();
		String subEvent = event.substring(0,3);
		
		
		MTEFProjection mp = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFunding().getFundingMTEFProjections() == null || 
					formBean.getFunding().getFundingMTEFProjections().size() == 0) {
				boolean afterFiscalYearStart	= AddFunding.isAfterFiscalYearStart( null );
				mtefProjections = new ArrayList<MTEFProjection>();
				mp = AddFunding.getMTEFProjection(request.getSession(), 0, afterFiscalYearStart, null);
				mtefProjections.add(mp);		
			} else {
				int lastIndex							= formBean.getFunding().getFundingMTEFProjections().size()-1;
				MTEFProjection lastMtef		= formBean.getFunding().getFundingMTEFProjections().get(lastIndex);
				//boolean afterFiscalYearStart	= AddFunding.isAfterFiscalYearStart( firstMtef.getProjectionDate() );
				String [] dateSplit				= lastMtef.getProjectionDate().split("/");
				Integer year;
				try {
					year	= Integer.parseInt( dateSplit[2] );
				}
				catch (Exception E) {
					year	= null;
					logger.error(E.getMessage());
				}
				
				mtefProjections 			= formBean.getFunding().getFundingMTEFProjections();
				if (subEvent.equals("del")) {
					mtefProjections.remove( (int)index );
//					Iterator<MTEFProjection> iter	= mtefProjections.iterator();
//					int offset;
//					if (afterFiscalYearStart)
//							offset	= 1;
//					else
//							offset	= 0;
//					while (iter.hasNext()) {
//						MTEFProjection proj	= iter.next();
//						proj.setProjectionDate( AddFunding.getFYDate(offset++, year) );
//					}
				} else { // In case we add a projection
					mp = AddFunding.getMTEFProjection(request.getSession(), mtefProjections.size(), false, year+1 );
					mtefProjections.add(mp);							
				}
			}
			formBean.getFunding().setFundingMTEFProjections(mtefProjections);			
		}
		formBean.getFunding().setEvent(null);
		formBean.getFunding().setDupFunding(true);
		formBean.getFunding().setFirstSubmit(false);
		return mapping.findForward("forward");
	}
}

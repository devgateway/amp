package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.ApplicationSettings;
import org.digijava.module.aim.helper.CategoryConstants;
import org.digijava.module.aim.helper.CategoryManagerUtil;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.MTEFProjection;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;

public class AddMTEFProjection extends Action{

	private List<MTEFProjection> mtefProjections=null;
	
	private String event;
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		EditActivityForm formBean = (EditActivityForm) form;
		formBean.setReset(false);
		event = formBean.getEvent();
		TeamMember teamMember = (TeamMember) session.getAttribute("currentMember");
		
		String perspCode = null;
		if (formBean.isDonorFlag()) perspCode = Constants.DONOR;
		else perspCode = Constants.MOFED;
		
		formBean.setProjections(CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.MTEF_PROJECTION_KEY, false));
		
		/*String currCode = Constants.DEFAULT_CURRENCY;
		if (teamMember.getAppSettings() != null) {
			ApplicationSettings appSettings = teamMember.getAppSettings();
			if (appSettings.getCurrencyId() != null) {
				currCode = CurrencyUtil.getCurrency(appSettings.getCurrencyId()).getCurrencyCode();
			}
		}*/		
		long index = formBean.getTransIndexId();
		String subEvent = event.substring(0,3);
		MTEFProjection mp = null;
		if (subEvent.equalsIgnoreCase("del") || subEvent.equalsIgnoreCase("add")) {
			if (formBean.getFundingMTEFProjections() == null) {
				mtefProjections = new ArrayList();
				mp = AddFunding.getMTEFProjection(request.getSession(), 0);
				mtefProjections.add(mp);		
			} else {
				mtefProjections = formBean.getFundingMTEFProjections();
				if (subEvent.equals("del")) {
					Iterator<MTEFProjection> iter	= mtefProjections.iterator();
					int j							= 1;
					while (iter.hasNext()) {
						MTEFProjection proj	= iter.next();
						if (proj.getIndexId() == index) {
							iter.remove();
						}
						else {
							proj.setProjectionDate( AddFunding.getFYDate(j++) );
						}
					}
					MTEFProjection temp = new MTEFProjection();
					temp.setIndexId(index);
					mtefProjections.remove(temp);					
				} else {
					mp = AddFunding.getMTEFProjection(request.getSession(), mtefProjections.size() );
					mtefProjections.add(mp);							
				}
			}
			formBean.setFundingMTEFProjections(mtefProjections);			
		}
		formBean.setEvent(null);
		formBean.setDupFunding(true);
		formBean.setFirstSubmit(false);

		return mapping.findForward("forward");
		
	}
	
	/*private MTEFProjection getMTEFProjection(String currCode) {
		MTEFProjection mp = new MTEFProjection();
		mp.setCurrencyCode(currCode);
		
		mp.setIndex(mtefProjections.size());
		mp.setIndexId(System.currentTimeMillis());
		return mp;
	}*/
	
}

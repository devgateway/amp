package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.digijava.module.aim.form.IndicatorForm;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.dbentity.AmpIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.SectorUtil;

public class SelectCreateIndicators extends Action {
	private static Logger logger = Logger
			.getLogger(SelectCreateIndicators.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		 String search = request.getParameter("search");
		
		Collection nonDefaultInd = null;
		Collection activityInd = null;
		Collection nonDefActInd = new ArrayList();
		boolean sameIndicator = false;
		
		HttpSession session = request.getSession();
		
		IndicatorForm indForm = (IndicatorForm) form;

		nonDefaultInd = IndicatorUtil.getAllNonDefaultIndicators();
		activityInd = IndicatorUtil.getActivityIndicatorsList(indForm
				.getActivityId());

		Iterator nonDefaultItr = nonDefaultInd.iterator();

		while (nonDefaultItr.hasNext()) {
			AmpIndicator tempNonDefaultInd = (AmpIndicator) nonDefaultItr
					.next();
			Iterator activityIndItr = activityInd.iterator();
			sameIndicator = false;
			while (activityIndItr.hasNext() && sameIndicator == false) {
				ActivityIndicator tempActInd = (ActivityIndicator) activityIndItr
						.next();

				if (tempNonDefaultInd.getIndicatorId().equals(
						tempActInd.getIndicatorId()))
					sameIndicator = true;
			}
			if (sameIndicator == false)
				nonDefActInd.add(tempNonDefaultInd);
		}
		
		if(search==null){
			Collection allSectors = SectorUtil.getAllParentSectors();
			indForm.setAllSectors(allSectors);
			indForm.setTempNumResults(10);
			indForm.setSectorName("");
			indForm.setSearchKey(null);
			indForm.setPagedCol(null);
		}
		if(search!=null){

	        String alpha = request.getParameter("alpha");
			Collection col = null;
			Collection colAlpha = null;
			Collection<AllActivities> coll = null;

			if (alpha == null || alpha.trim().length() == 0) {
				col = new ArrayList();
				indForm.setNumResults(indForm.getTempNumResults());
				
				if(indForm.getAction() == null){
					indForm.setAction("selected");
					
				}
				
				if (!indForm.getAction().equals("viewall")) {
					if (indForm.getSearchkey().trim().length() != 0) {
						
						col = IndicatorUtil.searchForindicator(indForm.getSearchkey());

					} else {

						col = IndicatorUtil.searchForindicator(indForm.getSectorName());
					
		  			 }
				} else if (indForm.getSearchkey().trim().length() != 0) {
					
					col = IndicatorUtil.searchForindicator(indForm.getSearchkey().trim());
				} else  {
					
					col = IndicatorUtil.getAmpIndicator();
				
				}
				if (col != null && col.size() > 0) {
					List temp = (List) col;
					
					col = (Collection) temp;

					if (indForm.getCurrentAlpha() != null) {
						indForm.setCurrentAlpha(null);
						//eaForm.setStartAlphaFlag(true);
					}
					indForm.setStartAlphaFlag(true);

					String[] alphaArray = new String[26];
					int i = 0;
					for(char c = 'A'; c <= 'Z'; c++) {
						Iterator itr = col.iterator();
						while(itr.hasNext()) {
							AmpIndicator org = (AmpIndicator) itr.next();
							if (org.getName().toUpperCase().indexOf(c) == 0) {
								alphaArray[i++] = String.valueOf(c);
								break;
							}
						}
					}
					indForm.setAlphaPages(alphaArray);
				} else {
					indForm.setAlphaPages(null);
				}

			} else {
				col = indForm.getCols();
				indForm.setCurrentAlpha(alpha);
				if (!alpha.equals("viewAll")) {
					indForm.setStartAlphaFlag(false);
					colAlpha = new ArrayList();
					Iterator itr = col.iterator();
					while(itr.hasNext()) {
						AmpIndicator org = (AmpIndicator) itr.next();
						if (org.getName().toUpperCase().startsWith(alpha)) {
							colAlpha.add(org);
						}
					}
					indForm.setColsAlpha(colAlpha);
				}
				else
					indForm.setStartAlphaFlag(true);
			}

	   //     OrgProjectId hvOrgs[] = eaForm.getSelectedOrganizations();

	        Collection newCol = new ArrayList();
	        newCol=col;
	    
			int stIndex = 1;
			int edIndex = indForm.getNumResults();
			Vector vect = new Vector();
			int numPages;

			if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
				if (edIndex > newCol.size()) {
					edIndex = newCol.size();
				}
				vect.addAll(newCol);
				numPages = newCol.size() / indForm.getNumResults();
				numPages += (newCol.size() % indForm.getNumResults() != 0) ? 1 : 0;
			} else {
				if (edIndex > colAlpha.size()) {
					edIndex = colAlpha.size();
				}
				vect.addAll(colAlpha);
				numPages = colAlpha.size() / indForm.getNumResults();
				numPages += (colAlpha.size() % indForm.getNumResults() != 0) ? 1 : 0;
			}


	        Collection tempCol = new ArrayList();
			for (int i = (stIndex - 1); i < edIndex; i++) {
				tempCol.add(vect.get(i));
			}

			Collection pages = null;

			if (numPages > 1) {
				pages = new ArrayList();
				for (int i = 0; i < numPages; i++) {
					Integer pageNum = new Integer(i + 1);
					pages.add(pageNum);
				}
			}

			indForm.setCols(newCol);
			indForm.setPagedCol(tempCol);
			
			indForm.setPages(pages);
			indForm.setCurrentPage(new Integer(1));
			indForm.setAction("");
			
			    session.setAttribute("forStep9","true");
				return mapping.findForward("toAdditionOfIndiForStep9");
				
			
		}
		
		indForm.setNondefaultindicators(nonDefActInd);
		indForm.setActivityId(indForm.getActivityId());
		if("true".equalsIgnoreCase(request.getParameter("addIndicatorForStep9"))){
			session.setAttribute("forStep9","true");
			return mapping.findForward("toAdditionOfIndiForStep9");
		}
		return mapping.findForward("forward");
	}
}
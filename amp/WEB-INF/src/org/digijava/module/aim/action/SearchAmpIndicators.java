package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.form.ThemeForm;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;


import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.digijava.module.aim.helper.AllActivities;
import org.digijava.module.aim.helper.AllMEIndicators;
import org.digijava.module.aim.helper.IndicatorsBean;
import org.digijava.module.aim.helper.OrgProjectId;
import java.util.HashSet;
import java.util.Arrays;

public class SearchAmpIndicators extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		ThemeForm eaForm = (ThemeForm) form;  
		eaForm.setReset(false);
		eaForm.setIndPopupReset(false);

        String alpha = request.getParameter("alpha");
		Collection col = null;
		Collection colAlpha = null;
		Collection<AllActivities> coll = null;

		if (alpha == null || alpha.trim().length() == 0) {
			col = new ArrayList();
			eaForm.setNumResults(eaForm.getTempNumResults());

			if (!eaForm.getSectorName().equalsIgnoreCase("all")) {
				if (eaForm.getKeyword().trim().length() != 0) {
					// serach for indicators based on the keyword and the
					// organisation type
					col = ProgramUtil.searchForindicators(eaForm.getKeyword().trim(),eaForm.getSectorName());
				} else {
					// search for indicators based on organisation type only
					col = ProgramUtil.searchForindicator(eaForm.getSectorName());
				}
			} else if (eaForm.getKeyword().trim().length() != 0) {
				// search based on the given keyword only.
				col = ProgramUtil.searchForindicator(eaForm.getKeyword().trim());
			} else  {
				// get all indicators since keyword field is blank and ind type field has 'ALL'.
				col = ProgramUtil.getAmpThemeIndicators();
				
				/**
		         * Returns All project indicator.
		         */		
				
				coll =  MEIndicatorsUtil.getAllActivityIds();
				 
				 for(Iterator itr = coll.iterator(); itr.hasNext(); ) {
					 	AllActivities act = (AllActivities) itr.next();
					 		Collection<AllMEIndicators> prjIndsCol = act.getAllMEIndicators();
					 			 if(prjIndsCol != null){
					 				List<AllMEIndicators> prjIndsList = new ArrayList<AllMEIndicators>(prjIndsCol);
					 				 for(Iterator indItr = prjIndsList.iterator(); indItr.hasNext(); ) {
					 					AllMEIndicators tInd = (AllMEIndicators) indItr.next();
					 					AmpThemeIndicators ind = new AmpThemeIndicators();
					 					
					 					ind.setAmpThemeIndId(tInd.getAmpMEIndId());
					 					ind.setName(tInd.getName());
					 					
					 					col.add(ind);
					 				 }
									 
							 }
					  }
				 
			}
			if (col != null && col.size() > 0) {
				List temp = (List) col;
				
				col = (Collection) temp;

				if (eaForm.getCurrentAlpha() != null) {
					eaForm.setCurrentAlpha(null);
					//eaForm.setStartAlphaFlag(true);
				}
				eaForm.setStartAlphaFlag(true);

				String[] alphaArray = new String[26];
				int i = 0;
				for(char c = 'A'; c <= 'Z'; c++) {
					Iterator itr = col.iterator();
					while(itr.hasNext()) {
						AmpThemeIndicators org = (AmpThemeIndicators) itr.next();
						if (org.getName().toUpperCase().indexOf(c) == 0) {
							alphaArray[i++] = String.valueOf(c);
							break;
						}
					}
				}
				eaForm.setAlphaPages(alphaArray);
			} else {
				eaForm.setAlphaPages(null);
			}

		} else {
			col = eaForm.getCols();
			eaForm.setCurrentAlpha(alpha);
			if (!alpha.equals("viewAll")) {
				eaForm.setStartAlphaFlag(false);
				colAlpha = new ArrayList();
				Iterator itr = col.iterator();
				while(itr.hasNext()) {
					AmpThemeIndicators org = (AmpThemeIndicators) itr.next();
					if (org.getName().toUpperCase().startsWith(alpha)) {
						colAlpha.add(org);
					}
				}
				eaForm.setColsAlpha(colAlpha);
			}
			else
				eaForm.setStartAlphaFlag(true);
		}

   //     OrgProjectId hvOrgs[] = eaForm.getSelectedOrganizations();

        Collection newCol = new ArrayList();
        newCol=col;
    
		int stIndex = 1;
		int edIndex = eaForm.getNumResults();
		Vector vect = new Vector();
		int numPages;

		if (alpha == null || alpha.trim().length() == 0 || alpha.equals("viewAll")) {
			if (edIndex > newCol.size()) {
				edIndex = newCol.size();
			}
			vect.addAll(newCol);
			numPages = newCol.size() / eaForm.getNumResults();
			numPages += (newCol.size() % eaForm.getNumResults() != 0) ? 1 : 0;
		} else {
			if (edIndex > colAlpha.size()) {
				edIndex = colAlpha.size();
			}
			vect.addAll(colAlpha);
			numPages = colAlpha.size() / eaForm.getNumResults();
			numPages += (colAlpha.size() % eaForm.getNumResults() != 0) ? 1 : 0;
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

        eaForm.setCols(newCol);
		eaForm.setPagedCol(tempCol);
		
		eaForm.setPages(pages);
		eaForm.setCurrentPage(new Integer(1));
	
    	return mapping.findForward("forward");
	}
}


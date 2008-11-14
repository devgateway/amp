package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.SectorUtil;


public class SearchSectorsForInd extends Action {

	private static Logger logger = Logger.getLogger(SelectLocation.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		eaForm.setReset(false);
		//eaForm.setOrgPopupReset(false);

		eaForm.setNumResults(eaForm.getTempNumResults());

		if(eaForm.getKeyword()!=null) request.getSession().setAttribute("keywordForSectors",eaForm.getKeyword());
			else eaForm.setKeyword((String)request.getSession().getAttribute("keywordForSectors"));

		
		
		Collection col = new ArrayList();
		eaForm.setNumResults(eaForm.getTempNumResults());
		int page = 0;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		eaForm.setCurrentPage(new Integer(page));

		if(eaForm.getKeyword()!=null)
		if (eaForm.getKeyword().trim().length() != 0) {
			// search based on the given keyword only.
			
		col=(SectorUtil.searchForSector(eaForm.getKeyword().trim()));
		
		int stIndex = 1;
		int edIndex = eaForm.getNumResults();

		if (eaForm.getNumResults() == 0 || eaForm.isSectorReset() == true) {
			eaForm.setTempNumResults(10);
		} else {
			stIndex = ((page - 1) * eaForm.getNumResults()) + 1;
			edIndex = page * eaForm.getNumResults();
		}
		
		Vector vect = new Vector();
		int numPages=0;
		
			if (edIndex > col.size()) {
				edIndex = col.size();
			}
			vect.addAll(col);
			if(eaForm.getNumResults() > 0) {
				numPages = col.size() / eaForm.getNumResults();
				numPages += (col.size() % eaForm.getNumResults() != 0) ? 1 : 0;
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

		eaForm.setSearchedSectors(col);

		eaForm.setCols(col);
		eaForm.setPagedCol(tempCol);
		eaForm.setPages(pages);
		eaForm.setCurrentPage(new Integer(page));
			
//		eaForm.setSearchLocs(col);
//		eaForm.setPagedCol(tempCol);
//		eaForm.setPages(pages);
//		eaForm.setCurrentPage(new Integer(1));

//		eaForm.setSearchLocs(LocationUtil.searchForLocation(eaForm.getKeyword().trim(),implvl));
		
		} 
		else {
			//System.out.println("no input in keyword field.....");
		}

		return mapping.findForward("forward");
	}
}

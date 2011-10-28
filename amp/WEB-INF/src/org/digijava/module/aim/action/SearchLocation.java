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
import org.digijava.module.aim.util.LocationUtil;


public class SearchLocation extends Action {

	private static Logger logger = Logger.getLogger(SelectLocation.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		eaForm.setReset(false);
		//eaForm.setOrgPopupReset(false);

		eaForm.getLocation().setNumResults(eaForm.getLocation().getTempNumResults());
		//eaForm.setTempNumResults(10);
		////System.out.println("tempNumResults----->"+eaForm.getTempNumResults());
			
		if(eaForm.getLocation().getKeyword()!=null) request.getSession().setAttribute("keywordForLocation",eaForm.getLocation().getKeyword());
		else eaForm.getLocation().setKeyword((String)request.getSession().getAttribute("keywordForLocation"));
			
		Collection col = new ArrayList();
		//eaForm.setNumResults(eaForm.getTempNumResults());

		int page = 0;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		eaForm.getLocation().setCurrentPage(new Integer(page));

		int implvl=eaForm.getLocation().getImpLevelValue().intValue();
		if(eaForm.getLocation().getKeyword()!=null)
		
		if (eaForm.getLocation().getKeyword().trim().length() != 0) {
			// search based on the given keyword only.
			
		col=(LocationUtil.searchForLocation(eaForm.getLocation().getKeyword().trim(),implvl));

		int stIndex = 1;
		int edIndex = eaForm.getLocation().getNumResults();

		if (eaForm.getLocation().getNumResults() == 0 || eaForm.getLocation().isLocationReset() == true) {
			eaForm.getLocation().setTempNumResults(10);
		} else {
			stIndex = ((page - 1) * eaForm.getLocation().getTempNumResults()) + 1;
			edIndex = page * eaForm.getLocation().getTempNumResults();
		}
		////System.out.println("start->"+stIndex);
		Vector vect = new Vector();
		int numPages=0;
		
			if (edIndex > col.size()) {
				edIndex = col.size();
			}
			vect.addAll(col);
			if(eaForm.getLocation().getNumResults() > 0) {
				numPages = col.size() / eaForm.getLocation().getNumResults();
				numPages += (col.size() % eaForm.getLocation().getNumResults() != 0) ? 1 : 0;
			}
			
		Collection tempCol = new ArrayList();
		for (int i = (stIndex - 1); i < edIndex; i++) {
			tempCol.add(vect.get(i));
		}

		Collection<Integer> pages = null;

		if (numPages > 1) {
			pages = new ArrayList();
			for (int i = 0; i < numPages; i++) {
				Integer pageNum = new Integer(i + 1);
				pages.add(pageNum);
			}
		}
		
		eaForm.getLocation().setSearchLocs(col);

		eaForm.getLocation().setCols(col);
		eaForm.getLocation(). setPagedCol(tempCol);
		eaForm.getLocation().setPages(pages);
		eaForm.getLocation().setCurrentPage(new Integer(page));
		
		} 
		
		return mapping.findForward("forward");
	}
}

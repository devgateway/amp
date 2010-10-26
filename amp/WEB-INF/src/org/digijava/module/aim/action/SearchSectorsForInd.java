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

		eaForm.getSectors().setNumResults(eaForm.getSectors().getTempNumResults());

		if(eaForm.getSectors().getKeyword()!=null) request.getSession().setAttribute("keywordForSectors",eaForm.getSectors().getKeyword());
			else eaForm.getSectors().setKeyword((String)request.getSession().getAttribute("keywordForSectors"));

		
		
		Collection col = new ArrayList();
		eaForm.getSectors().setNumResults(eaForm.getSectors().getTempNumResults());
		int page = 0;

		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}
		
		eaForm.getSectors().setCurrentPage(new Integer(page));

		if(eaForm.getSectors().getKeyword()!=null)
		if (eaForm.getSectors().getKeyword().trim().length() != 0) {
			// search based on the given keyword only.
			// this class seems not to be used any more... need to review indicator code in the future...
		col=SectorUtil.searchForSector(eaForm.getSectors().getKeyword().trim(),null);
		
		int stIndex = 1;
		int edIndex = eaForm.getSectors().getNumResults();

		if (eaForm.getSectors().getNumResults() == 0 || eaForm.getSectors().isSectorReset() == true) {
			eaForm.getSectors().setTempNumResults(10);
		} else {
			stIndex = ((page - 1) * eaForm.getSectors().getNumResults()) + 1;
			edIndex = page * eaForm.getSectors().getNumResults();
		}
		
		Vector vect = new Vector();
		int numPages=0;
		
			if (edIndex > col.size()) {
				edIndex = col.size();
			}
			vect.addAll(col);
			if(eaForm.getSectors().getNumResults() > 0) {
				numPages = col.size() / eaForm.getSectors().getNumResults();
				numPages += (col.size() % eaForm.getSectors().getNumResults() != 0) ? 1 : 0;
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

		eaForm.getSectors().setSearchedSectors(col);

		eaForm.getSectors().setCols(col);
		eaForm.getSectors().setPagedCol(tempCol);
		eaForm.getSectors().setPages(pages);
		eaForm.getSectors().setCurrentPage(new Integer(page));
					} 
		else {
			//System.out.println("no input in keyword field.....");
		}

		return mapping.findForward("forward");
	}
}

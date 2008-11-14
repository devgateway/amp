package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.util.DbUtil;
@Deprecated
public class SelectOrganization extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		if (request.getParameter("orgSelReset") != null
				&& request.getParameter("orgSelReset").equals("false")) {
			eaForm.setOrgSelReset(false);
		} else {
			eaForm.setOrgSelReset(true);
			eaForm.setPagedCol(null);			
			eaForm.reset(mapping, request);
		}
		
		if(request.getParameter("changeOrganisation") == null)
			eaForm.setPrevOrg(null);
			
		eaForm.setPagesToShow(10);
		int page = 0;
		if (request.getParameter("page") == null) {
			page = 1;
		} else {
			page = Integer.parseInt(request.getParameter("page"));
		}

		if (eaForm.getNumResults() == 0 || eaForm.isOrgSelReset() == true) {
			eaForm.setTempNumResults(10);
			eaForm.setOrgTypes(DbUtil.getAllOrgTypes());
			if (eaForm.getAlphaPages() != null)
				eaForm.setAlphaPages(null);
		} else {
			int stIndex = ((page - 1) * eaForm.getNumResults()) + 1;
			int edIndex = page * eaForm.getNumResults();
			
			if (eaForm.getStartAlphaFlag()) {
				if (edIndex > eaForm.getCols().size()) {
					edIndex = eaForm.getCols().size();
				}
			}
			else {
				if (edIndex > eaForm.getColsAlpha().size()) {
					edIndex = eaForm.getColsAlpha().size();
				}
			}

			Vector vect = new Vector();
			
			if (eaForm.getStartAlphaFlag())
				vect.addAll(eaForm.getCols());
			else
				vect.addAll(eaForm.getColsAlpha());

			Collection tempCol = new ArrayList();

			for (int i = (stIndex - 1); i < edIndex; i++) {
				tempCol.add(vect.get(i));
			}

			eaForm.setPagedCol(tempCol);
			eaForm.setCurrentPage(new Integer(page));
		}

		return mapping.findForward("forward");
	}
}

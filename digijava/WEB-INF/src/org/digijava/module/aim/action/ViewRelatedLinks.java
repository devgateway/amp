/***
 * ViewRelatedLinks.java
 * @author Priyajith 
 */

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.RelatedLinksForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;

public class ViewRelatedLinks 
extends Action {
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		RelatedLinksForm rlForm = (RelatedLinksForm) form;
		
		boolean permitted = false;
		HttpSession session = request.getSession();
		if (session.getAttribute("ampAdmin") != null) {
			String key = (String) session.getAttribute("ampAdmin");
			if (key.equalsIgnoreCase("yes")) {
				TeamMember tmem = (TeamMember) session.getAttribute("currentMember");
				if (tmem != null && tmem.getTeamId() != null) {
					permitted = true;
				}
			} else {
				if (session.getAttribute("teamLeadFlag") != null) {
					key = (String) session.getAttribute("teamLeadFlag");
					if (key.equalsIgnoreCase("true")) {
						permitted = true;	
					}
				}
			}
		}
		if (!permitted) {
			return mapping.findForward("index");
		}

		int page = 0;
		int numRecords = Constants.NUM_RECORDS;
		Long teamId = null;

		if (session.getAttribute("currentMember") != null) {
			TeamMember tm = (TeamMember) session.getAttribute("currentMember");
			teamId = tm.getTeamId();
			numRecords = tm.getAppSettings().getDefRecsPerPage();
		}
		
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;
		}
		
		Collection pagedCol = rlForm.getAllDocuments();

		if (pagedCol == null || pagedCol.size() == 0) {
			pagedCol = new ArrayList();
			pagedCol =DbUtil.getAllDocuments(teamId);
			rlForm.setAllDocuments(pagedCol);
		}

		/*
		int stIndex = ((page - 1) * numRecords) + 1;
		int edIndex = page * numRecords;
		if (edIndex > col.size()) {
			edIndex = col.size();
		}

		Vector vect = new Vector();
		vect.addAll(col);

		Collection pagedCol = new ArrayList();
		for (int i = (stIndex - 1); i < edIndex; i++) {
			pagedCol.add(vect.get(i));
		}

		int numPages = col.size() / numRecords;
		numPages += (col.size() % numRecords != 0) ? 1 : 0;

		Collection pages = rlForm.getPages();
		if (pages == null || pages.size() == 0) {
			pages = new ArrayList();
			if (numPages > 1) {
				pages = new ArrayList();
				for (int i = 0; i < numPages; i++) {
					Integer pageNum = new Integer(i + 1);
					pages.add(pageNum);
				}
			}
			rlForm.setPages(pages);			
		}
		
		rlForm.setCurrentPage(new Integer(page));
		*/
		rlForm.setRelatedLinks(pagedCol);
		return mapping.findForward("forward");
	}
}
 

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.cms.dbentity.CMSContentItem;

public class RemoveSelDocuments extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;
		
		Collection prevDocs = null;
		Collection newDocs = new ArrayList();
		long selDocs[] = null;
		
		if (eaForm.getDocFileOrLink().equals("file")) {
			prevDocs = eaForm.getDocumentList();
			selDocs = eaForm.getSelDocs();
		} else {
			prevDocs = eaForm.getLinksList();
			selDocs = eaForm.getSelLinks();
		}
		
		Iterator itr = prevDocs.iterator();
		while (itr.hasNext()) {
			CMSContentItem cmsItem = (CMSContentItem) itr.next();
			boolean flag = false;
			for (int i = 0;i < selDocs.length;i ++) {
				if (cmsItem.getId() == selDocs[i]) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				newDocs.add(cmsItem);
			}
		}

		if (eaForm.getDocFileOrLink().equals("file")) {
			eaForm.setSelDocs(null);
			eaForm.setDocumentList(newDocs);
		} else {
			eaForm.setSelLinks(null);
			eaForm.setLinksList(newDocs);
		}
		eaForm.setStep("5");

		return mapping.findForward("forward");
	}
}

package org.digijava.module.aim.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.EditActivityForm;
import org.digijava.module.aim.helper.RelatedLinks;
import org.digijava.module.aim.util.DocumentUtil;

public class RemoveSelDocuments extends Action {

	private static Logger logger = Logger.getLogger(RemoveSelDocuments.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response)
			throws java.lang.Exception {

		EditActivityForm eaForm = (EditActivityForm) form;

		Collection prevDocs = null;
		Collection newDocs = new ArrayList();
		long selDocs[] = null;

        if (eaForm.getDocFileOrLink().equals("document")) {
            Site currentSite = RequestUtils.getSite(request);
            if (eaForm.getSelManagedDocs() != null) {
                for(int i = 0; i < eaForm.getSelManagedDocs().length; i++) {
                    String managedDocId = eaForm.getSelManagedDocs()[i];
                    DocumentUtil.removeDocument(currentSite, eaForm.getDocumentSpace(), managedDocId);
                }
                eaForm.setManagedDocumentList(DocumentUtil.getDocumentsForSpace(currentSite, eaForm.getDocumentSpace()));
                eaForm.setSelManagedDocs(null);
            }
            eaForm.setStep("6");

            return mapping.findForward("forward");

        }

		if (eaForm.getDocFileOrLink().equals("file")) {
			prevDocs = eaForm.getDocumentList();
			selDocs = eaForm.getSelDocs();
		} else {
			prevDocs = eaForm.getLinksList();
			selDocs = eaForm.getSelLinks();
		}

		Iterator itr = prevDocs.iterator();
		while (itr.hasNext()) {
			RelatedLinks rl = (RelatedLinks) itr.next();
			boolean flag = false;
			for (int i = 0;i < selDocs.length;i ++) {
				if (rl.getRelLink().getId() == selDocs[i]) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				newDocs.add(rl);
			}
		}

		if (eaForm.getDocFileOrLink().equals("file")) {
			eaForm.setSelDocs(null);
			eaForm.setDocumentList(newDocs);
		} else {
			eaForm.setSelLinks(null);
			eaForm.setLinksList(newDocs);
		}
		eaForm.setStep("6");

		return mapping.findForward("forward");
	}
}

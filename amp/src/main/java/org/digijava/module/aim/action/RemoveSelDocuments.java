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
import org.digijava.module.aim.util.DocumentUtil;

public class RemoveSelDocuments extends Action {

    private static Logger logger = Logger.getLogger(RemoveSelDocuments.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        throw new RuntimeException("unimplemented!");
//      EditActivityForm eaForm = (EditActivityForm) form;
//
//      Collection prevDocs = null;
//      Collection newDocs = new ArrayList();
//      long selDocs[] = null;
//
//        if (eaForm.getDocuments().getDocFileOrLink().equals("document")) {
//            Site currentSite = RequestUtils.getSite(request);
//            if (eaForm.getDocuments().getSelManagedDocs() != null) {
//                for(int i = 0; i < eaForm.getDocuments().getSelManagedDocs().length; i++) {
//                    String managedDocId = eaForm.getDocuments().getSelManagedDocs()[i];
//                    DocumentUtil.removeDocument(currentSite, eaForm.getDocuments().getDocumentSpace(), managedDocId);
//                }
//                eaForm.getDocuments().setManagedDocumentList(DocumentUtil.getDocumentsForSpace(currentSite, eaForm.getDocuments().getDocumentSpace()));
//                eaForm.getDocuments().setSelManagedDocs(null);
//            }
//            eaForm.setStep("6");
//
//            return mapping.findForward("forward");
//
//        }
//
//      if (eaForm.getDocuments().getDocFileOrLink().equals("file")) {
//          prevDocs = eaForm.getDocuments().getDocumentList();
//          selDocs = eaForm.getDocuments().getSelDocs();
//      } else {
//          prevDocs = eaForm.getDocuments().getLinksList();
//          selDocs = eaForm.getDocuments().getSelLinks();
//      }
//
//      Iterator itr = prevDocs.iterator();
//      while (itr.hasNext()) {
//          RelatedLinks rl = (RelatedLinks) itr.next();
//          boolean flag = false;
//          for (int i = 0;i < selDocs.length;i ++) {
//              if (rl.getRelLink().getId() == selDocs[i]) {
//                  flag = true;
//                  break;
//              }
//          }
//          if (!flag) {
//              newDocs.add(rl);
//          }
//      }
//
//      if (eaForm.getDocuments().getDocFileOrLink().equals("file")) {
//          eaForm.getDocuments().setSelDocs(null);
//          eaForm.getDocuments().setDocumentList(newDocs);
//      } else {
//          eaForm.getDocuments().setSelLinks(null);
//          eaForm.getDocuments().setLinksList(newDocs);
//      }
//      eaForm.setStep("6");
//
//      return mapping.findForward("forward");
    }
}

/**
 * DocumentSelected.java
 * @author Priyajith
 */

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DocumentSelected extends Action {

    private static Logger logger = Logger.getLogger(DocumentSelected.class);

    private static final String LINK_START = "http://";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response) throws java.lang.Exception {

        throw new RuntimeException("unimplemented!");
//      EditActivityForm eaForm = (EditActivityForm) form;
//
//      HttpSession session = request.getSession();
//      TeamMember tm = (TeamMember) session.getAttribute("currentMember");
//
//      if (eaForm.getDocuments().getDocFileOrLink().equals("file") || eaForm.getDocuments().getDocFileOrLink().equals("document")) {
//          if (eaForm.getDocuments().getDocTitle() == null || eaForm.getDocuments().getDocTitle().trim().length() == 0 ||
//                  eaForm.getDocuments().getDocFile() == null) {
//              return mapping.findForward("forward");
//          }
//      } else {
//          if (eaForm.getDocuments().getDocTitle() == null || eaForm.getDocuments().getDocTitle().trim().length() == 0 ||
//                  eaForm.getDocuments().getDocWebResource() == null || eaForm.getDocuments().getDocWebResource().trim().length() == 0) {
//              return mapping.findForward("forward");
//          }
//      }
//
//        if (DocumentUtil.isDMEnabled()) {
//            if(eaForm.getDocuments().getDocFileOrLink().equals("document")) {
//                FormFile formFile = eaForm.getDocuments().getDocFile();
//                Site currentSite = RequestUtils.getSite(request);
//                String docType                    = null;
//                AmpCategoryValue docTypeValue = CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getDocuments().getDocType());
//                if (docTypeValue != null) {
//                  docType = docTypeValue.getValue();
//                }
//                
//                DocumentUtil.addDocument(currentSite, eaForm.getDocuments().getDocumentSpace(), formFile.getFileName(), eaForm.getDocuments().getDocTitle(), eaForm.getDocuments().getDocDescription(), eaForm.getDocuments().getDocDate(), docType,formFile.getFileData());
//                eaForm.getDocuments().setManagedDocumentList(DocumentUtil.getDocumentsForSpace(currentSite, eaForm.getDocuments().getDocumentSpace()));
//
//                return mapping.findForward("forward");
//            }
//        }
//      CMSContentItem cmsItem = new CMSContentItem();
//
//      cmsItem.setTitle(eaForm.getDocuments().getDocTitle());
//      if (eaForm.getDocuments().getDocDescription() == null ||
//              eaForm.getDocuments().getDocDescription().trim().length() == 0) {
//          cmsItem.setDescription(" ");
//      } else {
//          cmsItem.setDescription(eaForm.getDocuments().getDocDescription());
//      }
//      
//      if (eaForm.getDocuments().getDocComment() != null && eaForm.getDocuments().getDocComment().trim().length() != 0) 
//          cmsItem.setDocComment( eaForm.getDocuments().getDocComment() );
//      else
//          cmsItem.setDocComment("");
//      
//      if (eaForm.getDocuments().getDocDate() == null ||
//              eaForm.getDocuments().getDocDate().trim().length() == 0) {
//          cmsItem.setDate(" ");
//      } else {
//          cmsItem.setDate(eaForm.getDocuments().getDocDate());
//      }
//      if(!eaForm.getDocuments().getDocFileOrLink().equals("link")){
//          if(eaForm.getDocuments().getDocType() == null) {
//              cmsItem.setDocType(null);
//          }
//          else {
//              cmsItem.setDocType( CategoryManagerUtil.getAmpCategoryValueFromDb(eaForm.getDocuments().getDocType()) );
//          }
//      }       
//      
//      if ( eaForm.getDocuments().getDocLang() == null ) {
//          cmsItem.setDocLanguage( null );
//      }
//      else
//          cmsItem.setDocLanguage( CategoryManagerUtil.getAmpCategoryValueFromDb( eaForm.getDocuments().getDocLang() ) );
//
//      if (eaForm.getDocuments().getDocFileOrLink().equals("file")) {
//          cmsItem.setIsFile(true);
//          FormFile formFile = eaForm.getDocuments().getDocFile();
//          if (formFile != null) {
//              cmsItem.setFileName(formFile.getFileName());
//              cmsItem.setContentType(formFile.getContentType());
//              if (formFile.getFileSize() != 0) {
//                  cmsItem.setFile(formFile.getFileData());
//              } else {
//                  byte[] temp = {0};
//                  cmsItem.setFile(temp);
//              }
//          }
//      } else {
//          cmsItem.setIsFile(false);
//          String url = eaForm.getDocuments().getDocWebResource();
//          if (url.length() > LINK_START.length()) {
//              String temp = url.substring(0,LINK_START.length());
//              if (!temp.equalsIgnoreCase(LINK_START)) {
//                  url = LINK_START + url;
//              }
//          } else {
//              url = LINK_START + url;
//          }
//
//          cmsItem.setUrl(url);
//          byte[] temp = {0};
//          cmsItem.setFile(temp);
//      }
//
//      cmsItem.setId(System.currentTimeMillis());
//      RelatedLinks rl = new RelatedLinks();
//      rl.setShowInHomePage(eaForm.getDocuments().isShowInHomePage());
//      rl.setRelLink(cmsItem);
//      rl.setMember(org.digijava.module.aim.util.TeamMemberUtil.getAmpTeamMember(tm.getMemberId()));
//
//
//      if (cmsItem.getIsFile()) {
//          if (eaForm.getDocuments().getDocumentList() == null) {
//              eaForm.getDocuments().setDocumentList(new ArrayList());
//          }
//          eaForm.getDocuments().getDocumentList().add(rl);
//      } else {
//          if (eaForm.getDocuments().getLinksList() == null) {
//              eaForm.getDocuments().setLinksList(new ArrayList());
//          }
//          eaForm.getDocuments().getLinksList().add(rl);
//      }
//      return mapping.findForward("forward");
    }
}

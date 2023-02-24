package org.digijava.module.aim.action ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.form.OrgTypeManagerForm;
import org.digijava.module.aim.util.DbUtil;

public class OrgTypeManager extends Action {

          private static final Logger logger = Logger.getLogger(OrgTypeManager.class);

          public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {

                     HttpSession session = request.getSession();
                     if (session.getAttribute("ampAdmin") == null) {
                                return mapping.findForward("index");
                     } else {
                                String str = (String)session.getAttribute("ampAdmin");
                                if (str.equals("no")) {
                                          return mapping.findForward("index");
                                }
                     }                   

                     final int NUM_RECORDS = 10;

                     Collection org = new ArrayList();
                     OrgTypeManagerForm orgForm = (OrgTypeManagerForm) form;
                     int page = 0;
                     
                     logger.debug("In organisation type manager action");
                    
                     
                     if (request.getParameter("page") == null) {
                                page = 1;
                     } else {
                            
                        page = Integer.parseInt(request.getParameter("page"));
                     }
                     orgForm.setCurrentPage(page);
                     

                     Collection orgTypeColl = (Collection) session.getAttribute("ampOrgType");
                     if (orgTypeColl == null) {
                         orgTypeColl = DbUtil.getAllOrgTypes();
                         session.setAttribute("ampOrgType", orgTypeColl);
                     }
                     
                     int numPages = orgTypeColl.size() / NUM_RECORDS;
                     numPages += (orgTypeColl.size() % NUM_RECORDS != 0) ? 1 : 0;
                     

                     /*
                      * check whether the numPages is less than the page . if yes return error.
                      */
                     
                     int stIndex = ((page - 1) * NUM_RECORDS) + 1;
                     int edIndex = page * NUM_RECORDS;
                     if (edIndex > orgTypeColl.size()) {
                                edIndex = orgTypeColl.size();
                     }

                     Vector vect = new Vector();
                     vect.addAll(orgTypeColl);
                     
                     for (int i = (stIndex-1); i < edIndex; i++) {
                        org.add(vect.get(i));
                     }
                     
                     Collection pages = null;
                     
                     if (numPages > 1) {
                                pages = new ArrayList();
                                for (int i = 0;i < numPages;i ++) {
                                          Integer pageNum = new Integer(i+1);
                                          pages.add(pageNum);
                                }
                     }
                     
                     orgForm.setOrganisation(org);
                     orgForm.setPages(pages);
                     orgForm.setPagesSize(numPages);
                     
                    
                     logger.debug("Organisation Type manager returning");
                     return mapping.findForward("forward");
          }
}

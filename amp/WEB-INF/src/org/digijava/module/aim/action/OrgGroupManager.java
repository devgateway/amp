package org.digijava.module.aim.action ;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.form.OrgGroupManagerForm;
import javax.servlet.http.*;
import java.util.*;

public class OrgGroupManager extends Action {

		  private static Logger logger = Logger.getLogger(OrgGroupManager.class);

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
					 OrgGroupManagerForm orgForm = (OrgGroupManagerForm) form;
					 int page = 0;
					 
					 logger.debug("In organisation group manager action");
					 
					 if (request.getParameter("page") == null) {
								page = 1;
					 } else {
								/*
								 * check whether the page is a valid integer
								 */
								page = Integer.parseInt(request.getParameter("page"));
					 }

					 Collection ampOrg = (Collection)session.getAttribute("ampOrgGrp");
					 if (ampOrg == null) {
					 	ampOrg = DbUtil.getAllOrganisationGroup();
						session.setAttribute("ampOrgGrp",ampOrg);
					 }
					 
					 int numPages = ampOrg.size() / NUM_RECORDS;
					 numPages += (ampOrg.size() % NUM_RECORDS != 0) ? 1 : 0;

					 /*
					  * check whether the numPages is less than the page . if yes return error.
					  */
					 
					 int stIndex = ((page - 1) * NUM_RECORDS) + 1;
					 int edIndex = page * NUM_RECORDS;
					 if (edIndex > ampOrg.size()) {
								edIndex = ampOrg.size();
					 }

					 Vector vect = new Vector();
					 vect.addAll(ampOrg);
					 
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
					 orgForm.setCurrentPage(new Integer(page));
					
					 logger.debug("Organisation Group manager returning");
					 return mapping.findForward("forward");
		  }
}

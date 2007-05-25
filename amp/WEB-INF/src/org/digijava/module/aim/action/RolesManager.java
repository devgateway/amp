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
import org.digijava.module.aim.form.RolesForm;
import org.digijava.module.aim.util.TeamMemberUtil;

public class RolesManager extends Action {

		  private static Logger logger = Logger.getLogger(RolesManager.class);

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
					 
					 Collection roles = new ArrayList();
					 RolesForm rForm = (RolesForm) form;
					 int page = 0;
					 
					 logger.debug("In role manager");
					 
					 if (request.getParameter("page") == null) {
								page = 1;
					 } else {
								/*
								 * check whether the page is a valid integer
								 */
								page = Integer.parseInt(request.getParameter("page"));
					 }

					 Collection ampRoles = (Collection)session.getAttribute("ampRoles");
					 if (ampRoles == null) {
								ampRoles = TeamMemberUtil.getAllTeamMemberRoles();
								session.setAttribute("ampRoles",ampRoles);
					 }
					
					 int numPages = ampRoles.size() / NUM_RECORDS;
					 numPages += (ampRoles.size() % NUM_RECORDS != 0) ? 1 : 0;

					 /*
					  * check whether the numPages is less than the page . if yes return error.
					  */
					 
					 int stIndex = ((page - 1) * NUM_RECORDS) + 1;
					 int edIndex = page * NUM_RECORDS;
					 if (edIndex > ampRoles.size()) {
								edIndex = ampRoles.size();
					 }

					 Vector vect = new Vector();
					 vect.addAll(ampRoles);
					 
					 for (int i = (stIndex-1);i < edIndex;i ++) {
								roles.add(vect.get(i));
					 }

					 Collection pages = null;

					 if (numPages > 1) {
								pages = new ArrayList();
								for (int i = 0;i < numPages;i ++) {
										  Integer pageNum = new Integer(i+1);
										  pages.add(pageNum);
								}
					 }
					 rForm.setRoles(roles);
					 rForm.setPages(pages);
				
					 logger.debug("Role manager returning");
					 return mapping.findForward("forward");
		  }
}


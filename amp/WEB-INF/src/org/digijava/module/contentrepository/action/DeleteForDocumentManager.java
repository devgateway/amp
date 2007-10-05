/**
 * 
 */
package org.digijava.module.contentrepository.action;

import java.io.PrintWriter;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

/**
 * @author Alex Gartner
 *
 */
public class DeleteForDocumentManager extends Action {
		public ActionForward execute(ActionMapping mapping, ActionForm form,
				javax.servlet.http.HttpServletRequest request,
				javax.servlet.http.HttpServletResponse response)
				throws java.lang.Exception {
			String nodeUUID		= request.getParameter("uuid");
			if (nodeUUID == null)
				throw new Exception("Parameter uuid is required.");
			
			Boolean hasDeleteRigths		= DocumentManagerUtil.deleteDocumentWithRightsChecking(nodeUUID, request);
			PrintWriter out				= response.getWriter();
			
			if (hasDeleteRigths == null) {
				out.println("<font color='red'>Error. Document Manager is unable to delete the specified document</font>");
			}
			else {
					if ( !hasDeleteRigths.booleanValue() ) {
						out.println("<font color='red'>Error. You do not have the right to delete the specified document</font>");
					}
					else {
						out.println("<div id='successfullDiv'>Document deleted successfully</div>");
					}
			}
			return null;
		}

}

/**DigifeedExportAction.java
 *  * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.action;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.digifeed.core.FeedControl;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 23, 2005
 * Receives an XML export request and replies with the XML stream
 * Mandatory parameter: feed=feedId
 */
public class DigifeedExportAction extends Action	 {
	private static Logger logger = Logger.getLogger(DigifeedExportAction.class);
	

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
		
	
		Map params=request.getParameterMap();		
		response.setContentType("text/xml");
		response.setHeader("Content-Disposition",
        "inline; filename=output.xml");
		
		
		FeedControl.fetchXML(params, request, response);
		return null;
		}
}

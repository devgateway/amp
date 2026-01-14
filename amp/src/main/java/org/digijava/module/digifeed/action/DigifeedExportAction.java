/**DigifeedExportAction.java
 *  * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.digifeed.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.digifeed.core.FeedControl;

import java.util.Map;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 23, 2005
 * Receives an XML export request and replies with the XML stream
 * Mandatory parameter: feed=feedId
 */
public class DigifeedExportAction extends Action     {
    private static Logger logger = Logger.getLogger(DigifeedExportAction.class);
    

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        
        Map params=request.getParameterMap();
        String contentType="text/xml";
        String fileName="output.xml";
        if(FeedControl.isGZipped(params)) {
            fileName+=".gz";
            contentType="application/x-gzip";
        }
            
        
        
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, fileName, true));
        
        
        
        FeedControl.fetchXML(params, request, response);
        return null;
        }
}

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.util.FeaturesUtil;

import java.io.OutputStream;

public class DownloadFileFromHome extends Action {
    
    private static Logger logger = Logger.getLogger(DownloadFileFromHome.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        String placeholder  = request.getParameter("placeholder"); 
        
        if (placeholder != null) {
            try {
                int temp = Integer.parseInt(placeholder);
                AmpHomeThumbnail thumbnail = FeaturesUtil.getAmpHomeThumbnail(temp);
                if(thumbnail!=null)
                {
                    byte[] file = thumbnail.getOptionalFile();  
                    if (file != null) {
                        OutputStream outServlet = response.getOutputStream();
                        response.setContentType(thumbnail.getOptionalFileContentType());
                        response.setHeader("Content-disposition",
                                ResponseUtil.encodeContentDispositionForDownload(request, thumbnail.getOptionalFileName()));
                        outServlet.write(file);
                        outServlet.close();
                    }
                }

            } catch (NumberFormatException nfe) {
                logger.error("Trying to parse " + placeholder + " to int");
            }
        }

        return null;
    }
    
}

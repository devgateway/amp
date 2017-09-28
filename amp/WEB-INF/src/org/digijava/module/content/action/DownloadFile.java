package org.digijava.module.content.action;

import java.io.OutputStream;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.dbentity.AmpContentItemThumbnail;
import org.digijava.module.content.util.DbUtil;

public class DownloadFile extends Action {
    
    private static Logger logger = Logger.getLogger(DownloadFile.class);
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
            throws java.lang.Exception {

        String index = request.getParameter("index"); 
        String pageCode = request.getParameter("pageCode");
        
        if (index != null && pageCode != null) {
            try {
                int intIndex = Integer.parseInt(index);
                TreeSet<AmpContentItemThumbnail> sorted;
                AmpContentItem contentItem;
                
                contentItem = DbUtil.getContentItemByPageCode(pageCode);
                sorted = new TreeSet<AmpContentItemThumbnail>(
                        contentItem.getContentThumbnails());
                //TODO: This is wrong, there should be a better way than retrieve all objects each time
                Object[] arrContentThumbnailItems = sorted.toArray();
                AmpContentItemThumbnail thumbnail = (AmpContentItemThumbnail) arrContentThumbnailItems[intIndex];

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
                logger.error("Trying to parse " + index + " to int");
            }
        }

        return null;
    }
    
}

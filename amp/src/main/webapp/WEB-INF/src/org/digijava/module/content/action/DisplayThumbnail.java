package org.digijava.module.content.action;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.content.util.*;
import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.dbentity.AmpContentItemThumbnail;
import org.digijava.module.content.form.ContentForm;

public class DisplayThumbnail extends Action {

    private static Logger logger = Logger.getLogger(DisplayThumbnail.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String index = request.getParameter("index");
        String pageCode = request.getParameter("pageCode");
        String labelText = request.getParameter("labelText");
        Boolean isAdmin = Boolean.parseBoolean(request.getParameter("isAdmin"));
        //ContentForm contentForm = (ContentForm) form;

        HttpSession session = request.getSession();
        if (index != null) {
            if (labelText != null && labelText.equals("true")) {
                response.setContentType("text/plain");
                try {
                    int intIndex = Integer.parseInt(index);
                    TreeSet<AmpContentItemThumbnail> sorted;
                    AmpContentItem contentItem;
                    
                    if (isAdmin) {
                        Set<AmpContentItemThumbnail> sessionSet = (Set<AmpContentItemThumbnail>) session
                                .getAttribute("contentThumbnails");
                        sorted = new TreeSet<AmpContentItemThumbnail>(
                                sessionSet);
                    } else {
                        contentItem = DbUtil.getContentItemByPageCode(pageCode);
                        sorted = new TreeSet<AmpContentItemThumbnail>(
                                contentItem.getContentThumbnails());
                    }
                    //TODO: This is wrong, there should be a better way than retrieve all objects each time
                    Object[] arrContentThumbnailItems = sorted.toArray();
                    AmpContentItemThumbnail thumbnail = (AmpContentItemThumbnail) arrContentThumbnailItems[intIndex];
                    String hasRelDoc = "false"; 
                    if(!thumbnail.getOptionalFileName().equals("")){
                        hasRelDoc = "true";
                    }

                    String resp = hasRelDoc + "*";
                    resp += thumbnail.getThumbnailLabel() + "*";
                    response.getWriter().print(resp);
                } catch (IOException e) {
                    logger.error("Trying to parse " + index + " to int",
                            e);
                }
            } else {

                try {
                    int intIndex = Integer.parseInt(index);
                    ServletOutputStream os = response.getOutputStream();
                    TreeSet<AmpContentItemThumbnail> sorted;
                    AmpContentItem contentItem;

                    if (isAdmin) {
                        Set<AmpContentItemThumbnail> sessionSet = (Set<AmpContentItemThumbnail>) session
                                .getAttribute("contentThumbnails");
                        sorted = new TreeSet<AmpContentItemThumbnail>(
                                sessionSet);
                    } else {
                        contentItem = DbUtil.getContentItemByPageCode(pageCode);
                        sorted = new TreeSet<AmpContentItemThumbnail>(
                                contentItem.getContentThumbnails());
                    }

                    Object[] arrContentThumbnailItems = sorted.toArray();
                    AmpContentItemThumbnail thumbnail = (AmpContentItemThumbnail) arrContentThumbnailItems[intIndex];
                    if (thumbnail != null) {
                        response.setContentType(thumbnail
                                .getThumbnailContentType());
                        os.write(thumbnail.getThumbnail());
                        boolean optFile = false;
                        if (thumbnail.getOptionalFile() != null) {
                            optFile = true;
                        }
                        // session.setAttribute("homeThumbnailOptfile" +
                        // placeholder, optFile);
                        os.flush();
                    }
                } catch (NumberFormatException nfe) {
                    logger.error("Trying to parse " + index + " to int");
                }
            }
        } else {
            BufferedImage bufferedImage = new BufferedImage(30, 30,
                    BufferedImage.TRANSLUCENT);
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.flush();
        }

        return null;
    }
}

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DisplayThumbnail extends Action {

    private static Logger logger = Logger.getLogger(DisplayThumbnail.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String placeholder = request.getParameter("placeholder");
        String action = request.getParameter("relDocs");
        HttpSession session = request.getSession();
        if (placeholder != null) {
            if (action != null) {
                response.setContentType("text/plain");
                Boolean hasRelDocs = (Boolean) session.getAttribute("homeThumbnailOptfile" + placeholder);
                if (hasRelDocs != null && hasRelDocs){
                    try {
                        AmpHomeThumbnail test = FeaturesUtil.getAmpHomeThumbnail(Integer.parseInt(placeholder));
                        String resp = hasRelDocs.toString()+"*";
                        resp += test.getThumbnailLabel()+"*";
                        response.getWriter().print(resp);
                    } catch (IOException e) {
                          logger.error("Trying to parse " + placeholder + " to int",e);
                    }
                }
            } else {

                try {
                    int temp = Integer.parseInt(placeholder);
                    ServletOutputStream os = response.getOutputStream();
                    AmpHomeThumbnail test = FeaturesUtil.getAmpHomeThumbnail(temp);
                    if (test != null) {
                        os.write(test.getThumbnail());
                        boolean optFile = false;
                        if (test.getOptionalFile() != null) {
                            optFile = true;
                        }
                        session.setAttribute("homeThumbnailOptfile" + placeholder, optFile);
                        os.flush();
                   } 
                } catch (NumberFormatException nfe) {
                    logger.error("Trying to parse " + placeholder + " to int");
                }
            }
        } else {
            BufferedImage bufferedImage = new BufferedImage(30, 30, BufferedImage.TRANSLUCENT);
            ServletOutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "png", os);
            os.flush();
        }

        return null;
    }
}

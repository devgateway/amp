package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;

public class DisplayFlag extends Action {
    
    private static Logger logger = Logger.getLogger(DisplayFlag.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,
            HttpServletRequest request,HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        if (id != null) {
            try {
                long temp = Long.parseLong(id);
                Long cId = new Long(temp);
                ServletOutputStream os = response.getOutputStream();
                os.write(FeaturesUtil.getFlag(cId));
                os.flush();                                 
            } catch (NumberFormatException nfe) {
                logger.error("Trying to parse " + id + " to long");
            }
            
        } else {
            ServletContext ampContext = getServlet().getServletContext();
            Boolean defFalgExist = (Boolean) ampContext.getAttribute(Constants.DEF_FLAG_EXIST);
            ServletOutputStream os = response.getOutputStream();
            if (defFalgExist != null && defFalgExist.booleanValue() == true) {              
                byte[] defaultFlag = FeaturesUtil.getDefaultFlag();
                if(defaultFlag != null)
                {
                    os.write(defaultFlag);
                }
                else
                {
                    BufferedImage bufferedImage = ImageIO.read(new File(getServlet().getServletContext().getRealPath("/TEMPLATE/ampTemplate/img_2/amp_logo.gif")));
                    //BufferedImage bufferedImage = new BufferedImage(30, 30, BufferedImage.TRANSLUCENT);
                    ImageIO.write(bufferedImage, "gif", os);
                }
                os.flush();                                 
            } else {
                BufferedImage bufferedImage = ImageIO.read(new File(getServlet().getServletContext().getRealPath("/TEMPLATE/ampTemplate/img_2/amp_logo.gif")));
                //BufferedImage bufferedImage = new BufferedImage(30, 30, BufferedImage.TRANSLUCENT);
                ImageIO.write(bufferedImage, "gif", os);
            }
        }
        
        return null;
    }
    

}

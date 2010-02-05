package org.digijava.module.aim.action;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpHomeThumbnail;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

public class DisplayThumbnail extends Action {
	
	private static Logger logger = Logger.getLogger(DisplayThumbnail.class);
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,
			HttpServletRequest request,HttpServletResponse response) throws Exception {

		String placeholder = request.getParameter("placeholder");
		if (placeholder != null) {
			try {
				int temp = Integer.parseInt(placeholder);
				AmpHomeThumbnail thumb = FeaturesUtil.getAmpHomeThumbnail(temp);
				if (thumb != null) {
					ServletOutputStream os = response.getOutputStream();
					os.write(thumb.getThumbnail());
					os.flush();					
				}
			} catch (NumberFormatException nfe) {
				logger.error("Trying to parse " + placeholder + " to int");
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

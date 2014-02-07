 /*
 * EditActivity.java
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpStructureImg;
import org.digijava.module.aim.util.ActivityUtil;


import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;


 /**
 * Writes the structure image on the response
 *
 * @author mmoras
 */
public class DisplayStructureImage
    extends Action {

  private ServletContext ampContext = null;

  private static Logger logger = Logger.getLogger(DisplayStructureImage.class);
  
  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
          HttpServletRequest request,
          HttpServletResponse response) throws Exception {
	  
	  Long strId = request.getParameter("structureId") != null ? Long.parseLong(request.getParameter("structureId")): null;
	  Long imgId = request.getParameter("imgId") != null ? Long.parseLong(request.getParameter("imgId")): null;
	  
	  if(strId != null ){
		  AmpStructureImg image = null;
		  if( imgId != null){
			  image = ActivityUtil.getStructureImage(strId, imgId);  
		  }else{
			  image = ActivityUtil.getMostRecentlyUploadedStructureImage(strId);
		  }
		  if(image != null){  
			  response.setContentType(image.getContentType());
			  response.getOutputStream().write(image.getImgFile());
		  } else {
              response.setContentType("image/png");
              int canvasWidth = 200;
              int canvasHeight = 30;
              String noImageTxt = "No images for this structure.";
              BufferedImage graph = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
              Graphics2D g2d = graph.createGraphics();
              g2d.setBackground(new Color(255, 255, 255, 255));


              java.awt.Font f = new java.awt.Font("Helvetica", Font.PLAIN, 14);
              g2d.setFont(f);
              GlyphVector glv = g2d.getFont().createGlyphVector(g2d.
                      getFontRenderContext(), noImageTxt);

              int captionWidth = (int)glv.getVisualBounds().getWidth();
              int captionHeight = (int)glv.getVisualBounds().getHeight();



              g2d.setColor(new Color(0,0,0,255));
              g2d.drawString(noImageTxt, (canvasWidth - captionWidth)/2 , (canvasHeight - captionHeight)/2 + captionHeight);

              ImageIO.write(graph, "png", response.getOutputStream());

              graph.flush();
          }
	  }
	  
	  return null;	  
  }
}

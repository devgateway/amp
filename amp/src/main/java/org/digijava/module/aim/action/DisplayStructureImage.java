 /*
 * EditActivity.java
 * Created: Feb 10, 2005
 */

package org.digijava.module.aim.action;

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
          }
      }
      
      return null;    
  }
}

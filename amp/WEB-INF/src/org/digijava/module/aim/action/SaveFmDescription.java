package org.digijava.module.aim.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.visibility.AmpObjectVisibility;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.aim.dbentity.AmpFeaturesVisibility;
import org.digijava.module.aim.dbentity.AmpFieldsVisibility;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.form.SaveFmDescriptionForm;
import org.digijava.module.aim.util.FeaturesUtil;

public class SaveFmDescription extends Action {
    public enum ObjectVisibility {
        MODULE, FEATURE, FIELD
    }
    private static Logger logger = Logger.getLogger(SaveFmDescription.class);
      @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) {
          SaveFmDescriptionForm saveFmDescriptionForm=(SaveFmDescriptionForm)form;
          String objectVisibility=saveFmDescriptionForm.getObjectVisibility();
          String description=saveFmDescriptionForm.getDescription();
          Long id=saveFmDescriptionForm.getId();
          String result="success"; //  response to ajax call
          Class<? extends AmpObjectVisibility> clazz=null;
          ObjectVisibility objVisibility=ObjectVisibility.valueOf(objectVisibility.toUpperCase());
            switch (objVisibility) {
            case MODULE:
                clazz = AmpModulesVisibility.class; break;
            case FEATURE:
                clazz = AmpFeaturesVisibility.class; break;
            case FIELD:
                clazz = AmpFieldsVisibility.class; break;
            }
          try {
            FeaturesUtil.upadateObjectVisibility(clazz, id, description);
        } catch (DgException e) {
            result="fail";
            logger.error("error",e);
        }
        try {
            response.getWriter().print(result);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("error",e);
        }
          return null;
          
      }

}

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.form.ComponentsForm;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.ComponentsUtil;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GetComponents extends Action{
        private static Logger logger = Logger.getLogger(GetSectorSchemes.class);
    
            public ActionForward execute(ActionMapping mapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response) throws java.lang.Exception {
    
                            HttpSession session = request.getSession();
                            if (session.getAttribute("ampAdmin") == null) {
                                return mapping.findForward("index");
                            } else {
                                String str = (String)session.getAttribute("ampAdmin");
                                if (str.equals("no")) {
                                    return mapping.findForward("index");
                                }
                            }
                            logger.info("came into the components manager");
                            List<AmpComponent> com = new ArrayList<AmpComponent>(ComponentsUtil.getAmpComponents());
                            if (FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.COMPONENTS_SORT_ORDER).equalsIgnoreCase("code")){
                                Collections.sort((List<AmpComponent>)com, new ComponentsUtil.HelperComponetCodeComparator());
                            }
                            else if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.COMPONENTS_SORT_ORDER).equalsIgnoreCase("type")){
                                Collections.sort((List<AmpComponent>)com, new ComponentsUtil.HelperComponetTypeComparator());
                            }
                            else if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.COMPONENTS_SORT_ORDER).equalsIgnoreCase("tittle")){
                                Collections.sort((List<AmpComponent>)com, new ComponentsUtil.HelperComponetTypeComparator());
                            }
                            else if(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.COMPONENTS_SORT_ORDER).equalsIgnoreCase("date")){
                                Collections.sort((List<AmpComponent>)com, new ComponentsUtil.HelperComponetDateComparator());
                            }
                            else{
                                Collections.sort((List<AmpComponent>)com);
                            }
                            ComponentsForm compForm = (ComponentsForm) form;
                            compForm.setComponents(com);
        return mapping.findForward("default");
      }
    
}

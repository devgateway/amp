package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.form.AmpActivityProgramSettingsForm;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.*;

public class AmpActivityProgramSettingsAction
    extends Action {
        private static Logger logger = Logger.getLogger(
            AmpActivityProgramSettingsAction.class);

        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws java.
            lang.
            Exception {

                AmpActivityProgramSettingsForm ampActivityProgramSettingsForm = (
                    AmpActivityProgramSettingsForm) form;

                
                if (request.getParameter("save")!=null) {
                        ProgramUtil.saveAmpActivityProgramSettings(
                            ampActivityProgramSettingsForm.getSettingsList());
                        ampActivityProgramSettingsForm.setEvent(null);
                        return mapping.findForward("forward");
                }
                else {
                        //Load programs settings
                        ampActivityProgramSettingsForm.setProgramList(
                            ProgramUtil.getAllThemes());
                        ampActivityProgramSettingsForm.setSettingsList(
                            ProgramUtil.getAmpActivityProgramSettingsList());
                        return mapping.findForward("forward");

                }

        }
}

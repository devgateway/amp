package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AmpActivityProgramSettingsForm;
import org.digijava.module.aim.util.ProgramUtil;

public class AmpActivityProgramSettingsAction
    extends Action {
        private static Logger logger = Logger.getLogger(
            AmpActivityProgramSettingsAction.class);

        public ActionForward execute(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws java.
            lang.
            Exception {

                AmpActivityProgramSettingsForm ampActivityProgramSettingsForm = (AmpActivityProgramSettingsForm) form;

                
                if (request.getParameter("save")!=null) {
                        ProgramUtil.saveAmpActivityProgramSettings(ampActivityProgramSettingsForm.getSettingsList());
                        ampActivityProgramSettingsForm.setEvent(null);
                        return mapping.findForward("forward");
                }
                else {
                        //Load programs settings
                        ampActivityProgramSettingsForm.setProgramList(ProgramUtil.getAllThemes());
                        // Remove from the list of available themes the one used as indirect program.
                        AmpTheme indirectProgram = NDDService.getDstProgramRoot();
                        if (indirectProgram != null) {
                                ampActivityProgramSettingsForm.getProgramList().remove(indirectProgram);
                        }

                        ampActivityProgramSettingsForm.setSettingsList
                                (ProgramUtil.getAmpActivityProgramSettingsList(true));
                        return mapping.findForward("forward");

                }

        }
}

package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AmpActivityProgramSettingsForm;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AmpActivityProgramSettingsAction
        extends Action {
    private static Logger logger = Logger.getLogger(
            AmpActivityProgramSettingsAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws java.lang.Exception {

        AmpActivityProgramSettingsForm ampActivityProgramSettingsForm = (AmpActivityProgramSettingsForm) form;


        if (request.getParameter("save") != null) {
            ActionMessages errors = validate(ampActivityProgramSettingsForm.getSettingsList());
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                ProgramUtil.saveAmpActivityProgramSettings(ampActivityProgramSettingsForm.getSettingsList());
                ampActivityProgramSettingsForm.setEvent(null);
            }
            return mapping.findForward("forward");
        } else {
            //Load programs settings
            ampActivityProgramSettingsForm.setProgramList(ProgramUtil.getAllThemes());
            // Remove from the list of available themes the one used as indirect program.
            AmpTheme indirectProgram = NDDService.getDstIndirectProgramRoot();
            if (indirectProgram != null) {
                ampActivityProgramSettingsForm.getProgramList().remove(indirectProgram);
            }

            ampActivityProgramSettingsForm.
                    setSettingsList(ProgramUtil.getAmpActivityProgramSettingsList(true));
            return mapping.findForward("forward");

        }

    }

    private ActionMessages validate(List<AmpActivityProgramSettings> settingsList) {
        ActionMessages errors = new ActionMessages();
        settingsList.stream().forEach((setting) -> {
            AmpActivityProgramSettings oldSetting =
                    (AmpActivityProgramSettings) PersistenceManager.getSession().get(AmpActivityProgramSettings.class,
                            setting.getAmpProgramSettingsId());
            if (oldSetting.getDefaultHierarchy() != null && setting.getDefaultHierarchy().getAmpThemeId() == -1) {
                // we are removing
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage("error.aim.removeProgramSetting", oldSetting.getName()));

            }
        });
        return errors;

    }
}

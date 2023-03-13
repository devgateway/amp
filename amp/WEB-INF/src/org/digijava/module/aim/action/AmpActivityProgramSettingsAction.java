package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.form.AmpActivityProgramSettingsForm;
import org.digijava.module.aim.form.helpers.AmpActivityProgramSettingsDTO;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.util.ProgramUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
//                List<AmpActivityProgramSettings> tempSettings = dtoToEntity(ampActivityProgramSettingsForm.getSettingsList();


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

            List<AmpActivityProgramSettings> tempAmpActivitySettingsEntities = (List<AmpActivityProgramSettings>) ProgramUtil.getAmpActivityProgramSettingsList(true);
            List<AmpActivityProgramSettingsDTO> tempAmpActivitySettingsDTO = entityToDto(tempAmpActivitySettingsEntities);

            ampActivityProgramSettingsForm.
                    setSettingsListDTO(tempAmpActivitySettingsDTO);
            return mapping.findForward("forward");

        }

    }

    private List dtoToEntity (List<AmpActivityProgramSettingsDTO> dto) {
        AmpActivityProgramSettingsForm form = new AmpActivityProgramSettingsForm();

        for (AmpActivityProgramSettingsDTO ampActivityProgramSetting : dto) {
            AmpActivityProgramSettings ampActivityProgramSettingEntity = new AmpActivityProgramSettings();

            AmpTheme settingTheme = ProgramUtil.getThemeById(ampActivityProgramSetting.getDefaultHierarchy());


            ampActivityProgramSettingEntity.setName(ampActivityProgramSetting.getName());
            ampActivityProgramSettingEntity.setAllowMultiple(ampActivityProgramSetting.isAllowMultiple());
            ampActivityProgramSettingEntity.setAllowMultiple(ampActivityProgramSetting.isAllowMultiple());
            ampActivityProgramSettingEntity.setDefaultHierarchy(settingTheme);

            if (ampActivityProgramSetting.getStartDate() != null) {
                ampActivityProgramSettingEntity.setStartDate(DateConversion.getDate(ampActivityProgramSetting.getStartDate()));
            }

            if (ampActivityProgramSetting.getEndDate() != null) {
                ampActivityProgramSettingEntity.setEndDate(DateConversion.getDate(ampActivityProgramSetting.getEndDate()));
            }

            form.getSettingsList().add(ampActivityProgramSettingEntity);
        }

        return form.getSettingsList();
    }

    private List<AmpActivityProgramSettingsDTO> entityToDto(List<AmpActivityProgramSettings> entity) {
        AmpActivityProgramSettingsForm form = new AmpActivityProgramSettingsForm();

        if (form.getSettingsListDTO() == null) {
            form.setSettingsListDTO(new ArrayList<>());
        }


        for (AmpActivityProgramSettings ampActivityProgramSetting : entity) {
            AmpActivityProgramSettingsDTO ampActivityProgramSettingDto = new AmpActivityProgramSettingsDTO();

            ampActivityProgramSettingDto.setAmpProgramSettingsId(ampActivityProgramSetting.getAmpProgramSettingsId());
            ampActivityProgramSettingDto.setName(ampActivityProgramSetting.getName());
            ampActivityProgramSettingDto.setAllowMultiple(ampActivityProgramSetting.isAllowMultiple());
            ampActivityProgramSettingDto.setAllowMultiple(ampActivityProgramSetting.isAllowMultiple());
//            ampActivityProgramSettingDto.setDefaultHierarchy(ampActivityProgramSetting.getDefaultHierarchy());

            if (ampActivityProgramSetting.getStartDate() != null) {
                ampActivityProgramSettingDto.setStartDate(DateConversion.convertDateToString(ampActivityProgramSetting.getStartDate()));
            }

            if (ampActivityProgramSetting.getEndDate() != null) {
                ampActivityProgramSettingDto.setEndDate(DateConversion.convertDateToString(ampActivityProgramSetting.getEndDate()));
            }

            form.getSettingsListDTO().add(ampActivityProgramSettingDto);
        }

        form.setSettingsListDTO(form.getSettingsListDTO());
        return form.getSettingsListDTO();
    }

    private ActionMessages validate(List<AmpActivityProgramSettings> settingsList) {
        ActionMessages errors = new ActionMessages();
        settingsList.stream().forEach((setting) -> {
            AmpActivityProgramSettings oldSetting =
                    (AmpActivityProgramSettings) PersistenceManager.getSession().get(AmpActivityProgramSettings.class,
                            setting.getAmpProgramSettingsId());
//            if (oldSetting.getDefaultHierarchy() != null && setting.getDefaultHierarchy().getAmpThemeId() == -1) {
//                // we are removing
//                errors.add(ActionMessages.GLOBAL_MESSAGE,
//                        new ActionMessage("error.aim.removeProgramSetting", oldSetting.getName()));
//
//            }

//            if (setting.getStartDate() != null && setting.getEndDate() != null) {
//                if (setting.getStartDate().after(setting.getEndDate())) {
//                    errors.add(ActionMessages.GLOBAL_MESSAGE,
//                            new ActionMessage("error.aim.programSettingDateRange", oldSetting.getName()));
//                }
//            }
        });
        return errors;

    }
}

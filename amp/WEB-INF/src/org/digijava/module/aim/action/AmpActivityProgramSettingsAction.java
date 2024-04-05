package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
import org.digijava.kernel.ampapi.endpoints.ndd.NDDService;
import org.digijava.kernel.exception.DgException;
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
import java.util.Date;
import java.util.List;

public class AmpActivityProgramSettingsAction
        extends Action {
    private static final Logger LOGGER = Logger.getLogger(AmpActivityProgramSettingsAction.class);

    public AmpActivityProgramSettingsAction() {
        super();
    }


    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        AmpActivityProgramSettingsForm ampActivityProgramSettingsForm = (AmpActivityProgramSettingsForm) form;

        if (request.getParameter("save") != null) {
            ActionMessages errors = validate(ampActivityProgramSettingsForm.getSettingsListDTO());

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            } else {
                List<AmpActivityProgramSettings> settingsDtoToEntity = dtoToEntity(ampActivityProgramSettingsForm.getSettingsListDTO());

                ProgramUtil.saveAmpActivityProgramSettings(settingsDtoToEntity);
                ampActivityProgramSettingsForm.setSettingsListDTO(entityToDto(settingsDtoToEntity));
                ampActivityProgramSettingsForm.setEvent(null);
            }
            return mapping.findForward("forward");
        } else {
            ampActivityProgramSettingsForm.setSettingsListDTO(getProgramList(ampActivityProgramSettingsForm));
            return mapping.findForward("forward");
        }

    }

    private List<AmpActivityProgramSettingsDTO> getProgramList(AmpActivityProgramSettingsForm ampActivityProgramSettingsForm) {
        ampActivityProgramSettingsForm.setProgramList(ProgramUtil.getDefaultHierarchyPrograms());

        List<AmpActivityProgramSettings> tempAmpActivitySettingsEntities = ProgramUtil.getAmpActivityProgramSettingsList(true);
        return entityToDto(tempAmpActivitySettingsEntities);
    }


    private List<AmpActivityProgramSettings> dtoToEntity (List<AmpActivityProgramSettingsDTO> dto) {
        AmpActivityProgramSettingsForm form = new AmpActivityProgramSettingsForm();

        if (form.getSettingsList() == null) {
            form.setSettingsList(new ArrayList<>());
        }

        for (AmpActivityProgramSettingsDTO ampActivityProgramSetting : dto) {
            AmpActivityProgramSettings ampActivityProgramSettingEntity = new AmpActivityProgramSettings();

            ampActivityProgramSettingEntity.setAmpProgramSettingsId(ampActivityProgramSetting.getAmpProgramSettingsId());
            ampActivityProgramSettingEntity.setName(ampActivityProgramSetting.getName());
            ampActivityProgramSettingEntity.setAllowMultiple(ampActivityProgramSetting.isAllowMultiple());

            if (ampActivityProgramSetting.getDefaultHierarchyId() != null && ampActivityProgramSetting.getDefaultHierarchyId() != -1) {
                AmpTheme selectedTheme = ProgramUtil.getThemeById(ampActivityProgramSetting.getDefaultHierarchyId());
                ampActivityProgramSettingEntity.setDefaultHierarchy(selectedTheme);

                if (ampActivityProgramSetting.getStartDate() != null) {
                    ampActivityProgramSettingEntity.setStartDate(DateConversion.getDate(ampActivityProgramSetting.getStartDate()));
                }

                if (ampActivityProgramSetting.getEndDate() != null) {
                    ampActivityProgramSettingEntity.setEndDate(DateConversion.getDate(ampActivityProgramSetting.getEndDate()));
                }
            }

            form.getSettingsList().add(ampActivityProgramSettingEntity);
        }

        form.setSettingsList(form.getSettingsList());
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

            if (ampActivityProgramSetting.getDefaultHierarchy() != null) {
                ampActivityProgramSettingDto.setDefaultHierarchy(ampActivityProgramSetting.getDefaultHierarchy());
                ampActivityProgramSettingDto.setDefaultHierarchyId(ampActivityProgramSetting.getDefaultHierarchy().getAmpThemeId());
            }


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

    private ActionMessages validate(List<AmpActivityProgramSettingsDTO> settingsList) {
        ActionMessages errors = new ActionMessages();
        settingsList.stream().forEach((setting) -> {
            AmpActivityProgramSettings oldSetting = null;
            try {
                oldSetting = ProgramUtil.getAmpActivityProgramSettings(setting.getAmpProgramSettingsId());
            } catch (DgException e) {
                throw new RuntimeException(e);
            }

            if (oldSetting.getDefaultHierarchyId() != null && setting.getDefaultHierarchyId() == -1) {
                // we are removing
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage("error.aim.removeProgramSetting", oldSetting.getName()));

            }

            Date startDate = setting.getStartDate() != null ? DateConversion.getDate(setting.getStartDate()) : null;
            Date endDate = setting.getEndDate() != null ? DateConversion.getDate(setting.getEndDate()) : null;

            if (startDate != null && endDate != null) {
                if (startDate.after(endDate)) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("error.aim.programSettingDateError", oldSetting.getName()));
                }
            }

            /**
             * It is not allowed to change program's date if indicator is already attached
             */
            if ((oldSetting.getDefaultHierarchy()!= null && oldSetting.getDefaultHierarchy().getIndicators().size() > 0)){
                if ((oldSetting.getStartDate() != null && startDate.getTime() != oldSetting.getStartDate().getTime()) ||
                        (oldSetting.getEndDate() != null && endDate.getTime() != oldSetting.getEndDate().getTime())) {
                    errors.add(ActionMessages.GLOBAL_MESSAGE,
                            new ActionMessage("error.aim.dateRange.indicator", oldSetting.getName()));
                }
            }

        });
        return errors;

    }
}

package org.digijava.module.aim.form;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.form.helpers.AmpActivityProgramSettingsDTO;
import org.digijava.module.aim.helper.DateConversion;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

public class AmpActivityProgramSettingsForm
    extends ActionForm {

        private String event;
        private List programList;
        private List<AmpActivityProgramSettings> settingsList;

        private List<AmpActivityProgramSettingsDTO> settingsListDTO;

        public String getEvent() {
                return event;
        }

        public List<AmpActivityProgramSettings> getProgramList() {
                return programList;
        }

        public List<AmpActivityProgramSettings> getSettingsList() {
                return settingsList;
        }

        public void setEvent(String event) {
                this.event = event;
        }

        public void setProgramList(List programList) {
                this.programList = programList;
        }

        public void setSettingsList(List settingsList) {
                this.settingsList = settingsList;
        }

        @Override
        public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
                ActionErrors errors = new ActionErrors();
                if (this.settingsListDTO != null) {
                        for (AmpActivityProgramSettingsDTO setting : settingsListDTO) {
                                Date startDate = setting.getStartDate() != null ? DateConversion.getDate(setting.getStartDate()) : null;
                                Date endDate = setting.getEndDate() != null ? DateConversion.getDate(setting.getEndDate()) : null;

                                if (startDate != null && endDate != null) {
                                        if (startDate.after(endDate)) {
                                                errors.add("startDate", new org.apache.struts.action.ActionMessage(
                                                        "error.aim.activityProgramSettings.startDateAfterEndDate"));
                                        }
                                }
                        }
                }
                return errors;
        }

        @Override
        public void reset(ActionMapping mapping, HttpServletRequest request) {
                if (this.settingsListDTO != null) {
                        Iterator settingsIter = settingsListDTO.iterator();
                        while (settingsIter.hasNext()) {
                                AmpActivityProgramSettingsDTO setting = (
                                    AmpActivityProgramSettingsDTO)
                                    settingsIter.next();
                                setting.setAllowMultiple(false);
                        }
                }

        }

        public List<AmpActivityProgramSettingsDTO> getSettingsListDTO() {
                return settingsListDTO;
        }

        public void setSettingsListDTO(List<AmpActivityProgramSettingsDTO> settingsListDTO) {
                this.settingsListDTO = settingsListDTO;
        }
}

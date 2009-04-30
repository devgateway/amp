package org.digijava.module.aim.form;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;

public class AmpActivityProgramSettingsForm
    extends ActionForm {

        private String event;
        private List programList;
        private List settingsList;

        public String getEvent() {
                return event;
        }

        public List getProgramList() {
                return programList;
        }

        public List getSettingsList() {
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

        public void reset(ActionMapping mapping, HttpServletRequest request) {
                if (this.settingsList != null) {
                        Iterator settingsIter = settingsList.iterator();
                        while (settingsIter.hasNext()) {
                                AmpActivityProgramSettings setting = (
                                    AmpActivityProgramSettings)
                                    settingsIter.next();
                                setting.setAllowMultiple(false);
                        }
                }

        }

}

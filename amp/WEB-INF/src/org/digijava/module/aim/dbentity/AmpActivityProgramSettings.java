package org.digijava.module.aim.dbentity;

import java.io.Serializable;

import org.digijava.module.aim.util.Identifiable;
import org.digijava.module.aim.util.ProgramUtil;

public class AmpActivityProgramSettings implements Serializable, Identifiable {

        private AmpTheme defaultHierarchy;
        private boolean allowMultiple;
        private Long ampProgramSettingsId;
        private String name;
        public AmpTheme getDefaultHierarchy() {
                return defaultHierarchy;
        }

        public boolean isAllowMultiple() {
                return allowMultiple;
        }

        public Long getAmpProgramSettingsId() {
                return ampProgramSettingsId;
        }

        public String getName() {
                return name;
        }

        public void setDefaultHierarchy(AmpTheme defaultHierarchy) {
                this.defaultHierarchy = defaultHierarchy;
        }

        public void setAllowMultiple(boolean allowMultiple) {
                this.allowMultiple = allowMultiple;
        }

        public void setAmpProgramSettingsId(Long ampProgramSettingsId) {
                this.ampProgramSettingsId = ampProgramSettingsId;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Long getDefaultHierarchyId() {
                Long id = null;
                if (defaultHierarchy != null) {
                        id = defaultHierarchy.getAmpThemeId();
                }
                return id;
        }

        public void setDefaultHierarchyId(Long id) {
                if (id != null && id != new Long( -1)) {
                        this.defaultHierarchy = ProgramUtil.getThemeById(id);                       
                }
                else {
                        this.defaultHierarchy = null;
                }
        }

        public AmpActivityProgramSettings() {}

        public AmpActivityProgramSettings(String name) {
                this.name = name;
                this.allowMultiple = false;
                this.defaultHierarchy = null;
        }

        @Override
        public Object getIdentifier() {
            return ampProgramSettingsId;
//          return null;
        }

    @Override
    public String toString() {
        return name;
    }
}

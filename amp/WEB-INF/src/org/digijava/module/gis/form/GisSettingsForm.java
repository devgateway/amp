package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpSectorScheme;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.gis.dbentity.GisSettings;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: George
 * Date: 4/2/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class GisSettingsForm extends ActionForm {
    Collection<AmpSectorScheme> secShcemes;
    Collection<AmpTheme> topLevelPrograms;
    Long[] secSchemesSelected;
    Long[] programsSelected;
    GisSettings gisSettings;

    public GisSettings getGisSettings() {
        return gisSettings;
    }

    public void setGisSettings(GisSettings gisSettings) {
        this.gisSettings = gisSettings;
    }

    public Collection<AmpSectorScheme> getSecShcemes() {
        return secShcemes;
    }

    public void setSecShcemes(Collection<AmpSectorScheme> secShcemes) {
        this.secShcemes = secShcemes;
    }

    public Collection<AmpTheme> getTopLevelPrograms() {
        return topLevelPrograms;
    }

    public void setTopLevelPrograms(Collection<AmpTheme> topLevelPrograms) {
        this.topLevelPrograms = topLevelPrograms;
    }

    public Long[] getSecSchemesSelected() {
        return secSchemesSelected;
    }

    public void setSecSchemesSelected(Long[] secSchemesSelected) {
        this.secSchemesSelected = secSchemesSelected;
    }

    public Long[] getProgramsSelected() {
        return programsSelected;
    }

    public void setProgramsSelected(Long[] programsSelected) {
        this.programsSelected = programsSelected;
    }
}

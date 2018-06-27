/**
 * 
 */
package org.dgfoundation.amp.reports.mondrian;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.newreports.ReportSettingsImpl;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.util.DbUtil;

/**
 * Reports utility methods
 * 
 * @author Nadejda Mandrescu
 */
public class MondrianReportUtils {
    protected static final Logger logger = Logger.getLogger(MondrianReportUtils.class);
    
    /**
     * @return default configuration for the current user settings
     */
    public static ReportSettingsImpl getCurrentUserDefaultSettings() {
        ReportSettingsImpl settings = new ReportSettingsImpl();
        settings.setCurrencyFormat(FormatHelper.getDefaultFormat());
        AmpApplicationSettings ampAppSettings = AmpARFilter.getEffectiveSettings();
        if (ampAppSettings == null) {
            settings.setCalendar(DbUtil.getAmpFiscalCalendar(DbUtil.getBaseFiscalCalendar()));
        } else { 
            settings.setCalendar(ampAppSettings.getFiscalCalendar());
        }
        return settings;
    }
}

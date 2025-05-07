/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.filters;

import org.digijava.kernel.ampapi.endpoints.settings.SettingField;
import org.digijava.kernel.ampapi.endpoints.settings.SettingOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Filter Options Builder
 * 
 * @author Nadejda Mandrescu
 */
public class FiltersBuilder {
    
    public static SettingField buildComputedYears() {
        List<SettingOptions.Option> yearOptions = new ArrayList<SettingOptions.Option>();
        // for now we'll replicate the same requirement as in ReportsFilterPicker.modeRefreshDropdowns
        int curYear = new GregorianCalendar().get(Calendar.YEAR);
        yearOptions.add(new SettingOptions.Option(FiltersConstants.CURRENT, 
                FiltersConstants.ID_NAME_MAP.get(FiltersConstants.CURRENT), String.valueOf(curYear), true));
        // keeping logical order from current year 
        for (int year = curYear - 1; year > curYear - 10; year --) {
            // it can be 'Fiscal Year 2015-2016', etc in the future when linking to non-Greg Calendar
            String yearStr = String.valueOf(year);
            yearOptions.add(new SettingOptions.Option(yearStr, yearStr, yearStr));
        }
        
        return buildOptionsField(FiltersConstants.COMPUTED_YEAR, FiltersConstants.CURRENT, yearOptions);
    }
    
    public static SettingField buildOptionsField(String id, String defaultId, List<SettingOptions.Option> options) {
        return SettingField.create(id, FiltersConstants.ID_GROUP_MAP.get(id), FiltersConstants.ID_NAME_MAP.get(id),
                new SettingOptions(defaultId, options));
    }

}

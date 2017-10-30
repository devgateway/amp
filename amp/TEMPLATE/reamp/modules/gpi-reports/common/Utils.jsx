const CONTEXT = 'GPI_REPORTS';
const SETTINGS_DEFINITIONS_EP = '/rest/settings-definitions/gpi-reports';
import * as Constants from '../common/Constants';
class Utils {     
    static initializeFilterWidget() {               
          return new ampFilter( {
               draggable: true,
               caller: CONTEXT
           });    
    }
    
    static initializeSettingsWidget() {               
        return new AMPSettings.SettingsWidget( {
            draggable: true,
            caller: CONTEXT,
            isPopup: true,
            definitionUrl: SETTINGS_DEFINITIONS_EP
        });     
    }
    
    static showFilters(element, filter, onFilterApply, onFilterCancel) {        
        filter.setElement( element);
        filter.showFilters();
        $(element).show();
        filter.on( 'cancel', onFilterCancel);
        filter.on( 'apply', onFilterApply);
    }
    
    static showSettings(element, settingsWidget, onSettingsApply, onSettingsCancel) {
        settingsWidget.setElement(element);
        settingsWidget.definitions.loaded.done( function() {
            settingsWidget.show();
        });
        $(element).show();
        settingsWidget.on( 'close', onSettingsCancel);
        settingsWidget.on( 'applySettings', onSettingsApply);
    }  
    
    static capitalizeFirst(str) {               
        if (str) {
            return str.charAt(0).toUpperCase() + str.substr(1).toLowerCase();             
        } 
        
        return str;        
    }   
    
    /**
     * Get the start date and end of an year
     */
    static getStartEndDates(settingsWidget, calendars, year, years, converted) {        
        const settings = settingsWidget.toAPIFormat();
        const calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] : settingsWidget.definitions.getDefaultCalendarId();
        const calendar = years.filter( calendar => calendar.calendarId == calendarId )[0];
        const yearObject = calendar.years.filter(yearObject => yearObject.year == year)[0];
        const dates = {};        
        dates.start = yearObject.start;
        dates.end = yearObject.end;        
        return dates;
    }
        
    static getYears( settingsWidget, years ) {
        let result = [];
        if ( settingsWidget && settingsWidget.definitions ) {
            settingsWidget.definitions.loaded.done( function() {
                const settings = settingsWidget.toAPIFormat()
                const calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] : settingsWidget.definitions.getDefaultCalendarId();
                const calendar = years.filter( calendar => calendar.calendarId == calendarId )[0];
                if ( calendar ) {
                    for (let yearObject of calendar.years) {
                        result.push(yearObject.year);
                     }                    
                }
            });
        }
        return result;
    }        

}

export default Utils;

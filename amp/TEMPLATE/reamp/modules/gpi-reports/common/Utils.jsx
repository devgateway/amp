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
    
    static getStartEndDates(settings, calendars, year) {
        const dates = {};
        const calendarId = settings[Constants.CALENDAR_SETTING];
        const calendar = calendars.filter(cal => cal.ampFiscalCalId == calendarId)[0];
        if(calendar && calendar.baseCal === Constants.ETH_BASE_CALENDAR) {
           if (this.isLeapYear(year + 1)) {
               dates.start = year + '-09-12';
               dates.end = (year + 1) + '-09-10'; 
           } else {
               dates.start = year + '-09-11';
               dates.end = (year + 1) + '-09-10';
           }            
        } else{
            dates.start = year + '-01-01';
            dates.end = year + '-12-31';
        }
        
        return dates;
    }
    
    static isLeapYear(year) {
        return new Date(year, 1, 29).getMonth() == 1;
    }

    static getYears( settingsWidget, years ) {
        let result = [];
        if ( settingsWidget && settingsWidget.definitions ) {
            settingsWidget.definitions.loaded.done( function() {
                const settings = settingsWidget.toAPIFormat()
                const calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] : settingsWidget.definitions.getDefaultCalendarId();
                const calendar = years.filter( calendar => calendar.calendarId == calendarId )[0];
                if ( calendar ) {
                    result = calendar.years.slice();
                }
            });
        }
        return result;
    }        

}

export default Utils;

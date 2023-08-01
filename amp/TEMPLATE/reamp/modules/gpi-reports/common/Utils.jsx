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

    static showFilters( element, filter, onFilterApply, onFilterCancel ) {
        filter.setElement( element );
        filter.showFilters();
        $( element ).show();
        filter.on( 'cancel', onFilterCancel );
        filter.on( 'apply', onFilterApply );
    }

    static showSettings( element, settingsWidget, onSettingsApply, onSettingsCancel ) {
        settingsWidget.setElement( element );
        settingsWidget.definitions.loaded.done( function() {
            settingsWidget.show();
        });
        $( element ).show();
        settingsWidget.on( 'close', onSettingsCancel );
        settingsWidget.on( 'applySettings', onSettingsApply );
    }

    static capitalizeFirst( str ) {
        if ( str ) {
            return str.charAt( 0 ).toUpperCase() + str.substr( 1 ).toLowerCase();
        }

        return str;
    }

    /**
     * Get the start date and end of an year
     */
    static getStartEndDates( settingsWidget, calendars, year, years, fromCalendarId) {
        const settings = settingsWidget.toAPIFormat();
        let calendarId;
        if (fromCalendarId) {
            calendarId = fromCalendarId;
        } else {
            calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] : settingsWidget.definitions.getDefaultCalendarId(); 
        }
        
        const calendar = Utils.getYearByCalendarId (years ,calendarId);
        const yearObject = calendar.years.filter( yearObject => yearObject.year == year )[0];
        return {start: yearObject.start, end: yearObject.end};
    }

    static getCalendarId(settingsWidget){
        const settings  = settingsWidget.toAPIFormat();
        const calendarId = settings && settings['calendar-id'] ?  settings['calendar-id'] :
            settingsWidget.definitions.getDefaultCalendarId();
        return calendarId;
    }

    static hasCalendarChanged(settingsWidget, calendarId){
        return Utils.getCalendarId(settingsWidget) !== calendarId;
    }
    static getYears(settingsWidget, years, fromCalendarId) {
        let result = [];
        if ( settingsWidget && settingsWidget.definitions ) {
            settingsWidget.definitions.loaded.done( function() {
                let calendarId;
                if (fromCalendarId) {
                    calendarId = fromCalendarId; 
                } else {
                    const settings = settingsWidget.toAPIFormat()
                    calendarId = settings && settings['calendar-id'] ? settings['calendar-id'] : settingsWidget.definitions.getDefaultCalendarId();  
                }
                
                result = this.getCalendarYears( years, calendarId );
            }.bind(this));
        }

        return result;
    }
    static getYearByCalendarId(years, calendarId, translatedFY){
        return years.filter( calendar => calendar.calendarId == calendarId )[0];
    }
    static getCalendarPrefix(settingsWidget, calendars, translatedFY){
        const fiscalYear = translatedFY ? translatedFY : Constants.FY;
        const cal = calendars.filter( calendar => calendar.id == Utils.getCalendarId(settingsWidget) )[0];
        return cal.isFiscal ? fiscalYear + ' ' : '';
    }


    static getCalendarYears( years, calendarId ) {
        let result = []
        const calendar = Utils.getYearByCalendarId(years,calendarId)
        if ( calendar ) {
            for ( let yearObject of calendar.years ) {
                result.push( yearObject.year );
            }
        }

        return result;
    }

}

export default Utils;

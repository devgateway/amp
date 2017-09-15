const CONTEXT = 'GPI_REPORTS';
const SETTINGS_DEFINITIONS_EP = '/rest/settings-definitions/gpi-reports;'
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
}

export default Utils;

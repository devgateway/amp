import commonListsApi from '../api/CommonListsApi.jsx';



export function getOrgListSuccess(orgList){
    return {type: 'LOAD_ORG_LIST_SUCCESS', orgList: orgList}
}

export function fetchYearsSuccess(years){
    return {type: 'FETCH_YEARS_SUCCESS', years: years}
}

export function fetchReportVisibilityConfigurationSuccess(reportVisibility) {
    return {type: 'FETCH_REPORT_VISIBILITY_CONF_SUCCESS', reportVisibility: reportVisibility};
}

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
}

export function getCalendarsSuccess(calendars){
    return {type: 'LOAD_CALENDARS_SUCCESS', calendars: calendars}
}

export function getOrgList(group){
    return function(dispatch) {
        return commonListsApi.getOrgList(group).then(response => {                          
           dispatch(getOrgListSuccess(response));            
        }).catch(error => {
            throw(error);
        });
    }; 
}


export function getYears() {
    return function(dispatch) {
        return commonListsApi.getYears().then(response => {            
             dispatch(fetchYearsSuccess(response));                                   
        }).catch(error => {
            throw(error);
        });
    }; 
}


export function fetchReportVisibilityConfiguration() {
    return function(dispatch) {
        return commonListsApi.fetchReportVisibilityConfiguration().then(response => {            
             dispatch(fetchReportVisibilityConfigurationSuccess(response));                                   
        }).catch(error => {
            throw(error);
        });
    }; 
}

export function getSettings(){
    return function(dispatch) {
        return commonListsApi.getSettings().then(response => {
            dispatch(getSettingsSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}

export function getCalendars(){
    return function(dispatch) {
        return commonListsApi.getCalendars().then(response => {
            dispatch(getCalendarsSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}
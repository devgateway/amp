import commonListsApi from '../api/CommonListsApi.jsx';

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
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
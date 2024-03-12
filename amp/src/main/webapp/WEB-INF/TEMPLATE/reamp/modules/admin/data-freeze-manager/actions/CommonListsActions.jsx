import commonListsApi from '../api/CommonListsApi.jsx';

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
}

export function getUserInfoSuccess( user ) {
    return { type: 'LOAD_USER_INFO_SUCCESS', user: user }
}

export function getSettings(){
    return function(dispatch) {
        return commonListsApi.getSettings().then(response => {
            dispatch(getSettingsSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
};

export function getUserInfo() {
    return function( dispatch ) {
        return commonListsApi.getUserInfo().then( response => {
            dispatch( getUserInfoSuccess( response ) );
        }).catch( error => {
            throw ( error );
        });
    }
};
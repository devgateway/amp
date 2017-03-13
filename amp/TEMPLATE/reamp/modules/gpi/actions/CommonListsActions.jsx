import commonListsApi from '../api/CommonListsApi.jsx';

export function getCurrencyListSuccess(currencyList){
    return {type: 'LOAD_CURRENCY_LIST_SUCCESS', currencyList: currencyList}
}

export function getOrgListSuccess(orgList){
    return {type: 'LOAD_ORG_LIST_SUCCESS', orgList: orgList}
}

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
}

export function getOrgList(){
    return function(dispatch) {
        return commonListsApi.getOrgList().then(response => {
            dispatch(getOrgListSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }; 
}

export function getCurrencyList(){
    return function(dispatch) {
        return commonListsApi.getCurrencyList().then(response => {            
            var currencies = [];
            var currencySetting = response.find(function(setting){
                return setting.id === 'currency-code'
            });
            if(currencySetting && currencySetting.value){
                currencies = currencySetting.value.options 
            }          
            dispatch(getCurrencyListSuccess(currencies));
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
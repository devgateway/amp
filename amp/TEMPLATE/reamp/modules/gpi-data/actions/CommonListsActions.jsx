import commonListsApi from '../api/CommonListsApi.jsx';

export function getCurrencyListSuccess(currencyList){
    return {type: 'LOAD_CURRENCY_LIST_SUCCESS', currencyList: currencyList}
}

export function getOrgListSuccess(orgList){
    return {type: 'LOAD_ORG_LIST_SUCCESS', orgList: orgList}
}

export function getVerifiedOrgListSuccess(verifiedOrgList){
    return {type: 'LOAD_VERIFIED_ORG_LIST_SUCCESS', verifiedOrgList: verifiedOrgList}
}

export function getSettingsSuccess(settings){
    return {type: 'LOAD_SETTINGS_SUCCESS', settings: settings}
}

export function getUserInfoSuccess(userInfo){
    return {type: 'LOAD_USER_INFO_SUCCESS', userInfo: userInfo}
}

export function getOrgList(verifiedOrgs){
    return function(dispatch) {
        return commonListsApi.getOrgList(verifiedOrgs).then(response => {
            if (verifiedOrgs) {                
                dispatch(getVerifiedOrgListSuccess(response));
            } else{
                dispatch(getOrgListSuccess(response));  
            }
            
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

export function getUserInfo(){
    return function(dispatch) {
        return commonListsApi.getUserInfo().then(response => {
            dispatch(getUserInfoSuccess(response));
        }).catch(error => {
            throw(error);
        });
    }
}
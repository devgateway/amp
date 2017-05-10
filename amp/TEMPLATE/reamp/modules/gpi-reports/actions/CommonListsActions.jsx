import commonListsApi from '../api/CommonListsApi.jsx';



export function getOrgListSuccess(orgList){
    return {type: 'LOAD_ORG_LIST_SUCCESS', orgList: orgList}
}

export function fetchYearsSuccess(years){
    return {type: 'FETCH_YEARS_SUCCESS', years: years}
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
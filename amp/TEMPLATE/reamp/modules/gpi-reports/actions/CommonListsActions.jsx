import commonListsApi from '../api/CommonListsApi.jsx';



export function getOrgListSuccess(orgList){
    return {type: 'LOAD_ORG_LIST_SUCCESS', orgList: orgList}
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
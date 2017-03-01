import aidOnBudgetApi from '../api/AidOnBudgetApi.jsx';
export function getAidOnBudgetListSuccess(data){
    return {type: 'LOAD_AID_ON_BUDGET_LIST_SUCCESS', data: data }
}
export function saveSuccess(data){
    return {type: 'AID_ON_BUDGET_SAVE_SUCCESS', data: data } 
}

export function deleteSuccess(data){
    return {type: 'AID_ON_BUDGET_DELETE_SUCCESS', data: data } 
}

export function addNewAidOnBudget() {
    return {type: 'ADD_AID_ON_BUDGET', data: {aidOnBudget: {}} } 
}

export function updateAidOnBudget(aidOnBudget) {
    return {type: 'UPDATE_AID_ON_BUDGET', data: {aidOnBudget: aidOnBudget, errors: [], infoMessages:[]} } 
}

export function loadAidOnBudgetList() {
      return function(dispatch) {
      return aidOnBudgetApi.getAidOnBudgetList().then(response => {
         var data = {
             aidOnBudgetList: response.data,
             errors: [],
             infoMessages: []        
        };
         
        dispatch(getAidOnBudgetListSuccess(data));
      }).catch(error => {
        throw(error);
      });
    };
}

export function save(data){      
    return function(dispatch) {
        return aidOnBudgetApi.save(data).then(response => {
            const result = {};
            if (response.result === "saved") {
                result.aidOnBudget = response.data;
                result.infoMessages = [{messageKey: 'amp.gpi-data-aid-on-budget:save-successful'}];
            } 
            
            if (response.error) {                 
                result.errors = []
                for (var error in response.error) {                    
                    let messageKey = 'amp.gpi-data-aid-on-budget:server-errors-' + error;
                    result.errors.push({messageKey: messageKey});
                }
            }
            
          dispatch(saveSuccess(result));
        }).catch(error => {          
          throw(error);
        });
      };
}

export function deleteAidOnBudget(data) {
    return function(dispatch) {
        if (data.id) {
            return aidOnBudgetApi.deleteAidOnBudget(data).then(response => {
                const result = {
                        aidOnBudget: data,
                        infoMessages: [{messageKey: 'amp.gpi-data-aid-on-budget:delete-successful'}]
                };
                dispatch(deleteSuccess(result));
              }).catch(error => {
                throw(error);
              });   
        } else {
            const result = {
                    aidOnBudget: data,
                    infoMessages: [{messageKey: 'amp.gpi-data-aid-on-budget:delete-successful'}]
            };
            dispatch(deleteSuccess(result));
        }        
      }; 
}

export function saveAllEdits() {
    
}

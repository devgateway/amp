import aidOnBudgetApi from '../api/AidOnBudgetApi.jsx';
export function getAidOnBudgetListSuccess(aidOnBudgetList){
    return {type: 'LOAD_AID_ON_BUDGET_LIST_SUCCESS', aidOnBudgetList: aidOnBudgetList }
}
export function saveSuccess(data){
    return {type: 'AID_ON_BUDGET_SAVE_SUCCESS', data: data } 
}


export function loadAidOnBudgetList() {
      return function(dispatch) {
      return aidOnBudgetApi.getAidOnBudgetList().then(response => {
        dispatch(getAidOnBudgetListSuccess(response.data));
      }).catch(error => {
        throw(error);
      });
    };
}

export function save(data){      
    return function(dispatch) {
        return aidOnBudgetApi.save(data).then(response => {
          dispatch(saveSuccess(response.data));
        }).catch(error => {
          throw(error);
        });
      };
}

import dataFreezeApi from '../api/DataFreezeApi.jsx';
import Utils from '../common/Utils.jsx';
import * as Constants from '../common/Constants';

export const DATA_FREEZE_EVENT_SAVING = 'DATA_FREEZE_EVENT_SAVING';
export const DATA_FREEZE_EVENT_ON_SAVE = 'DATA_FREEZE_EVENT_ON_SAVE';
export const LOAD_DATA_FREEZE_EVENT_LIST_SUCCESS ='LOAD_DATA_FREEZE_EVENT_LIST_SUCCESS'

export function getDataFreezeEventListSuccess(data) {
     return { type: LOAD_DATA_FREEZE_EVENT_LIST_SUCCESS, data: data }
}

export function savingEvent(){
     return {type:DATA_FREEZE_EVENT_SAVING,data:{saving:true}};
}

export function onSave(data) {
     return { type: DATA_FREEZE_EVENT_ON_SAVE, data: data }
}

export function onSaveAllEdits(data) {
     return { type: 'DATA_FREEZE_EVENT_ON_SAVE_ALL_EDITS', data: data }
}

export function deleteSuccess(data) {
     return { type: 'DATA_FREEZE_EVENT_DELETE_SUCCESS', data: data }
}

export function addNewDataFreezeEvent() {
     return { type: 'ADD_DATA_FREEZE_EVENT', data: { dataFreezeEvent: { isEditing: true, enabled: true } } }
}

export function updateDataFreezeEvent(dataFreezeEvent) {
     return {
          type: 'UPDATE_DATA_FREEZE_EVENT',
          data: { dataFreezeEvent: dataFreezeEvent, errors: [], infoMessages: [] }
     }
}

export function loadDataFreezeEventList(data) {
     return function (dispatch) {
          return dataFreezeApi.getDataFreezeEventList(data).then(response => {
               // this second call should be implemented in its own action
               // due to lack of time im implementing here and then while refactoring
               // in code review we will move it
               return dataFreezeApi.getFrozenActivities().then(frozenActivites => {
                    var results = {
                         dataFreezeEventList: [],
                         errors: [],
                         infoMessages: [],
                         frozenActivities: {}
                    };

                    results.paging = data.paging;
                    results.sorting = data.sorting;
                    if (response.error) {
                         results.errors = Utils.extractErrors(response.error);
                    } else {
                         results.dataFreezeEventList = response.data;
                         results.paging.totalRecords = response.totalRecords;
                         results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);
                    }
                    results.frozenActivities.count = frozenActivites.freezingCount;
                    results.frozenActivities.freezingDate = frozenActivites.freezingDate;
                    return dispatch(getDataFreezeEventListSuccess(results));
               })
          }).catch(error => {
               throw ( error );
          });
     };
}

export function save(data) {
     return function (dispatch) {
          dispatch(savingEvent());
          const errors = Utils.validateDataFreezeEvent(data);
          if (errors.length > 0) {
               const result = {};
               result.dataFreezeEvent = data;
               result.errors = errors;
               result.infoMessages = [];
               return dispatch(onSave(result));
          }

          return dataFreezeApi.save(data).then(response => {
               const result = { errors: [] };
               result.dataFreezeEvent = response.data || data;
               if (response.result === Constants.SAVE_SUCCESSFUL) {
                    result.dataFreezeEvent.isEditing = false;
                    result.infoMessages = [{ messageKey: 'amp.data-freeze-event:save-successful' }];
               }

               if (response.errors || response.error) {
                    result.dataFreezeEvent.isEditing = true;
                    if (response.errors) {
                         result.errors = [...Utils.extractErrors(response.errors, result.dataFreezeEvent)]
                    }

                    if (response.error) {
                         result.errors = [...Utils.extractErrors(response.error, result.dataFreezeEvent)]
                    }
               }
               dispatch(onSave(result));
          }).catch(error => {
               throw ( error );
          });

     };
}

export function deleteDataFreezeEvent(data) {
     return function (dispatch) {
          if (data.id) {
               return dataFreezeApi.deleteDataFreezeEvent(data).then(response => {
                    const result = { infoMessages: [], errors: [] };
                    result.dataFreezeEvent = data;
                    if (response.error) {
                         result.errors = [...Utils.extractErrors(response.error, result.dataFreezeEvent)]
                    } else {
                         result.infoMessages = [{ messageKey: 'amp.data-freeze-event:delete-successful' }];
                    }

                    dispatch(deleteSuccess(result));
               }).catch(error => {
                    throw ( error );
               });
          } else {
               const result = {
                    dataFreezeEvent: data,
                    infoMessages: [{ messageKey: 'amp.data-freeze-event:delete-successful' }]
               };
               dispatch(deleteSuccess(result));
          }
     };
}

export function removeFromState(data) {
     return function (dispatch) {
          const result = {
               dataFreezeEvent: data,
               infoMessages: []
          };
          dispatch(deleteSuccess(result));
     }
}

export function unfreezeAll() {
     return function (dispatch) {
          return dataFreezeApi.unfreezeAll().then(response => {
               let result = { infoMessages: [], errors: [] };
               if (response.result === 'SUCCESSFUL') {
                    result.infoMessages.push({ messageKey: 'amp.data-freeze-event:unfreeze-all-successful' });
               } else {
                    result.errors.push({ messageKey: 'amp.data-freeze-event:unfreeze-all-failed' });
               }

               dispatch({ type: 'UNFREEZE_ALL', data: result });
          });

     }
}

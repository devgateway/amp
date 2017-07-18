const defaultState = {
     data: {
          dataFreezeEventList: [],
          errors: [],
          infoMessages: [],
          cid: 1,
          paging: {
               recordsPerPage: 10,
               offset: 0,
               currentPageNumber: 1,
               totalPageCount: 1
          },
          sorting: {
               orderBy: 'freezingDate',
               sortOrder: 'desc'
          }, frozenActivities: {
               count: 0,
               freezingDate: ''
          }
     }
};

export default function dataFreezeEventReducer(state: Object = defaultState.data, action: Object) {
     switch (action.type) {
          case 'LOAD_DATA_FREEZE_EVENT_LIST_SUCCESS':
               var newState = Object.assign({}, action.data);
               newState.cid = state.cid;
               return newState;
          case 'DATA_FREEZE_EVENT_ON_SAVE':
               var newState = Object.assign({}, state);
               var actionData = Object.assign({}, action.data);
               newState.dataFreezeEventList = newState.dataFreezeEventList.map(function (dataFreezeEvent) {
                    return ((dataFreezeEvent.id && dataFreezeEvent.id === actionData.dataFreezeEvent.id) || (dataFreezeEvent.cid && dataFreezeEvent.cid === actionData.dataFreezeEvent.cid)) ? Object.assign({}, actionData.dataFreezeEvent) : dataFreezeEvent;
               });
               newState.errors = actionData.errors || [];
               newState.infoMessages = actionData.infoMessages || [];
               return newState;
          case 'DATA_FREEZE_EVENT_ON_SAVE_ALL_EDITS':
               var newState = Object.assign({}, state);
               var actionData = Object.assign({}, action.data);
               newState.dataFreezeEventList = newState.dataFreezeEventList.map(function (dataFreezeEvent) {
                    var found = actionData.dataFreezeEventList.find(obj => {
                         obj
                         return ((dataFreezeEvent.id && dataFreezeEvent.id === obj.id) || (dataFreezeEvent.cid && dataFreezeEvent.cid === obj.cid))
                    });
                    return found ? Object.assign({}, found) : dataFreezeEvent;
               });
               newState.errors = actionData.errors || [];
               newState.infoMessages = actionData.infoMessages || [];
               return newState;
          case 'DATA_FREEZE_EVENT_DELETE_SUCCESS':
               var newState = Object.assign({}, state);
               var actionData = Object.assign({}, action.data);
               newState.dataFreezeEventList = [...newState.dataFreezeEventList.filter(dataFreezeEvent => dataFreezeEvent.id !== actionData.dataFreezeEvent.id || dataFreezeEvent.cid !== actionData.dataFreezeEvent.cid)]
               newState.errors = actionData.errors || [];
               newState.infoMessages = actionData.infoMessages || [];
               return newState;
          case 'ADD_DATA_FREEZE_EVENT':
               var newState = Object.assign({}, state);
               var inEdit = newState.dataFreezeEventList.filter(dataFreezeEvent => {
                    return dataFreezeEvent.isEditing
               })
               if (inEdit.length == 0) {
                    var actionData = Object.assign({}, action.data);
                    actionData.dataFreezeEvent.cid = state.cid;
                    newState.dataFreezeEventList = [Object.assign({}, actionData.dataFreezeEvent), ...newState.dataFreezeEventList];
                    newState.errors = [];
                    newState.infoMessages = [];
                    newState.cid = ++newState.cid;
               }
               return newState;
          case 'UPDATE_DATA_FREEZE_EVENT':
               var newState = Object.assign({}, state);
               var inEdit = newState.dataFreezeEventList.filter(dataFreezeEvent => {
                    return dataFreezeEvent.isEditing
               });

               var actionData = Object.assign({}, action.data);
               newState.dataFreezeEventList = newState.dataFreezeEventList.map(function (dataFreezeEvent) {
                    return ((dataFreezeEvent.id && dataFreezeEvent.id === actionData.dataFreezeEvent.id) || (dataFreezeEvent.cid && dataFreezeEvent.cid === actionData.dataFreezeEvent.cid)) ? Object.assign({}, actionData.dataFreezeEvent) : dataFreezeEvent;
               });
               newState.errors = actionData.errors || [];
               newState.infoMessages = actionData.infoMessages || [];
               return newState;
          case 'UNFREEZE_ALL':
               var actionData = Object.assign({}, action.data);
               var newState = Object.assign({}, state);
               newState.errors = actionData.errors || [];
               newState.infoMessages = actionData.infoMessages || [];
               return newState;
        
          default:
               return state;
     }
}
const defaultState = {
        data: {
            donorNotesList:[],
            errors: [],
            infoMessages: [],
            cid: 1,
            paging:  {
                recordsPerPage: 10,
                offset: 0,
                currentPageNumber: 1,
                totalPageCount : 1
            },
            sorting: {
                orderBy: 'notesDate',
                sortOrder: 'desc'
            }             
        }
};

export default function donorNotesReducer(state: Object = defaultState.data, action: Object) {    
    switch (action.type) {
    case 'LOAD_DONOR_NOTES_LIST_SUCCESS':
        var newState = Object.assign({}, action.data);        
        newState.cid = state.cid;               
        return newState;
    case 'DONOR_NOTES_ON_SAVE':       
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        if (actionData.errors && actionData.errors.length > 0) {
            newState.donorNotesList = [...newState.donorNotesList]
        } else {
            newState.donorNotesList = newState.donorNotesList.map(function(donorNotes) { return ((donorNotes.id && donorNotes.id === actionData.donorNotes.id) || (donorNotes.cid && donorNotes.cid === actionData.donorNotes.cid)) ? Object.assign({}, actionData.donorNotes) : donorNotes; });
        }   
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;
    case 'DONOR_NOTES_ON_SAVE_ALL_EDITS':        
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.donorNotesList = newState.donorNotesList.map(function(donorNotes) { 
            var found = actionData.donorNotesList.find(obj =>{ obj
                return ((donorNotes.id && donorNotes.id === obj.id) || (donorNotes.cid && donorNotes.cid === obj.cid))     
            })
            
            return found ? Object.assign({}, found) : donorNotes;            
        });        
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];        
        return newState;        
    case 'DONOR_NOTES_DELETE_SUCCESS':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.donorNotesList = [...newState.donorNotesList.filter(donorNotes => donorNotes.id !== actionData.donorNotes.id || donorNotes.cid !== actionData.donorNotes.cid)]
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || []; 
        return newState;
    case 'ADD_DONOR_NOTES':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        actionData.donorNotes.cid = state.cid;
        newState.donorNotesList = [Object.assign({}, actionData.donorNotes), ...newState.donorNotesList];
        newState.errors = [];
        newState.infoMessages = [];
        newState.cid = ++newState.cid;        
        return newState; 
    case 'UPDATE_DONOR_NOTES':
        var newState = Object.assign({}, state);
        var actionData = Object.assign({}, action.data);
        newState.donorNotesList =  newState.donorNotesList.map(function(donorNotes) { return ((donorNotes.id && donorNotes.id === actionData.donorNotes.id) || (donorNotes.cid && donorNotes.cid === actionData.donorNotes.cid)) ? Object.assign({}, actionData.donorNotes) : donorNotes; });     
        newState.errors = actionData.errors || [];
        newState.infoMessages = actionData.infoMessages || [];         
        return newState;   
    default:            
        return state;
    }
}
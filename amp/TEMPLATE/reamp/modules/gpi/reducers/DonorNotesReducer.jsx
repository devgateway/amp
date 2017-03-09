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
        const data = Object.assign({}, action.data)
        data.cid = state.cid;               
        return data;
    case 'DONOR_NOTES_ON_SAVE':
        var data = {};
        if (action.data.errors && action.data.errors.length > 0) {
            data.donorNotesList = [...state.donorNotesList]
        } else {
            data.donorNotesList = state.donorNotesList.map(function(donorNotes) { return ((donorNotes.id && donorNotes.id === action.data.donorNotes.id) || (donorNotes.cid && donorNotes.cid === action.data.donorNotes.cid)) ? Object.assign({}, action.data.donorNotes) : donorNotes; });
        }   
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;
    case 'DONOR_NOTES_ON_SAVE_ALL_EDITS':        
        var data = {};
        data.donorNotesList = state.donorNotesList.map(function(donorNotes) { 
            var found = action.data.donorNotesList.find(obj =>{ obj
                return ((donorNotes.id && donorNotes.id === obj.id) || (donorNotes.cid && donorNotes.cid === obj.cid))     
            })
            
            return found ? Object.assign({}, found) : donorNotes;            
        });
        
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;        
    case 'DONOR_NOTES_DELETE_SUCCESS':
        var data = {};
        data.donorNotesList = [...state.donorNotesList.filter(donorNotes => donorNotes.id !== action.data.donorNotes.id || donorNotes.cid !== action.data.donorNotes.cid)]
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;
    case 'ADD_DONOR_NOTES':
        var data = {};
        action.data.donorNotes.cid = state.cid;
        data.donorNotesList = [Object.assign({}, action.data.donorNotes), ...state.donorNotesList];
        data.errors = [];
        data.infoMessages = [];
        data.cid = ++state.cid;
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data; 
    case 'UPDATE_DONOR_NOTES':
        var data = {};
        data.donorNotesList =  state.donorNotesList.map(function(donorNotes) { return ((donorNotes.id && donorNotes.id === action.data.donorNotes.id) || (donorNotes.cid && donorNotes.cid === action.data.donorNotes.cid)) ? Object.assign({}, action.data.donorNotes) : donorNotes; });     
        data.errors = action.data.errors || [];
        data.infoMessages = action.data.infoMessages || []; 
        data.cid = state.cid; 
        data.paging = state.paging;
        data.sorting = state.sorting;
        return data;   
    default:            
        return state;
    }
}
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
    default:            
        return state;
    }
}
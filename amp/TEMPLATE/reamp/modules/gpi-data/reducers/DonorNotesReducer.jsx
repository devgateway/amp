const INDICATOR_1 = '1';
const INDICATOR_5A = '5a';
const defaultState = {
    data: {
        '1': {
            donorNotesList: [],
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
                orderBy: 'notesDate',
                sortOrder: 'desc'
            }
        },
        '5a': {
            donorNotesList: [],
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
                orderBy: 'notesDate',
                sortOrder: 'desc'
            }
        }
    }
};

export default function donorNotesReducer( state: Object = defaultState.data, action: Object ) {
    var indicatorCode = action.data ? action.data.indicatorCode : INDICATOR_1;
    switch ( action.type ) {
        case 'LOAD_DONOR_NOTES_LIST_SUCCESS':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            actionData.cid = state[indicatorCode].cid; 
            newState[indicatorCode] = actionData;           
            return newState;
        case 'DONOR_NOTES_ON_SAVE':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            if ( actionData.errors && actionData.errors.length > 0 ) {
                newState[indicatorCode].donorNotesList = [...newState[indicatorCode].donorNotesList]
            } else {
                newState[indicatorCode].donorNotesList = newState[indicatorCode].donorNotesList.map( function( donorNotes ) { return ( ( donorNotes.id && donorNotes.id === actionData.donorNotes.id ) || ( donorNotes.cid && donorNotes.cid === actionData.donorNotes.cid ) ) ? Object.assign( {}, actionData.donorNotes ) : donorNotes; });
            }
            newState[indicatorCode].errors = actionData.errors || [];
            newState[indicatorCode].infoMessages = actionData.infoMessages || [];            
            return newState;
        case 'DONOR_NOTES_ON_SAVE_ALL_EDITS':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            newState[indicatorCode].donorNotesList = newState[indicatorCode].donorNotesList.map( function( donorNotes ) {
                var found = actionData.donorNotesList.find( obj => {
                    obj
                    return ( ( donorNotes.id && donorNotes.id === obj.id ) || ( donorNotes.cid && donorNotes.cid === obj.cid ) )
                })

                return found ? Object.assign( {}, found ) : donorNotes;
            });
            newState[indicatorCode].errors = actionData.errors || [];
            newState[indicatorCode].infoMessages = actionData.infoMessages || [];
            return newState;
        case 'DONOR_NOTES_DELETE_SUCCESS':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            newState[indicatorCode].donorNotesList = [...newState[indicatorCode].donorNotesList.filter( donorNotes => donorNotes.id !== actionData.donorNotes.id || donorNotes.cid !== actionData.donorNotes.cid )]
            newState[indicatorCode].errors = actionData.errors || [];
            newState[indicatorCode].infoMessages = actionData.infoMessages || [];
            return newState;
        case 'ADD_DONOR_NOTES':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            actionData.donorNotes.cid = state[indicatorCode].cid;
            newState[indicatorCode].donorNotesList = [Object.assign( {}, actionData.donorNotes ), ...newState[indicatorCode].donorNotesList];
            newState[indicatorCode].errors = [];
            newState[indicatorCode].infoMessages = [];
            newState[indicatorCode].cid = ++newState[indicatorCode].cid;
            return newState;
        case 'UPDATE_DONOR_NOTES':
            var newState = Object.assign( {}, state );
            var actionData = Object.assign( {}, action.data );
            newState[indicatorCode].donorNotesList = newState[indicatorCode].donorNotesList.map( function( donorNotes ) { return ( ( donorNotes.id && donorNotes.id === actionData.donorNotes.id ) || ( donorNotes.cid && donorNotes.cid === actionData.donorNotes.cid ) ) ? Object.assign( {}, actionData.donorNotes ) : donorNotes; });
            newState[indicatorCode].errors = actionData.errors || [];
            newState[indicatorCode].infoMessages = actionData.infoMessages || [];
            return newState;
        default:
            return state;
    }
}
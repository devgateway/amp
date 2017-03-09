import donorNotesApi from '../api/DonorNotesApi.jsx';
import Utils from '../common/utils.jsx';

export function onSave(data){
    return {type: 'DONOR_NOTES_ON_SAVE', data: data } 
}

export function onSaveAllEdits(data){
    return {type: 'DONOR_NOTES_ON_SAVE_ALL_EDITS', data: data } 
}

export function addNewDonorNotes() {
    return {type: 'ADD_DONOR_NOTES', data: {donorNotes: {isEditing: true}} } 
}

export function updateDonorNotes(donorNotes) {
    return {type: 'UPDATE_DONOR_NOTES', data: {donorNotes: donorNotes, errors: [], infoMessages:[]} } 
}

export function save(data){    
    return function(dispatch) {
        const errors = Utils.validateDonorNotes(data);
        if(errors.length > 0 ){
            const result = {};
            result.donorNotes = data;
            result.errors = errors;
            result.infoMessages = [];
            return dispatch(onSave(result));            
        } 
        
        return donorNotesApi.save(data).then(response => {
            const result = {errors: []};
            result.donorNotes = response.data || data;
            if (response.result === "SAVED") {                    
                result.donorNotes.isEditing = false;
                result.infoMessages = [{messageKey: 'amp.gpi-data-donor-notes:save-successful'}];
            } 
            
            if (response.errors || response.error) {
                result.donorNotes.isEditing = true;
                if(response.errors){
                    result.errors = [...Utils.extractErrors(response.errors , result.donorNotes)] 
                }
                
                if(response.error){
                    result.errors = [...Utils.extractErrors(response.error , result.donorNotes)]
                }                
            }
            
            dispatch(onSave(result));
        }).catch(error => {          
            throw(error);
        });            
        
    };
}

export function saveAllEdits(donorNotesList) {
    return function(dispatch) {
        var allErrors = [];
        for (var donorNotes of donorNotesList) {
            const errors = Utils.validateDonorNotes(donorNotes);
            allErrors = [...allErrors, ...errors];
        }
        
        if(allErrors.length > 0 ){
            const result = {};
            result.donorNotesList = donorNotesList;
            result.errors = allErrors;
            result.infoMessages = [];
            return dispatch(onSaveAllEdits(result));            
        } 
        
        
        return donorNotesApi.save(donorNotesList).then(response => {
            const result = {errors:[], infoMessages: []};
            
            if (response.error) {
                results.errors = Utils.extractErrors(response.error); 
                return dispatch(onSaveAllEdits(result));
            }
            
            const list = [];
            for (var item of response) {                    
                if (item.result === "SAVED"){
                    item.data.isEditing = false;
                    list.push(item.data);                       
                } else {                        
                    allErrors = [...allErrors, ...Utils.extractErrors(item.errors, item.data)]
                }                    
            }               
            
            result.donorNotesList = list;
            result.errors = [...result.errors, ...allErrors];
            if (list.length > 0) {
                var params = {};
                params.saved = list.length;
                params.total = donorNotesList.length;
                result.infoMessages = [{messageKey: 'amp.gpi-data-donor-notes:save-all', params: params}];  
            }                
            
            dispatch(onSaveAllEdits(result));
        }).catch(error => {          
            throw(error);
        });            
    };
    
}
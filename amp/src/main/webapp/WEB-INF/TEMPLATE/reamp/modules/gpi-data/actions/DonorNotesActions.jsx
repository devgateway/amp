import donorNotesApi from '../api/DonorNotesApi.jsx';
import Utils from '../common/utils.jsx';

export function getDonorNotesListSuccess(data){
    return {type: 'LOAD_DONOR_NOTES_LIST_SUCCESS', data: data }
}

export function deleteSuccess(data){
    return {type: 'DONOR_NOTES_DELETE_SUCCESS', data: data } 
}

export function onSave(data){
    return {type: 'DONOR_NOTES_ON_SAVE', data: data } 
}

export function onSaveAllEdits(data){
    return {type: 'DONOR_NOTES_ON_SAVE_ALL_EDITS', data: data } 
}

export function addNewDonorNotes(indicatorCode) {    
    return {type: 'ADD_DONOR_NOTES', data: {donorNotes: {isEditing: true, indicatorCode: indicatorCode}, indicatorCode: indicatorCode} } 
}

export function updateDonorNotes(donorNotes, indicatorCode) {
    return {type: 'UPDATE_DONOR_NOTES', data: {donorNotes: donorNotes, errors: [], infoMessages:[], indicatorCode: indicatorCode} } 
}


export function loadDonorNotesList(data, indicatorCode) {
    return function(dispatch) {
        return donorNotesApi.getDonorNotesList(data, indicatorCode).then(response => {
            
            var results = {
                    donorNotesList: [],                    
                    errors: [],
                    infoMessages: [],
                    indicatorCode: indicatorCode
            };
            
            results.paging = data.paging;
            results.sorting = data.sorting;             
            if (response.error) {
                results.errors = Utils.extractErrors(response.error);                
            } else {
                results.donorNotesList = response.data;
                results.paging.totalRecords = response.totalRecords;
                results.paging.totalPageCount = Math.ceil(results.paging.totalRecords / results.paging.recordsPerPage);                
            }  
            
            dispatch(getDonorNotesListSuccess(results));
        }).catch(error => {
            throw(error);
        });
    };
}

export function save(data, indicatorCode){    
    return function(dispatch) {
        const errors = Utils.validateDonorNotes(data);
        if(errors.length > 0 ){
            const result = {};
            result.donorNotes = data;
            result.errors = errors;
            result.infoMessages = [];
            result.indicatorCode = indicatorCode;
            return dispatch(onSave(result));            
        } 

        let cid = data.cid;
        return donorNotesApi.save(data).then(response => {
            const result = {errors: [], indicatorCode: indicatorCode};
            result.donorNotes = response.data || data;
            if (cid) {
                result.donorNotes.cid = cid;
            }
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

export function deleteDonorNotes(data, indicatorCode) {
    return function(dispatch) {
        if (data.id) {            
            return donorNotesApi.deleteDonorNotes(data).then(response => {
                const result = {infoMessages: [], errors: [], indicatorCode: indicatorCode};
                result.donorNotes = data;
                if(response.error){
                    result.errors = [...Utils.extractErrors(response.error , result.donorNotes)]
                } else{
                    result.infoMessages = [{messageKey: 'amp.gpi-data-donor-notes:delete-successful'}]; 
                }
                
                dispatch(deleteSuccess(result));
            }).catch(error => {
                throw(error);
            });   
        } else {
            const result = {
                    donorNotes: data,
                    infoMessages: [{messageKey: 'amp.gpi-data-donor-notes:delete-successful'}],
                    indicatorCode: indicatorCode
            };
            dispatch(deleteSuccess(result));
        }        
    }; 
}

export function removeFromState(data, indicatorCode) {
    return function(dispatch) {
        const result = {
                donorNotes: data,
                infoMessages: [],
                indicatorCode: indicatorCode
        };
        dispatch(deleteSuccess(result))  
    };    
}

export function saveAllEdits(donorNotesList, indicatorCode) {
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
            result.indicatorCode = indicatorCode;
            return dispatch(onSaveAllEdits(result));            
        }

        let cids = Utils.getCids(donorNotesList);
        return donorNotesApi.save(donorNotesList).then(response => {
            const result = {errors:[], infoMessages: [], indicatorCode: indicatorCode};
            
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
            
            result.donorNotesList = Utils.restoreCids(list, cids);
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
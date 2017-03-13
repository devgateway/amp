class Utils {    
    static isNumber(input) {
        return typeof(input) != "boolean" && !isNaN(input);
    }
    
    static isUndefinedOrBlank(obj, field) {
        var result = false;
        if(obj[field] === '' || !obj[field]){
            result = true;
        }
        
        return result;        
    }
    
    static validateAidOnBudget(aidOnBudget){
        const errors = [];
        if (!this.isNumber(aidOnBudget['amount']) || this.isUndefinedOrBlank(aidOnBudget, 'amount')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-amount-invalid', id: aidOnBudget.id, cid: aidOnBudget.cid, affectedFields:['amount']});            
        } 
        
        if(this.isUndefinedOrBlank(aidOnBudget, 'donorId')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-donor-agency-required', id: aidOnBudget.id, cid: aidOnBudget.cid, affectedFields:['donorId']});
        }
        
        if(this.isUndefinedOrBlank(aidOnBudget, 'currencyCode')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-currency-required', id: aidOnBudget.id, cid: aidOnBudget.cid, affectedFields:['currencyCode']});
        }
        
        if(this.isUndefinedOrBlank(aidOnBudget, 'indicatorDate')){
            errors.push({messageKey: 'amp.gpi-data-aid-on-budget:validation-date-required', id: aidOnBudget.id, cid: aidOnBudget.cid, affectedFields:['indicatorDate']}); 
        }
        
        return errors
    }
    
    static validateDonorNotes(donorNotes){
        const errors = [];
        var message;
        if (this.isUndefinedOrBlank(donorNotes, 'donorId')) {
            message = {messageKey: 'amp.gpi-data:validation-all-fields-required', id: donorNotes.id, cid: donorNotes.cid, affectedFields:['donorId']};
        }    
        
        if (this.isUndefinedOrBlank(donorNotes, 'notesDate')) {
            if (message) {
                message.affectedFields.push('notesDate');                
            } else {
                message = {messageKey: 'amp.gpi-data:validation-all-fields-required', id: donorNotes.id, cid: donorNotes.cid, affectedFields:['notesDate']};
            }            
        }
        
        if (this.isUndefinedOrBlank(donorNotes, 'notes')) {
            if (message) {
                message.affectedFields.push('notes');                
            } else {
                message = {messageKey: 'amp.gpi-data:validation-all-fields-required', id: donorNotes.id, cid: donorNotes.cid, affectedFields:['notes']};
            }            
        }   
        
        if (message) {
            errors.push(message)
        }
        
        return errors
    }
    
    static extractErrors(errors, obj) {
        var errorMessages = [];    
        if (errors) {  
            errors = Array.isArray(errors) ? errors : [errors];
            errors.forEach(function(error){                
                for (var key in error) {                    
                    let messageKey = 'amp.gpi-data:server-errors-' + key;
                    let message = {messageKey: messageKey}; 
                    if (obj && obj.id) {
                        message.id = obj.id;
                    }
                    
                    if (obj && obj.cid) {
                        message.cid = obj.cid;
                    }
                    
                    errorMessages.push(message);
                }
            }); 
        }  
        
        return errorMessages;
    }
}

export default Utils;

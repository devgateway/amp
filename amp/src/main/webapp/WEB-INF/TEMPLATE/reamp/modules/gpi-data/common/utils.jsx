class Utils {    
    static isNumber(input) {
        return typeof(input) != "boolean" && !isNaN(input);
    }
    
    static isUndefinedOrBlank(obj, field) {        
        var result = false;
        if (obj[field] == null || this.trim(obj[field]) === '') {
            result = true;
        }        
        return result;        
    }
    
    static trim(str) {
        if(str && typeof str === 'string'){
            return str.trim();
        }
        
        return str
    }
    
    static validateAidOnBudget(aidOnBudget){
        const errors = [];
        var message;        
        if (!this.isNumber(aidOnBudget['amount']) || this.isUndefinedOrBlank(aidOnBudget, 'amount')){
            message = {messageKey: 'amp.gpi-data:validation-all-fields-required', id: aidOnBudget.id, cid: aidOnBudget.cid, affectedFields:['amount']};                        
        }  
        
        message = this.checkRequiredField(aidOnBudget,'donorId', message);
        message = this.checkRequiredField(aidOnBudget,'currencyCode', message);
        message = this.checkRequiredField(aidOnBudget,'indicatorDate', message);         
        if (message) {
            errors.push(message);
        }
        
        return errors
    }
       
   static validateDonorNotes(donorNotes){
        const errors = [];
        var message;
        
        message = this.checkRequiredField(donorNotes,'donorId', message);
        message = this.checkRequiredField(donorNotes,'notesDate', message);
        message = this.checkRequiredField(donorNotes,'notes', message);        
        if (message) {
            errors.push(message)
        }
        
        return errors
    }
   
   static checkRequiredField(obj, field, message) {        
       if(this.isUndefinedOrBlank(obj, field)){
           if (message) {
               message.affectedFields.push(field);
           } else {
               message = {messageKey: 'amp.gpi-data:validation-all-fields-required', id: obj.id, cid: obj.cid, affectedFields:[field]}
           }              
       }       
       return message;
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
    
    static capitalizeFirst(str) {               
        if (str) {
            return str.charAt(0).toUpperCase() + str.substr(1).toLowerCase();             
        } 
        
        return str;        
    }

    static getCids(data) {
        return data.map(el => el.cid);
    }

    static restoreCids(data, cids) {
        return data.map((el, idx) => {
            let cid = cids[idx];
            return cid ? {cid: cids[idx], ...el} : el;
        });
    }
}

export default Utils;

class Utils { 
static extractErrors(errors, obj) {
        var errorMessages = [];    
        if (errors) {  
            errors = Array.isArray(errors) ? errors : [errors];
            errors.forEach(function(error){                
                for (var key in error) {                    
                    let messageKey = 'amp.data-freeze:server-errors-' + key;
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
class Utils { 
    static capitalizeFirst(str) {               
        if (str) {
            return str.charAt(0).toUpperCase() + str.substr(1).toLowerCase();             
        } 
        
        return str;        
    }
    
    static validatePerformanceRule(rule){
        const errors = [];
        let message;
        message = this.checkRequiredField( rule, 'name', message );
        message = this.checkRequiredField( rule.level || {}, 'id', message );
        message = this.checkRequiredField( rule, 'typeClassName', message );
        if ( message ) {
            errors.push( message );
        }
        
        return errors;        
    }
    
    static isUndefinedOrBlank( obj, field ) {
        var result = false;
        if ( obj[field] === '' || obj[field] === undefined || obj[field] === null ) {
            result = true;
        }
        return result;
    }
    
    static checkRequiredField( obj, field, message ) {
        if ( this.isUndefinedOrBlank( obj, field ) ) {
            if ( message ) {
                message.affectedFields.push( field );
            } else {
                message = { messageKey: 'amp.performance-rule:required-fields-message', id: obj.id, affectedFields: [field] }
            }
        }
        return message;
    }
}

export default Utils;
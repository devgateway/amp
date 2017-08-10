import * as Constants from '../common/Constants';
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
        message = this.checkRequiredField( rule, Constants.FIELD_NAME, message );
        message = this.checkRequiredField( rule.level || {}, Constants.FIELD_ID, message );
        message = this.checkRequiredField( rule, Constants.FIELD_TYPE, message );
        if ( message ) {
            errors.push( message );
        }
        errors.push(...this.validateAttributes(rule));
        return errors;        
    }
    
    static isUndefinedOrBlank( obj, field ) {
        let result = false;
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
                message = { messageKey: 'amp.performance-rule:required-fields-message', id: obj.id, affectedFields: [field] };
            }
        }
        return message;
    }
    
    static validateAttributes(rule){
        const errors = [];
        const attributes = rule[Constants.FIELD_ATTRIBUTES] || [];
        for (let attribute of attributes) {
           if(attribute.type === Constants.FIELD_TYPE_AMOUNT) {
               if(!this.isNumber(attribute.value)){
                   errors.push({ messageKey: 'amp.performance-rule:invalid-input', id: rule.id, affectedFields: [attribute.name] }); 
               }
           }
        }
        return errors;        
    }
    
    static isNumber(input) {
        return typeof(input) != "boolean" && !isNaN(input);
    }
}

export default Utils;
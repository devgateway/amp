import * as Constants from '../common/Constants';

class Utils {
    static capitalizeFirst(str) {               
        if (str) {
            return str.charAt(0).toUpperCase() + str.substr(1).toLowerCase();             
        } 
        
        return str;        
    }
    
    static validatePerformanceRule(rule, attributeList){
        const errors = [];
        let message;
        message = this.checkRequiredField( rule, Constants.FIELD_NAME, message );
        message = this.checkRequiredField( rule.level || {}, Constants.FIELD_ID, message );
        message = this.checkRequiredField( rule, Constants.FIELD_TYPE, message );
        if ( message ) {
            errors.push( message );
        }
        errors.push(...this.validateAttributes(rule, attributeList));
        return errors;        
    }
    
    static isUndefinedOrBlank(obj, field ) {
        return this.valueIsUndefinedOrBlank(obj[field]);        
    }
    
    static valueIsUndefinedOrBlank(value) {
        let result = false;
        if ( value === '' || value === undefined || value === null ) {
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
    
    static validateAttributes(rule, attributeList){        
        const errors = [];
        const attributes = rule[Constants.FIELD_ATTRIBUTES] || [];               
        for (let attr of attributeList) {
            let attribute = attributes.filter(obj => obj.name === attr.name)[0] || {};
            if(this.valueIsUndefinedOrBlank(attribute.value)) {
               const error = errors.filter(error => error.messageKey === 'amp.performance-rule:parameters-required')[0];            
               if (error) {
                   error.affectedFields.push(attr.name);
               } else {
                   errors.push({ messageKey: 'amp.performance-rule:parameters-required', id: rule.id, affectedFields: [attr.name] });
               }            
           }
            
           if(attr.type === Constants.FIELD_TYPE_AMOUNT) {
               if(!this.isNumber(attribute.value) || attribute.value < 0){
                   errors.push({ messageKey: 'amp.performance-rule:invalid-input', id: rule.id, affectedFields: [attr.name] }); 
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
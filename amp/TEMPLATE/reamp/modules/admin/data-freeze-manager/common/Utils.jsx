import moment from 'moment';
import * as Constants from '../common/Constants';
class Utils {
    static extractErrors( errors, obj ) {
        var errorMessages = [];
        if ( errors ) {
            errors = Array.isArray( errors ) ? errors : [errors];
            errors.forEach( function( error ) {
                for ( var key in error ) {
                    let messageKey = 'amp.data-freezing:server-errors-' + key;
                    let message = { messageKey: messageKey };
                    if ( obj && obj.id ) {
                        message.id = obj.id;
                    }

                    if ( obj && obj.cid ) {
                        message.cid = obj.cid;
                    }

                    errorMessages.push( message );
                }
            });
        }

        return errorMessages;
    }


    static isUndefinedOrBlank( obj, field ) {
        var result = false;
        if ( obj[field] === '' || obj[field] === undefined || obj[field] === null ) {
            result = true;
        }
        return result;
    }

    static validateDataFreezeEvent( dataFreezeEvent ) {
        const errors = [];
        var message;

        message = this.checkRequiredField( dataFreezeEvent, 'freezingDate', message );
        message = this.checkRequiredField( dataFreezeEvent, 'sendNotification', message );
        message = this.checkRequiredField( dataFreezeEvent, 'freezeOption', message );
        if ( message ) {
            errors.push( message );
        }
        errors.push( ...this.validateFreezingDate( dataFreezeEvent ) );
        errors.push( ...this.validateStartAndEndDate( dataFreezeEvent ) );
        errors.push( ...this.validateGracePeriod( dataFreezeEvent ) );
        return errors
    }

    static validateFreezingDate( dataFreezeEvent ) {
        let errors = [];
        let freezingDate = moment( dataFreezeEvent.freezingDate, Constants.EP_DATE_FORMAT );
        let today = moment();
        if ( today.startOf( 'day' ).isAfter( freezingDate ) ) {
            errors.push( { messageKey: 'amp.data-freezing:invalid-freeze-date', id: dataFreezeEvent.id, cid: dataFreezeEvent.cid, affectedFields: ['freezingDate'] });
        }
        return errors;
    }

    static validateStartAndEndDate( dataFreezeEvent ) {
        let errors = [];
        
        if((dataFreezeEvent.openPeriodStart && !dataFreezeEvent.openPeriodEnd) || (!dataFreezeEvent.openPeriodStart && dataFreezeEvent.openPeriodEnd)) {
            errors.push( { messageKey: 'amp.data-freezing:invalid-open-period', id: dataFreezeEvent.id, cid: dataFreezeEvent.cid, affectedFields: ['openPeriodStart', 'openPeriodEnd'] });
        }
            
        if (dataFreezeEvent.openPeriodStart && dataFreezeEvent.openPeriodEnd) {
            let openPeriodStart = moment( dataFreezeEvent.openPeriodStart, Constants.EP_DATE_FORMAT );
            let openPeriodEnd = moment( dataFreezeEvent.openPeriodEnd, Constants.EP_DATE_FORMAT );
            let freezingDate = moment( dataFreezeEvent.freezingDate, Constants.EP_DATE_FORMAT );
            if ( openPeriodStart.isAfter( freezingDate ) === false ) {
                errors.push( { messageKey: 'amp.data-freezing:start-date-should-be-after-freeze-date', id: dataFreezeEvent.id, cid: dataFreezeEvent.cid, affectedFields: ['openPeriodStart', 'freezingDate'] });
            }

            if ( openPeriodStart.isAfter( openPeriodEnd ) ) {
                errors.push( { messageKey: 'amp.data-freezing:start-date-should-not-be-greater-than-end-date', id: dataFreezeEvent.id, cid: dataFreezeEvent.cid, affectedFields: ['openPeriodStart', 'openPeriodEnd'] });
            } 
        }        
        return errors;

    }

    static validateGracePeriod( dataFreezeEvent ) {
        let errors = [];
        if ( dataFreezeEvent.gracePeriod < 0 ) {
            errors.push( { messageKey: 'amp.data-freezing:invalid-grace-period', id: dataFreezeEvent.id, cid: dataFreezeEvent.cid, affectedFields: ['gracePeriod'] });
        }
        return errors;
    }

    static checkRequiredField( obj, field, message ) {
        if ( this.isUndefinedOrBlank( obj, field ) ) {
            if ( message ) {
                message.affectedFields.push( field );
            } else {
                message = { messageKey: 'amp.data-freezing:required-fields-message', id: obj.id, cid: obj.cid, affectedFields: [field] }
            }
        }
        return message;
    }

    static extractErrors( errors, obj ) {
        var errorMessages = [];
        if ( errors ) {
            errors = Array.isArray( errors ) ? errors : [errors];
            errors.forEach( function( error ) {
                for ( var key in error ) {
                    let messageKey = 'amp.data-freezing:server-errors-' + key;
                    let message = { messageKey: messageKey };
                    if ( obj && obj.id ) {
                        message.id = obj.id;
                    }

                    if ( obj && obj.cid ) {
                        message.cid = obj.cid;
                    }

                    errorMessages.push( message );
                }
            });
        }

        return errorMessages;
    }


}

export default Utils;
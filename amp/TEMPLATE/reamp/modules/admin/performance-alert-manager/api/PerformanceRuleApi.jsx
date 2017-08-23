import { postJson, delay, fetchJson, deleteJson, putJson } from 'amp/tools';
import { loadTranslations } from 'amp/modules/translate';
class PerformanceRuleApi {
    static getPerformanceRuleList( data ) {
        const url = '/rest/performance/admin/?page=' + data.paging.currentPageNumber + '&size=' + data.paging.recordsPerPage;
        return new Promise(( resolve, reject ) => {
            return fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }

    static save( data ) {
        const saveFunc = data.id ? putJson : postJson;
        const url = data.id ? '/rest/performance/rules/' + data.id : '/rest/performance/rules';
        return new Promise(( resolve, reject ) => {
            return saveFunc(url, data ).then((response) => {
                resolve({status: response.status} );
            }).catch((error) => {               
                reject( error );
            });
        });
    }

    static deletePerformanceRule( data ) {
        const url = '/rest/performance/rules/' + data.id;
        return new Promise(( resolve, reject ) => {
            return deleteJson( url, {}).then( response => {
                resolve( {});
            }).catch( error => {
                reject( error );
            });
        });
    }

    static getTypeList() {
        const url = '/rest/performance/types';
        return new Promise(( resolve, reject ) => {
            return fetchJson( url ).then(( response ) => {
                const toTranslate = {};
                response.forEach( function( ruleType ) {
                    toTranslate[ruleType.name + 'TranslatedDescription'] = ruleType.description;
                    toTranslate[ruleType.name + 'TranslatedMessage'] = ruleType.message;
                });
                
                return loadTranslations( toTranslate ).then( trns => {                   
                   for ( let key in trns ) {                       
                       const name = key.endsWith('TranslatedDescription') ? key.replace('TranslatedDescription','') : key.replace('TranslatedMessage','');
                       const rule = response.filter(ruleType => ruleType.name === name)[0];
                       rule[key] = trns[key];                      
                   }                  
                   resolve( response );
                });                
                                
            }).catch(( error ) => {
                reject( error );
            });
        });
    }
    
    static getLevelList() {
        const url = '/rest/performance/levels';
        return new Promise(( resolve, reject ) => {
            return fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }
    
    static getAttributeList(ruleType){
        const url = '/rest/performance/attributes?rule-type=' + ruleType;
        return new Promise(( resolve, reject ) => {
            return fetchJson( url ).then(( response ) => {
                const toTranslate = {};
                response.forEach( function( atrr ) {
                    toTranslate[atrr.name + 'TranslatedDescription'] = atrr.description;                    
                });
                
                return loadTranslations( toTranslate ).then( trns => {                   
                   for ( let key in trns ) {                       
                       const name = key.replace('TranslatedDescription','');
                       const attribute = response.filter(attr => attr.name === name)[0];
                       attribute[key] = trns[key];                      
                   }                  
                   resolve( response );
                });                
            }).catch(( error ) => {
                reject( error );
            });
        }); 
    }

}

export default PerformanceRuleApi;
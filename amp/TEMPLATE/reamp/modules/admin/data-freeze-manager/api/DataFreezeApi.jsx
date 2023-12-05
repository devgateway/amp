import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
import { loadTranslations } from 'amp/modules/translate';
import {ACCESS_DENIED_STATUS} from  '../common/Constants';
class DataFreezeApi {
    static getDataFreezeEventList( data ) {
        const url = '/rest/data-freeze/event/list?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder;
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                if (response && response.status == ACCESS_DENIED_STATUS) {
                    this.redirectIfAccessDenied(response);
                } else {
                    resolve( response );  
                }               
            }).catch(( error ) => {
                reject( error );
            });
        });
    }
    
    static getFrozenActivities() {
          const url = '/rest/data-freeze/event/list-frozen-activities';
          return new Promise((resolve, reject) => {
               fetchJson(url).then((response) => {
                   if (response && response.status == ACCESS_DENIED_STATUS) {
                       this.redirectIfAccessDenied(response);
                   } else {
                       resolve(response)  
                   }                   
               }).catch((error) => {
                    reject(error);
               });
          });
    }
     
    static save( data ) {
        delete data.isEditing
        return new Promise(( resolve, reject ) => {
            postJson( '/rest/data-freeze/event', data ).then( response => {                
                if (response && response.status == ACCESS_DENIED_STATUS) {
                    this.redirectIfAccessDenied(response); 
                } else {
                    resolve( response.json() ); 
                }               
            }).catch( error => {
                reject( error );
            });
        });
    }

    static deleteDataFreezeEvent( data ) {
        var url = '/rest/data-freeze/event/' + data.id;
        return new Promise(( resolve, reject ) => {
            deleteJson( url, {}).then( response => {
                if (response && response.status == ACCESS_DENIED_STATUS) {
                    this.redirectIfAccessDenied(response);
                } else {
                    resolve( {}); 
                }               
            }).catch( error => {
                reject( error );
            });
        });
    }

    static unfreezeAll() {
        return new Promise(( resolve, reject ) => {
            postJson( '/rest/data-freeze/event/unfreeze-all', {}).then( response => {
                if (response && response.status == ACCESS_DENIED_STATUS) {
                    this.redirectIfAccessDenied(response);
                } else {
                    resolve( { result: 'SUCCESSFUL' }); 
                }                
            }).catch( error => {
                reject( { result: 'FAILED', error: error });
            });
        });
    }
    
    static redirectIfAccessDenied(response) {
        if (response && response.status == ACCESS_DENIED_STATUS) {
            window.location.href = '/';           
        }       
    }
}

export default DataFreezeApi;
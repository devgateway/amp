import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
class DataFreezeApi {
    static getDataFreezeEventList( data ) {
        const url = '/rest/data-freeze/event/list?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder;
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }

     static getFrozenActivities() {
          const url = '/rest/data-freeze/event/list-frozen-activities';
          return new Promise((resolve, reject) => {
               fetchJson(url).then((response) => {
                    resolve(response)
               }).catch((error) => {
                    reject(error);
               });
          });
     }
    static save( data ) {
        delete data.isEditing
        return new Promise(( resolve, reject ) => {
            postJson( '/rest/data-freeze/event', data ).then( response => {
                resolve( response.json() );
            }).catch( error => {
                reject( error );
            });
        });
    }

    static deleteDataFreezeEvent( data ) {
        var url = '/rest/data-freeze/event/' + data.id;
        return new Promise(( resolve, reject ) => {
            deleteJson( url, {}).then( response => {
                resolve( {});
            }).catch( error => {
                reject( error );
            });
        });
    }

    static unfreezeAll() {
        return new Promise(( resolve, reject ) => {
            postJson( '/rest/data-freeze/event/unfreeze-all', {}).then( response => {
                resolve( { result: 'SUCCESSFUL' });
            }).catch( error => {
                reject( { result: 'FAILED', error: error });
            });
        });
    }
}

export default DataFreezeApi;
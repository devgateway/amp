import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
class PerformanceAlertRuleApi {
    static getPerformanceAlertRuleList( data ) {
        const url = '/rest/performance/rules/list?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder;
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }

    static save( data ) {
        delete data.isEditing
        return new Promise(( resolve, reject ) => {
            postJson( '/rest/performance/rules', data ).then( response => {
                resolve( response.json() );
            }).catch( error => {
                reject( error );
            });
        });
    }

    static deletePerformanceAlertRule( data ) {
        var url = '/rest/performance/rules/' + data.id;
        return new Promise(( resolve, reject ) => {
            deleteJson( url, {}).then( response => {
                resolve( {});
            }).catch( error => {
                reject( error );
            });
        });
    }


}

export default PerformanceAlertRuleApi;
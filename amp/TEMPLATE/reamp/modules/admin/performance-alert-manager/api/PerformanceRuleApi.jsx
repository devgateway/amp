import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
class PerformanceRuleApi {
    static getPerformanceRuleList( data ) {
        const url = '/rest/performance/admin/?page=' + data.paging.currentPageNumber + '&size=' + data.paging.recordsPerPage;
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

    static deletePerformanceRule( data ) {
        var url = '/rest/performance/rules/' + data.id;
        return new Promise(( resolve, reject ) => {
            deleteJson( url, {}).then( response => {
                resolve( {});
            }).catch( error => {
                reject( error );
            });
        });
    }

    static getTypeList() {
        const url = '/rest/performance/types';
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }
    
    static getLevelList() {
        const url = '/rest/performance/levels';
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }
    
    static getAttributeList(ruleType){
        const url = '/rest/performance/attributes?ruleType=' + ruleType;
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });
        }); 
    }

}

export default PerformanceRuleApi;
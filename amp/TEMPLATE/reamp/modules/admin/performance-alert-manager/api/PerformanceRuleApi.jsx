import { postJson, delay, fetchJson, deleteJson, putJson } from 'amp/tools';
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
        const saveFunc = data.id ? putJson : postJson;
        const url = data.id ? '/rest/performance/rules/' + data.id : '/rest/performance/rules';
        return new Promise(( resolve, reject ) => {
            saveFunc(url, data ).then((response) => {
                resolve({status: response.status} );
            }).catch((error) => {               
                reject( error );
            });
        });
    }

    static deletePerformanceRule( data ) {
        const url = '/rest/performance/rules/' + data.id;
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
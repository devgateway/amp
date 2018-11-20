import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {

    static getSettings() {
        return new Promise(( resolve, reject ) => {
            fetchJson( '/rest/amp/settings' ).then(( response ) => {
                resolve( response );
            }).catch(( error ) => {
                reject( error );
            });
        });
    }

    static getUserInfo() {
        var url = '/rest/security/user';
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( userInfo ) => {
                resolve( userInfo )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }


}

export default CommonListsApi;
import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
import { REPORTS_CONFIGURATION } from '../common/ReportsConfiguration';
class ReportsApi {
    static fetchReportData( requestData, report ) {
        return new Promise(( resolve, reject ) => {
            postJson( REPORTS_CONFIGURATION[report].url, requestData ).then( response => {
                resolve( response.json() );

            }).catch( error => {
                reject( error );
            });
        });
    }

    static fetchRemarks( url ) {
        return new Promise(( resolve, reject ) => {
            fetchJson( url ).then(( remarks ) => {
                resolve( remarks )
            }).catch(( error ) => {
                reject( error );
            });
        });
    }

    static fetchSupportingEvidence( requestData ) {
        return new Promise(( resolve, reject ) => {
            postJson('/rest/gpi/report/documents/', requestData ).then( response => {                               
                resolve(response.json());
            }).catch( error => {
                reject( error );
            });
        });
    }

    static downloadFile( requestData, format, code ) {        
        delete requestData.recordsPerPage;
        delete requestData.page;
        let url = '/rest/gpi/report/export/' + format + '/' + code;
        this.postDownload( url, requestData, 'post' );
    }
    
    static postDownload( path, params, method ) {
        method = method || "post";
        var form = document.getElementById( "exportForm" );
        if ( !form ) {
            form = document.createElement( "form" );
            form.id = "exportForm";
        }
        form.setAttribute( "method", method );
        form.setAttribute( "action", path );
        form.setAttribute( "accept-charset", "UTF-8" );
        var query = document.createElement( "input" );
        form.appendChild(query);
        query.setAttribute( "type", "hidden" );
        query.setAttribute( "name", 'formParams' );
        query.setAttribute( "value", JSON.stringify(params));       
        document.body.appendChild( form );
        form.submit();
        $( form ).empty();
        $( form ).remove();
    }
}
export default ReportsApi;
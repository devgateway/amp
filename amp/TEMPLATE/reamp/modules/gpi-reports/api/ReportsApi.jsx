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
    
    static fetchSupportingEvidence( url ) {
        return new Promise(( resolve, reject ) => {
            resolve( 
                      {
                          "donorId" : 40,
                          "activityId" : 1232,
                          "documents" : [
                           {
                            "title" :  "Governement link",
                            "question" : "11a",
                            "description" : "Electronic link to project document",
                            "type" : "link",
                            "url" : "http://eth.amp.org/contentrepository/downloadFile.do?uuid=62d3bba9-d997-4411-b54b-659eb4c3aeb7"
                           },
                           {
                            "title" :  "Gov. document M&E",
                            "question" : "11c",
                            "description" : "Electronic link to gov. existing data source",
                            "type" : "document",
                            "url" : "http://eth.amp.org/contentrepository/downloadFile.do?uuid=62d3bba9-a897-5555-b54b-659eb4c3aeb9"
                           }
                          ]
                         }
                        )
            /*fetchJson( url ).then(( response ) => {
                resolve( response )
            }).catch(( error ) => {
                reject( error );
            });*/
        });
    }



}

export default ReportsApi;
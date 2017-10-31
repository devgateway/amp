import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {    
   static getOrgList(group) {  
       var url = group ? '/rest/filters/org-groups' : '/rest/gpi/report/donors/';
         return new Promise((resolve, reject) => {
            fetchJson(url).then((orgList) => {                
                resolve(orgList)
            }).catch((error) => {
                reject(error);
            });
        });
        
    }  
   
   static getYears(){
       return new Promise((resolve, reject) => {
           fetchJson('/rest/gpi/report/years/').then((response) => {                            
               resolve(response);
           }).catch((error) => {
               reject(error);
           });
       });
   }
   
   static fetchReportVisibilityConfiguration(  ) {
       return new Promise(( resolve, reject ) => {
           fetchJson('/rest/gpi/report/visibility').then(( conf ) => {
               resolve(conf )
           }).catch(( error ) => {
               reject( error );
           });
       });
   }
   
   static getCalendars(){
       return new Promise((resolve, reject) => {
           fetchJson('/rest/gpi/report/calendars/').then((response) => {                            
               resolve(response);
           }).catch((error) => {
               reject(error);
           });
       });
   }
   
   static getSettings() {          
       return new Promise((resolve, reject) => {
          fetchJson('/rest/amp/settings').then((response) => {
              resolve(response);
          }).catch((error) => {
              reject(error);
          });
      });   
   }
}

export default CommonListsApi;
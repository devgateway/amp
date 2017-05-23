import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {    
   static getOrgList(group) {  
         var url = group ? '/rest/filters/org-groups' : '/rest/filters/orgs';
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
           fetchJson('/rest/settings-definitions/reports').then((response) => {
               let yearRange = response.filter(setting => setting.id === 'year-range')[0];               
               let years = [];              
               if (yearRange && yearRange.value) {            
                   var rangeFrom = yearRange.value.rangeFrom;
                   var rangeTo = yearRange.value.rangeTo;
                   for (var i = rangeFrom; i <= rangeTo; i++) {
                      years.push(i);
                   }           
               }               
               resolve(years);
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
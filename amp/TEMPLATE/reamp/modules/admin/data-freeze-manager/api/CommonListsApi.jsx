import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {    
        
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
import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {    
    static getCurrencyList() {              
        return new Promise((resolve, reject) => {
            fetchJson('/rest/settings-definitions/gpi-data').then((response) => {
                resolve(response);
            }).catch((error) => {
                reject(error);
            });
        });
        
    }
    
    static getOrgList(verifiedOrgs) {  
         var url = verifiedOrgs ? '/rest/gpi/users-verified-orgs' : '/rest/filters/orgs';
         return new Promise((resolve, reject) => {
            fetchJson(url).then((orgList) => {
                resolve(orgList)
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
    
    static getUserInfo(){
        var url = '/rest/security/user';
        return new Promise((resolve, reject) => {
           fetchJson(url).then((userInfo) => {
               let user  = {};
               user["national-coordinator"] = userInfo ? userInfo["national-coordinator"] : false;               
               resolve(user)
           }).catch((error) => {
               reject(error);
           });
       });
    }
}

export default CommonListsApi;
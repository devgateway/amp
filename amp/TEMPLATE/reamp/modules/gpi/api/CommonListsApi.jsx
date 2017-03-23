import { postJson, delay, fetchJson } from 'amp/tools';
class CommonListsApi {    
    static getCurrencyList() {
        const request = new Request('/rest/settings-definitions/gpi', {
            method: 'GET'      
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
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
        const request = new Request('/rest/amp/settings', {
            method: 'GET'      
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
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
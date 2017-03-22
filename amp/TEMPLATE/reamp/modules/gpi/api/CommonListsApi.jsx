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
    
}

export default CommonListsApi;
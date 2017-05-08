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
}

export default CommonListsApi;
import { postJson, delay, fetchJson } from 'amp/tools';

/**
 *    
 */
class CommonListsApi {

    static getSettings() {  
        var url = '/rest/amp/settings';
            return new Promise((resolve, reject) => {
                fetchJson(url).then((settings) => {                
                    resolve(settings)
                }).catch((error) => {
                    reject(error);
                });
            });        
        } 

    static getActivity(activityId) {  
        var url = '/rest/activity/projects/' + activityId;
            return new Promise((resolve, reject) => {
            fetchJson(url).then((activity) => {                
                resolve(activity)
            }).catch((error) => {
                reject(error);
            });
        });        
    }  

    static getActivityInfo(activityId) {  
        var url = '/rest/activity/info/' + activityId;
            return new Promise((resolve, reject) => {
                fetchJson(url).then((activityInfo) => {                
                    resolve(activityInfo)
                }).catch((error) => {
                    reject(error);
                });
            });        
    } 

    static getFields(){
        return new Promise((resolve, reject) => {
            fetchJson('/rest/activity/fields/').then((fields) => {                            
                resolve(fields);
            }).catch((error) => {
                reject(error);
            });
        });
    }

    static getFieldSubList(parentName, childrenName){
        let fieldName = childrenName ? parentName + '~' + childrenName : parentName;
        return new Promise((resolve, reject) => {
            fetchJson('/rest/activity/fields/' + fieldName).then((fields) => {                            
                resolve(fields);
            }).catch((error) => {
                reject(error);
            });
        });
    }   
   
    static getFundingData(activityId, currencyId) {  
        var url = '/rest/activity/' + activityId + '/preview/fundings?currency-id=' + currencyId;
            return new Promise((resolve, reject) => {
                fetchJson(url).then((fundingInfo) => {                
                    resolve(fundingInfo)
                }).catch((error) => {
                    reject(error);
                });
            });        
    } 

    static fetchFieldsData(requestData) {
        return new Promise((resolve, reject ) => {
            postJson('/rest/activity/field/id-values', requestData).then(response => {
                resolve(response.json() );

            }).catch(error => {
                reject(error );
            });
        });
    }


    static fetchResources(requestData) {
        return new Promise((resolve, reject ) => {
            postJson('/rest/resource', requestData).then(response => {
                resolve(response.json() );

            }).catch(error => {
                reject(error );
            });
        });
    }
   
}

export default CommonListsApi;
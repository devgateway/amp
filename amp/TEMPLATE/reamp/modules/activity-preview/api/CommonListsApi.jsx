import { postJson, delay, fetchJson } from 'amp/tools';

class CommonListsApi {    

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
       return new Promise((resolve, reject) => {
           fetchJson('/rest/activity/fields/' + parentName + '~' + childrenName).then((fields) => {                            
               resolve(fields);
           }).catch((error) => {
               reject(error);
           });
       });
   }   
   
}

export default CommonListsApi;
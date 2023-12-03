import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
class AidOnBudgetApi {  
    
    static getAidOnBudgetList(data) { 
        const url = '/rest/gpi/aid-on-budget?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder + '&timestamp=' + Date.now();   
        return new Promise((resolve, reject) => {
            fetchJson(url).then((response) => {
                resolve(response)
            }).catch((error) => {
                reject(error);
            });
        });
    }
    
    static save(data) { 
        const url = Array.isArray(data) ? '/rest/gpi/aid-on-budget/save-all' : '/rest/gpi/aid-on-budget'; 
        return new Promise((resolve, reject) => {
            postJson(url, data).then(response => {
                resolve(response.json());
            }).catch(error => {
                reject(error);
            });
        });        
    }    
    
    static deleteAidOnBudget(aidOnBudget) {        
        var url = '/rest/gpi/aid-on-budget/' + aidOnBudget.id;         
        return new Promise((resolve, reject) => {
            deleteJson(url, {}).then(response => {
                resolve(response.json());
            }).catch(error => {
                reject(error);
            });
        });  
    }
    
}

export default AidOnBudgetApi;
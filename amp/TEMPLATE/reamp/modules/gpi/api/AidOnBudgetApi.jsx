import { postJson, delay, fetchJson } from 'amp/tools';
class AidOnBudgetApi {  
    
    static getAidOnBudgetList(data) { 
        const url = '/rest/gpi/aid-on-budget?offset=' + data.paging.offset + '&count=' + data.paging.recordsPerPage + '&orderby=' + data.sorting.orderBy + '&sort=' + data.sorting.sortOrder;    
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
        const request = new Request(url, {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify(data)
        });
        
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        });
    }
    
    
    static deleteAidOnBudget(aidOnBudget) {      
        const request = new Request('/rest/gpi/aid-on-budget/' + aidOnBudget.id, {
            method: 'DELETE',
            headers: {'Content-Type':'application/json'}
        });
        return fetch(request).then(response => {
            return response.json();
        }).catch(error => {
            return error;
        })
        
    }
    
}

export default AidOnBudgetApi;
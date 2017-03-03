class AidOnBudgetApi {

 
  static getAidOnBudgetList() {   
    const request = new Request('/rest/gpi/aid-on-budget', {
      method: 'GET'      
    });

    return fetch(request).then(response => {
      return response.json();
    }).catch(error => {
      return error;
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
        method: 'DELETE'        
   });
   return fetch(request).then(response => {
          return response.json();
        }).catch(error => {
          return error;
    })

  }
 
}

export default AidOnBudgetApi;
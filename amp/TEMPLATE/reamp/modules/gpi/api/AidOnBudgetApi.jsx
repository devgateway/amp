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

  static save(aidOnBudget) {
    
    const request = new Request('/rest/gpi/aid-on-budget', {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(aidOnBudget)
    });
    debugger
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
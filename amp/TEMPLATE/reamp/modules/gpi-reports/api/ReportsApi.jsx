import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
import { REPORTS_CONFIGURATION } from '../common/ReportsConfiguration';
class ReportsApi {     
 
   
    static fetchReportData(requestData, report) {         
        return new Promise((resolve, reject) => {
            postJson(REPORTS_CONFIGURATION[report] .url, requestData).then(response => {
                resolve(response.json());
                resolve(sampleOutput);
            }).catch(error => {
                reject(error);
            });
        }); 
    }
    
}

export default ReportsApi;
import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
import { REPORTS_CONFIGURATION } from '../common/ReportsConfiguration';
class ReportsApi {     
 
   
    static fetchReportData(requestData, report) {         
        return new Promise((resolve, reject) => {
            postJson(REPORTS_CONFIGURATION[report] .url, requestData).then(response => {
                resolve(response.json());
               
            }).catch(error => {
                reject(error);
            });
        }); 
    }
    
    static fetchRemarks(url) {         
        return new Promise((resolve, reject) => {
            resolve([{
                date : '2017-04-04',
                remark : 'remark1'
            }, {
                date : '2017-04-05',
                remark : 'remark2'
            }
        ]);
            /*postJson(url, {}).then(response => {
                resolve(response.json());
                
            }).catch(error => {
                reject(error);
            });*/
        }); 
    }
    
 
    
}

export default ReportsApi;
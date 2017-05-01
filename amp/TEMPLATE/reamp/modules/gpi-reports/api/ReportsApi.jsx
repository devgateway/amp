import { postJson, delay, fetchJson, deleteJson } from 'amp/tools';
import { REPORTS_CONFIGURATION } from '../common/ReportsConfiguration';
class ReportsApi {     
 
   
    static fetchReportData(requestData, report) { 
        var sampleOutput = {
                "page": {
                  "headers": [
                    {
                      "columnName": "Year",
                      "originalColumnName": "Year",
                      "description": null
                    },
                    {
                      "columnName": "Donor Group",
                      "originalColumnName": "Donor Group",
                      "description": null
                    },
                    {
                      "columnName": "National Budget Execution Procedures",
                      "originalColumnName": "National Budget Execution Procedures",
                      "description": "Lorem ipsum description"
                    },
                    {
                      "columnName": "National Financial Reporting Procedures",
                      "originalColumnName": "National Financial Reporting Procedures",
                      "description": "Lorem ipsum description"
                    },
                    {
                      "columnName": "National Auditing Procedures",
                      "originalColumnName": "National Auditing Procedures",
                      "description": "Lorem ipsum description"
                    },
                    {
                      "columnName": "National Procurement Execution Procedures",
                      "originalColumnName": "National Procurement Execution Procedures",
                      "description": "Lorem ipsum description"
                    }
                  ],
                  "contents": [
                    {
                      "Year": "2008",
                      "Donor Group": "BILATERAL Group",
                      "National Budget Execution Procedures": "1,311,920,598.535",
                      "National Financial Reporting Procedures": "496,260,762.69",
                      "National Auditing Procedures": "1,156,920,598.45",
                      "National Procurement Execution Procedures": "200,260,762.89"
                     },
                     {
                       "Year": "2008",
                       "Donor Group": "International Financial Institutions",
                       "National Budget Execution Procedures": "215,000,000",
                       "National Financial Reporting Procedures": "0",
                       "National Auditing Procedures": "0",
                       "National Procurement Execution Procedures": "0"
                     },
                     {
                         "Year": "2008",
                         "Donor Group": "International Financial Institutions",
                         "National Budget Execution Procedures": "215,000,000",
                         "National Financial Reporting Procedures": "0",
                         "National Auditing Procedures": "0",
                         "National Procurement Execution Procedures": "0"
                     },
                     {
                       "Year": "2010",
                       "Donor Group": "BILATERAL Group",
                       "National Budget Execution Procedures": "1,598.535",
                       "National Financial Reporting Procedures": "496.69",
                       "National Auditing Procedures": "0",
                       "National Procurement Execution Procedures": "0"
                     }                     
                   ],
                   "recordsPerPage" : 10,
                   "currentPageNumber" : 1,
                   "totalPageCount" : 1,
                   "totalRecords" : 3
                },
                "settings": {
                  "currency-code": "USD",
                  "calendar-id": "4",
                  "year-range": {
                    "type": "INT_VALUE",
                    "from": "2008",
                    "to": "2008",
                    "rangeFrom": "1980",
                    "rangeTo": "2030"
                  },
                  "amount-format": {
                    "number-divider": 1,
                    "max-frac-digits": 3,
                    "decimal-symbol": ".",
                    "use-grouping": true,
                    "group-separator": ",",
                    "group-size": 3
                  }
                },
                "empty": false
              };
        
        return new Promise((resolve, reject) => {
            postJson(REPORTS_CONFIGURATION[report] .url, requestData).then(response => {
                //resolve(response.json());
                resolve(sampleOutput);
            }).catch(error => {
                reject(error);
            });
        }); 
    }
    
}

export default ReportsApi;
import reportsApi from '../api/ReportsApi';

export function fetchReport9bMainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_9B_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport6MainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_6_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport9bMainReport(requestData, reportCode) {
    return function(dispatch) {
        return reportsApi.fetchReportData(requestData, reportCode).then(response => {            
            dispatch(fetchReport9bMainReportSuccess(response));                                
        }).catch(error => {
            throw(error);
        });
    }; 
}


export function fetchReport6MainReport(requestData, reportCode) {
    return function(dispatch) {
        return reportsApi.fetchReportData(requestData, reportCode).then(response => {            
            dispatch(fetchReport6MainReportSuccess(response));                                
        }).catch(error => {
            throw(error);
        });
    }; 
}







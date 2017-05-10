import reportsApi from '../api/ReportsApi';

export function fetchReport9bMainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_9B_MAIN_REPORT_SUCCESS', data: reportData}
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







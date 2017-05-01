import reportsApi from '../api/ReportsApi';

export function fetchReport9bDataSuccess(reportData){
    return {type: 'FETCH_REPORT_9B_DATA_SUCCESS', data: reportData}
}

export function fetchReport9bData(requestData, reportCode){
    return function(dispatch) {
        return reportsApi.fetchReportData(requestData, reportCode).then(response => {            
            dispatch(fetchReport9bDataSuccess(response));           
        }).catch(error => {
            throw(error);
        });
    }; 
}
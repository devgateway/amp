import reportsApi from '../api/ReportsApi';

export function fetchReport9bMainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_9B_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport6MainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_6_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport6MainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_6_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport5bMainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_5B_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReport5aMainReportSuccess(reportData){
    return {type: 'FETCH_REPORT_5A_MAIN_REPORT_SUCCESS', data: reportData}
}

export function fetchReportSuccess(data){
    return {type: 'FETCH_REPORT_SUCCESS', data: data}
}

export function fetchRemarksSuccess(code, remarks){
    return {type: 'FETCH_REMARKS_SUCCESS', data:{code: code, remarks: remarks}}
}

export function clearRemarks(code){
    return {type: 'CLEAR_REMARKS', data: {code: code}}
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

export function fetchReport5bMainReport(requestData, reportCode) {
    return function(dispatch) {
        return reportsApi.fetchReportData(requestData, reportCode).then(response => {            
            dispatch(fetchReport5bMainReportSuccess(response));                                
        }).catch(error => {
            throw(error);
        });
    }; 
}

export function fetchRemarks(code, url) {
    return function(dispatch) {
        return reportsApi.fetchRemarks(url).then(response => {            
            dispatch(fetchRemarksSuccess(code, response));                                
        }).catch(error => {
            throw(error);
        });
    }; 
}


export function fetchReportData(requestData, reportCode) {
    return function(dispatch) {
        return reportsApi.fetchReportData(requestData, reportCode).then(response => {            
            dispatch(fetchReportSuccess({reportData: response, code: reportCode, requestData: requestData }));                                
        }).catch(error => {
            throw(error);
        });
    };  
}


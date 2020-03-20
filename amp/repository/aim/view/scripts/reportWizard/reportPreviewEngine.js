function ReportPreviewSettings() {
	this.reportPeriod = "A";
	this.years		= true;
	this.quarters	= true;
	this.months		= false;
	this.yearsLength	= 3;
	this.startYear	= new Date().getFullYear() - this.yearsLength;
	this.endYear	= new Date().getFullYear();
	this.summary	= false;
	this.totalsOnly	= false;
	this.showOriginalCurrency = false;
	
	this.columns		= ["Project Title","Donor Agency"];
	this.measures		= ["Commitments", "Disbursements"];
	this.hierarchies	= ["Primary Sector"];

	this.monthNames			= repManagerParams.monthNames;
	
}

function ReportPreviewEngine (rpSettings) {
	this.rpSettings	= rpSettings;

	var postString = {
		"groupingOption": rpSettings.reportPeriod,
		"add_columns": rpSettings.columns,
		"add_measures": rpSettings.measures,
		"add_hierarchies": rpSettings.hierarchies,
		"recordsPerPage": 10,
		"page": 1,
		"show_empty_rows": true,
		"show_original_currency" : rpSettings.showOriginalCurrency,
		"settings": {
			"year-range": {
				"from": rpSettings.startYear + "",
				"to": rpSettings.endYear + ""
			}
		},
		"filters": {
			"date": {
				"start": rpSettings.startYear + "-01-01",
				"end":  rpSettings.endYear + "-12-31"
			}
		},
		"sorting": [{
			"columns": ["Project Title"],
			"asc": false
		}]
	};

	var self = this;
	$.ajax({
		type: "POST",
		url:"/rest/data/report/preview",
		dataType: "html",
		headers : {
			'Accept' : 'text/html',
			'Content-Type' : 'application/json'
		},
		async:true,
		data: JSON.stringify(postString),
		success:  function(data) {
			setPreview('previewBodySectionDiv',data);
		},
		error : function(request, status, error) {
			console.info(error);
		}
	});
}

function setPreview(parentElId, data) {
	this.parentEl	= document.getElementById(parentElId);
	this.parentEl.innerHTML		= data;
}


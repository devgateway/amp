function ReportPreviewSettings() {
	this.reportPeriod = "A";
	this.years		= true;
	this.quarters	= true;
	this.quartersLength	= 3;
	this.months		= false;
	this.monthsLength	= 3;
	this.startYear	= new Date().getFullYear() - 3;
	this.endYear	= new Date().getFullYear();
	this.yearsLength	= 3;
	this.summary	= false;
	this.totalsOnly	= false;
	
	this.columns		= ["Project Title","Donor Agency"];
	this.measures		= ["Commitments", "Disbursements"];
	this.hierarchies	= ["Primary Sector"];
	
	this.fundingName		= repManagerParams.previewFundingTrn;
	this.totalCostName		= repManagerParams.previewTotalCostTrn;
	this.reportTotalsName	= repManagerParams.previewReportTotalsTrn;
	
	//this.monthNames		= ["January","February","March","April","May","June","July","August","September","October","November","December"];
	this.monthNames			= repManagerParams.monthNames;
	this.quarterNames		= ["Q1","Q2","Q3","Q4"];
	
	this.reportTableId	= "reportTable";
	this.reportTableClass	= "html2ReportTable inside";
	
	this.rowHeaderClass		= "reportHeader";
	this.cellHeaderClass	= "reportHeader";
	this.reportTotalsClass	= "reportTotals";
	
	this.hierarchyCellClass		= "hierarchyCell";
	this.textCellClass			= "clsTableCellDataHtml";
	
	this.hierarchyLevelClasses	= ["firstLevel","secondLevel","thirdLevel", "fourthLevel", "fifthLevel"];
	
	this.numOfActivities	= 2;
	this.rowsPerHierarchy	= 2;
	
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
			'Accept' : 'application/json',
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
	this.tableEl	= document.createElement("TABLE");
	this.tableEl.setAttribute("align","center");
	this.tableBodyEl	= document.createElement("TBODY");

	this.tableEl.appendChild(this.tableBodyEl);
	this.parentEl.innerHTML		= data;
}


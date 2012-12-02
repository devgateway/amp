function ReportPreviewSettings() {
	this.years		= true;
	this.quarters	= true;
	this.quartersLength	= 3;
	this.months		= false;
	this.monthsLength	= 3;
	this.startYear	= 2007; //TODO base this on system date
	this.endYear	= 2010;
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
	this.rows		= new Array();
	this.rdArray	= new Array();
	
	this.rpSettings.colCounter	= new Array();
	for (var i = 0; i < this.rpSettings.columns.length; i++) {
		this.rpSettings.colCounter.push(1);
	}
	this.rpSettings.hierCounter	= new Array();
	for (var i = 0; i < this.rpSettings.hierarchies.length; i++) {
		this.rpSettings.hierCounter.push(1);
	}
	
	this.parentEl	= null;
	this.tableEl	= null;
	
	if ( this.rpSettings.summary )
		this.rpSettings.columns	= new Array ();
	else 
		if ( this.rpSettings.columns.length == 0 ) 
			return;
	
	this.rows.push( new Row1(this.rpSettings) );
	this.rows.push( new Row2(this.rpSettings) );
	this.rows.push( new Row3(this.rpSettings) );
	this.rows.push( new Row4(this.rpSettings) );
	this.rows.push( new Row5(this.rpSettings) );
	
//	var firstRows	= 0;
//	for ( var i=this.rpSettings.hierarchies.length; i>0 ;i-- ) { //for each level
//		var numOfSubGroups	= Math.pow(this.rpSettings.rowsPerHierarchy, i);
//		for ( var j=0; j<numOfSubGroups; j++ ) { // for each hierarchy item 
//			if ( i==this.rpSettings.hierarchies.length ) {
//				firstRows++;
//				var i2	= 1;
//				for ( i2=this.rpSettings.hierarchies.length; i2>0 ;i2-- ) {
//					var divider		= Math.pow(this.rpSettings.rowsPerHierarchy, i2);
//					if ( firstRows % divider == 1 )
//						break;
//				}
//				for ( var k=0; k<this.rpSettings.numOfActivities; k++) {
//					var maxLevel	= (k==0)?i2:0;
//					var actRow	= new ActivityRow(this.rpSettings, maxLevel);
//				}
//			}
//		}
//	}
	
	
	if ( this.rpSettings.hierarchies.length == 0 ) {
		var reportData	= new ColumnReportData(this.rpSettings, null);
		this.rdArray.push(reportData);
	}
	else {
		for (var i=0; i<this.rpSettings.rowsPerHierarchy; i++) {
			var reportData	= new GroupReportData(this.rpSettings, 0, null);
			this.rdArray.push(reportData);
		}
	}
}
ReportPreviewEngine.prototype.renderTable	= function( parentElId ) {
	this.parentEl	= document.getElementById(parentElId);
	this.tableEl	= document.createElement("TABLE");
	this.tableEl.id	= this.rpSettings.reportTableId;
	this.tableEl.className 	= this.rpSettings.reportTableClass;
	this.tableEl.setAttribute("align","center");
	this.tableBodyEl	= document.createElement("TBODY");
	
	
	for (var i=0; i<this.rdArray.length; i++ ) {
		var tempRowsArray	= this.rdArray[i].getRowsArray();
		this.rows	= this.rows.concat(tempRowsArray);
	}
	
	for (var i=0; i<this.rows.length; i++) {
		var row		= this.rows[i];
		row.renderRow();
		if ( row.rowEl != null ) {		
			for (var j=0; j<row.cells.length; j++) {
				var cell	= row.cells[j];
				cell.renderCell();
				if ( cell.cellEl != null)
					row.rowEl.appendChild(cell.cellEl);
			}
			
			this.tableBodyEl.appendChild( row.rowEl );
		}
	} 
	this.tableEl.appendChild(this.tableBodyEl);
	this.parentEl.innerHTML		= "";
	this.parentEl.appendChild ( this.tableEl );
};




function ReportData (rpSettings) {
	this.rpSettings			= rpSettings;
} 
ReportData.prototype.getRowsArray		= function( ) {
	;
};


ColumnReportData.prototype				= new ReportData();
ColumnReportData.prototype.parent		= ReportData;
ColumnReportData.prototype.constructor	= ColumnReportData;
function ColumnReportData( rpSettings, firstRow ) {
	this.parent.call(this, rpSettings);
	this.rows				= new Array();
	
	if ( firstRow == null) {
		firstRow	= new ActivityRow(this.rpSettings, -1);
	}
	this.rows.push(firstRow);
	
	for (var i=0; i<this.rpSettings.numOfActivities-1; i++ ) {
		var row	= new ActivityRow(this.rpSettings, -1);
		this.rows.push(row);
	}
};
ColumnReportData.prototype.getRowsArray	= function( ) {
	return this.rows;
};



GroupReportData.prototype				= new ReportData();
GroupReportData.prototype.parent		= ReportData;
GroupReportData.prototype.constructor	= GroupReportData;
function GroupReportData( rpSettings, hierarchyIdx, firstRow ) {
	this.parent.call(this, rpSettings);
	this.hierarchyIdx		= hierarchyIdx;
	this.children			= new Array();
	//this.summmaryFirstRow	= null;
	
	if ( firstRow == null ) {
		if ( !this.rpSettings.summary ) {
			firstRow	= new ActivityRow(this.rpSettings, hierarchyIdx);
		}
		else 
			firstRow	= new SummaryFirstHierarchyRow(this.rpSettings, hierarchyIdx);
	}

	/**
	 * For summary reports, smallest groups (hierarchies) do not have trailing cells.
	 * (Everything is on a single line) 
	 */
	if ( this.rpSettings.summary && this.hierarchyIdx == this.rpSettings.hierarchies.length-1 )
		this.hierarchyRow	= firstRow;
	else
		this.hierarchyRow		= new HierarchyRow(this.rpSettings, hierarchyIdx); //Trailing cells row
	
	var child	= null;
	if ( this.rpSettings.hierarchies.length == hierarchyIdx+1) {
		if ( !this.rpSettings.summary ) {
			child	= new ColumnReportData(this.rpSettings, firstRow);
			this.children.push(child);
		}
		else
			this.hierarchyRow	= firstRow;
	}
	else
		for (var i=0; i<this.rpSettings.rowsPerHierarchy; i++) {
			firstRow	= (i==0)?firstRow:null;
			child	= new GroupReportData(this.rpSettings, hierarchyIdx+1, firstRow);
			this.children.push(child);
		}
};
GroupReportData.prototype.getRowsArray	= function( ) {
	var rowsArray	= new Array();
	for (var i=0;i<this.children.length; i++) {
		var childArray	= this.children[i].getRowsArray();
		rowsArray	= rowsArray.concat(childArray);
	}
//	if ( this.summmaryFirstRow != null) 
//		rowsArray.push(this.summmaryFirstRow);
	rowsArray.push(this.hierarchyRow);
	return rowsArray;	
};



function PreviewRow(rpSettings) {
	this.rpSettings	= rpSettings;
}

PreviewRow.prototype.renderRow	= function( ) {
	this.rowEl	= document.createElement("TR");
	this.cells	= new Array();
};

Row1.prototype				= new PreviewRow();
Row1.prototype.parent		= PreviewRow;
Row1.prototype.constructor	= Row1;
function Row1( rpSettings ) {
	this.parent.call(this, rpSettings);
};
Row1.prototype.renderRow	= function() {
	this.parent.prototype.renderRow.call(this);
	this.rowEl.className	= this.rpSettings.rowHeaderClass;
	
	
	for (var i=0; i<this.rpSettings.hierarchies.length; i++) {
		var cell	= new ColTitleCell(this.rpSettings, this.rpSettings.hierarchies[i]);
		this.cells.push(cell);
	}
	for (var i=0; i<this.rpSettings.columns.length; i++) {
		var cell	= new ColTitleCell(this.rpSettings, this.rpSettings.columns[i]);
		this.cells.push(cell);
	}
	var fundingCell		= new FundingNameCell(this.rpSettings, this.rpSettings.fundingName);
	this.cells.push(fundingCell);
	var totalCostsCell	= new TotalCostNameCell(this.rpSettings, this.rpSettings.totalCostName);
	this.cells.push(totalCostsCell);
};

Row2.prototype				= new PreviewRow();
Row2.prototype.parent		= PreviewRow;
Row2.prototype.constructor	= Row2;
function Row2( rpSettings ) {
	this.parent.call(this, rpSettings);
};
Row2.prototype.renderRow	= function( ) {
	this.parent.prototype.renderRow.call(this);
	this.rowEl.className	= this.rpSettings.rowHeaderClass;
	
	var firstYearCell	= new YearsNameCell(this.rpSettings, this.rpSettings.startYear);
	this.cells.push(firstYearCell);
	for (var i=0; i<this.rpSettings.yearsLength-2; i++) {
		var cell 	= new YearsNameCell(this.rpSettings, "...");
		this.cells.push(cell);
	}
	var lastYearCell	= new YearsNameCell(this.rpSettings, this.rpSettings.endYear);
	this.cells.push(lastYearCell);
	
	for ( var i=0; i<this.rpSettings.measures.length; i++ ) {
		var cell	= new TotalMeasureNameCell(this.rpSettings, this.rpSettings.measures[i]);
		this.cells.push(cell);
	}
};

Row3.prototype				= new PreviewRow();
Row3.prototype.parent		= PreviewRow;
Row3.prototype.constructor	= Row3;
function Row3( rpSettings ) {
	this.parent.call(this, rpSettings);
};

Row3.prototype.renderRow	= function( ) {
	if ( !this.rpSettings.totalsOnly && (this.rpSettings.months || this.rpSettings.quarters) ) {
		this.parent.prototype.renderRow.call(this);
		this.rowEl.className	= this.rpSettings.rowHeaderClass;
		
		for (var k=0; k<this.rpSettings.yearsLength; k++) {
			if ( this.rpSettings.months ) {
				var firstMonthCell	= new SubYearNameCell(this.rpSettings,this.rpSettings.monthNames[0]);
				this.cells.push(firstMonthCell);
				var cell			= new SubYearNameCell(this.rpSettings,"...");
				this.cells.push(cell);
				var lastMonthCell	= new SubYearNameCell(this.rpSettings,this.rpSettings.monthNames[11]);
				this.cells.push(lastMonthCell);
			}
			if (this.rpSettings.quarters) {
				var firstQuarterCell	= new SubYearNameCell(this.rpSettings,this.rpSettings.quarterNames[0]);
				this.cells.push(firstQuarterCell);
				var cell			= new SubYearNameCell(this.rpSettings,"...");
				this.cells.push(cell);
				var lastQuarterCell	= new SubYearNameCell(this.rpSettings,this.rpSettings.quarterNames[3]);
				this.cells.push(lastQuarterCell);
			}
		}
	}
};

Row4.prototype				= new PreviewRow();
Row4.prototype.parent		= PreviewRow;
Row4.prototype.constructor	= Row4;
function Row4( rpSettings ) {
	this.parent.call(this, rpSettings);
};

Row4.prototype.renderRow	= function( ) {
	if ( !this.rpSettings.totalsOnly ) {
		this.parent.prototype.renderRow.call(this);
		this.rowEl.className	= this.rpSettings.rowHeaderClass;
		
		var subYearLength		= (this.rpSettings.quarters)?this.rpSettings.quartersLength:((this.rpSettings.months)?this.rpSettings.monthsLength:1);
		
		for (var k=0; k<this.rpSettings.yearsLength; k++)
			for (var j=0; j<subYearLength; j++)
				for (var i=0; i<this.rpSettings.measures.length; i++ ) {
					var cell	= new MeasureNameCell(this.rpSettings, this.rpSettings.measures[i]);
					this.cells.push(cell);
				}
	}
};

Row5.prototype				= new PreviewRow();
Row5.prototype.parent		= PreviewRow;
Row5.prototype.constructor	= Row5;
function Row5( rpSettings ) {
	this.parent.call(this, rpSettings);
};

Row5.prototype.renderRow	= function( ) {
	this.parent.prototype.renderRow.call(this);
	var reportTotalsNameCell	= new ReportTotalsName(this.rpSettings, this.rpSettings.reportTotalsName + ":");
	this.cells.push(reportTotalsNameCell);
	
	var numOfEmptyCells			= this.rpSettings.hierarchies.length + this.rpSettings.columns.length-1;
	for (var i=0; i<numOfEmptyCells; i++ ) {
		var cell	= new ReportTotalsName(this.rpSettings, "");
		this.cells.push(cell);
	}
	
	if ( !this.rpSettings.totalsOnly) {
		var subYearLength		= (this.rpSettings.quarters)?this.rpSettings.quartersLength:((this.rpSettings.months)?this.rpSettings.monthsLength:1);
		var numOfAmounts		= this.rpSettings.yearsLength*subYearLength*this.rpSettings.measures.length;
		for (var i=0; i<numOfAmounts; i++) {
			var amount	= Math.pow(this.rpSettings.numOfActivities, this.rpSettings.hierarchies.length+1)  ;
			var cell	= new ReportTotalsAmountCell(this.rpSettings, amount + " 000");
			this.cells.push(cell);
		}
	}
	for ( var i=0; i<this.rpSettings.measures.length; i++ ) {
		var amount	= Math.pow(this.rpSettings.numOfActivities, this.rpSettings.hierarchies.length+1) *10  ;
		var cell 	= 	new ReportTotalCostAmountCell(this.rpSettings, amount + " 000");
		this.cells.push(cell);
	}
};

ActivityRow.prototype				= new PreviewRow();
ActivityRow.prototype.parent		= PreviewRow;
ActivityRow.prototype.constructor	= ActivityRow;
function ActivityRow( rpSettings, hierarchyIdx ) {
	this.parent.call(this, rpSettings);
	this.hierarchyIdx	= hierarchyIdx;
};

ActivityRow.prototype.renderRow	= function( ) {
	this.parent.prototype.renderRow.call(this );
	if ( this.hierarchyIdx >= 0)
		for ( var i=this.hierarchyIdx; i<this.rpSettings.hierarchies.length; i++ ) {
			var hCell	= new HierarchyCell(this.rpSettings, this.rpSettings.hierarchies[i] + " " + this.rpSettings.hierCounter[i]++, i );
			this.cells.push(hCell);
		}
	for ( var i=0; i<this.rpSettings.columns.length; i++ ) {
		var tCell	= new TextCell(this.rpSettings, this.rpSettings.columns[i] + " " + this.rpSettings.colCounter[i]++ );
		this.cells.push(tCell);
	}
	if ( !this.rpSettings.totalsOnly) {
		var subYearLength		= (this.rpSettings.quarters)?this.rpSettings.quartersLength:((this.rpSettings.months)?this.rpSettings.monthsLength:1);
		var numOfAmounts		= this.rpSettings.yearsLength*subYearLength*this.rpSettings.measures.length;
		for (var i=0; i<numOfAmounts; i++) {
			var cell	= new AmountCell(this.rpSettings, "1 000");
			this.cells.push(cell);
		}
	}
	for ( var i=0; i<this.rpSettings.measures.length; i++ ) {
		var cell 	= 	new AmountCell(this.rpSettings, "10 000");
		this.cells.push(cell);
	}
};

HierarchyRow.prototype				= new PreviewRow();
HierarchyRow.prototype.parent		= PreviewRow;
HierarchyRow.prototype.constructor	= HierarchyRow;
function HierarchyRow( rpSettings, hierarchyIdx ) {
	this.parent.call(this, rpSettings);
	this.hierarchyIdx	= hierarchyIdx;
};
HierarchyRow.prototype.renderRow	= function( ) {
	this.parent.prototype.renderRow.call(this );
	this.rowEl.className	= this.rpSettings.hierarchyLevelClasses[this.hierarchyIdx];
	if ( this.hierarchyIdx >= 0)
		for ( var i=this.hierarchyIdx; i<this.rpSettings.hierarchies.length; i++ ) {
			if ( i!=this.hierarchyIdx ) {//TODO
				var cell	= new EmptyCell(this.rpSettings, "" );
				this.cells.push(cell);
			}
//			if ( this.rpSettings.summary )  { // in case of summary reports
//				if ( true || this.hierarchyIdx == this.rpSettings.hierarchies.length-1 ) { // if this is the last/smallest hierarchy
//					var hCell	= new HierarchyCell(this.rpSettings, this.rpSettings.hierarchies[i] + " " + this.rpSettings.hierCounter[i]++, i );
//					this.cells.push(hCell);
//				}
//			}
		}
	for ( var i=0; i<this.rpSettings.columns.length; i++ ) {
		var cell	= new EmptyCell(this.rpSettings, "" );
		this.cells.push(cell);
	}
	if ( !this.rpSettings.totalsOnly ) {
		var subYearLength		= (this.rpSettings.quarters)?this.rpSettings.quartersLength:((this.rpSettings.months)?this.rpSettings.monthsLength:1);
		var numOfAmounts		= this.rpSettings.yearsLength*subYearLength*this.rpSettings.measures.length;
		for (var i=0; i<numOfAmounts; i++) {
			
			var amount	= Math.pow(this.rpSettings.rowsPerHierarchy,this.rpSettings.hierarchies.length - this.hierarchyIdx -1) * this.rpSettings.numOfActivities ;
			var cell	= new HierarchyAmountCell(this.rpSettings, amount + " 000");
			this.cells.push(cell);
		}
	}
	for ( var i=0; i<this.rpSettings.measures.length; i++ ) {
		var amount	= Math.pow(this.rpSettings.rowsPerHierarchy,this.rpSettings.hierarchies.length - this.hierarchyIdx -1) * this.rpSettings.numOfActivities  * 10;
		var cell 	= 	new HierarchyAmountCell(this.rpSettings, amount + " 000");
		this.cells.push(cell);
	}
};

SummaryFirstHierarchyRow.prototype				= new PreviewRow();
SummaryFirstHierarchyRow.prototype.parent		= PreviewRow;
SummaryFirstHierarchyRow.prototype.constructor	= SummaryFirstHierarchyRow;
function SummaryFirstHierarchyRow( rpSettings, hierarchyIdx ) {
	this.parent.call(this, rpSettings);
	this.hierarchyIdx		= hierarchyIdx;
};

SummaryFirstHierarchyRow.prototype.renderRow	= function( ) {
	this.parent.prototype.renderRow.call(this);
	this.rowEl.className	= this.rpSettings.hierarchyLevelClasses[this.rpSettings.hierarchies.length-1];
	if ( this.hierarchyIdx >= 0)
		for ( var i=this.hierarchyIdx; i<this.rpSettings.hierarchies.length; i++ ) {
			if ( true || this.hierarchyIdx == this.rpSettings.hierarchies.length-1 ) { // if this is the last/smallest hierarchy
				var hCell	= new HierarchyCell(this.rpSettings, this.rpSettings.hierarchies[i] + " " + this.rpSettings.hierCounter[i]++, i );
				this.cells.push(hCell);
			}
		}
	
	
	if ( !this.rpSettings.totalsOnly ) {
		var subYearLength		= (this.rpSettings.quarters)?this.rpSettings.quartersLength:((this.rpSettings.months)?this.rpSettings.monthsLength:1);
		var numOfAmounts		= this.rpSettings.yearsLength*subYearLength*this.rpSettings.measures.length;
		for (var i=0; i<numOfAmounts; i++) {
			
			//var amount	= Math.pow(this.rpSettings.numOfActivities,this.rpSettings.hierarchies.length - this.hierarchyIdx -1) * this.rpSettings.numOfActivities ;
			var cell	= new HierarchyAmountCell(this.rpSettings, this.rpSettings.numOfActivities + " 000");
			this.cells.push(cell);
		}
	}
	for ( var i=0; i<this.rpSettings.measures.length; i++ ) {
		//var amount	= Math.pow(this.rpSettings.numOfActivities,this.rpSettings.hierarchies.length - this.hierarchyIdx -1) * this.rpSettings.numOfActivities  * 10;
		var cell 	= 	new HierarchyAmountCell(this.rpSettings, (this.rpSettings.numOfActivities*10) + " 000");
		this.cells.push(cell);
	}
};


function PreviewCell(rpSettings, cellName) {
	this.rpSettings	= rpSettings;
	this.cellName	= cellName;
	this.nodeEl		= null;
	this.cellEl		= null;
}

PreviewCell.prototype.renderCell	= function( ) {
	this.cellEl		= document.createElement("TD");
	//this.cellEl.setAttribute("rowspan", 1);
	//this.cellEl.setAttribute("colspan", 1);
	
	this.cellEl.innerHTML	= this.cellName;
};

ColTitleCell.prototype = new PreviewCell();
ColTitleCell.prototype.parent = PreviewCell;
ColTitleCell.prototype.constructor = ColTitleCell;
function ColTitleCell(rpSettings, cellName) {
	this.parent.call(this, rpSettings, cellName);
}

ColTitleCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.cellHeaderClass;
	var rowsp	= 2;
	if ( !this.rpSettings.totalsOnly ) {
		rowsp	+= 1;
	}
	if ( this.rpSettings.months || this.rpSettings.quarters ) {
		rowsp += 1;
	}
	this.cellEl.setAttribute("rowSpan",rowsp);
};

FundingNameCell.prototype				= new PreviewCell();
FundingNameCell.prototype.parent		= PreviewCell;
FundingNameCell.prototype.constructor	= FundingNameCell;
function FundingNameCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};

FundingNameCell.prototype.renderCell	= function( ) {
	if ( !this.rpSettings.totalsOnly ) {
		this.parent.prototype.renderCell.call(this);
		this.cellEl.className	= this.rpSettings.cellHeaderClass;
		var subYearLength = 1;
		if (this.rpSettings.months)
			subYearLength	= this.rpSettings.monthsLength;
		if (this.rpSettings.quarters)
			subYearLength	= this.rpSettings.quartersLength;
		var colspan	= this.rpSettings.yearsLength * this.rpSettings.measures.length * subYearLength;
		this.cellEl.setAttribute("colSpan", colspan);
	}
	
};

TotalCostNameCell.prototype				= new PreviewCell();
TotalCostNameCell.prototype.parent		= PreviewCell;
TotalCostNameCell.prototype.constructor	= TotalCostNameCell;
function TotalCostNameCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
	
};

TotalCostNameCell.prototype.renderCell	= function(  ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.cellHeaderClass;
	this.cellEl.setAttribute("colSpan", this.rpSettings.measures.length);
};

YearsNameCell.prototype				= new PreviewCell();
YearsNameCell.prototype.parent		= PreviewCell;
YearsNameCell.prototype.constructor	= YearsNameCell;
function YearsNameCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
	
};

YearsNameCell.prototype.renderCell	= function () {
	if ( !this.rpSettings.totalsOnly ) {
		this.parent.prototype.renderCell.call(this);
		this.cellEl.className	= this.rpSettings.cellHeaderClass;
		var subYearLength = 1;
		if (this.rpSettings.months)
			subYearLength	= this.rpSettings.monthsLength;
		if (this.rpSettings.quarters)
			subYearLength	= this.rpSettings.quartersLength;
		var colspan	= this.rpSettings.measures.length * subYearLength;
		this.cellEl.setAttribute("colSpan", colspan);
	}
};

TotalMeasureNameCell.prototype				= new PreviewCell();
TotalMeasureNameCell.prototype.parent		= PreviewCell;
TotalMeasureNameCell.prototype.constructor	= TotalMeasureNameCell;
function TotalMeasureNameCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
	
};

TotalMeasureNameCell.prototype.renderCell	= function() {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.cellHeaderClass;
	var rowspan	= 1;
	if ( !this.rpSettings.totalsOnly ) 
		rowspan ++;
	if ( this.rpSettings.months || this.rpSettings.quarters )
		rowspan ++;
	
	this.cellEl.setAttribute("rowSpan", rowspan);
};

/* 3rd Header Row */
SubYearNameCell.prototype				= new PreviewCell();
SubYearNameCell.prototype.parent		= PreviewCell;
SubYearNameCell.prototype.constructor	= SubYearNameCell;
function SubYearNameCell( rpSettings,cellName ) {
	this.parent.call(this, rpSettings,cellName);	
};

SubYearNameCell.prototype.renderCell	= function() {
	if ( this.rpSettings.months || this.rpSettings.quarters ){
		this.parent.prototype.renderCell.call(this);
		this.cellEl.className	= this.rpSettings.cellHeaderClass;
		var colspan	= this.rpSettings.measures.length;
		this.cellEl.setAttribute("colSpan", colspan);
	}
		
};

/* 4th Header Row */

MeasureNameCell.prototype				= new PreviewCell();
MeasureNameCell.prototype.parent		= PreviewCell;
MeasureNameCell.prototype.constructor	= MeasureNameCell;
function MeasureNameCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};

MeasureNameCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this );
	this.cellEl.className	= this.rpSettings.cellHeaderClass;
	
};

/* 5th Header Row */

ReportTotalsName.prototype				= new PreviewCell();
ReportTotalsName.prototype.parent		= PreviewCell;
ReportTotalsName.prototype.constructor	= ReportTotalsName;
function ReportTotalsName( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};
ReportTotalsName.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.reportTotalsClass;
};


EmptyCell.prototype				= new PreviewCell();
EmptyCell.prototype.parent		= PreviewCell;
EmptyCell.prototype.constructor	= EmptyCell;
function EmptyCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, "");
};


ReportTotalsAmountCell.prototype			= new PreviewCell();
ReportTotalsAmountCell.prototype.parent		= PreviewCell;
ReportTotalsAmountCell.prototype.constructor	= ReportTotalsAmountCell;
function ReportTotalsAmountCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
	
};
ReportTotalsAmountCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.reportTotalsClass;
};


ReportTotalCostAmountCell.prototype				= new PreviewCell();
ReportTotalCostAmountCell.prototype.parent		= PreviewCell;
ReportTotalCostAmountCell.prototype.constructor	= ReportTotalCostAmountCell;
function ReportTotalCostAmountCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};
ReportTotalCostAmountCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.reportTotalsClass;
};

HierarchyCell.prototype				= new PreviewCell();
HierarchyCell.prototype.parent		= PreviewCell;
HierarchyCell.prototype.constructor	= HierarchyCell;
function HierarchyCell( rpSettings, cellName, level ) {
	this.parent.call(this, rpSettings, cellName);
	this.level		= level;
};
HierarchyCell.prototype.renderCell	= function() {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className	= this.rpSettings.hierarchyLevelClasses[this.level] + " " + this.rpSettings.hierarchyCellClass;
	
	var numOfLevelsRemaining	= this.rpSettings.hierarchies.length - (this.level+1);
	
	var activityLines			= Math.pow(this.rpSettings.rowsPerHierarchy, numOfLevelsRemaining) * this.rpSettings.numOfActivities;
	var groupRows				= 1;
	for (var i=1; i<=numOfLevelsRemaining; i++) {
		groupRows		+= Math.pow(this.rpSettings.rowsPerHierarchy, i);
	}
	var rowspan		= this.rpSettings.summary? groupRows : groupRows + activityLines ;
	this.cellEl.setAttribute("rowSpan", rowspan );
};



TextCell.prototype				= new PreviewCell();
TextCell.prototype.parent		= PreviewCell;
TextCell.prototype.constructor	= TextCell;
function TextCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};
TextCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className		= this.rpSettings.textCellClass;
};

AmountCell.prototype				= new PreviewCell();
AmountCell.prototype.parent		= PreviewCell;
AmountCell.prototype.constructor	= AmountCell;
function AmountCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};
AmountCell.prototype.renderCell	= function( ) {
	this.parent.prototype.renderCell.call(this);
	this.cellEl.className		= this.rpSettings.textCellClass;
};

HierarchyAmountCell.prototype				= new PreviewCell();
HierarchyAmountCell.prototype.parent		= PreviewCell;
HierarchyAmountCell.prototype.constructor	= HierarchyAmountCell;
function HierarchyAmountCell( rpSettings, cellName ) {
	this.parent.call(this, rpSettings, cellName);
};


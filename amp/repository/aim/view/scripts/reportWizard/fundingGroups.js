YAHOO.namespace("YAHOO.amp.reportwizard");
YAHOO.amp.reportwizard.colIdToName		= new Array();
YAHOO.amp.reportwizard.measureIdToName	= new Array();
YAHOO.amp.reportwizard.fundingGroups	= new Array();
YAHOO.amp.reportwizard.fundingGroups["donor"]= new Array(
				 'A.C. Chapter'
				,'Primary Sector Sub-Sector'
				,'Primary Sector Sub-Sub-Sector'
				,'Implementation Level'
				,'Implementation Location' 
				,'Accession Instrument'
				,'Executing Agency'
				,'Executing Agency Type'
				,'Donor'
				,'Donor Group'
				,'Donor Agency'
				,'Primary Sector'
				,'Status', 'Administrative Level 0', 'Administrative Level 1', 'Administrative Level 2','Administrative Level 3'
				,'Type Of Assistance'
				,'Financing Instrument'
				,'National Planning Objectives Level 1'
				,'National Planning Objectives Level 2'
				,'National Planning Objectives Level 3'
				,'National Planning Objectives Level 4'
				,'National Planning Objectives Level 5'
				,'National Planning Objectives Level 6'
				,'National Planning Objectives Level 7'
				,'National Planning Objectives Level 8'
				,'Primary Program Level 1'
				,'Primary Program Level 2'
				,'Primary Program Level 3'
				,'Primary Program Level 4'
				,'Primary Program Level 5'
				,'Primary Program Level 6'
				,'Primary Program Level 7'
				,'Primary Program Level 8'
				,'Indirect Primary Program Level 1'
				,'Indirect Primary Program Level 2'
				,'Indirect Primary Program Level 3'
				,'Indirect Primary Program Level 4'
				,'Indirect Primary Program Level 5'
				,'Indirect Primary Program Level 6'
				,'Indirect Primary Program Level 7'
				,'Indirect Primary Program Level 8'
				,'Componente'
				,'Secondary Program Level 1'
				,'Secondary Program Level 2'
				,'Secondary Program Level 3'
				,'Secondary Program Level 4'
				,'Secondary Program Level 5'
				,'Secondary Program Level 6'
				,'Secondary Program Level 7'
				,'Secondary Program Level 8'
				,'Tertiary Program Level 1'
				,'Tertiary Program Level 2'
				,'Tertiary Program Level 3'
				,'Tertiary Program Level 4'
				,'Tertiary Program Level 5'
				,'Tertiary Program Level 6'
				,'Tertiary Program Level 7'
				,'Tertiary Program Level 8'				
				,'Donor Type'
				,'Credit/Donation'
				,'Beneficiary Agency'
				,'Beneficiary Agency Groups'
				,'Implementing Agency'
				,'Implementing Agency Groups'
				,'Implementing Agency Type'
				,'Institutions'
				,'Component Type'
				,'Secondary Sector'
				,'Secondary Sector Sub-Sector'
				,'Responsible Organization'
				,'Activity Created By'
				,'Project Category'
				,'Funding Status'
				,'Mode of Payment'
				,'Payment Capital - Recurrent'
				,'Budget Department'
				,'Budget Organization'
				,'Budget Sector'
				,'Budget Program'
                ,'Tertiary Sector'
				,'Tertiary Sector Sub-Sector'
                ,'Tertiary Sector Sub-Sub-Sector'
                ,'Government Approval Procedures'
                ,'Joint Criteria'
                , 'Activity Budget'
                , 'Multi Donor'
                , 'Capital Expenditure'
                , 'Sector Tag'
                , 'Sector Tag Sub-Sector'
                , 'Sector Tag Sub-Sub-Sector'
                , 'Agreement Title + Code'
                , 'Agreement Code'
                , 'Contracting Agency'
                , 'Contracting Agency Acronym'                
                , 'Contracting Agency Groups'
                , 'Project Implementing Unit'
                , 'Type of Cooperation'
                , 'Type of Implementation'
                , 'Modalities'
                , 'Budget Structure'
                , 'Indirect On Budget'
                , 'Humanitarian Aid'
                , 'Concessionality Level'
                , 'Disaster Response Marker'
                , 'Indicator Name'
                , 'Logframe Category'
                , 'Risk'
                , 'Indicator Sector'
                , 'Indicator Type'
                , 'Quaternary Sector'
                , 'Quaternary Sector Sub-Sector'
                , 'Quaternary Sector Sub-Sub-Sector'
                , 'Quinary Sector'
                , 'Quinary Sector Sub-Sector'
                , 'Quinary Sector Sub-Sub-Sector'
			);

YAHOO.amp.reportwizard.fundingGroups["regional"]		= new Array(
				'Status','Primary Sector','Primary Sector Sub-Sector','National Planning Objectives Level 1','Regional Region'
			);
YAHOO.amp.reportwizard.fundingGroups["component"]		= new Array(
				 'Component Type','Administrative Level 1','Status','Primary Sector','National Planning Objectives Level 1','Administrative Level 3','Administrative Level 2','Component Name','Project Title','Component Funding Organization','Component Second Responsible Organization'
			);
YAHOO.amp.reportwizard.fundingGroups["contribution"]	= new Array(
				'Costing Donor','Parent National Planning Objectives', 'National Planning Objectives Level 1', 'Primary Program Level 1', 
				'Secondary Program Level 1', 'Primary Sector Sub-Sector',
				'Status', 'Primary Sector', 'A.C. Chapter',
				'Accession Instrument', 'Donor', 'Secondary Sector', 'Secondary Sector Sub-Sector', 'Activity Created By' ,'Tertiary Sector'
				,'Tertiary Sector Sub-Sector'
                ,'Tertiary Sector Sub-Sub-Sector'
			);

YAHOO.amp.reportwizard.fundingGroups["pledge"]= new Array(
		'Related Projects', 'Pledges Donor Group', 'Pledges Aid Modality',
		'Pledges Type Of Assistance',
		'Pledges Titles', 'Pledges sectors', 'Pledges Secondary Sectors', 'Pledges Tertiary Sectors', 'Pledges Quaternary Sectors', 'Pledges Quinary Sectors',
		'Pledges Programs', 'Pledges Secondary Programs', 'Pledges Tertiary Programs', 
		'Pledges Administrative Level 1', 'Pledges Administrative Level 2', 'Pledge Status'
	);

YAHOO.amp.reportwizard.fundingGroups["incompatible_hierarchies"]= new Array(
	);

YAHOO.amp.reportwizard.fundingGroups["measureless_only_hierarchies"]= new Array(
	  'Indicator Name'
    , 'Logframe Category'
    , 'Risk'
    , 'Indicator Sector'
    , 'Indicator Type'
);

function insertColInfo (id, name) {
		YAHOO.amp.reportwizard.colIdToName[id]=name;
}

function insertMeasureInfo (id, name) {
		YAHOO.amp.reportwizard.measureIdToName[id]=name;
}


function checkIfColIsHierarchy(id) {
	var reportGroupDivEl		= document.getElementById("reportGroupDiv") ;
	var radios					= reportGroupDivEl.getElementsByTagName("input");
	var colName					= YAHOO.amp.reportwizard.colIdToName[id];
	if ( colName == null )
			return false;
	var fgArray;
	for (var i=0; i<radios.length; i++) {
		if ( radios[i].checked ) {
			fgArray	= YAHOO.amp.reportwizard.fundingGroups[ radios[i].value ];
		}
	}
	if (fgArray == null) return false;

	if (repManager.forDesktopTabs
		&& YAHOO.amp.reportwizard.fundingGroups["measureless_only_hierarchies"].indexOf(colName) >= 0) {
		return false;
	}
	
	for (j=0; j<fgArray.length; j++) {
		if ( fgArray[j]==colName ) 
			return true;
	}
	return false;
}

function checkincompatiblehierarchies(id){
	var fgArray	= YAHOO.amp.reportwizard.fundingGroups["incompatible_hierarchies"];
	var colName	= YAHOO.amp.reportwizard.colIdToName[id];
	if (fgArray == null) return false;
	
	for (j=0; j<fgArray.length; j++) {
		if ( fgArray[j]==colName ) 
			return true;
	}
	return false;
}

function checkSelectedHierarchies() {
	var selHierEl		= document.getElementById("dest_hierarchies_ul") ;
	var selColEl		= document.getElementById("dest_col_ul") ;
	var cols			= selColEl.getElementsByTagName("li");
	var hiers			= selHierEl.getElementsByTagName("li");
	
	var srcHierEl		= document.getElementById("source_hierarchies_ul") ;
	var srcHiers		= srcHierEl.getElementsByTagName("li");
	
	for ( var i=0; i<hiers.length; i++ ) {
		var isInSelectedColumns = false;
		for ( var j=0; j<cols.length; j++ ) {
			if ( getColDbId(hiers[i]) == getColDbId(cols[j]) ) {
				isInSelectedColumns	= true;
				break;
			}
		}
		if ( !isInSelectedColumns ) {
			selHierEl.removeChild( hiers[i] );
			i=i-1;
		}
			
	}
	for ( var i=0; i<srcHiers.length; i++ ) {
		var isInSelectedColumns = false;
		for ( var j=0; j<cols.length; j++ ) {
			if ( getColDbId(srcHiers[i]) == getColDbId(cols[j]) ) {
				isInSelectedColumns	= true;
				break;
			}
		}
		if ( !isInSelectedColumns ) {
			srcHierEl.removeChild( srcHiers[i] );
			i=i-1;
		}
			
	}
	
}

function getColDbId( liEl ) {
	return (liEl.getElementsByTagName("input")[0]).value;
}

function generateHierarchies(e) {
	checkSelectedHierarchies();
	//debugger;
	var newDDObj		= new MyDragAndDropObject("source_hierarchies_ul","dest_hierarchies_ul");
	var ulEl			= document.getElementById("dest_col_ul") ;
	var hierUlEl		= document.getElementById("source_hierarchies_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	for (var i=0; i<items.length; i++ ) {
		colId	= (items[i].getElementsByTagName("input")[0]).value;
		if ( checkIfColIsHierarchy(colId) && (document.getElementById('hier_li_'+colId)==null) ) {
			var childNodes				= items[i].childNodes;
			var itemData;
			hasDescription = childNodes.length > 2;
			if (hasDescription) {
				itemData = childNodes[1].data + childNodes[2].outerHTML;
			} else {
				itemData = childNodes[childNodes.length-1].data;
			}
			var newObj = ColumnsDragAndDropObject.generateLi("hier_li_"+colId, 
					"list1 text-align", "hier_chk_"+colId, colId, "selectedHierarchies", itemData);
			hierUlEl.appendChild( newObj );
			var newDDProxy				= new YAHOO.util.DDProxy( newObj.id );
			newDDObj.addActions( newDDProxy );
				
		}
	}
	repManager.showHideHierarchies();
}

function findMeasurelessOnlyHiers(hiersColIds) {
	return hiersColIds.map(colIdToName).filter(isMeasurelessOnlyHierarchy);
}

function isMeasurelessOnlyHierarchy(colName) {
	return YAHOO.amp.reportwizard.fundingGroups["measureless_only_hierarchies"].indexOf(colName) !== -1
}

var mtefPattern = /MTEF \d\d\d\d/;
var pipelineMtefPattern = /Pipeline MTEF Projections \d\d\d\d/;
var projectionMtefPattern = /Projection MTEF Projections \d\d\d\d/;
var realMtefPattern = /Real MTEF \d\d\d\d/;

function isAmountColumn(colName) {
	return colName == 'Proposed Project Amount' || colName == 'Revised Project Amount'
		|| mtefPattern.test(colName) || pipelineMtefPattern.test(colName) || projectionMtefPattern.test(colName)
		|| realMtefPattern.test(colName);
}

function colIdToName(id) {
	return YAHOO.amp.reportwizard.colIdToName[id];
}

function updateColumnVisibility(reportType) {
    if (reportType === 'regional') {
        ColumnsDragAndDropObject.showObjsByDbId('source_col_div', [colNameToId('Regional Region')]);
        ColumnsDragAndDropObject.hideObjsByDbId('source_col_div', [colNameToId('Administrative Level 1')]);
    } else {
        ColumnsDragAndDropObject.showObjsByDbId('source_col_div', [colNameToId('Administrative Level 1')]);
        ColumnsDragAndDropObject.hideObjsByDbId('source_col_div', [colNameToId('Regional Region')]);
    }
}

function colNameToId(name) {
	return YAHOO.amp.reportwizard.colIdToName.indexOf(name);
}

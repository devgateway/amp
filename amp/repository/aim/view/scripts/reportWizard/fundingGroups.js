YAHOO.namespace("YAHOO.amp.reportwizard");

YAHOO.amp.reportwizard.colIdToName		= new Array();
YAHOO.amp.reportwizard.fundingGroups	= new Array();
YAHOO.amp.reportwizard.fundingGroups["donor"]			= new Array(
				'A.C. Chapter', 'Primary Sector Sub-Sector', 'Implementation Level', 'Accession Instrument', 
				'Executing Agency', 'Donor', 'Donor Group', 
				'Donor Agency', 'Primary Sector', 'Status', 'Region', 
				'Type Of Assistance', 'Financing Instrument', 'On/Off Budget',
				'National Planning Objectives', 'Primary Program', 'Componente', 
				'Secondary Program', 'Donor Type', 'Credit/Donation', 'Beneficiary Agency', 
				'Implementing Agency', 'Component Name',
				'Secondary Sector', 'Secondary Sector Sub-Sector',
				'Responsible Organization', 'Activity Created By', 'Project Category'
			);
YAHOO.amp.reportwizard.fundingGroups["regional"]		= new Array(
				'Status',
				'A.C. Chapter', 'Accession Instrument', 'Financing Instrument',
				'Implementation Level', 'National Planning Objectives' , 'Primary Program', 
				'Secondary Program', 'Primary Sub-Sector', 'Type Of Assistance', 
				'Primary Sector', 'Region', 'Primary Sector Sub-Sector',
				'Project Title', 'Beneficiary Agency', 'Implementing Agency',
				'Secondary Sector', 'Secondary Sector Sub-Sector', 'Responsible Organization', 'Activity Created By'
			);
YAHOO.amp.reportwizard.fundingGroups["component"]		= new Array(
				'Status', 'A.C. Chapter', 'Accession Instrument', 'Financing Instrument',
				'Implementation Level', 'National Planning Objectives', 'Primary Program',
				'Secondary Program', 'Primary Sector Sub-Sector', 'Type Of Assistance',
				'Primary Sector', 'Component Name', 'Project Title', 'Beneficiary Agency', 'Implementing Agency', 
				'Secondary Sector', 'Secondary Sector Sub-Sector', 'Responsible Organization', 'Activity Created By'
			);
YAHOO.amp.reportwizard.fundingGroups["contribution"]	= new Array(
				'Costing Donor', 'National Planning Objectives', 'Primary Program', 
				'Project Title', 'Secondary Program', 'Primary Sector Sub-Sector',
				'Status', 'Primary Sector', 'A.C. Chapter',
				'Accession Instrument', 'Donor', 'Secondary Sector', 'Secondary Sector Sub-Sector', 'Activity Created By'
			);

function insertColInfo (id, name) {
		YAHOO.amp.reportwizard.colIdToName[id]=name;
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
	
}

function getColDbId( liEl ) {
	return (liEl.getElementsByTagName("input")[0]).value;
}

function generateHierarchies(e) {
	var newDDObj		= new MyDragAndDropObject();
	var ulEl			= document.getElementById("dest_col_ul") ;
	var hierUlEl		= document.getElementById("source_hierarchies_ul") ;
	var items			= ulEl.getElementsByTagName("li");
	for (var i=0; i<items.length; i++ ) {
		colId	= (items[i].getElementsByTagName("input")[0]).value;
		if ( checkIfColIsHierarchy(colId) && (document.getElementById('hier_li_'+colId)==null) ) {
				var childNodes				= items[i].childNodes;
				//var startHtml				= "<input type='checkbox' name='selectedColumns' id='hier_chk_"+colId+"' value='"+colId+"' />  ";
				//var newObj					= document.createElement("li");
				//newObj.innerHTML			= startHtml + childNodes[childNodes.length-1].data;
				//newObj.setAttribute('class','list1');
				//newObj.setAttribute('id', 'hier_li_'+colId);
				
				var newObj					= ColumnsDragAndDropObject.generateLi(
											"hier_li_"+colId, "list1", "hier_chk_"+colId, colId, "selectedHierarchies", childNodes[childNodes.length-1].data
											);
				hierUlEl.appendChild( newObj );
				var newDDProxy				= new YAHOO.util.DDProxy( newObj.id );
				newDDObj.addActions( newDDProxy );
				
		}
	}
	
	checkSelectedHierarchies();
}


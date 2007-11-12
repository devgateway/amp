<style type="text/css">
.all_markup
{margin:1em} 
.all_markup table
{border-collapse:collapse; width: 90%} 
.all_markup th
{border:1px solid #000;padding:.25em;background-color:#fff; font-size:small; color: #666666}
.all_markup th a, .all_markup th a:hover
{font-size:small; text-decoration: none;}
.all_markup td
{border-bottom:1px solid #000;padding:.25em;font-size:x-small}
.all_markup .yui-dt-odd {background-color:#eee;} /*light gray*/ 
.all_markup .yui-dt-selected {background-color:#97C0A5;} /*green*/ 
.all_markup .yui-dt-sortedbyasc, .all_markup .yui-dt-sortedbydesc {background-color:#eee;}

.all_markup .yui-dt-headtext {margin-right:5px;padding-right:15px;}
.all_markup .yui-dt-sortedbyasc .yui-dt-headcontainer {background: url('/repository/contentrepository/view/images/arrow_up.gif') no-repeat right;}/*arrow up*/
.all_markup .yui-dt-sortedbydesc .yui-dt-headcontainer {background: url('/repository/contentrepository/view/images/arrow_dn.gif') no-repeat right;}/*arrow down*/

#versions_markup {margin:1em;} 
#versions_markup table {border-collapse:collapse;} 
#versions_markup th {border:1px solid #000;padding:.25em;background-color:#fff;color:Black; font-size:x-small}
#versions_markup td {border-bottom:1px solid #000;padding:.25em;} 
</style>
<link rel="stylesheet" type="text/css" href="<digi:file src='module/contentrepository/scripts/datatable/assets/datatable.css'/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/contentrepository/scripts/fonts/fonts.css'/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/contentrepository/scripts/menu/assets/menu.css'/>"> 

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/datatable/datatable-beta-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/datatable/datasource-beta-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/ajaxconnection/connection-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/panel/dom-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/menu/menu-min.js'/>" > .</script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/container/container-core-min.js'/>" > .</script>
<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");
	YAHOO.namespace("YAHOO.amp.table");
	
	/* AJAX Callback object for showing versions*/
	var callbackForVersions	= {
		success: function (o) {
			YAHOO.amp.panels[1].setBody(o.responseText);
			YAHOO.amp.table.enhanceVersionsMarkup();
		},
		failure: function () {
			YAHOO.amp.panels[1].setBody("<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>");
		}
	}
	
	function requestVersions(uuid) {
		var request = YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/getVersionsForDocumentManager.do?uuid='+uuid, callbackForVersions);
	}
	
	/* Function for creating YAHOO datatable for versions */
	YAHOO.amp.table.enhanceVersionsMarkup = function() {
		    this.columnHeadersForVersions = [
		        {key:"v_file_name",text:"File Name",sortable:true},
		        {key:"v_resource_title",text:"Resource Title",sortable:true},
		        {key:"v_date",text:"Date",type:"date",sortable:true},
		        {key:"v_description",text:"Description",sortable:false},
		        {key:"v_actions",text:"Actions",sortable:false}
		    ];
		    this.columnSetForVersions = new YAHOO.widget.ColumnSet(this.columnHeadersForVersions);
		
		    var versionsMarkup = YAHOO.util.Dom.get("versions_markup");
		    YAHOO.amp.table.dataTableForVersions = new YAHOO.widget.DataTable(versionsMarkup,this.columnSetForVersions	);
	};

</script>
<c:set var="translation1">
				<digi:trn key="contentrepository:documentDeleteConfirm">Are you sure you want to delete this document ?</digi:trn>
</c:set>
<c:set var="translation2">
				<digi:trn key="contentrepository:documentWaitForDelete">Deleting document ... </digi:trn>
</c:set>
<c:set var="translation3">
				<digi:trn key="contentrepository:documentDeleteConnectionProblems">Your request has not been carried out due to connection problems. We are sorry. Please try again !</digi:trn>
</c:set>
<c:set var="translation_no_doc_selected">
			<digi:trn key="contentrepository:noDocumentSelectedAlert">No document has been selected !</digi:trn>
</c:set>
<c:set var="translation_remove_failed">
			<digi:trn key="contentrepository:removeFailedAlert">Documents cannot be removed !</digi:trn>
</c:set>
<c:set var="translation_make_public_failed">
			<digi:trn key="contentrepository:makePublicFailedAlert">The request for making the document public failed. Please try again.</digi:trn>
</c:set>
<c:set var="translation_validation_title">
			<digi:trn key="contentrepository:plsSpecifyTitle">Please specify a title !</digi:trn>
</c:set>
<c:set var="translation_validation_filedata">
			<digi:trn key="contentrepository:plsSpecifyPath">Please specify a file path !</digi:trn>
</c:set>
<script type="text/javascript">
YAHOO.namespace("YAHOO.amp");
YAHOO.namespace("YAHOO.amp.table");

/* Function for creating YAHOO datatable for all documents*/
YAHOO.amp.table.enhanceMarkup = function(markupName) {

    this.columnHeaders = [
        {key:"file_name",text:"File Name",sortable:true},
        {key:"resource_title",text:"Resource Title",sortable:true},
        {key:"date",text:"Date",type:"date",sortable:true},
        {key:"content_type",text:"Content Type",sortable:true},
        {key:"description",text:"Description",sortable:false},
        {key:"actions",text:"Actions",sortable:false}
    ];
    this.columnSet 	= new YAHOO.widget.ColumnSet(this.columnHeaders);

    var markup	 				= YAHOO.util.Dom.get(markupName);
    //var datasource				= YAHOO.util.DataSource(markup);
    var options					= {paginated:true, 
	                				 
	                    				rowsPerPage: 10,
	                    				pageCurrent: 1,
	                    				startRecordIndex: 1,
								        pageLinksLength: 2
	                    			
	                			};
    
	var dataTable 				= new YAHOO.widget.DataTable(markupName, this.columnSet, null, options);
    
    return dataTable;
};

/* Ajax function that creates a callback object after a delete command 
was issued in order to delete the respective row/document*/
function getCallbackForDelete (row, table) {
	callbackForDelete = {
		success: function(o) {
			YAHOO.amp.panels[2].setBody(o.responseText);
			if (document.getElementById("successfullDiv") != null) {
				if (YAHOO.amp.table.teamtable != null)
						YAHOO.amp.table.teamtable.deleteRow(row);
				if (YAHOO.amp.table.mytable != null)
						YAHOO.amp.table.mytable.deleteRow(row);		
				for (i=0; i<YAHOO.amp.datatables.length; i++) {
						YAHOO.amp.datatables[i].deleteRow(row);
				}
			}
					

		},
		failure: function(o) {
			//YAHOO.amp.panels[2].setBody("<div align='center'><font color='red'>${translation3}</font></div>");
			alert('${translation3}');
		}
	}
	return callbackForDelete;
}
/* Function called after clicking delete */
function deleteRow(uuid, o) {
	var a			= document.getElementById('a'+uuid);
	var possibleRow	= a;
	while (true) {
		possibleRow	= possibleRow.parentNode;
		if (possibleRow.nodeName.toLowerCase()=="tr")
				break;
	}
	if ( confirmDelete() ) {
		//var translation2				= "${translation2}";
		//YAHOO.amp.panels[2].setBody("<div align='center'>" + translation2 + "<br /> <img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0'/> </div>" );
		//YAHOO.amp.panels[2].setFooter("<div align='right'><button type='button' onClick='hidePanel(2)'>Close</button></div>");
		//showPanel(2);
		//YAHOO.amp.table.dataTable.deleteRow(possibleRow);
		YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/deleteForDocumentManager.do?uuid='+uuid, getCallbackForDelete(possibleRow, YAHOO.amp.table.dataTable));
		
	}
}
function confirmDelete() {
	var ret		= confirm('${translation1}');
	return ret;
}
</script> 


<script type="text/javascript">
YAHOO.namespace("YAHOO.amp");
YAHOO.namespace("YAHOO.amp.table");

var isMinusPrivate	= true;
var isMinusTeam		= true;

YAHOO.amp.minuses			= new Array();
YAHOO.amp.num_of_tables		= 0;
YAHOO.amp.datatables		= new Array();
YAHOO.amp.windowControllers	= new Array();

function WindowControllerObject(bodyContainerEl) {
	this.bodyContainerElement	= bodyContainerEl;
	this.titleSpanEl;
	
	this.datatable				= null;
	
	this.lastPopulateObject		= null;
	
	this.setTitle				= function (title) {
									this.titleSpanEl.innerHTML	= title;
								};
	
	this.reload					= function() 
								{
									populateCallback(null, null, lastPopulateObject);
								};
	this.populateCallback		= function (sType, aArgs, obj) {
				this.lastPopulateObject	= obj;
				var parameters	= "";
				if ( obj.publicDocs != null ) {
						var publicDocs	= "<%= org.digijava.module.contentrepository.helper.CrConstants.GET_PUBLIC_DOCUMENTS %>";
						parameters	+= "&"+publicDocs+"="+obj.publicDocs;
				}
				if (obj.rights != null) {
					if (obj.rights.versioningRights != null) 
						parameters	+= "&versioningRights=" + obj.rights.versioningRights;
					if (obj.rights.deleteRights != null) 
						parameters	+= "&deleteRights=" + obj.rights.deleteRights;
				}
				if (obj.userName != null)
					parameters	+= "&otherUsername=" + obj.userName;
				if (obj.teamId != null)
					parameters	+= "&otherTeamId=" + obj.teamId;
					
				if (obj.docListInSession != null) {
					parameters	+= "&docListInSession=" + obj.docListInSession;
				}
				//alert(parameters);
				this.bodyContainerElement.innerHTML="<div align='center'>Please wait a moment...<br /><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0' /> </div>";
				YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/documentManager.do', getCallbackForOtherDocuments(this.bodyContainerElement, this),
								'ajaxDocumentList=true'+parameters );
				};
				
	this.populateWithSelDocs	= function (documentsType, rights) {
									var o				= new Object();
									o.docListInSession	= documentsType;
									if (rights != null) {
										o.rights	= rights;
									}
									this.populateCallback (null, null, o);
								}
	
	this.populateWithPublicDocs	= function () {
									var o				= new Object();
									o.publicDocs		= true;
									this.populateCallback(null, null, o);
								}
}

/* Used for creating a new window */
function newWindow(title, showSelectButton, otherDocumentsDiv) {
	var i;

	YAHOO.amp.minuses[YAHOO.amp.num_of_tables]	= true;

	var newDiv 						= document.createElement("div");
	newDiv.id						= "newDivId" + YAHOO.amp.num_of_tables;
	
	var tableTemplateElement		= document.getElementById("tableTemplate");
	
	newDiv.innerHTML				= tableTemplateElement.innerHTML + "<br />" + "<br />";
	
	var otherDocumentsDivElement	= document.getElementById(otherDocumentsDiv);
	
	otherDocumentsDivElement.appendChild(newDiv);
	
	newDiv							= document.getElementById("newDivId" + YAHOO.amp.num_of_tables);
	
	for(i=0; i<newDiv.childNodes.length; i++) {
		if ( newDiv.childNodes[i].nodeName.toLowerCase() == 'table' ) {
				newDiv.childNodes[i].style.background	= 'white';
				break;
		}
		
	}
	
	var otherDocumentsImgElement	= getElementByNameFromList("otherDocumentsImg", newDiv.getElementsByTagName("img") );
	var otherDocumentsDivElement	= getElementByNameFromList("otherDocumentsDiv", newDiv.getElementsByTagName("a") );
	var otherDocumentsTdElement		= otherDocumentsDivElement.parentNode;
	var otherDocumentsTrElement		= otherDocumentsTdElement.parentNode;
	var otherDocumentsButtonElement	= getElementByNameFromList("otherDocumentsButton", newDiv.getElementsByTagName("button") );
	
	if (!showSelectButton) {
		otherDocumentsButtonElement.style.display	= 'none';
	}
	
	otherDocumentsImgElement.id		= "otherDocumentsImg" + YAHOO.amp.num_of_tables;
	otherDocumentsTrElement.id		= "otherDocumentsTr" + YAHOO.amp.num_of_tables;
	otherDocumentsTdElement.id		= "otherDocumentsTd" + YAHOO.amp.num_of_tables;
	otherDocumentsButtonElement.id	= "otherDocumentsMenu" + YAHOO.amp.num_of_tables;
	
	var windowController			= new WindowControllerObject(otherDocumentsTdElement);
	
	/* Finding the title wrapper element */
	var temp = otherDocumentsButtonElement;
	while (temp != null) {
		temp	= temp.nextSibling;	
		if (temp.nodeName.toLowerCase() == 'span') {
			windowController.titleSpanEl	= temp;
			break;
		}
	}
	windowController.setTitle(title);
	/*END - Finding the title wrapper element */
	
	var obj							= new ContextObject(otherDocumentsImgElement, otherDocumentsTrElement, YAHOO.amp.num_of_tables);
	
	var menuObj						= null;
	if (showSelectButton) {
				menuObj	= addMenuToDocumentList(YAHOO.amp.num_of_tables, newDiv, windowController);
				YAHOO.util.Event.addListener(otherDocumentsButtonElement, "click", showMenu, menuObj, true);
	}
	
	YAHOO.util.Event.addListener(otherDocumentsImgElement.parentNode, "click", callbackToggle, obj, true);
	
	YAHOO.amp.windowControllers[YAHOO.amp.num_of_tables]	= windowController;
	
	YAHOO.amp.num_of_tables++;
	
	return windowController;
}

/* Wrapper function for toggleView function. Used by new windows. */
function callbackToggle(e, obj) {
	YAHOO.amp.minuses[this.num]		= toggleView( this.innerTr.id, this.plusMinusImg.id, YAHOO.amp.minuses[this.num]);
}

/* Creates object used for toggle view */
function ContextObject(plusMinusImg, innerTr, num) {
	this.plusMinusImg	= plusMinusImg;
	this.innerTr		= innerTr;
	this.num			= num
}

/* Returns the element with name elName form the list list  */
function getElementByNameFromList(elName, list) {
	var j;
	for(j=0; j<list.length; j++) {
		if (list[j].name == elName) {
			return list[j];
		}
	}
	return null;
}

function saveSelectedDocuments() {
	doSelectedDocuments('set');
}

function removeSelectedDocuments() {
	doSelectedDocuments('remove');
}

function doSelectedDocuments(action) {
	selectedDocs	= getAllSelectedDocuments();
	if (selectedDocs.length == 0) {
		alert('${translation_no_doc_selected}');
		return;
	}
	
	var postString 	= createPostString(selectedDocs, action);
	//alert(postString);	
	
	var callback;
	if (action == 'set') {
		callback	= {
							success:function(o) {
											window.opener.location.replace(window.opener.location.href); 
											window.close();
											}
							};
	}
	if (action == 'remove') {
		callback	= {
						success:function(o) {
									window.location.replace(window.location.href);						
								},
						failure:function(o){
									alert('${translation_remove_failed}');
								}
						}
	}
	
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/selectDocumentDM.do", callback, postString );
}

function createPostString(selectedDocs, action) {
	var i;
	var postString 	= "action=" + action;
	for (i=0; i<selectedDocs.length; i++) {
		postString	+= "&selectedDocs=" + selectedDocs[i];
	}
	return postString;
}


/* Gets all selected documents on the page*/
function getAllSelectedDocuments () {
	var i;
	result	= new Array();
	for (i=0; i<YAHOO.amp.datatables.length; i++) {
			getSelectedDocumentsFromDatatable(YAHOO.amp.datatables[i], result);
			//alert('Selected files so far: ' + result);
	}
	return result;
}

/* Returns the UUIDs of the selected documents in the datatable 'datatable'. 
 If vec not null the results are added to vec array and vec is returned. 
 Otherwise they are returned as a new array 
*/
function getSelectedDocumentsFromDatatable(datatable, vec) {
	var i;
	var result;
	if (vec != null)
			result	= vec;
	else
		result	= new Array();
	//alert (datatable);
	trEls	= datatable.getSelectedRows();
	//alert("Rows Selected: " + trEls.length);
	
	var vector_length		= result.length;
	for (i=0; i<trEls.length; i++) {
		//alert(i);
		var divDocumentUUID	= getElementByNameFromList ( "aDocumentUUID", trEls[i].getElementsByTagName("a") );
		//alert("adding:" + divDocumentUUID + " uuid: " + divDocumentUUID.innerHTML);
		//alert(result.length + i);
		result[vector_length + i]	= divDocumentUUID.innerHTML;
	}
	return result;
}

/* Show & sets position of document selector menu on a new window */
function showMenu(e, obj) {
	
	this.moveTo(  YAHOO.util.Event.getPageX(e), YAHOO.util.Event.getPageY(e) );
	this.show();
}
/* Function that creates AJAX callback object that is used when receiving 
document list from server. windowController.datatable field will be set to the created datatable. */
function getCallbackForOtherDocuments(containerElement, windowController) {
	var num						= YAHOO.amp.num_of_tables - 1;
	var divId					= "other_markup" + num;
	callbackForOtherDocuments	= {
		success: function(o) {
					containerElement.innerHTML	= "<div class='all_markup' align='center' id='"+divId+"'>" + o.responseText + "</div>";
					var datatable				= YAHOO.amp.table.enhanceMarkup(divId);
					datatable.subscribe("cellClickEvent",datatable.onEventSelectRow);
					
					YAHOO.amp.datatables[YAHOO.amp.num_of_tables-1] = datatable;
					windowController.datatable	= datatable;
				},
		failure: function(o) {
					containerElement.innerHTML	= "Unable to retrieve requested documents";
				}
	};
	
	return callbackForOtherDocuments;

}

/* Creating document selector menu for new window */
function addMenuToDocumentList (menuNum, containerElement, windowController) {
	var menu		= new YAHOO.widget.Menu("mymenu" + menuNum);
	
	var membersMenu	= new YAHOO.widget.Menu("membersMenu" + menuNum);
	
	<logic:notEmpty name="tMembers">
	<logic:iterate name="tMembers" id="member">
		var scopeObj	= {
			teamId				: '<bean:write name="member" property="teamId" />',
			userName			: '<bean:write name="member" property="email" />'
		};
		var onclickObj 	= {
			fn					: windowController.populateCallback,
			obj					: scopeObj,
			scope				: windowController
			
		};
		var menuItem	= new YAHOO.widget.MenuItem('<bean:write name="member" property="email" />', { onclick:onclickObj } );
		membersMenu.addItem(menuItem); 

	</logic:iterate>
	menu.addItem(  new YAHOO.widget.MenuItem("Team Member Documents", {submenu: membersMenu})   );
	</logic:notEmpty>
	
	<logic:notEmpty name="meTeamMember">
	var scopeObj	= {
			teamId				: '<bean:write name="meTeamMember" property="teamId" />'
		};
		var onclickObj 	= {
			fn					: windowController.populateCallback,
			obj					: scopeObj,
			scope				: windowController
			
		};
		
	menu.addItem(  new YAHOO.widget.MenuItem("Team Documents", {onclick: onclickObj} )   );
	</logic:notEmpty>
	
		var onclickObj 	= {
			fn					: windowController.populateWithPublicDocs,
			scope				: windowController
			
		};
		
	menu.addItem(  new YAHOO.widget.MenuItem("Public Documents", {onclick: onclickObj} )   );
	
	menu.render(containerElement);
	//menu.show();
	return menu;

}
/* 	 the view for body of window
elementId	- html id of the html element that should be hidden/unhidden
iconId		- html id of the html plus/minus image 
isMinus 	- true if body is hidden right now
*/
function toggleView(elementId, iconId, isMinus) {
	var icon	= document.getElementById(iconId);
	var element	= document.getElementById(elementId);
	if (isMinus) {
			icon.src				= '/repository/contentrepository/view/images/dhtmlgoodies_plus.gif';
			element.style.display	= 'none';
			
			isMinus		= false;
	}
	else{
			icon.src	= '/repository/contentrepository/view/images/dhtmlgoodies_minus.gif';
			element.style.display	= 'table-row';
			
			isMinus		= true;
	}
	return isMinus;
}
/* Configures the form with id typeId */
function configPanel(panelNum, title, description, uuid) {
	document.getElementById('addDocumentErrorHolderDiv').innerHTML = '';

	var myForm		= document.getElementById('typeId').form;
	
	myForm.docTitle.value		= title;
	myForm.docDescription.value	= description;
	myForm.uuid.value			= uuid;
	myForm.fileData.value		= null;
}


/* Sets whether we are currently adding a new 
 personal/team document or a new version */
function setType(typeValue) {
	//alert('setting type:' + typeValue);
	var typeElement		= document.getElementById('typeId');
	typeElement.value	= typeValue;
	typeElement.form.type.value = typeValue;
	//alert(typeElement.form.type.value);
}

function validateAddDocument() {
	//alert( document.forms['crDocumentManagerForm'].docTitle.value );
	//alert( document.forms['crDocumentManagerForm'].fileData.value );
	var msg	= '';
	if (document.forms['crDocumentManagerForm'].docTitle.value == '')
		msg = msg + '${translation_validation_title}  ' ;
	if (document.forms['crDocumentManagerForm'].fileData.value == '')
		msg = msg + '${translation_validation_filedata}' ;
	
	document.getElementById('addDocumentErrorHolderDiv').innerHTML	= msg;
	if (msg.length == 0)
			return true;
	return false;	
}

function setAttributeOnNode(action, uuid, doReload) {
	
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert("${translation_make_public_failed}");
						};
	
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/setAttributes.do?uuid="+uuid+"&action="+action, callback);
}

/* Number of possible panels on this page */
YAHOO.amp.panelCounter	= 3;

YAHOO.util.Event.addListener(window, "load", initPanel) ;
</script>

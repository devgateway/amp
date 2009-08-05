<style type="text/css">

.yui-tt{ background: LightYellow; border-color: black }

.all_markup {margin:1em} 
.all_markup table {border-collapse:collapse;border: 1px solid #d7eafd;  width: 90%} 
.all_markup th {padding:.25em;background-color:rgb(153, 153, 153); font-size:12px; color: black; text-align: center;border-right: #ffcccc 1px solid;border-bottom: #cccccc 1px solid;}
.all_markup th a, .all_markup th a:hover {font-size: 10px;font: bold 7.5pt "Verdana"; color:black; text-decoration: none;}
.all_markup td {padding:.25em;font-size:11px;color:#0E69B3;font-family:	Arial,Helvetica,sans-serif;font-size:10px;letter-space:2px;}
.all_markup .yui-dt-even {background-color:#FFFFFF;} 
.all_markup .yui-dt-odd {background-color:#CCDBFF;} /* a light blue color */ 
.all_markup .yui-dt-selected {background-color:#A7CC25;} /*green*/
.all_markup .yui-dt-headtext {background-color: rgb(153, 153, 153); color: black;margin-right:5px;padding-right:15px;font-size: 10px;font: bold 7.5pt "Verdana"; color:black;}
.all_markup .yui-dt-headcontainer {background-color: rgb(153, 153, 153); color: black;}
.all_markup .yui-dt-sortedbyasc .yui-dt-headcontainer {color: black;background: url('/repository/contentrepository/view/images/up.gif') no-repeat right;}/*arrow up*/
.all_markup .yui-dt-sortedbydesc .yui-dt-headcontainer {color: black;background: url('/repository/contentrepository/view/images/down.gif') no-repeat right;}/*arrow down*/
.all_markup .yui-dt-sortedbyasc, .all_markup .yui-dt-sortedbydesc {background-color: rgb(153, 153, 153); color: black;}

.versions_markup {margin:1em; overflow: auto; } 
.versions_markup table {border-collapse:collapse; overflow: auto;border: 1px solid #d7eafd;} 
.versions_markup th {padding:.25em;background-color:rgb(153, 153, 153); font-size:12px; color: black; text-align: center;border-right: #ffcccc 1px solid;border-bottom: #cccccc 1px solid;}
.versions_markup th a, .versions_markup th a:hover {font-size: 10px;font: bold 7.5pt "Verdana"; color:black; text-decoration: none;}
.versions_markup td {padding:.25em;font-size:11px;color:#0E69B3;font-family:	Arial,Helvetica,sans-serif;font-size:10px;letter-space:2px;}
.versions_markup .yui-dt-odd {background-color:#CCDBFF;} /* a light blue color */ 
.versions_markup .yui-dt-headtext {background-color: rgb(153, 153, 153); color: black;margin-right:5px;padding-right:15px;font-size: 10px;font: bold 7.5pt "Verdana"; color:black;}
.versions_markup .yui-dt-headcontainer {background-color: rgb(153, 153, 153); color: black;}
.versions_markup .yui-dt-sortedbyasc .yui-dt-headcontainer {background: url('/repository/contentrepository/view/images/up.gif') no-repeat right;}/*arrow up*/
.versions_markup .yui-dt-sortedbydesc .yui-dt-headcontainer {background: url('/repository/contentrepository/view/images/down.gif') no-repeat right;}/*arrow down*/
.versions_markup .yui-dt-sortedbyasc, .versions_markup .yui-dt-sortedbydesc {background-color: rgb(153, 153, 153); color: black;}

#menuContainerDiv .yuimenu {z-index: 101;}
#menuContainerDiv ul.first-of-type { background: transparent; z-index: 300000;} 
#menuContainerDiv ul.first-of-type li  {
	background: transparent; z-index: 300001
}
#menuContainerDiv ul.first-of-type li.selected  {
	background: #8c8ad0;
}
#menuContainerDiv ul.first-of-type a{float: none; background: transparent; color: #000000; font-size: 10px; text-decoration: none; font-style: normal;}
#menuContainerDiv ul.first-of-type li.selected a.selected{ 
	color: #ffffff; text-decoration: underline; font-size: 10px; font-style: normal;
}
</style>

<link rel="stylesheet" type="text/css" href="<digi:file src='module/contentrepository/scripts/datatable/assets/datatable.css'/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/contentrepository/scripts/menu/assets/menu.css'/>"> 
<link rel="stylesheet" type="text/css" href="<digi:file src='module/aim/scripts/panel/assets/reset.css'/>"> 


<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/datatable/datatable-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/datatable/datasource-beta-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/ajaxconnection/connection-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/panel/dom-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/menu/menu-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/container/container-core-min.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" > </script>


<%@page import="java.net.URLDecoder"%>

<c:set var="translation_public_ver_msg">
		<digi:trn key="contentrepository:publicVersionMsg">The marked version is currently public</digi:trn>
</c:set>

<c:set var="headerVersion">
				<digi:trn key="contentrepository:versionhistory:header:version">Version</digi:trn>
</c:set>
<c:set var="headerType">
				<digi:trn key="contentrepository:versionhistory:header:type">Type</digi:trn>
</c:set>

<c:set var="headerFileName">
	<digi:trn key="contentrepository:versionhistory:header:resourcename">Resource Name</digi:trn>
</c:set>

<c:set var="headerDate">
				<digi:trn key="contentrepository:versionhistory:header:date">Date</digi:trn>
</c:set>

<c:set var="headerFileSize">
				<digi:trn key="contentrepository:TableHeader:Size">Size (MB)</digi:trn>
</c:set>

<c:set var="headerNotes">
				<digi:trn key="contentrepository:versionhistory:header:notes">Notes</digi:trn>
</c:set>

<c:set var="headerAction">
				<digi:trn key="contentrepository:versionhistory:header:actions">Actions</digi:trn>
</c:set>


<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp");
	YAHOO.namespace("YAHOO.amp.table");

	YAHOO.widget.DataTable.MSG_EMPTY = "<digi:trn key="aim:noRecordsFound">No records found</digi:trn>";
	
	/* AJAX Callback object for showing versions*/
	var callbackForVersions	= {
		success: function (o) {
			YAHOO.amp.panels[1].setBody( "<div class='versions_markup' align='center' id='versions_div'>" + o.responseText + "</div>");
			setHeightOfDiv("versions_div", 250, 250);
			YAHOO.amp.table.enhanceVersionsMarkup();
			YAHOO.amp.panels[1].setFooter("* ${translation_public_ver_msg}");
			//createToolTips( document.getElementById('versions_div') );
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
			    {key:"v_ver_num",type:"number", text:"${headerVersion}",sortable:true},
			    {key:"v_type",text:"${headerType}",sortable:true},
		        {key:"v_file_name",text:"${headerFileName}",sortable:true},
		        {key:"v_date",text:"${headerDate}",type:"date",sortable:true},
		        {key:"size",type:"number",text:"${headerFileSize}",sortable:true},
		        {key:"v_notes",text:"${headerNotes}",sortable:false},
		        {key:"v_actions",text:"${headerAction}",sortable:false}
		    ];
		    this.columnSetForVersions = new YAHOO.widget.ColumnSet(this.columnHeadersForVersions);
	      var options					= {
	                    				rowsPerPage: 7,
	                    				pageCurrent: 1,
	                    				startRecordIndex: 1,
								        pageLinksLength: 5
	                			};
		
		    var versionsDiv = YAHOO.util.Dom.get("versions_div");
		    YAHOO.amp.table.dataTableForVersions = new YAHOO.widget.DataTable(versionsDiv,this.columnSetForVersions, null, options	);
	};

</script>
<c:set var="translation1">
	<digi:trn key="contentrepository:documentdeleteconfirm">Are you sure you want to delete this document ?</digi:trn>
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
<c:set var="translation_validation_url">
			<digi:trn key="contentrepository:plsSpecifyUrl;">Please specify a Url !</digi:trn>
</c:set>

<c:set var="translation_validation_title_chars">
			<digi:trn key="contentrepository:plsTitleChars">Please only use letters, digits, '_' and space !</digi:trn>
</c:set>
<c:set var="translation_validation_filedata">
			<digi:trn key="contentrepository:plsSpecifyPath">Please select a file path !</digi:trn>
</c:set>
<c:set var="translation_unableToRetriveDocuments">
			<digi:trn key="contentrepository:unableToRetriveDocs">Unable to retrieve requested documents</digi:trn>
</c:set>

<c:set var="translation_mandatory_fields">
			<digi:trn key="contentrepository:mandatoryFieldsMsg">The marked fields are mandatory</digi:trn>
</c:set>
<c:set var="translation_add_new_content">
			<digi:trn key="contentrepository:addNewContentTitle">Add new content</digi:trn>
</c:set>
<c:set var="translation_add_new_version">
			<digi:trn key="contentrepository:addNewVersionTitle">Add new version</digi:trn>
</c:set>

<c:set var="trans_headerType">
	 <digi:trn key="contentrepository:TableHeader:Type">Type</digi:trn>  
</c:set>
<c:set var="trans_headerFileName">
	 <digi:trn key="contentrepository:TableHeader:ResourceName">Resource Name</digi:trn>  
</c:set>
<c:set var="trans_headerSelect">
	 <digi:trn key="contentrepository:TableHeader:Select">Select</digi:trn>  
</c:set>
<c:set var="trans_headerResourceTitle">
	 <digi:trn key="contentrepository:TableHeader:Title">Title</digi:trn>  
</c:set>
<c:set var="trans_headerDate">
	 <digi:trn key="contentrepository:TableHeader:Date">Date</digi:trn>
</c:set>
<c:set var="trans_fileSize">
	 <digi:trn key="contentrepository:TableHeader:Size">Size (MB)</digi:trn>
</c:set>
<c:set var="trans_headerContentType">
	<digi:trn key="contentrepository:TableHeader:ContentType">Content Type</digi:trn>  
</c:set>
<c:set var="trans_cmDocType">
	<digi:trn key="contentrepository:TableHeader:CmDocType">Document Type</digi:trn>  
</c:set>

<c:set var="trans_headerDescription">
	 <digi:trn key="contentrepository:TableHeader:Description">Description</digi:trn>
</c:set>

<c:set var="trans_headerActions">
	<digi:trn key="contentrepository:TableHeader:Actions">Actions</digi:trn>
</c:set>


<c:set var="trans_teamMemberDocuments">
	<digi:trn key="contentrepository:MenuItem:TeamMemberDocuments">Team Member Documents</digi:trn>
</c:set>

<c:set var="trans_teamDocuments">
	<digi:trn key="contentrepository:MenuItem:TeamDocuments">Team Documents</digi:trn>
</c:set>

<c:set var="trans_publicDocuments">
	<digi:trn key="contentrepository:MenuItem:PublicDocuments">Public Documents</digi:trn>
</c:set>

<c:set var="trans_options">
	<digi:trn key="contentrepository:MenuItem:Options">Options</digi:trn>
</c:set>

<c:set var="trans_optionsShowOnlyDocuments">
	<digi:trn key="contentrepository:MenuItem:Options:ShowOnlyDocs">Show only documents</digi:trn>
</c:set>

<c:set var="trans_optionsShowOnlyWebLinks">
	<digi:trn key="contentrepository:MenuItem:Options:ShowOnlyWebLinks">Show only web links</digi:trn>
</c:set>

<script type="text/javascript">
YAHOO.namespace("YAHOO.amp");
YAHOO.namespace("YAHOO.amp.table");

/* Function for creating YAHOO datatable for all documents*/
YAHOO.amp.table.enhanceMarkup = function(markupName) {
	var checkBoxToHide = document.getElementById("checkBoxToHide");
	
	if(checkBoxToHide != null && checkBoxToHide.value == "true"){
	    this.columnHeaders = [
			{key:"select",type:"checkbox", text:"${trans_headerSelect}",sortable:false,width:10},
			{key:"resource_title",text:"${trans_headerResourceTitle}",sortable:true,width:150},
		    {key:"type",text:"${trans_headerType}",sortable:true},
	        {key:"file_name",text:"${trans_headerFileName}",sortable:true,width:150},
	        {key:"date",type:"Date",text:"${trans_headerDate}",sortable:true},
	        {key:"size",type:"number",text:"${trans_fileSize}",sortable:true},
	        {key:"cm_doc_type",text:"${trans_cmDocType}",sortable:true},
	        {key:"description",text:"${trans_headerDescription}",sortable:false,width:100},
	        {key:"actions",text:"${trans_headerActions}",sortable:false,width:150}
	    ];
	}
	else{
	    this.columnHeaders = [
      			{key:"resource_title",text:"${trans_headerResourceTitle}",sortable:true,width:150},
       		    {key:"type",text:"${trans_headerType}",sortable:true},
       	        {key:"file_name",text:"${trans_headerFileName}",sortable:true,width:150},
        	    {key:"date",type:"Date",text:"${trans_headerDate}",sortable:true},
	   	        {key:"size",type:"number",text:"${trans_fileSize}",sortable:true},
            	{key:"cm_doc_type",text:"${trans_cmDocType}",sortable:true},
	            {key:"description",text:"${trans_headerDescription}",sortable:false,width:100},
	            {key:"actions",text:"${trans_headerActions}",sortable:false,width:150}
	    ];
	}
    this.columnSet 	= new YAHOO.widget.ColumnSet(this.columnHeaders);

    var markup	 				= YAHOO.util.Dom.get(markupName);
    //var datasource				= YAHOO.util.DataSource(markup);
    var options					= {
    	    						pageCurrent:1,
									rowsPerPage:10,
							        pageLinksLength:2												        
	                			  };

	var dataTable 				= new YAHOO.widget.DataTable(markupName, this.columnSet, null, options);

	// this is for document in activity form, to be able to select them, since the checbox is removed
	dataTable.subscribe("cellClickEvent", dataTable.onEventSelectRow);
	
	if ( dataTable.getRecordSet().getLength() == null || dataTable.getRecordSet().getLength() == 0 ) {
		dataTable.showEmptyMessage();
	}
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
			else 
				YAHOO.amp.panels[2].show();

		},
		failure: function(o) {
			//YAHOO.amp.panels[2].setBody("<div align='center'><font color='red'>${translation3}</font></div>");
			//alert("${translation3}");
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
	
	this.showOnlyLinks			= false;
	this.showOnlyDocs			= false;
	
	this.clickedShowOnlyLinks	= function (sType, aArgs, obj) {
									//alert(obj);
									if ( this.showOnlyLinks ) {
										this.showOnlyLinks	= false;
									}
									else {
										this.showOnlyLinks	= true;
										this.showOnlyDocs	= false;
									}
									//alert(this.showOnlyLinks);
									obj.mItemDoc.cfg.setProperty("checked", this.showOnlyDocs);
									obj.mItemLink.cfg.setProperty("checked", this.showOnlyLinks);
									if ( this.lastPopulateObject != null )
										this.populateCallback(null, null, this.lastPopulateObject);
									return;
								}
	this.clickedShowOnlyDocs	= function (sType, aArgs, obj) {
									//alert(obj);
									if ( this.showOnlyDocs ) {
										this.showOnlyDocs	= false;
									}
									else {
										this.showOnlyLinks	= false;
										this.showOnlyDocs	= true;
									}
									//alert(this.showOnlyDocs);
									obj.mItemDoc.cfg.setProperty("checked", this.showOnlyDocs);
									obj.mItemLink.cfg.setProperty("checked", this.showOnlyLinks);
									if ( this.lastPopulateObject != null )
										this.populateCallback(null, null, lastPopulateObject);
									return;
								}
	
	
	this.setTitle				= function (title) {
									this.titleSpanEl.innerHTML	= title;
								};
	
	this.reload					= function() 
								{
									populateCallback(null, null, this.lastPopulateObject);
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
					if (obj.rights.showVersionsRights != null) 
						parameters	+= "&showVersionsRights=" + obj.rights.showVersionsRights;
					if (obj.rights.makePublicRights != null) 
						parameters	+= "&makePublicRights=" + obj.rights.makePublicRights;
					if (obj.rights.viewAllRights != null) 
						parameters	+= "&viewAllRights=" + obj.rights.viewAllRights;
				}
				if (obj.userName != null)
					parameters	+= "&otherUsername=" + obj.userName;
				if (obj.teamId != null)
					parameters	+= "&otherTeamId=" + obj.teamId;
					
				if (obj.docListInSession != null) {
					parameters	+= "&docListInSession=" + obj.docListInSession;
				}
				if ( this.showOnlyLinks ) 
						parameters	+= "&showOnlyLinks=" + this.showOnlyLinks;
				if ( this.showOnlyDocs ) 
						parameters	+= "&showOnlyDocs=" + this.showOnlyDocs;
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
				var divForRenderingMenu		= document.getElementById("menuContainerDiv");
				if ( divForRenderingMenu == null )
					divForRenderingMenu		= newDiv;
				menuObj	= addMenuToDocumentList(YAHOO.amp.num_of_tables, divForRenderingMenu, windowController);
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
	selectedDocs			= getAllSelectedDocuments();
	var updatedDocsAction	= '<%=org.digijava.module.contentrepository.helper.CrConstants.REQUEST_UPDATED_DOCUMENTS_IN_SESSION%>';
	if (selectedDocs.length == 0) {
		alert("${translation_no_doc_selected}");
		return;
	}
	
	var postString 	= createPostString(selectedDocs, action);
	//alert(postString);	
	
	var callback;
	if (action == 'set') {
		callback	= {
							success:function(o) {
											var urlstr = window.opener.location.href;
											urlstr = urlstr.replace('~addSector=true',"");
											urlstr = urlstr.replace('~delPledge=true',"");
											urlstr = urlstr.replace('~addPledge=true',"");
											urlstr = urlstr.replace('~remSectors=true',"");
											//alert(urlstr);
											window.opener.location.replace(urlstr+"&"+updatedDocsAction+"=true"); 
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
									alert("${translation_remove_failed}");
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
					datatable.subscribe("checkboxClickEvent", datatable.onEventSelectRow);
					//
	
					YAHOO.amp.datatables[YAHOO.amp.num_of_tables-1] = datatable;
					windowController.datatable	= datatable;
				
					//createToolTips(containerElement);
				},
		failure: function(o) {
					containerElement.innerHTML	= "${translation_unableToRetriveDocuments}";
				}
	};
	
	return callbackForOtherDocuments;

}

/* Creating document selector menu for new window */
function addMenuToDocumentList (menuNum, containerElement, windowController) {
	var menu		= new YAHOO.widget.Menu("mymenu" + menuNum);
	
	var membersMenu	= new YAHOO.widget.Menu("membersMenu" + menuNum);
	
	var optionsMenu	= new YAHOO.widget.Menu("optionsMenu" + menuNum);
	
	<logic:notEmpty name="tMembers">
	<logic:iterate name="tMembers" id="member" indexId="counterId">
		var scopeObj	= {
			teamId				: '<bean:write name="member" property="teamId" />',
			userName			: '<bean:write name="member" property="email" />'
		};
		var onclickObj 	= {
			fn					: windowController.populateCallback,
			obj					: scopeObj,
			scope				: windowController
			
		};
		var menuId		= "myMember-${counterId}";
		var menuItem	= new YAHOO.widget.MenuItem('<bean:write name="member" property="email" />', { onclick:onclickObj, id:menuId } );
		membersMenu.addItem(menuItem); 

	</logic:iterate>
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_teamMemberDocuments}", {submenu: membersMenu})   );
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
		
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_teamDocuments}", {onclick: onclickObj} )   );
	</logic:notEmpty>
	/*
		var onclickObj 	= {
			fn					: windowController.populateWithPublicDocs,
			scope				: windowController
			
		};
		
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_publicDocuments}", {onclick: onclickObj} )   );
	
	var scopeObj	= {
			mItemDoc			: null,
			mItemLink			: null
		};
	var onclickObj 	= {
			fn					: windowController.clickedShowOnlyDocs,
			obj					: scopeObj,
			scope				: windowController
			
	};
	var showDocItem			= new YAHOO.widget.MenuItem("${trans_optionsShowOnlyDocuments}", {onclick: onclickObj} );
	scopeObj.mItemDoc		= showDocItem;
	
	var onclickObj 	= {
			fn					: windowController.clickedShowOnlyLinks,
			obj					: scopeObj,
			scope				: windowController
	};
	var showLinkItem		= new YAHOO.widget.MenuItem("${trans_optionsShowOnlyWebLinks}", {onclick: onclickObj});
	scopeObj.mItemLink		= showLinkItem;
	
	optionsMenu.addItem( showDocItem );
	optionsMenu.addItem( showLinkItem );
	
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_options}", {submenu: optionsMenu})   );
	*/
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
			element.style.display	= '';
			isMinus		= true;
	}
	return isMinus;
}
/* Configures the form with id typeId */
function configPanel(panelNum, title, description, optionId, uuid, isAUrl) {
	document.getElementById('addDocumentErrorHolderDiv').innerHTML = '';
	if (optionId == null)
		optionId	= 0;

	var myForm		= document.getElementById('typeId').form;
	myForm.docTitle.value		= title;
	myForm.docDescription.value	= description;
	myForm.docNotes.value		= '';
	myForm.uuid.value			= uuid;
	myForm.fileData.value		= null;
	myForm.webLink.value		= '';
	myForm.docType.disabled		= false;
	if (isAUrl == null) 
		isAUrl	= false;
		
	if (isAUrl) {
		myForm.webResource[1].checked				= true;
		myForm.webResource[1].defaultChecked		= true;
		myForm.webResource[0].checked				= false;
		myForm.webResource[0].defaultChecked		= false;	
	}
	else {
		myForm.webResource[0].checked				= true;		
		myForm.webResource[0].defaultChecked		= true;
		myForm.webResource[1].checked				= false;
		myForm.webResource[1].defaultChecked		= false;
	}
	
	selectResourceType();
	
	if (uuid != null && uuid.length > 0) {
		myForm.webResource[1].disabled				= true;
		myForm.webResource[0].disabled				= true;
		
		myForm.docTitle.readOnly					= true;
		myForm.docTitle.style.background			= "#eeeeee"; 
		myForm.docTitle.style.color					= "darkgray";
		
		myForm.docDescription.readOnly				= true;
		myForm.docDescription.style.backgroundColor	= "#eeeeee";
		myForm.docDescription.style.color			= "darkgray";

		myForm.docType.style.backgroundColor	= "#eeeeee";
		myForm.docType.style.color			= "darkgray";


		
		setPanelHeader(0, "${translation_add_new_version}");
		
		var opts									= myForm.docType.options;
		for ( j=0; j<opts.length; j++ ) {
			if ( opts[j].value	== optionId ) {
				opts[j].selected	= true;
				break;
			}
		}
		myForm.docType.disabled						= true;
	}
	else {
		myForm.webResource[1].disabled				= false;
		myForm.webResource[0].disabled				= false;
		
		myForm.docTitle.readOnly					= false;
		myForm.docTitle.style.backgroundColor		= "";
		myForm.docTitle.style.color					= "";
		
		myForm.docDescription.readOnly				= false;
		myForm.docDescription.style.backgroundColor	= "";
		myForm.docDescription.style.color			= "";
		
		myForm.docType.selectedIndex				= 0;
		
		setPanelHeader(0, "${translation_add_new_content}");
	}
	
	setPanelFooter(0, "* ${translation_mandatory_fields}");
	
}

function selectResourceType() {
	var myForm		= document.getElementById('typeId').form;
	var elFile	= document.getElementById('tr_path');
	var elUrl	= document.getElementById('tr_url');
	if (myForm.webResource[1].checked) {
		elFile.style.display	= "none";
		elUrl.style.display		= "";
	}
	else {
		alex					= 'ùù éè &é"\'(-è_çà';
		elFile.style.display	= "";
		elUrl.style.display		= "none";	
	}
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
	var regexp	= new RegExp("[a-zA-Z0-9_ÀÁÃÄÇÈÉËÌÍÏÑÒÓÕÖÙÚÜàáãäçèéëìíïñòóõöùúü%& ']+");
	//alert( document.forms['crDocumentManagerForm'].docTitle.value );
	//alert( document.forms['crDocumentManagerForm'].fileData.value );
	var msg	= '';
	if (document.forms['crDocumentManagerForm'].docTitle.value == '')
		msg = msg + "${translation_validation_title}" ;
	else {
		var title	= document.forms['crDocumentManagerForm'].docTitle.value;
		var found	= regexp.exec(title);
		//document.forms['crDocumentManagerForm'].docTitle.value = title;
		if ( found != title ) {
			msg = msg + "${translation_validation_title_chars}" ;
		}
	}
	if ( document.forms['crDocumentManagerForm'].webResource[0].checked == true && 
			document.forms['crDocumentManagerForm'].fileData.value == '')
		msg = msg + "${translation_validation_filedata}" ;
	if ( document.forms['crDocumentManagerForm'].webResource[1].checked == true && 
			document.forms['crDocumentManagerForm'].webLink.value == '')
		msg = msg + "${translation_validation_url}" ;

	//document.forms['crDocumentManagerForm'].docDescription.value = escape(document.forms['crDocumentManagerForm'].docDescription.value);
	document.getElementById('addDocumentErrorHolderDiv').innerHTML	= msg;
	if (msg.length == 0)
			return true;
	return false;	
}

function setHeightOfDiv(divId, maxLimit, value ){
	var obj	= document.getElementById(divId);
	obj.style.width		= "580px";
	obj.style.overflow	= "auto";
	if (obj.offsetHeight > maxLimit)  {
		obj.style.height	= value;
		obj.style.overflow	= "scroll";
	}
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

function createToolTips(containerElement) {
	var elements	= containerElement.getElementsByTagName("a");
	
	for (i=0; i<elements.length; i++) {
		if ( elements[i].id != null ) {
			createToolTip(elements[i], containerElement);
		}
	}
	
}

function createToolTip (id, containerElement) {
		new YAHOO.widget.Tooltip("tt"+id, { context: id, container: containerElement });
} 


function downloadFile(uuid) {
	if (checkDocumentUuid(uuid)) {
		window.location='/contentrepository/downloadFile.do?uuid='+uuid;
	}
}

function checkDocumentUuid(uuid) {
	//alert(uuid);
	var stop = '<digi:trn jsFriendly="true">Please save the activity before downloading the file !</digi:trn>';
	if (uuid.indexOf("TEMPORARY") >= 0) {
		alert(stop);
		return false;
	}
	return true;
}

/* Number of possible panels on this page */
YAHOO.amp.panelCounter	= 3;

YAHOO.util.Event.addListener(window, "load", initPanel) ;


</script>


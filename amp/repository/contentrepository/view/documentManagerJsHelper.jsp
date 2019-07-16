<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.helper.GlobalSettingsConstants"%>
<%@page import="org.digijava.kernel.ampapi.endpoints.settings.SettingsConstants"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>
<%@ taglib uri="/taglib/resourcesettings" prefix="rs" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<!-- Individual YUI CSS files -->
<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/js_2/yui/datatable/assets/skins/sam/datatable.css">
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 

<%-- Huge JS block included directly in the DocumentManager page --%>

<style type="text/css">

.yui-tt{ background: LightYellow; border-color: #CCCCCC }
.yui-skin-sam .yui-dt th{background:#C7D4DB;border-color: white}
.yui-skin-sam .yui-dt table {border-color: #CCCCCC}
.yui-skin-sam .yui-dt-sortable span {font-size: 11px; font-family: Arial,Helvetica,sans-serif;}

.all_markup table {border-collapse:collapse;border: 1px solid #CCCCCC;  width: 100%}
.all_markup td {padding:.25em;font-family:	Arial,Helvetica,sans-serif;font-size:11px;letter-space:2px;}
.all_markup th {padding:.25em; font-size:11px; color: black; text-align: center;border-right: white 1px solid;border-bottom: #cccccc 1px solid;}
.all_markup th a, .all_markup th a:hover {font-size: 10px;font: bold 7.5pt "Verdana"; color:black; text-decoration: none;}
.all_markup tr.yui-dt-selected td {background-color:#a5bcf2;}/*green*/
.all_markup .yui-dt {width: 100%;} 
.all_markup .yui-dt-even td {background-color:#FFFFFF;} 
.all_markup .yui-dt-odd td {background-color:#f2f2f2;} /* blue*/
.all_markup .yui-dt-headtext {background-color: rgb(153, 153, 153); color: black;margin-right:5px;padding-right:15px;font-size: 10px;font: bold 7.5pt "Verdana"; color:black;}
.all_markup .yui-dt-headcontainer {background-color: #C7D4DB; color: black;}
.all_markup .yui-dt-sortedbyasc .yui-dt-headcontainer td{color: black;background: url('/repository/contentrepository/view/images/up.gif') no-repeat right;}/*arrow up*/
.all_markup .yui-dt-sortedbydesc .yui-dt-headcontainer td{color: black;background: url('/repository/contentrepository/view/images/down.gif') no-repeat right;}/*arrow down*/
.all_markup .yui-dt-sortedbyasc, .all_markup .yui-dt-sortedbydesc td{background-color: rgb(153, 153, 153); color: black;}


.versions_markup {margin:1em; overflow: auto; } 
.versions_markup table {width: 95%; border-collapse:collapse; overflow: auto;border: 1px solid #d7eafd;} 
.versions_markup th {padding:.25em;background-color:rgb(153, 153, 153); font-size:11px; color: black; text-align: center;border-right: white 1px solid;border-bottom: #cccccc 1px solid;}
.versions_markup th a, .versions_markup th a:hover {font-size: 10px;font: bold 7.5pt "Verdana"; color:black; text-decoration: none;}
.versions_markup td {padding:.25em;font-size:11px;color:#0E69B3;font-family:	Arial,Helvetica,sans-serif;font-size:10px;letter-space:2px;}
.versions_markup .yui-dt-odd td {background-color:#A7CC25;} /* a light blue color -- this doesn't apply (?)' */ 
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

#panelForTemplates .bd {     
    /* Apply scrollbars for all browsers. */ 
    height: 400px;
    width:450px; overflow: auto;      
     
}

.showActions {
	white-space: nowrap;
}
.wordWrap {
word-wrap: break-word;
white-space: pre-wrap; 
white-space: -moz-pre-wrap; 
white-space: -pre-wrap; 
}

div.actionsDivItem a span {
color : #376091;
font-size : 11 px;
font-weight : bold;
}

#show_legend_pop_box_private, #show_legend_pop_box_team  {display:none; margin-top:13px; position:absolute; width:300px; padding:8px; border:1px solid #CCCCCC; background-color:#FDFFE3;}
#show_legend_pop_box_private {text-align: left;}

</style>

<style>
	#show_legend_pop_box_private {
		margin-top: 17px;
	}
</style>


<!-- this is style for labels -->
<style>
.resource_label {font-size:11px; color:#FFFFFF; padding:2px; margin-right:3px;}
</style>


<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/connection/connection-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/dragdrop/dragdrop-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event-mouseenter/event-mouseenter-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/selector/selector-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/event-delegate/event-delegate-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/calendar/calendar-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/paginator/paginator-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datatable/datatable-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/tabview/tabview-min.js"></script> 

<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/FormatDateHelper.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/FilterAsYouTypePanel.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/ActionsMenu.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/documentPanelHelper.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='module/contentrepository/scripts/DynamicList.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src='script/tooltip/wz_tooltip.js'/>" > </script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>

<%@page import="java.net.URLDecoder"%>

<c:set var="translation_public_ver_msg">
		<digi:trn jsFriendly="true">The marked version is currently public</digi:trn>
</c:set>

<c:set var="headerVersion">
				<digi:trn jsFriendly="true">Version</digi:trn>
</c:set>

<script type="text/javascript">
	var show_index = false;
	var show_category = false;
	var show_organisations = false;
	
	<feature:display name="Resource Columns" module="Content Repository">
	
	<field:display name="Resource Index" feature="Resource Columns">
		show_index = true;
	</field:display>
	
	<field:display name="Resource Category" feature="Resource Columns">
		show_category = true;
	</field:display>

	<field:display name="Resource Organisations" feature="Resource Columns">
		show_organisations = true;
	</field:display>
	</feature:display>
</script>


<c:set var="headerPublYear">
	<digi:trn jsFriendly="true">Publ.Year</digi:trn>
</c:set>

<c:set var="headerType">
	<digi:trn jsFriendly="true">Type</digi:trn>
</c:set>

<c:set var="headerIndex">
	<digi:trn jsFriendly="true">Index</digi:trn>
</c:set>

<c:set var="headerCategory">
	<digi:trn jsFriendly="true">Category</digi:trn>
</c:set>

<c:set var="headerFileName">
	<digi:trn jsFriendly="true">Resource Name</digi:trn>
</c:set>

<c:set var="headerDate">
	<digi:trn jsFriendly="true">Date</digi:trn>
</c:set>

<c:set var="headerFileSize">
	<digi:trn jsFriendly="true">Size (MB)</digi:trn>
</c:set>

<c:set var="headerNotes">
	<digi:trn jsFriendly="true">Notes</digi:trn>
</c:set>

<c:set var="headerAction">
	<digi:trn jsFriendly="true">Actions</digi:trn>
</c:set>

<c:set var="labelstrn">
	<digi:trn jsFriendly="true">Labels</digi:trn>
</c:set>
<c:set var="filterstrn">
	<digi:trn jsFriendly="true">Filters</digi:trn>
</c:set>
<c:set var="keywordstrn">
	<digi:trn jsFriendly="true">Keywords</digi:trn>
</c:set>
<c:set var="keywordModetrn">
    <digi:trn jsFriendly="true">Keyword Mode</digi:trn>
</c:set>


<script type="text/javascript">
	var prevPage = null;
	var applylabels = "<digi:trn jsFriendly="true">Apply Labels</digi:trn>";
	var nonetext = "<digi:trn jsFriendly="true">none</digi:trn>";
	YAHOO.namespace("YAHOO.amp");
	YAHOO.namespace("YAHOO.amp.table");

	//YAHOO.widget.DataTable.MSG_EMPTY = "<digi:trn>No records found</digi:trn>";

	/* Check FormatDateHelper.js for more information */
	FormatDateHelper.prototype.formatString		= '<%= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT)%>';
	
	/* AJAX Callback object for showing versions*/
	var callbackForVersions	= {
		success: function (o) {
			YAHOO.amp.panels[1].setBody( "<div class='versions_markup' align='center' id='versions_div'>"  + o.responseText + "</div>");
			setHeightOfDiv("versions_div", 250, 250);
			YAHOO.amp.table.enhanceVersionsMarkup();
			var footerText='* ${translation_public_ver_msg} \n <font color="red">*</font> The marked version is shared';
			YAHOO.amp.panels[1].setFooter(footerText);
			//createToolTips( document.getElementById('versions_div') );
		},
		failure: function () {
			YAHOO.amp.panels[1].setBody("<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>");
		}
	}
	
	function requestVersions(uuid) {
		var currTime	= new Date().getTime();
		var request = YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/getVersionsForDocumentManager.do?uuid='+uuid+'&time='+currTime, callbackForVersions);
	}
	
	/* Function for creating YAHOO datatable for versions */
	YAHOO.amp.table.enhanceVersionsMarkup = function() {
		    this.columnHeadersForVersions = [
			    {key:"v_ver_num", type:"number", label:"${headerVersion}", sortable:true},
			    {key:"v_type", label:"${headerType}", sortable:true},
		        {key:"v_file_name", label:"${headerFileName}", sortable:true},
		        {key:"v_date", label:"${headerDate}",type:"date", sortable:true},
		        {key:"v_pub", label:"Pub.year",type:"date", sortable:true},
		        {key:"size", type:"number",label:"${headerFileSize}", sortable:true},
		        <field:display name="Resource Index" feature="Resource Columns">
		        	{key:"resource_index", label:"${headerIndex}", sortable:true},
		        </field:display>
		        <field:display name="Resource Index" feature="Resource Columns">
		        	{key:"resource_category", label:"${headerCategory}", sortable:true},
		        </field:display>		        	
		        {key:"v_notes", label:"${headerNotes}", sortable:false},
		        {key:"v_actions", label:"${headerAction}", sortable:false}
		    ];
		    this.columnSetForVersions = new YAHOO.widget.ColumnSet(this.columnHeadersForVersions);
	      var options					= {
	                    				rowsPerPage: 7,
	                    				pageCurrent: 1,
	                    				startRecordIndex: 1,
								        pageLinksLength: 5,
								        MSG_EMPTY: "<digi:trn>No records found</digi:trn>",
								        MSG_LOADING: "<digi:trn>Loading...</digi:trn>"
	                			};





    var versionsDiv = YAHOO.util.Dom.get("versions_div");

    var tableEl						= versionsDiv.getElementsByTagName("table")[0];
    var ds 				= new YAHOO.util.DataSource( tableEl );
    ds.responseType 		= YAHOO.util.DataSource.TYPE_HTMLTABLE;
    ds.responseSchema		= {fields: this.columnHeadersForVersions};


    YAHOO.amp.table.dataTableForVersions = new YAHOO.widget.DataTable(versionsDiv,this.columnSetForVersions,ds , options	);
   };
	
	function getfiltertext(){
		return '${filterstrn}';
	}
	
	function getlabelsext(){
		return '${labelstrn}';
	}
	
	function getkeywordsext(){
		return '${labelstrn}';
	}
	
	function getkeywordModeext(){
        return '${keywordModetrn}';
    }
</script>
<c:set var="translation1">
	<digi:trn jsFriendly="true">Are you sure you want to delete this document ?</digi:trn>
</c:set>

<c:set var="translation2">
				<digi:trn>Deleting document ... </digi:trn>
</c:set>
<c:set var="translation3">
				<digi:trn>Your request has not been carried out due to connection problems. We are sorry. Please try again !</digi:trn>
</c:set>
<c:set var="translation_no_doc_selected">
			<digi:trn>No document has been selected !</digi:trn>
</c:set>
<c:set var="translation_remove_failed">
			<digi:trn>Documents cannot be removed !</digi:trn>
</c:set>
<c:set var="translation_make_public_failed">
			<digi:trn>The request for making the document public failed. Please try again.</digi:trn>
</c:set>
<c:set var="translation_validation_title">
			<digi:trn>Please specify a title !</digi:trn>
</c:set>
<c:set var="translation_validation_url">
			<digi:trn>Please specify a Url !</digi:trn>
</c:set>
<c:set var="translation_upload_failed_too_big">
	<digi:trn>${uploadFailedTooBigMsg}</digi:trn>
</c:set>

<c:set var="translation_validation_title_chars">
			<digi:trn>Please only use letters, digits, '_!@#$%^&', () and space !</digi:trn>
</c:set>
<c:set var="translation_validation_filedata">
			<digi:trn>Please select a file path !</digi:trn>
</c:set>
<c:set var="translation_unableToRetriveDocuments">
			<digi:trn>Unable to retrieve requested documents</digi:trn>
</c:set>

<c:set var="translation_mandatory_fields">
	<digi:trn>The marked fields are mandatory</digi:trn>
</c:set>
<c:set var="translation_add_new_content">
			<digi:trn>Add new content</digi:trn>
</c:set>
<c:set var="translation_add_new_version">
			<digi:trn>Add new version</digi:trn>
</c:set>

<c:set var="trans_headerType">
	  <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Type</digi:trn>  </b></span>
</c:set>
<c:set var="trans_headerFileName">
	  <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Resource Name</digi:trn> </b></span> 
</c:set>
<c:set var="trans_headerSelect">
	  <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Select</digi:trn> </b></span> 
</c:set>
<c:set var="trans_headerResourceIndex">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Index</digi:trn></b></span>
</c:set>
<c:set var="trans_headerResourceCategory">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Category</digi:trn></b></span>
</c:set>
<c:set var="trans_headerResourceOrganisations">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Organisations</digi:trn></b></span>  
</c:set>
<c:set var="trans_headerResourceTitle">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Title</digi:trn></b></span>  
</c:set>
<c:set var="trans_headerDate">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b> <digi:trn>Date</digi:trn></b></span>
</c:set>
<c:set var="trans_fileSize">
	  <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Size (MB)</digi:trn></b></span>
</c:set>
<c:set var="trans_headerContentType">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Content Type</digi:trn></b></span>  
</c:set>
<c:set var="trans_cmDocType">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Document Type</digi:trn></b></span>  
</c:set>

<c:set var="trans_headerLabels">
	  <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Labels</digi:trn></b></span>
</c:set>

<c:set var="trans_headerActions">
	 <span style='font-size:12px; font-family: Arial,sans-serif;'><b><digi:trn>Actions</digi:trn></b></span>
</c:set>


<c:set var="trans_teamMemberDocuments">
	<digi:trn>Team Member Documents</digi:trn>
</c:set>

<c:set var="trans_teamDocuments">
	<digi:trn>Team Documents</digi:trn>
</c:set>

<c:set var="trans_publicDocuments">
	<digi:trn>Public Documents</digi:trn>
</c:set>

<c:set var="trans_sharedDocuments">
	<digi:trn>Shared Documents</digi:trn>
</c:set>

<c:set var="trans_options">
	<digi:trn>Options</digi:trn>
</c:set>

<c:set var="trans_optionsShowOnlyDocuments">
	<digi:trn>Show only documents</digi:trn>
</c:set>

<c:set var="trans_optionsShowOnlyWebLinks">
	<digi:trn>Show only web links</digi:trn>
</c:set>


<c:set var="yearPublication">
	<digi:trn>Publ. Year</digi:trn>
</c:set>
<c:set var="trans_headerYearofPubl">
<c:choose>
     <c:when test="${yearPublication.length() < 15}">
		 <span style='font-size:12px; font-family: Arial,sans-serif;'><b>${yearPublication}</b></span>
     </c:when>
    <c:otherwise>
         <span style='font-size:12px; font-family: Arial,sans-serif;' class='wordWrap'><b>${yearPublication}</b></span>
    </c:otherwise>
</c:choose>
    
</c:set>

<c:set var="trans_wait">
	<digi:trn>Loading...</digi:trn>
</c:set>
<script type="text/javascript">
<!--
var trnWait="${trans_wait}";
//-->
</script>

<script type="text/javascript">
YAHOO.namespace("YAHOO.amp");
var myTable = YAHOO.namespace("YAHOO.amp.table");

/* Function for creating YAHOO datatable for all documents*/
myTable.enhanceMarkup = function(markupName) {	
	var checkBoxToHide = document.getElementById("checkBoxToHide");
	<%
    	String dt = request.getParameter("documentsType");		
	%>
	var dt = "<%= dt %>";
	if(checkBoxToHide != null && checkBoxToHide.value == "true"){
		//alert (1);
	    this.columnHeaders = [
			{key:"resource_title",label:"${trans_headerResourceTitle}", sortable:true},
		    {key:"type",label:"${trans_headerType}",sortable:true},
	        {key:"file_name",label:"${trans_headerFileName}", sortable:true},
	        {key:"date",type:"date",label:"${trans_headerDate}", sortable:true},
	        {key:"yearOfPublication",type:"text",label:"${trans_headerYearofPubl}",sortable:true},
	        {key:"size",type:"number",label:"${trans_fileSize}", sortable:true},
	        {key:"cm_doc_type",label:"${trans_cmDocType}", sortable:true},
	        {key:"labels",label:"${trans_headerLabels}", sortable:false},
	        {key:"actions",label:"${trans_headerActions}", sortable:false}
	    ];
	}
	else if ((checkBoxToHide == null) && (dt == "Related Documents")) {
		//alert (2);
		this.columnHeaders = [
    			{key:"resource_title",label:"${trans_headerResourceTitle}",sortable:true, width:150},
    		    {key:"type",label:"${trans_headerType}",sortable:true},
    	        {key:"file_name",label:"${trans_headerFileName}",sortable:true, width:150},
    	        {key:"date",type:"date",label:"${trans_headerDate}",sortable:true},
    	        {key:"yearOfPublication",type:"text",label:"${trans_headerYearofPubl}", sortable:true},
    	     	{key:"size",type:"number",label:"${trans_fileSize}",sortable:true},
    	        {key:"cm_doc_type",label:"${trans_cmDocType}",sortable:true},
    	        {key:"labels",label:"${trans_headerLabels}",sortable:false, width:100},
    	        {key:"actions",label:"${trans_headerActions}",sortable:false, width:150}
    	    ];
	}else {
		//alert(3);
		//debugger;
		
		var totalWidth = 70 + 50 + 40 + 50 + 50;
		var freeWidth = 0;
		var freeloaders = 4.5; // the 3 optional columns + 1.5 x cm_doc_type
		
		if (!show_index){
			freeWidth += 40;
			freeloaders --;
		}
		if (!show_category){
			freeWidth += 50;
			freeloaders --;
		}
		if (!show_organisations){
			freeWidth += 50;
			freeloaders --;
		}
		var extraSpacePerFreeLoader = freeloaders = 0 ? 0 : freeWidth / freeloaders;
		
	    this.columnHeaders = [];
	    this.columnHeaders.push({key:"resource_title",label:"${trans_headerResourceTitle}", sortable:true});
	    this.columnHeaders.push({key:"type",label:"${trans_headerType}", sortable:true, width: 40});
	    this.columnHeaders.push({key:"file_name",label:"${trans_headerFileName}", sortable:true});
	    this.columnHeaders.push({key:"date",type:"Date",label:"${trans_headerDate}",sortable:true, formatter: YAHOO.widget.DataTable.formatDate, width:75});
	    this.columnHeaders.push({key:"yearOfPublication", type:"number",label:"${trans_headerYearofPubl}",sortable:true, width:70});
	    this.columnHeaders.push({key:"cm_doc_type",label:"${trans_cmDocType}", sortable:true});
	    if (show_index)
	    	this.columnHeaders.push({key:"resource_index",label:"${trans_headerResourceIndex}", sortable:true, width: 100});
	    if (show_category)
	    	this.columnHeaders.push({key:"resource_category",label:"${trans_headerResourceCategory}", sortable:true, width:70});
	    if (show_organisations)
	    	this.columnHeaders.push({key:"resource_organisations",label:"${trans_headerResourceOrganisations}", sortable:true});
	    this.columnHeaders.push({key:"labels",label:"${trans_headerLabels}",sortable:true});
	    this.columnHeaders.push({key:"size",type:"number",label:"${trans_fileSize}",sortable:true, width:65});
	    this.columnHeaders.push({key:"actions",label:"${trans_headerActions}",sortable:false, width:60});
	}

    var markup	 				= YAHOO.util.Dom.get(markupName);
 
	var myPaginator = new YAHOO.widget.Paginator({ 
    	rowsPerPage:10,
    	template : "<span class='t_sm'><b><digi:trn>Pages:</digi:trn></b><span> {FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}",
    	//template : "{FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink}&nbsp;&nbsp;",
    	firstPageLinkLabel : 	"<digi:trn>&lt;&lt;</digi:trn>", 
        previousPageLinkLabel : "<digi:trn>prev</digi:trn>",
        nextPageLinkLabel		: '<digi:trn jsFriendly="true">next</digi:trn>',
        lastPageLinkLabel		: '<digi:trn jsFriendly="true">&gt;&gt</digi:trn>',
    	
        // use custom page link labels
        pageLabelBuilder: function (page,paginator) {
                var curr = paginator.getCurrentPage();
                if (curr == page) {
                	return "<span class='current-page'>"+page+"</span>|";
                }
                else {
                	return page;
                }
                
        }
    });	
	
    var oConfigs = { 
    		 // Create the Paginator	       
	         paginator:myPaginator,        
	         MSG_EMPTY: "<digi:trn>No records found</digi:trn>",
	         MSG_LOADING: "<digi:trn>Loading...</digi:trn>"
	        }; 

    var tableEl						= markup.getElementsByTagName("table")[0];
	var myDataSource 				= new YAHOO.util.DataSource( tableEl ); 
	myDataSource.responseType 		= YAHOO.util.DataSource.TYPE_HTMLTABLE; 
	myDataSource.responseSchema		= {fields: [{key: "resource_title"},
	                           		         {key: "type"},
			                           		 {key: "file_name"},
			                           		 {key: "date", parser: "date"},
			                           		 {key: "yearOfPublication"},
			                           		 {key: "size"},
			                           		 {key: "cm_doc_type"},
			                           		{key: "resource_index"},
			                           		{key: "resource_category"},
			                           		{key: "resource_organisations"},
			                           		 {key: "labels"},
			                           		{key: "actions"}
		                           		     	] 
	                           		     	};
    
	var dataTable 				= new YAHOO.widget.DataTable(markupName, this.columnHeaders, myDataSource, oConfigs);	
	
	// this is for document in activity form, to be able to select them, since the checbox is removed
	dataTable.subscribe("cellClickEvent", dataTable.onEventSelectRow);
	dataTable.subscribe("paginateEvent",hideCategories);
	dataTable.subscribe("rowMouseoverEvent", dataTable.onEventHighlightRow); 
	dataTable.subscribe("rowMouseoutEvent", dataTable.onEventUnhighlightRow);
	dataTable.subscribe('initEvent', sortColumn);
	//dataTable.setAttribute('width', "785px");
	
	 
	return dataTable;
};

function sortColumn() {
	//debugger;
	var columnSettingString = '<rs:value name="<%=SettingsConstants.SORT_COLUMN %>" />';
	// the setting has the value [ColumnName]_[ASC/DESC];
	
	var separatorPos = columnSettingString.lastIndexOf('_');
	var columnName = columnSettingString.substring(0, separatorPos);
	var sortOrderStr = columnSettingString.substring(separatorPos + 1);
	
	var sortOrder = -1;
	
	switch(sortOrderStr)
	{
		case 'ASC': sortOrder = YAHOO.widget.DataTable.CLASS_ASC; break;
		case 'DESC': sortOrder = YAHOO.widget.DataTable.CLASS_DESC; break;
		default: alert('invalid sort column setting: ' + columnSettingString); 
				sortOrder = YAHOO.widget.DataTable.CLASS_DESC;
				columnName = 'date';
				break;
	}

	this.sortColumn(this.getColumn(columnName), sortOrder);
}

function hideCategories(){
var categories	= YAHOO.amp.actionPanels;
	for (var categ in categories) {
		if ( categories[categ] ) {
			var panels	= categories[categ];
			hidePanels(panels);
		}
	}
}

/* Ajax function that creates a callback object after a delete command 
was issued in order to delete the respective row/document*/
function getCallbackForDelete (rows,tabType) {
	callbackForDelete = {
		success: function(o) {
			YAHOO.amp.panels[2].setBody(o.responseText);
			if (document.getElementById("successfullDiv") != null) {
				
				if (this.myRows != null) {
					//alert(YAHOO.amp.datatables.length + "|" + YAHOO.amp.num_of_tables);		
					for (var ii=0; ii<YAHOO.amp.datatables.length; ii++) {
						for (var i=0; i<this.myRows.length; i++) {
							YAHOO.amp.datatables[ii].deleteRow(this.myRows[i]);
						}
					}
					
					repositoryTabView.activatedLists[2]	= false;
					repositoryTabView.activatedLists[3]	= false;
					if(document.getElementById("shared_markup")!=null){
						sharedListObj.clearBody();	
					}
					
					if(document.getElementById("public_markup")!=null){
						publicListObj.clearBody();	
					}
					
				}
				updateFilterPanel('',tabType);
			}
			else 
				YAHOO.amp.panels[2].show();

		},
		failure: function(o) {
			//YAHOO.amp.panels[2].setBody("<div align='center'><font color='red'>${translation3}</font></div>");
			//alert("${translation3}");
		},
		myRows: rows
	}
	return callbackForDelete;
}
/* Function called after clicking delete */
function deleteRow(uuid,o) {
	var links			= document.getElementsByTagName('a');
	var possibleRows	= new Array();
	if (links != null) {
		for (var i=0; i<links.length; i++) {
			if ( links[i].id == ("aflag"+uuid) ) {
				var possibleRow	= links[i];
				while ( possibleRow != null ) {
					if (possibleRow.nodeName.toLowerCase()=="tr") {
							possibleRows.push(possibleRow);
							break;
					}
					possibleRow	= possibleRow.parentNode;
				}
			}
		}
	}
	if ( confirmDelete() ) {
		//var translation2				= "${translation2}";
		//YAHOO.amp.panels[2].setBody("<div align='center'>" + translation2 + "<br /> <img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0'/> </div>" );
		//YAHOO.amp.panels[2].setFooter("<div align='right'><button type='button' onClick='hidePanel(2)'>Close</button></div>");
		//showPanel(2);
		//YAHOO.amp.table.dataTable.deleteRow(possibleRow);
		YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/deleteForDocumentManager.do?uuid='+uuid, getCallbackForDelete(possibleRows,o));
		
	}
}
function confirmDelete() {
	var ret		= confirm('${translation1}');
	return ret;
}
</script> 


<style type="text/css">
.autofolding_table_header 
{
	white-space:normal !important;
	text-align: center;

}
.centered_th_label 
{
	padding:0px !important;
	margin: auto !important;
	width:auto !important;
}

</style>

<script type="text/javascript">

/*
  jQuery piece of code to add word wrapping to yui-generated table headers 
 */
 
 
$(window).load(function ()
		{
		    var i = setInterval(function ()
		    {
		        if ($('.yui-dt-label').length)
		        {
		            clearInterval(i);
		            $("[id^=yui-dt0-th]").addClass("autofolding_table_header");
		            $("autofolding_table_header, [id$=liner]").addClass("centered_th_label");
		        }
		    }, 100);
		});


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
					
				//for shared docs
				if(obj.sharedDocs!=null){
					parameters+= "&showSharedDocs=" + obj.sharedDocs;
				}
					
				if (obj.docListInSession != null) {
					parameters	+= "&docListInSession=" + obj.docListInSession;
				}

				if(obj.showActions !=null){
					parameters	+= "&showActions=" + obj.showActions;
				}
				
				if ( this.showOnlyLinks ) 
						parameters	+= "&showOnlyLinks=" + this.showOnlyLinks;
				if ( this.showOnlyDocs ) 
						parameters	+= "&showOnlyDocs=" + this.showOnlyDocs;

				//parameters += "&type=team2"
				//alert(parameters);
				this.bodyContainerElement.innerHTML="<div align='center'>${trans_wait}<br /><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' border='0' /> </div>";
				
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
	
	newDiv.innerHTML				= tableTemplateElement.innerHTML ;
	
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

function removeSelectedDocuments(removeFrom) {
	doSelectedDocuments('remove',removeFrom);
}

function doSelectedDocuments(action,removeFrom) {
	
	var trEls=$("#team_table").find("input.selDocs:checked");
	var result= new Array();
	for (i=0; i<trEls.length; i++) {		
		result[i]	= trEls[i].value;
	}
	selectedDocs= result;

	if(selectedDocs.length==0){
	selectedDocs			= getAllSelectedDocuments();
	}
	
	var updatedDocsAction	= '<%=org.digijava.module.contentrepository.helper.CrConstants.REQUEST_UPDATED_DOCUMENTS_IN_SESSION%>';
	if (selectedDocs.length == 0) {
		alert("${translation_no_doc_selected}");
		return;
	}
	
	var postString 	= createPostString(selectedDocs, action);
	var callback;
	if (action == 'set') {
		callback	= {
							success:function(o) {
											var urlstr = window.opener.location.href;
											urlstr = urlstr.replace('~addSector=true',"");
											urlstr = urlstr.replace('~delPledge=true',"");
											urlstr = urlstr.replace('~addPledge=true',"");
											urlstr = urlstr.replace('~remSectors=true',"");											
											if(urlstr.indexOf('?')!=-1 || urlstr.indexOf('~')!=-1){
												window.opener.location.replace(urlstr+"&"+updatedDocsAction+"=true");
											}else{
												window.opener.location.replace(urlstr+"?actionFlag=create&skipReset=false&"+updatedDocsAction+"=true");
											}
											//window.opener.location.replace(urlstr+"&"+updatedDocsAction+"=true"); 
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

	var url="/contentrepository/selectDocumentDM.do";
	if(removeFrom=='ORGANISATION_DOCUMENTS'){
		url+='?reloadOrgDocs=doNotReload';
	}
	YAHOO.util.Connect.asyncRequest("POST",url, callback, postString );
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
	if (vec != null) {
			result	= vec;
	}else{
		result	= new Array();
	}
	
	trEls	= datatable.getSelectedRows();
	
	
	var vector_length		= result.length;
	for (i=0; i<trEls.length; i++) {
		//alert(i);
		var divDocumentUUID	= getElementByNameFromList ( "aDocumentUUID", $('#'+trEls[i]).find("a") );
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
function getCallbackForOtherDocuments(containerElement, windowController, datatableDivId) {
	var num						= YAHOO.amp.num_of_tables - 1;
	var divId					= "other_markup" + num;
	if ( datatableDivId != null )
		divId		= datatableDivId;
	callbackForOtherDocuments	= {
		success: function(o) {
					//alert(o.responseText);
					containerElement.innerHTML	= "<div class='all_markup' align='left' id='"+divId+"'>" + o.responseText + "</div>";
					var datatable				= YAHOO.amp.table.enhanceMarkup(divId);
					datatable.subscribe("checkboxClickEvent", datatable.onEventSelectRow);
					YAHOO.amp.datatables.push( datatable );
					if ( windowController != null)
						windowController.datatable	= datatable;
					
					updateFilterPanel(divId,null);
					
					if (prevPage && prevPage > 0){ 
						//Re-establish the page that was being viewed before the ajax request
						datatable.configs.paginator.setPage(parseInt(prevPage), false);
					}
					 
					//createToolTips(containerElement);
				},
		failure: function(o) {
					containerElement.innerHTML	= "${translation_unableToRetriveDocuments}";
				}
	};
	
	return callbackForOtherDocuments;

}

function updateFilterPanel(divId,tabType){
	var type=tabType;
	if(divId==null || divId==''){
		if(tabType=='private'){
			divId='privateListObjDivId';
		}
		if(tabType=='team'){
			divId='teamListObjDivId';
		}
	}
	var filterAndLabelShow = $('div#'+divId).find('.yui-dt-empty');
	
	if(divId=='privateListObjDivId'){
		if(tabType==null){
			type = 'private';
		}
	}else if (divId=='teamListObjDivId'){
		if(tabType==null){
			type = 'team';
		}
	}
	
	var requestURL = "../../contentrepository/getResourcesInfo.do?type=" + type+ "&unique=" + new Date().getTime();
	$.get(requestURL, getResourcesInfoComplited, "xml");
}

function getResourcesInfoComplited(data, textStatus) {
	var exists = data.getElementsByTagName('resource-info')[0].attributes.getNamedItem("docsExist").value;
	var tabType= data.getElementsByTagName('resource-info')[0].attributes.getNamedItem("tabType").value;
	if(exists =='true'){
		if(tabType=='private'){
			$('#filterButtonId').show();
			$('#labelButtonId').show();
			
		}else if(tabType=='team'){
			$('#teamFilterButtonId').show();
			$('#teamLabelButtonId').show();		
		}
		
	}else{
		if(tabType=='private'){
			$('#filterButtonId').hide();
			$('#labelButtonId').hide();
			
		}else if(tabType=='team'){
			$('#teamFilterButtonId').hide();
			$('#teamLabelButtonId').hide();		
		}
	}
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
			userName			: '<bean:write name="member" property="email" />',
			showActions : 'false'
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
	var mItem1="${trans_teamMemberDocuments}";
	 menu.addItem(  new YAHOO.widget.MenuItem("${trans_teamMemberDocuments}", {submenu: membersMenu, id:mItem1})   );
	</logic:notEmpty>
	
	<logic:notEmpty name="meTeamMember">
		var scopeObj	= {
			teamId				: '<bean:write name="meTeamMember" property="teamId" />',
			showActions : 'false'
		};
		var onclickObj 	= {
			fn					: windowController.populateCallback,
			obj					: scopeObj,
			scope				: windowController
			
		};
		var mItem2="${trans_teamDocuments}";
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_teamDocuments}", {onclick: onclickObj, id:mItem2} )   );
	//shared docs tab
	var scopeObjForShared  = {
		sharedDocs : 'show',
		showActions : 'false'
	};

	var onclickObjForShared 	= {
			fn					: windowController.populateCallback,
			obj					: scopeObjForShared,
			scope				: windowController			
	};
	
	var mItem3="${trans_sharedDocuments}";
	menu.addItem(  new YAHOO.widget.MenuItem("${trans_sharedDocuments}", {onclick: onclickObjForShared, id:mItem3} )   );
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

$.getScript("/TEMPLATE/ampTemplate/script/common/FileTypeValidator.js");

/* Configures the form with id typeId */
function configPanel(panelNum, title, description, optionId, uuid, isAUrl,yearOfPublication, index, category) {
	document.getElementById('addDocumentErrorHolderDiv').innerHTML = '';
	if (optionId == null)
		optionId	= 0;

	if(yearOfPublication==null)
		yearOfPublication='-1';

	var myForm		= document.getElementById('typeId').form;
	myForm.docTitle.value		= title;
	myForm.docDescription.value	= description;
	myForm.docNotes.value		= '';
	myForm.uuid.value			= uuid;
	myForm.fileData.value		= null;
	myForm.webLink.value		= '';
	myForm.docType.disabled		= false;
	myForm.yearOfPublication.disabled		= false;
	if (myForm.docIndex)
    	myForm.docIndex.value = index ? index : ''; // field might be missing if feature is disabled
    	
	if (myForm.docCategory)
    	myForm.docCategory.value = category ? category : ''; // field might be missing if feature is disabled
	if (isAUrl == null) {
		isAUrl	= false;
    }
		
	selectResourceType(isAUrl);
	
	if (uuid != null && uuid.length > 0) {
		
		myForm.docTitle.readOnly					= true;
		myForm.docTitle.style.background			= "#eeeeee"; 
		myForm.docTitle.style.color					= "darkgray";
		
		myForm.docDescription.readOnly				= true;
		myForm.docDescription.style.backgroundColor	= "#eeeeee";
		myForm.docDescription.style.color			= "darkgray";

		myForm.docType.style.backgroundColor	= "#eeeeee";
		myForm.docType.style.color			= "darkgray";

		myForm.yearOfPublication.style.backgroundColor	= "#eeeeee";
		myForm.yearOfPublication.style.color			= "darkgray";

		if (typeof(myForm.docIndex) !== 'undefined')
		{
        	myForm.docIndex.readOnly                        = true;
        	myForm.docIndex.style.backgroundColor           = "#eeeeee";
        	myForm.docIndex.style.color                     = "darkgray";
		}
		if (typeof(myForm.docCategory) !== 'undefined')
		{
        	myForm.docCategory.readOnly                     = true;
        	myForm.docCategory.style.backgroundColor        = "#eeeeee";
        	myForm.docCategory.style.color                  = "darkgray";
		}

	
		setPanelHeader(0, "${translation_add_new_version}");
		
		var opts									= myForm.docType.options;
		for ( j=0; j<opts.length; j++ ) {
			if ( opts[j].value	== optionId ) {
				opts[j].selected	= true;
				break;
			}
		}
		myForm.docType.disabled						= true;

		//year of publication
		opts									= myForm.yearOfPublication.options;
		for ( j=0; j<opts.length; j++ ) {
			if ( opts[j].value	== yearOfPublication ) {
				opts[j].selected	= true;
				break;
			}
		}
		myForm.yearOfPublication.disabled			= true;
	}
	else {
		//myForm.webResource[1].disabled				= false;
		//myForm.webResource[0].disabled				= false;
		
		myForm.docTitle.readOnly					= false;
		myForm.docTitle.style.backgroundColor		= "";
		myForm.docTitle.style.color					= "";
		
		myForm.docDescription.readOnly				= false;
		myForm.docDescription.style.backgroundColor	= "";
		myForm.docDescription.style.color			= "";
		
		myForm.docType.selectedIndex				= 0;
		
		myForm.docType.style.backgroundColor	= "";
		myForm.docType.style.color			= "";

		myForm.yearOfPublication.style.backgroundColor	= "";
		myForm.yearOfPublication.style.color			= "";
		if (typeof(myForm.docIndex) !== 'undefined')
		{
        	myForm.docIndex.readOnly                = false;
        	myForm.docIndex.style.backgroundColor   = "";
        	myForm.docIndex.style.color             = "";
		}
		if (typeof(myForm.docCategory) !== 'undefined')
		{	
        	myForm.docCategory.readOnly              = false;
        	myForm.docCategory.style.backgroundColor = "";
        	myForm.docCategory.style.color           = "";
		}
		setPanelHeader(0, "${translation_add_new_content}");
	}
	
	setPanelFooter(0, "* ${translation_mandatory_fields}");
	
}

function selectResourceType(isUrl) {
	var myForm		= document.getElementById('typeId').form;
	var elFile	= document.getElementById('tr_path');
	var elUrl	= document.getElementById('tr_url');
	if(isUrl){
		elFile.style.display	= "none";
		elUrl.style.display		= "";
	}else{
		//alex					= '�� �� &�"\'(-�_��';
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
	// This code was commented. See https://jira.dgfoundation.org/browse/AMP-15171 for details
	// var regexp	= new RegExp("[a-zA-Z0-9_��������������������������������������%&' ()]+");
	var regexp	= new RegExp("[a-zA-Z0-9_��������������������������������������!@#$%^&' ()]+");

	var msg	= '';	
	if (document.forms['crDocumentManagerForm'].docTitle.value == '') {
		msg = msg + "${translation_validation_title}"+'<br>';
	}	
	else {
		var title	= document.forms['crDocumentManagerForm'].docTitle.value;
		var found	= regexp.exec(title);
		if ( found != title ) {
			msg = msg + "${translation_validation_title_chars}"+'<br>' ;
		}
		
	}

	var webUrlVisible=document.getElementById('tr_url');
	
	var fileData = document.forms['crDocumentManagerForm'].fileData;
	
	if(webUrlVisible.style.display=='none') { 
		if(fileData.value == '') { //adding document
			msg = msg + "${translation_validation_filedata}"+'<br>';
		} else if(!FileTypeValidator.isValid(fileData.value)) {
			msg = msg + FileTypeValidator.errorMessage +'<br>';
		}
	} else if(document.forms['crDocumentManagerForm'].webLink.value == '') { //adding url
		msg = msg + "${translation_validation_url}"+'<br>' ;
	}
    var fileData = document.forms['crDocumentManagerForm'].fileData;
    if (fileData && fileData.value != '' && fileData.files[0].size > ${uploadMaxFileSize}) {
        msg = msg + showFailedTooBigMsg("${translation_upload_failed_too_big}", ${maxFileSizeGS}) + '<br>';
    }

	document.getElementById('addDocumentErrorHolderDiv').innerHTML	= msg;
	if (msg.length == 0) {
			return true;
	}
	
	return false;
}

function showFailedTooBigMsg(msg, maxFileSizeGS){
	return msg.replace('{size}', maxFileSizeGS);
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

function shareDoc(uuid,shareWith,tabType){
	var callback	= new Object();
	callback.success	= function(o) {
		 					if ("${shareWithoutApprovalNeeded}" != "true") {
							var successAlert='<digi:trn jsFriendly="true">Your document will be shared with team members and will be available under Team resources tab after workspace manager approval</digi:trn>';                                                 
	 						alert(successAlert);
		 					}
	 	 					window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert('share failed');
						};
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/shareDoc.do?uuid="+uuid+"&shareWith="+shareWith+"&type="+tabType, callback);
}

function unshareDoc(uuid,tabType){
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert('unshare failed');
						};
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/unshareDoc.do?uuid="+uuid+"&type="+tabType, callback);
}

function approveVersion(versionId, baseNodeUUID){
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert('version approval failed');
						};
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/approveVersion.do?versionId="+versionId+"&baseNodeUUID="+baseNodeUUID, callback);
}

function rejectVersion(versionId, baseNodeUUID){
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							var failureAlert='<digi:trn jsFriendly="true">There seems to be a problem with the connection. Please try again later</digi:trn>';							
							alert(failureAlert);
							var myDiv=document.getElementById('loadingDiv');
							myDiv.style.display="none";
						};
	var myDiv=document.getElementById('loadingDiv');
	myDiv.style.display="block";
	//myDiv.innerHTML='<img src=\"/repository/contentrepository/view/images/ajax-loader-darkblue.gif\" height=\"20px\" />';
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/rejectVersion.do?versionId="+versionId+"&baseNodeUUID="+baseNodeUUID, callback);
}

function rejectDoc (uuid,actType,tabType){
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert('share failed');
						};
	//var panel		= YAHOO.amp.orgPanels[uuid]; 
	//panel.setBody("<div style='text-align: center;'><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' /></div>");
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/rejectDoc.do?actType="+actType+"&uuid="+uuid+"&type="+tabType, callback);
}

function setAttributeOnNode(action, uuid, doReload,tabType) {
	
	var callback	= new Object();
	callback.success	= function(o) {
							window.location.replace( window.location.href );
						};
	callback.failure	= function(o) {
							alert("${translation_make_public_failed}");
						};	
	YAHOO.util.Connect.asyncRequest("POST","/contentrepository/setAttributes.do?uuid="+uuid+"&action="+action+"&type="+tabType, callback);
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
		if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){ //Workaround to allow HTTP REFERER to be sent in IE (AMP-12638)
			var referLink = document.createElement('a');
			referLink.href='/contentrepository/downloadFile.do?uuid='+uuid;
			document.body.appendChild(referLink);
			referLink.click();
		} else {
			window.location='/contentrepository/downloadFile.do?uuid='+uuid;
		}
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

var organisationPanel;

function getCallbackForOrgs (panel) {
	var callbackObj	= {
			success: function (o) {
				panel.setBody( o.responseText );
				
			},
			failure: function () {
				panel.setBody("<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>");
			},
			cache: false
		}

	return callbackObj;
}

function showOrgsPanel(uuid) {
	if ( YAHOO.amp.orgPanels == null ) {
		YAHOO.amp.orgPanels	= new Object;
	}
	if (uuid == null) {
		uuid	= YAHOO.amp.orgPanels.lastUuid;
	}
	organisationPanel	= YAHOO.amp.orgPanels[uuid]; 
	if (organisationPanel == null) {
		organisationPanel 		= new YAHOO.widget.Panel("panelForOrganisations"+uuid, { width:"400px", visible:true, 
			draggable:true, close:true,
			effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
			modal:true } );
		organisationPanel.setHeader('<digi:trn jsFriendly="true">Participating Organizations</digi:trn>');
		organisationPanel.setBody("");
		//panel.setFooter("End of Panel #2");
		organisationPanel.render(document.body);
		YAHOO.amp.orgPanels[uuid]	= organisationPanel;
		organisationPanel.center();
	}
	organisationPanel.setBody("<div style='text-align: center;'><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' /></div>");
	organisationPanel.show();

	YAHOO.amp.orgPanels.lastUuid	= uuid;
	YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/docToOrg.do?orgsforuuid='+uuid, getCallbackForOrgs(organisationPanel) );
}

function deleteDocToOrgObj(uuid, ampOrgId) {
	var panel		= YAHOO.amp.orgPanels[uuid]; 
	panel.setBody("<div style='text-align: center;'><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' /></div>");
	var postString	= "removingUuid=" + uuid + "&removingOrgId=" + ampOrgId;
	YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/docToOrg.do?orgsforuuid='+uuid, getCallbackForOrgs(panel), postString );
}

var templatesPanel;

function getCallbackForTemplates (panel) {
	var callbackObj	= {
			success: function (o) {
				panel.setBody( o.responseText );
				
			},
			failure: function () {
				panel.setBody("<div align='center'><font color='red'>We are sorry but your request cannot be processed at this time</font></div>");
				var myDiv=document.getElementById('tempLoadingDiv');
				myDiv.style.display="none";
			}
	}	
	return callbackObj;

}

function addFromTemplate(ownType) {
	if ( YAHOO.amp.tempPanels == null ) {
		YAHOO.amp.tempPanels	= new Object;
	}
	templatesPanel	= YAHOO.amp.tempPanels[0]; 
	if (templatesPanel == null) {
		templatesPanel 		= new YAHOO.widget.Panel("panelForTemplates",{visible:true, 
			draggable:true, close:true, 
			modal:true,
			effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
			constraintoviewport: false } );
		templatesPanel.setHeader("<digi:trn>Create Document From Template</digi:trn>");
		templatesPanel.setBody("");
		templatesPanel.render(document.body);
		YAHOO.amp.tempPanels[0]	= templatesPanel;
		templatesPanel.center();
	}
	templatesPanel.setBody("<div style='text-align: center;'><img src='/repository/contentrepository/view/images/ajax-loader-darkblue.gif' /></div>");
	templatesPanel.show();

	//YAHOO.amp.orgPanels.lastUuid	= uuid;
	YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/docFromTemplate.do?actType=loadTemplates&docOwnerType='+ownType, getCallbackForTemplates(templatesPanel) );

}

function templateNameSelected(){
	var templateId=document.getElementById('selTempName').value;
	var myDiv=document.getElementById('tempLoadingDiv');
	myDiv.style.display="block";
	<digi:context name="loadTemp" property="context/module/moduleinstance/docFromTemplate.do?actType=getTemplate"/>;
    var url="${loadTemp}&templateId="+templateId; //+"&documentName="+docName
    YAHOOAmp.util.Connect.asyncRequest("POST", url, getCallbackForTemplates(templatesPanel));
}

function validateDocFromTemp(){
	var docName=document.getElementById('docName');
	if(docName==null || docName.value==''){
		alert('Please fill in document name');
		return false;
	}
}


function switchColors(element) {
	var tempColor					= element.style.color;
	element.style.color 			= element.style.backgroundColor;
	element.style.backgroundColor	= tempColor;
}

YAHOO.amp.actionPanels	= new Object();
function showActions(linkId, divId, category,timestamp){
	if ( !YAHOO.amp.actionPanels[category] )
		YAHOO.amp.actionPanels[category]	= new Object();
	if ( !YAHOO.amp.actionPanels[category].size ) {
		YAHOO.amp.actionPanels[category].size = 0;
	}
	if ( !YAHOO.amp.actionPanels.listenerAdded ) {
		YAHOO.util.Event.addListener(document,"click", hideActions, this, true );
		YAHOO.amp.actionPanels.listenerAdded	= true;
	}
	
	var panels	= YAHOO.amp.actionPanels[category];
	if ( !panels.timestamp || panels.timestamp != timestamp) {
		panels.timestamp	= timestamp;    
		for (var p in panels) {
			if ( panels[p] && panels[p].destroy ) {
					panels[p].destroy();
					panels[p]	= null;
			}
		}
		panels.size = 0;
	}
	var actionPanel		= panels[linkId];
    hidePanels(panels);
	if (actionPanel == null) {
		actionPanel		= new YAHOO.widget.Overlay(linkId+"actionoverlay", { context:[linkId,"tl","bl"],
			  visible:false,
			  effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
			  width:"150px",zIndex: 9 } );
		var actionDivEl	= document.getElementById(divId);
		actionDivEl.style.display	= "";
		actionPanel.setBody( actionDivEl );
		actionPanel.render(document.body);
		panels[linkId]	= actionPanel;
		panels.size++;
	}
	actionPanel.align("tl","bl");
	actionPanel.show();
	actionPanel.myIsVisible	= true;
}
function hideActions(e) {
	var clickedEl	= e.target||e.srcElement;
	if ( clickedEl.id.indexOf("Actions") >= 0 ) 
		return;
	 hideCategories();
	
}
function hidePanels(panels){
     for (var p in panels) {
			if ( panels[p] && panels[p].hide ) {
					panels[p].hide();
			}
	}
}



function getTemplateLabelsCb(formName, infoDivId) {
	filterWrapperTrnObj		= {
			labels: "<digi:trn>Labels</digi:trn>",
			filters: "<digi:trn>Filters</digi:trn>",
			keywords: "<digi:trn>Keywords</digi:trn>",
			mode: "<digi:trn>Mode</digi:trn>",
			apply: "<digi:trn>Apply</digi:trn>",
			close: "<digi:trn>Close</digi:trn>"
	};
	
	if ( !YAHOO.amp.templateFilterWrapper ) {
		YAHOO.amp.templateFilterWrapper	= new FilterWrapper(filterWrapperTrnObj);
	}
	var cb	= {
			click: function(e, label) {
				var infoDivEl			= document.getElementById(this.infoDivId);
				YAHOO.amp.templateFilterWrapper.filterLabels.push(label);
				infoDivEl.innerHTML		= YAHOO.amp.templateFilterWrapper.labelsToHTML();
			},
			applyClick: function(e, labelArray){
				alert(filterWrapperTrnObj);
				YAHOO.amp.templateFilterWrapper	= new FilterWrapper(filterWrapperTrnObj);
				for (var i=0; i<labelArray.length; i++) {
					YAHOO.amp.templateFilterWrapper.filterLabels.push(labelArray[i]);
				}
				infoDivEl.innerHTML		= YAHOO.amp.templateFilterWrapper.labelsToHTML();
			},
			infoDivId: infoDivId,
			formName: formName
	}
}

/* Number of possible panels on this page */
YAHOO.amp.panelCounter	= 3;

YAHOO.util.Event.addListener(window, "load", initPanel) ;

function showlegend(divIdSuffix) {
	var contentId = document.getElementById("show_legend_pop_box_"+divIdSuffix);
	contentId.style.display == "block" ? contentId.style.display = "none" : contentId.style.display = "block"; 
}
</script>

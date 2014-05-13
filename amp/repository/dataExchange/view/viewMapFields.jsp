<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="mapFieldsForm" id="mff"/>
<!-- Individual YUI CSS files -->
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/assets/skins/sam/autocomplete.css">
<!-- Individual YUI JS files --> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/animation/animation-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/datasource/datasource-min.js"></script> 
<script type="text/javascript" src="/TEMPLATE/ampTemplate/js_2/yui/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="<digi:file src='module/aim/scripts/filters/searchManager.js'/>" ></script>
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<style>
<!--
.ui-autocomplete {
	font-size:12px;
	border: 1px solid silver;
	max-height: 150px;
	overflow-y: scroll;
	background: white;
}

-->
</style>
<!-- for browse button -->
<style type="text/css">
<!--
div.charcounter-progress-container {
	width:100%; 
	height:3px;
	max-height:3px;
	border: 1px solid gray; 
	filter:alpha(opacity=20); 
	opacity:0.2;
}

div.charcounter-progress-bar {
	height:3px; 
	max-height:3px;
	font-size:3px;
	background-color:#5E8AD1;
}


-->
</style>

<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}

input.file {
	width: 300px;
	margin: 0;
}

input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}

div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}


.autocompleteDropdownText {
	border: 1px solid silver;
	font-size: 11px !important;
	font-family: Arial,Helvetica,sans-serif;
	cursor:pointer;
	
}

.autocompleteDropdownText:hover {
	background-color:#feff90;
}

div.optionsContainer {
	width:500px;
	height:200px;
	position:absolute;
	background-color:white;
	border:1px solid black;
	display:none;
}

table.optionsContainerTable {
	width:498px;
	height:190px;
	position:absolute;
}

div.flowContainer {
	overflow-y:auto;
	overflow-x:hidden;
	width:495px;
	height:180px;
	
}

td.infoWnd {
	border: 1px solid black;	
}

td.optionItem {
	padding:2px;
	border: 1px solid transparent;
	cursor:pointer;
}

td.optionItem:hover {
	border: 1px solid #bfc8d6;
	background-color: #dfe5ee;
}

td.optionItem:active {
	border: 1px solid #0e2342;
	background-color: #2b4b7a;
	color: white;
}



-->
</style>

<script type="text/javascript">
function saveRecord(id) {
	var loadingImgDiv = document.getElementById("loadingImg");
	loadingImgDiv.style.display="block";
		 var el = document.getElementById("ampValues["+id+"]");
		 //var txt = el.options[el.selectedIndex].innerHTML;
		 var txt = $(el).parent().find(".autocompleteDropdownText").val();
		 <digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
		 url = "<%= saveRecord %>?actionType=saveRecord&id="+id+"&ampId="+el.value+"&mappedValue="+txt;
		 mapFieldsForm.action =url;
		 //alert(url);
		 mapFieldsForm.submit();
		 return true;
	<%-- //alert('NOP');
	return;
	//debugger;
	var loadingImgDiv = document.getElementById("loadingImg");
	loadingImgDiv.style.display="block";
	
	var el = document.getElementById("map_fields_value_" + id);
	var txt = el.innerHTML;
	
	/*var el = document.getElementById("ampValues["+id+"]");
	var txt = el.options[el.selectedIndex].innerHTML;*/
	<digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
	url = "<%= saveRecord %>?actionType=saveRecord&id="+id+"&ampId=" + txt + "&mappedValue="+txt;
	mapFieldsForm.action =url;
	//alert(url);
	mapFieldsForm.submit();
	return true; --%>
}

function saveAll() {
	return;
	//debugger;
	 var checks = document.getElementsByName("selectedFields");
	 var isChecked = false;

	 var params = "&actionType=saveAllRecords";
	 for(i=0;i<checks.length;i++){
		 if(checks[i].checked) {
			 isChecked=true;
			 var opts = document.getElementById("ampValues["+checks[i].value+"]");
			 params+="&selectedAmpIds="+opts.value;
			 params+="&selectedAmpValues="+opts.options[opts.selectedIndex].text;
		 }
	 }
	 
	 if(isChecked != true) {
		 alert("Please check at least one record");
		 return true;
     }
	 
	 var loadingImgDiv = document.getElementById("loadingImg");
	 loadingImgDiv.style.display="block";
		
	 <digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
	 url = "<%= saveRecord %>";
	 var postString = params;
	 YAHOO.util.Connect.setForm( document.getElementById("logForm") );
	 YAHOO.util.Connect.asyncRequest('POST', url, { 
		 	            success: function() { 
		 	            	window.location.replace(url);
		 	            }, 
		 	            failure: function() { 
		 	            } 
		 	        },postString); 

     
	 return true;
}


function get_action_type(selectedFilterBy)
{
	var DUMMY_ACTION_TYPE = "###dummy###";
	var actionTypeString = DUMMY_ACTION_TYPE;
	if (selectedFilterBy == "Activity")
		actionTypeString = "searchActivitiesName";
	else if (selectedFilterBy == "Activity Status")
		actionTypeString = "searchActivitiesStatus";
	else if (selectedFilterBy == "Organization")
		actionTypeString = "searchActivitiesOrganization";
	else if (selectedFilterBy == "Organization Type")
		actionTypeString = "searchActivitiesOrganizationType";
	else if (selectedFilterBy == "Sector")
		actionTypeString = "searchSectors";
	else if (selectedFilterBy == "Sector Scheme")
		actionTypeString = "searchSectorSchemes";
	else
		alert("nothing");
	return actionTypeString;
}


function checksAll() {
	 var check = document.getElementById("checkAll");
	 $('[id^="Check_Amp"]').attr('checked', false);
	 $('[id^="Check_Amp"]').attr('checked', check.checked);
	 return true;
	 //var records = document.getElementById("filterAmpClass");
	 //var v = records.options[records.selectedIndex].text;
	 //var v = records.value;
	 //if("all" == v)
	 //else $('[id^="Check_'+v+'_"]').attr('checked', check.checked);
}

function showFilter() {
	var records = document.getElementById("filterAmpClass");
	var selectedAmpClass = records.value;
	page(1,selectedAmpClass);	
}

function page (page, ampSelectedClass){
	var form = document.getElementById('logForm');
//	var records = document.getElementById("filterAmpClass");
//	var selectedAmpClass = records.options[records.selectedIndex].text;
	
	form.action = "/dataExchange/mapFields.do?page="+page+"&selectedAmpClass="+ampSelectedClass;
	form.target="_self";
	form.submit();	
}

function sortMe(val) {
	
	var form = document.getElementById('logForm');
	<digi:context name="sel" property="/dataExchange/mapFields.do" />
		 url = "<%= sel %>" ;
		var sval = document.mapFieldsForm.sort.value;
		var soval = document.mapFieldsForm.sortOrder.value;

		if ( val == sval ) {
			if (soval == "asc")
				document.mapFieldsForm.sortOrder.value = "desc";
			else if (soval == "desc")
				document.mapFieldsForm.sortOrder.value = "asc";
		}
		else
			document.mapFieldsForm.sortOrder.value = "asc";

		form.sort.value = val;
		form.action = url;
		//form.target="_self";
		form.submit(); 
		
}

document.getElementsByTagName('body')[0].className='yui-skin-sam';

</script>

<style  type="text/css">
<!--

.contentbox_border{
        border: 1px solid black;
	border-width: 1px 1px 1px 1px; 
	background-color: #ffffff;
}

#statesAutoComplete ul,
{
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#contactsAutocomplete ul {
	list-style: square;
	padding-right: 0px;
	padding-bottom: 2px;
}

#statesAutoComplete div{
	padding: 0px;
	margin: 0px; 
}

#contactsAutocomplete div {
	padding: 0px;
	margin: 0px; 
}

#statesAutoComplete,
#contactsAutocomplete {
    width:15em; /* set width here */
    padding-bottom:2em;
}
#statesAutoComplete,contactsAutocomplete {
    z-index:3; /* z-index needed on top instance for ie & sf absolute inside relative issue */
    font-size: 12px;
}

#statesInput,
#contactInput {
    font-size: 12px;
}
.charcounter {
    display: block;
    font-size: 11px;
}

#statesAutoComplete {
    width:320px; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
#myImage {
    position:absolute; left:320px; margin-left:1em; /* place the button next to the input */
}

span.extContactDropdownEmail {
	color:grey;
}

-->
</style>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<!-- MAIN CONTENT PART START -->
<digi:form action="/mapFields.do" styleId="logForm">
<html:hidden property="sort" />
<html:hidden property="sortOrder" />

		<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
			<!-- BREADCRUMP START -->
			<tr>
				<td height="33">
					<div class="breadcrump_1"> 
						<span class="sec_name"><digi:trn>Data Import Manager</digi:trn></span>
						<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span><a href="/dataExchange/manageSource.do" class="l_sm"><digi:trn>Import Manager</digi:trn></a>
						<span class="breadcrump_sep"><b>»</b></span>
						<span class="bread_sel"><digi:trn>Mapping Tool</digi:trn></span>
					</div>
					<br>
					<div style="text-align: center; display: none;" id="loadingImg">
						<img src="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/loading.gif" border="0" height="17px"/>&nbsp;&nbsp; 
		        		<b class="ins_title"><digi:trn>Loading, please wait ...</digi:trn></b>
					</div>
				</td>
			</tr>
		</table>
		<!-- BREADCRUMP END -->
		<!-- MAIN CONTENT PART START -->
  		<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
  		<tr><td>
  			<div id="optionsDiv" class="optionsContainer">
  				<table class="optionsContainerTable">
  					<tr>
  						<td>
  							<div id="content" class="flowContainer">
  							</div>
  						</td>
  					</tr><tr>
  						<td id="info" height="12" class="infoWnd">Total
  						</td>
  					</tr>
  				</table>
  			</div>
  		</td></tr>
			<tr>
			    <td class="main_side_1">
				    <table class="inside" width="980px" border=0 cellpadding="0" cellspacing="0" style="margin:10px;" id="tableRecords">
				    	<tr>
		    				<td align=right colspan="8" class="inside">
		    					<a href="/dataExchange/mapFields.do" >
		    					<b><digi:trn>Mapping Tool</digi:trn></b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		    					<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true" class="t_sm"><b>[+] <digi:trn>Create New Source</digi:trn></b></a>
		   					 </td>
	  					</tr>
						<tr>
						<td colspan="8" align="center" background="images/ins_header.gif" class="inside"><b class="ins_header"><digi:trn>Filter by</digi:trn>:
							<html:select property="selectedAmpClass" styleClass="dropdwn_sm" onchange="showFilter()" styleId="filterAmpClass">
        						<logic:iterate id="cls" name="mapFieldsForm" property="ampClasses">
        							<%-- <bean:define id="itemAmpClass"><%= cls.toString().replaceAll(" ","") %></bean:define>--%>
        							<html:option value="${cls}" >${cls}</html:option>
								</logic:iterate>
        					</html:select>
						</b></td>
						</tr>
						<tr>
						    <td width="20" background="images/ins_bg.gif" class="inside"><b class="ins_title"><input id="checkAll" type="checkbox" onclick="checksAll()" /></b></td>
						    <td width="400" background="images/ins_bg.gif" class="inside"><b class="ins_title">
						    					<a  style="color:black" href="javascript:sortMe('iati items')" title="Click here to sort by Activity Details">
														<b><digi:trn>Iati Items</digi:trn></b>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='iati items' && mapFieldsForm.sortOrder=='asc'}">
														<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
													</c:if>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='iati items' && mapFieldsForm.sortOrder=='desc'}">
														<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
													</c:if>
												</a></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">
						    	<a  style="color:black" href="javascript:sortMe('iati values')" title="Click here to sort by Activity Details">
														<b><digi:trn>IATI values</digi:trn></b>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='iati values' && mapFieldsForm.sortOrder=='asc'}">
														<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
													</c:if>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='iati values' && mapFieldsForm.sortOrder=='desc'}">
														<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
													</c:if>
												</a></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><a  style="color:black" href="javascript:sortMe('current value')" title="Click here to sort by Activity Details">
														<b><digi:trn>Current value</digi:trn></b>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='current value' && mapFieldsForm.sortOrder=='asc'}">
														<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
													</c:if>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='current value' && mapFieldsForm.sortOrder=='desc'}">
														<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
													</c:if>
												</a></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title">
						    <a  style="color:black" href="javascript:sortMe('status')" title="Click here to sort by Activity Details">
														<b><digi:trn>Status</digi:trn></b>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='status' && mapFieldsForm.sortOrder=='asc'}">
														<img id="activityColumnImg" src="/repository/aim/images/up.gif" />
													</c:if>
													<c:if test="${empty mapFieldsForm.sort || mapFieldsForm.sort=='status' && mapFieldsForm.sortOrder=='desc'}">
														<img id="activityColumnImg" src="/repository/aim/images/down.gif" />
													</c:if>
												</a></b></td>
						    <td background="images/ins_bg.gif" class="inside"><b class="ins_title"><digi:trn>Values from AMP</digi:trn></b></td>
						    <%--
								<td width="50" background="images/ins_bg.gif" class="inside" align="center"><b class="ins_title"><digi:trn>Actions</digi:trn></b></td>
							--%>
						</tr>
						
						<logic:notEmpty name="mapFieldsForm" property="mappedFields">
							<logic:iterate id="field" name="mapFieldsForm" property="mappedFields">
								<tr id="Amp${field.ampField.shortAmpClass}_${field.ampField.id}">
								    <td bgcolor="#FFFFFF" class="inside">
								    	<html:checkbox name="mapFieldsForm"  property="selectedFields"  value="${field.ampField.id}" 
								    		styleId="Check_Amp${field.ampField.shortAmpClass}_${field.ampField.id}"/>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside"><div class="t_sm">${field.ampField.iatiItems}</div></td>
								    <td bgcolor="#FFFFFF" class="inside"><div class="t_sm">${field.ampField.iatiValues}</div></td>
								    <td bgcolor="#FFFFFF" class="inside">
								    	<div class="t_sm" id="map_fields_value_${field.ampField.id}">${field.ampField.ampValues}</div>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside">
								    	<div class="t_sm" align="center">
								    	<c:if test="${field.ampField.ampValues== null }">
							    			<img src="/TEMPLATE/ampTemplate/img_2/not_ok_ico.gif" />
								    	</c:if>
								    	<c:if test="${field.ampField.ampValues!= null }">
								    		<c:if test="${field.ampField.ampValues=='Add new' }">
								    			<bean:define id="itemDescr">
								    				<c:if test="${field.ampField.iatiPath=='Activity' }">
								    					<digi:trn>Activity will be added as new activity when import will be performed</digi:trn>
								    				</c:if>
								    				<c:if test="${field.ampField.iatiPath=='Sector' || field.ampField.iatiPath=='Vocabulary Code' || field.ampField.iatiPath=='Location' }">
								    					<digi:trn>Can not be automatically added. Please go to admin and manually add it</digi:trn>
								    				</c:if>
								    				&nbsp;
								    			</bean:define>
							    				<img src="/TEMPLATE/ampTemplate/img_2/ico_info.gif" title="${itemDescr}" />
							    			</c:if>
							    			<c:if test="${field.ampField.ampValues!='Add new' }">
								    			<img src="/TEMPLATE/ampTemplate/img_2/ok_ico.gif" />
							    			</c:if>
								    	</c:if>
								    	</div>
								    </td>
								    <td bgcolor="#FFFFFF" class="inside ampvalues" style="width:40%;">
	
	<%--
								    	<html:select  name="mapFieldsForm"  property="allSelectedAmpValues" styleClass="dropdwn_sm" styleId="ampValues[${field.ampField.id}]">
								  			<html:option value="-1" ><digi:trn>Add new</digi:trn></html:option>
								  			
  
        									<logic:iterate id="cls" name="mapFieldsForm" property="allEntitiesSorted" >
  
														<html:option value="${cls.key}">
															${cls.value}
														</html:option>
													</logic:iterate>
        								</html:select>
      --%>  								
        								
        								<div style="width: 500px;">
        									<html:hidden  name="mapFieldsForm"  property="allSelectedAmpValues" styleId="ampValues[${field.ampField.id}]"/>
        									<input class="autocompleteDropdownText" type="text" style="width: 500px;">
        								</div>
								    	
								  	</td>
								  	
								    	<td bgcolor="#FFFFFF" class="inside" align="center">
								    		<input type="button" value="<digi:trn>Save</digi:trn>" class="buttonx_sm" onclick="saveRecord(${field.ampField.id})" />
								    	</td>
								   
								</tr>
							</logic:iterate>
						</logic:notEmpty>

					</table>
					<div class="paging" style="font-size:11px;margin:10px;">
						<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
						<c:forEach var="page" begin="1" end="${mapFieldsForm.lastPage}">
							<bean:define id="currPage" name="mapFieldsForm" property="currentPage" />
							<c:if test="${mapFieldsForm.currentPage == page}">
								<b class="paging_sel">${page}</b>
							</c:if>
							<c:if test="${mapFieldsForm.currentPage != page}">
								<c:set var="translation">
									<digi:trn>Click here to goto Next Page</digi:trn>
								</c:set>
								<a href="javascript:page(${page},'${mapFieldsForm.selectedAmpClass}')" title="${translation}" class="l_sm">${page}</a>
							</c:if>
							|&nbsp;
						</c:forEach>
					</div>
				</td>
			</tr>
		</table>
</digi:form>

<br /><br />
<!-- MAIN CONTENT PART END -->

<script language="javascript">
	$("input.autocompleteDropdownText").click(function(e) {
		var originatorObj = $(e.target);
		curEvtObj = originatorObj;
		var originatorObjAbsPos = originatorObj.position();
		var optionsContainerDiv = $("#optionsDiv");
		optionsContainerDiv.css("display", "block");
		optionsContainerDiv.css("left", originatorObjAbsPos.left + "px");
		optionsContainerDiv.css("top", originatorObjAbsPos.top+20 + "px");
		
		var hiddenInput = originatorObj.parent().find("input[type='hidden'][name='allSelectedAmpValues']");
		if (hiddenInput.val()=="" || hiddenInput.val()==-1) {
			originatorObj.val("").css("color", "black");
		}
		
		
		getAutosuggestOptionValues(originatorObj.val(), 100);
	});
	
	var timeoutObj = null;
	var curEvtObj = null;
	$("input.autocompleteDropdownText").keyup(function(e) {
		
		if (timeoutObj != null) {
			window.clearTimeout (timeoutObj);
		}
		timeoutObj = window.setTimeout("getAutosuggestOptionValues(curEvtObj.val(), 100)", 300);
		
	});
	
	var getAutosuggestOptionValues = function (queryStr, maxNumber) {
		var url = "../../dataExchange/mapFields.do?action=getOptionsAjaxAction";
		$.ajax({
		  type: 'POST',
		  url: url,
		  data:{searchStr:queryStr , maxResultCount:maxNumber},
		  success: autocompleteRequestSuccess,
		  dataType: "json"
		});
	}
	
	var autocompleteRequestSuccess = function (data, textStatus, jqXHR) {
		var optionsContainer = $("#optionsDiv #content");
		var infoWnd = $("#optionsDiv .infoWnd");
		
		var optionsMarkup = [];
		optionsMarkup.push("<table width='100%'>");
		$(data.objects).each(function(index, element) {
    	optionsMarkup.push("<tr>");
    	optionsMarkup.push("<td nowrap class='optionItem'>");
    	if (element.val != null) {
    		optionsMarkup.push(element.val);
    	} else {
    		optionsMarkup.push("EMPTY VALUE");
    	}
    	optionsMarkup.push("<input class='objId' type='hidden' value='");
    	optionsMarkup.push(element.id);
    	optionsMarkup.push("'>");
    	optionsMarkup.push("<input class='objCaption' type='hidden' value='");
    	optionsMarkup.push(element.val);
    	optionsMarkup.push("'>");
    	optionsMarkup.push("</td>");
    	optionsMarkup.push("</tr>");
    	
		});
		optionsMarkup.push("</table>");
		optionsContainer.html(optionsMarkup.join(''));
		infoWnd.html("Total object count/Showing: " +  data.totalCount + "/" +  data.objects.length);
		
		$("td.optionItem").click(function (e) {
			var originatorObj = $(e.target);
			
			
			
			$("#optionsDiv").css("display", "none");
			

			var newVal = originatorObj.find(".objCaption").val();
			if (newVal.trim() == "") newVal = "EMPTY VALUE";
			curEvtObj.val(newVal);
			curEvtObj.parent().find("input[type='hidden'][name='allSelectedAmpValues']").val(originatorObj.find(".objId").val())
			checkForDefaultValues();
		});
	}
	
	var checkForDefaultValues = function() {
		$("input[type='hidden'][name='allSelectedAmpValues']").each(function(index, element) {
			var elementObj = $(element);
			if (elementObj.val() == null || elementObj.val()=="" || elementObj.val()==-1) {
				elementObj.parent().find(".autocompleteDropdownText").css("color", "#707070").val("Add new");
			}
		});
	}
	
	checkForDefaultValues();
	
	
	
	
		
</script>
</body>
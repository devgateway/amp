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
-->
</style>

<script type="text/javascript">
function saveRecord(id) {
	alert('NOP');
	return;
	debugger;
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
	return true;
}

function saveAll() {
	return;
	debugger;
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
						    <td width="50" background="images/ins_bg.gif" class="inside" align="center"><b class="ins_title"><digi:trn>Actions</digi:trn></b></td>
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
								    	<%-- <html:select  name="mapFieldsForm"  property="allSelectedAmpValues" styleClass="dropdwn_sm" styleId="ampValues[${field.ampField.id}]">
								  			<html:option value="-1" ><digi:trn>Add new</digi:trn></html:option>
        									<logic:iterate id="cls" name="field" property="sortedLabels" >
												<html:option value="${cls.key}">
												${cls.value}
												</html:option>
											</logic:iterate>
        								</html:select> --%>
								    	<div>
								        <div style="width:100%;" class="">
								            <html:text name="mapFieldsForm"  property="allSelectedAmpValues" style="width:100%;"  styleId="statesinput[${field.ampField.id}]"></html:text>
								            <div id="statesautocomplete[${field.ampField.id}]"></div>
								        </div>
								      </div>
								      <script type="text/javascript">
										var relatedActDataSource = new YAHOO.widget.DS_XHR("/message/messageActions.do", ["\n", ";"]);
										
										var selectedFilterBy = document.getElementById("filterAmpClass").value;
										var actionTypeString = get_action_type(selectedFilterBy);
										//debugger;
										
										
										relatedActDataSource.scriptQueryAppend = "actionType=" + actionTypeString;;
										relatedActDataSource.responseType = YAHOO.widget.DS_XHR.TYPE_FLAT;
										relatedActDataSource.queryMatchContains = true;
										relatedActDataSource.scriptQueryParam  = "srchStr";
										var relatedActAutoComp = new YAHOO.widget.AutoComplete("statesinput[${field.ampField.id}]","statesautocomplete[${field.ampField.id}]", relatedActDataSource);
										relatedActAutoComp.forceSelection = true;
										relatedActAutoComp.queryDelay = 0.5;
										$("#statesinput").css("position", "static");

										//define your itemSelect handler function:
										var itemSelectHandler = function(sType, aArgs) {
											//debugger;
											YAHOO.log(sType); // this is a string representing the event;
														      // e.g., "itemSelectEvent"
											var oMyAcInstance = aArgs[0]; // your AutoComplete instance
											var elListItem = aArgs[1]; // the <li> element selected in the suggestion
											   					       // container
											var oData = String(aArgs[2]); // object literal of data for the result
											var ampId = oData.substring (oData.lastIndexOf('(') + 1, oData.lastIndexOf(')')).trim();
											var value = oData.substring (0, oData.lastIndexOf('(')).trim();
											var loadingImgDiv = document.getElementById("loadingImg");
											loadingImgDiv.style.display="block";
												 var el = document.getElementById("ampValues["+${field.ampField.id}+"]");
												 <digi:context name="saveRecord" property="context/module/moduleinstance/mapFields.do"/>
												 url = "<%= saveRecord %>?actionType=saveRecord&id="+${field.ampField.id}+"&ampId="+ampId+"&mappedValue="+value;
												 mapFieldsForm.action =url;
												 mapFieldsForm.submit();
												 return true;
										};

										//subscribe your handler to the event, assuming
										//you have an AutoComplete instance myAC:
										relatedActAutoComp.itemSelectEvent.subscribe(itemSelectHandler);
									 </script>
								  	</td>
								    <td bgcolor="#FFFFFF" class="inside" align="center">
								    		<input type="button" value="<digi:trn>Save</digi:trn>" class="buttonx_sm" onclick="saveRecord(${field.ampField.id})" />
								    </td>
								</tr>
							</logic:iterate>
						</logic:notEmpty>
						<tr>
							  <td colspan="6" bgcolor="#FFFFFF" class="inside">&nbsp;</td>
							  <td bgcolor="#FFFFFF" class="inside" align="center"><input type="button" value="<digi:trn>Save All</digi:trn>" class="buttonx_sm" onclick="saveAll()"/></td>
						</tr>
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
</body>
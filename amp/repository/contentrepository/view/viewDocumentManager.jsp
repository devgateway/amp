<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.categorymanager.util.CategoryConstants"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>


<%@page import="org.digijava.module.contentrepository.util.DocumentManagerRights"%>
<%@include file="documentManagerDivHelper.jsp" %>

<DIV id="TipLayer"
	style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<digi:errors />


<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />

<bean:define id="isTeamLeader" name="myForm" property="teamLeader" />
<bean:define id="meTeamMember" name="myForm" property="teamMember" />

<bean:define id="tMembers" name="myForm" property="teamMembers" />
<bean:define id="selectedType" name="myForm" property="type" />

<%@include file="documentManagerJsHelper.jsp" %>
<digi:ref href="css_2/desktop_yui_tabs.css" type="text/css" rel="stylesheet" /> 
<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

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
	padding-left: 5px;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>
<style>
.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important;
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}
</style>

<style>
.yui-skin-sam a.yui-pg-page{
margin-left: 2px;
padding-right: 7px;
font-size: 11px;
border-right: 1px solid rgb(208, 208, 208);
}

.yui-skin-sam .yui-pg-pages{
border: 0px;
padding-left: 0px;
}
.yui-pg-current-page {
    background-color: #FFFFFF;
    color: rgb(208, 208, 208);
    padding: 0px;
}
.current-page {
    background-color: #FF6000;
    color: #FFFFFF;
    padding: 2px;
    font-weight: bold;
} 	

div#addDocumentDiv td {
	text-align:left;
}



.yui-skin-sam span.yui-pg-first,
.yui-skin-sam span.yui-pg-previous,
.yui-skin-sam span.yui-pg-next,
.yui-skin-sam span.yui-pg-last {
display: none;
}

.yui-skin-sam a.yui-pg-first {
margin-left: 2px;
padding-right: 7px;
border-right: 1px solid rgb(208, 208, 208);
}

</style>
<script  type="text/javascript" src="<digi:file src="module/aim/scripts/fileUpload.js"/>"></script>
<script language="javascript">
 var uploadDoc="<digi:trn jsFriendly='true'>Upload Doc</digi:trn>";
 var addWebLink="<digi:trn jsFriendly='true'>Add Web Link</digi:trn>";
 var createFromTemplate="<digi:trn jsFriendly='true'>Create From Template</digi:trn>";
 var createfromtemplateenable = false;
 var addWebLinkenable = false;
 var uploadDocenalbe=false;
 
 function setHoveredTable(tableId, hasHeaders) {
	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
}

</script>
<script type="text/javascript">
	YAHOO.namespace("YAHOO.amp.table");
	var privateListObj	= null;
	var teamListObj	= null;
	var breadcrumbName='Resources';
	var breadCrumbObj = null;
	var privateSeachObj = null;
	var fPanel = null;
	var teamFPanel = null;
	var sharedFPanel = null;
	
	function closePopups(){

		//Closing popups
		if(menuPanelForUser){
			menuPanelForUser.hide();
		}
		if(menuPanelForTeam){
			menuPanelForTeam.hide();
		}

		if(privateListObj && privateListObj.getFilterPanel(null, 'privateListObjDivId', false)){
			privateListObj.getFilterPanel().hide();
		}
		if(teamListObj && teamListObj.getFilterPanel(null, 'teamListObjDivId', false)){
			teamListObj.getFilterPanel().hide();
		}
		if(sharedListObj && sharedListObj.getFilterPanel(null, 'sharedListObjDivId', false)){
			sharedListObj.getFilterPanel().hide();
		}

		if(fPanel){
			fPanel.hide();
		}
		if(teamFPanel){
			teamFPanel.hide();
		}
		if(sharedFPanel){
			sharedFPanel.hide();
		}
	}
	
	function loadTab() {
		var visibleTabs =0;
		var myDoc =  document.getElementById("my_res");
		var teamDoc =  document.getElementById("team_res");
		var sharedDoc =  document.getElementById("shared_res");
		var publicDoc =  document.getElementById("public_res");
		
		//closePopups();
		
		if (myDoc !=null) {
			visibleTabs ++;
		}
		if (teamDoc !=null) {
			visibleTabs ++;
		}
		if (sharedDoc !=null) {
			visibleTabs ++;
		}
		if (publicDoc !=null) {
			visibleTabs ++;
		}
		for (var i=0; i<visibleTabs; i++) {			
				var tab		= repositoryTabView.getTab(i);
				if ( tab.get("active") && !repositoryTabView.activatedLists[i] ) {				
					repositoryTabView.dynLists[i].sendRequest();
					repositoryTabView.activatedLists[i]		= true;
				}
			
			
		}
		
		
		 //var sharedDocsFilterBtn = $('div#sharedListObjDivId').find('.yui-dt-empty');
		 //alert(sharedDocsFilterBtn.html());
	}
	
	function afterPageLoad(e) {
		trnObj		= {
				labels: "<digi:trn>Labels</digi:trn>",
				filters: "<digi:trn>Filters</digi:trn>",
				keywords: "<digi:trn>keywords</digi:trn>",
				apply: "<digi:trn>Apply</digi:trn>",
			//	reset: "<digi:trn>Reset</digi:trn>",
				close: "<digi:trn>Close</digi:trn>",
				none: "<digi:trn>none</digi:trn>",
				keywordMode: "<digi:trn>Keyword Mode</digi:trn>"
		};
		//set breadcrumb text
		var breadCrumbObj=$('div.breadcrump_cont').text(breadcrumbName);
		
		//search
		
		
		privateListObj			= new DynamicList(document.getElementById("my_markup"), "privateListObj","privateFilterDivId",
				${meTeamMember.teamId}, '${meTeamMember.email}', trnObj);
		privateListObj.filterInfoDivId	= "privateFilterInfo";
		if(privateListObj.setKeywordTextboxInformation)
		   privateListObj.setKeywordTextboxInformation("privateSearchStr","privateSearchButtonId");
		//privateListObj.sendRequest();
		teamListObj				= new DynamicList(document.getElementById("team_markup"), "teamListObj","teamFilterDivId", 
				${meTeamMember.teamId}, null, trnObj);
		teamListObj.filterInfoDivId	= "teamFilterInfo";
		if(teamListObj.setKeywordTextboxInformation)
		   teamListObj.setKeywordTextboxInformation("teamSearchStr","teamSearchButtonId");
		
		//teamListObj.sendRequest();
		sharedListObj				= new SharedDynamicList(document.getElementById("shared_markup"), "sharedListObj","sharedFilterDivId", trnObj);
		sharedListObj.filterInfoDivId	= "sharedFilterInfo";
		sharedListObj.setKeywordTextboxInformation("sharedSearchStr","sharedSearchButtonId");
		//sharedListObj.sendRequest();

		
		
 		publicListObj			= new PublicDynamicList(document.getElementById("public_markup"), "publicListObj","publicFilterDivId", trnObj);       
		publicListObj.filterInfoDivId	= "publicFilterInfo";
		publicListObj.setKeywordTextboxInformation("publicSearchStr","publicSearchButtonId");
		//publicListObj.sendRequest();
		repositoryTabView				= new YAHOO.widget.TabView("demo");
		repositoryTabView.addListener("activeTabChange", loadTab);
		
		repositoryTabView.dynLists	= new Array();
		var visibleTabs =0;
		var myDoc =  document.getElementById("my_res");
		var teamDoc =  document.getElementById("team_res");
		var sharedDoc =  document.getElementById("shared_res");
		var publicDoc =  document.getElementById("public_res");
		
		if (myDoc !=null) {
			repositoryTabView.dynLists.push( privateListObj );
			visibleTabs ++;
		}
		if (teamDoc !=null) {
			repositoryTabView.dynLists.push( teamListObj );
			visibleTabs ++;
		}
		if (sharedDoc !=null) {
			repositoryTabView.dynLists.push( sharedListObj );
			visibleTabs ++;
		}
		if (publicDoc !=null) {
			repositoryTabView.dynLists.push( publicListObj );
			visibleTabs ++;
		}	
		

		repositoryTabView.activatedLists	= new Array();
		for (var i=0; i<visibleTabs; i++)
			repositoryTabView.activatedLists.push(false);
		
		initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
		
		loadTab();
		
		fPanel	= new FilterAsYouTypePanel("labelButtonId", getLabelFilterCallbackObj(privateListObj), "mainLabels", trnObj);
		fAddPanel	= new FilterAsYouTypePanel("labelButtonId", labelCallbackObj, "addLabelPanel", trnObj);
		fPanel.initLabelArray(false);
		fAddPanel.initLabelArray(false);
		
		teamFPanel	= new FilterAsYouTypePanel("teamLabelButtonId", getLabelFilterCallbackObj(teamListObj), "teamMainLabels", trnObj);
		teamFPanel.initLabelArray(false);

		sharedFPanel	= new FilterAsYouTypePanel("sharedLabelButtonId", getLabelFilterCallbackObj(sharedListObj), "sharedMainLabels", trnObj);
		sharedFPanel.initLabelArray(false);
		
	
		templateFPanel	= new FilterAsYouTypePanel("templateLabelButtonId", 
				getTemplateLabelsCb("docFromTemplateForm", "templateFilterInfoDiv"), "templateMainLabels", trnObj);
		templateFPanel.initLabelArray(false);
	}
	
	YAHOO.util.Event.on(window, "load", afterPageLoad); 
</script>
<script type="text/javascript">
	function validateAddDocumentLocal(){
		var ret = false;
		if(validateAddDocument() == true){
			hidePanel(0);
			//document.forms['crDocumentManagerForm'].docTitle.value = escape(document.forms['crDocumentManagerForm'].docTitle.value);
			//document.forms['crDocumentManagerForm'].docDescription.value = escape(document.forms['crDocumentManagerForm'].docDescription.value);
			//document.forms['crDocumentManagerForm'].style.visibility='hidden';
			//document.getElementById('msgLoading').style.visibility='visible';
			//alert('asd');
			ret = true;
		}
		return ret;
	}
	
	function getLabelFilterCallbackObj( listObj ) {
		var labelFilterCallbackObj	= {
					click: function(e, label) {
						this.listObj.addRemoveLabel(label);
						this.listObj.sendRequest();
					},
					applyClick: function(e, labelArray){
						this.listObj.emptyLabels();
						for (var i=0; i<labelArray.length; i++) {
							this.listObj.addRemoveLabel(labelArray[i]);
						}
						if (labelArray.length == 0) {
							this.listObj.emptyLabels();
						}
						this.listObj.sendRequest();
					},
					listObj: listObj
				}
		
		return labelFilterCallbackObj;
	}
	
	var labelCallbackObj	= {
			click: function(e, label) {
				var postStr	= "action=add&docUUID="+this.docUUID+"&labelUUIDs="+label.uuid;
				this.sendLabelRequest(postStr);
			},
			applyClick: function (e, labelArray){
				var postStr	= "action=add&applyClick=true&docUUID="+this.docUUID;
				for (var i=0; i<labelArray.length; i++) {
					postStr	+= "&labelUUIDs="+labelArray[i].uuid;
				}
				this.sendLabelRequest(postStr);
			},
			remove: function(dUUID, lUUID) {
				var postStr	= "action=remove&docUUID="+dUUID+"&labelUUIDs="+lUUID;
				this.sendLabelRequest(postStr);
			},
			sendLabelRequest: function (postStr) {
				prevPage = $(".current-page").html();
				YAHOO.util.Connect.asyncRequest('POST', '/contentrepository/label.do?', this, postStr);
			},
			failure: function () {
				alert("We are sorry but your request cannot be processed at this time");
			},
			success: function () {
				this.dynamicList.sendRequest();
			},
			docUUID: "",
			dynamicList: null
	}

	var trnObj	= {
			addResource: "<digi:trn>Add Resource</digi:trn>"
	};
	
	var menuPanelForUser	= new ActionsMenu("actionsButtonId","actionsMenu",false,trnObj);
	var menuPanelForTeam	= new ActionsMenu("actionsButtonIdTeam","actionsMenu", true, trnObj);

	
	
	function reset(objListStr, resetFilters) {
        // reset search string
		document.getElementById(objListStr+'SearchStr').value='';
		var targetList = eval(objListStr +'ListObj');
        // reset filters and lables
        if (resetFilters) {
            targetList.resetFilterData(targetList.fDivId);
        }
        // execute the request with empty data
        targetList.sendRequest();
	}
	
	
</script>
<field:display name="Create From Template" feature="Add Resources">
	<script type="text/javascript">
		createfromtemplateenable=true;
	</script>
</field:display>
<field:display name="Add Web Link" feature="Add Resources">
	<script type="text/javascript">
		 addWebLinkenable = true;
	</script>
</field:display>
<field:display name="Upload Doc" feature="Add Resources">
	<script type="text/javascript">
		var uploadDocenalbe=true;
	</script>
</field:display>

<!-- BREADCRUMP START -->
<div class="breadcrump">
<div class="centering">
<div class="breadcrump_cont">
<span class="sec_name"><digi:trn>Resources</digi:trn></span></div>
</div>
</div>
<!-- BREADCRUMP END --> 

<!-- POPUPS START-->
<div id="menuContainerDiv"></div>
<div id="addDocumentDiv" class="dialog">
				<div align="center">
				<div id="addDocumentErrorHolderDiv" style="font-size:11px; color: red; padding:10px;"></div>
				<digi:form action="/documentManager.do" method="post" enctype="multipart/form-data" >
					<input type="hidden" name="type" id="typeId"/>
					<input type="hidden" name="uuid" id="nodeUUID"/>					
					<table cellpadding="3" cellspacing="3" border="0">						
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Title</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td><html:text property="docTitle" size="5" styleClass="inputx" style="width:300px;" /></td>
						</tr>
						<tr>							
							<td>
								<div class="t_sm"><b><digi:trn>Description</digi:trn>:</b></div>
							</td>
							<td><html:textarea property="docDescription" cols="" rows="" style="width:300px; height:70px;" styleClass="inputx"/></td>
						</tr>
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Notes</digi:trn>:</b></div>
							</td>
							<td><html:textarea property="docNotes" cols="" rows="" style="width:300px; height:70px;" styleClass="inputx"/></td>
						</tr>
						<tr>
		                    <td>
		                    	<div class="t_sm"><b><digi:trn>Year Of Publication</digi:trn>:</b></div>
		                    </td>
		                    <td>		
		                        <html:select property="yearOfPublication" styleClass="dropdwn_sm">
		                        	<html:option value="-1"><digi:trn>select...</digi:trn></html:option>
		                        	<c:forEach var="year" items="${crDocumentManagerForm.years}">
		                        		<html:option value="${year}">${year}</html:option>
		                        	</c:forEach>
		                        </html:select>
		                    </td>
		                </tr>
					<field:display name="Resource Index" feature="Resource Columns">
						<tr>
							<td>
								<div class="t_sm">
									<b><span title="<digi:trn>Document Index</digi:trn>">
										<digi:trn>Index</digi:trn>
									</span></b>
								</div>
							</td>
							<td>
								<a title="<digi:trn>Document Index</digi:trn>">
									<html:textarea property="docIndex" cols="" rows="" style="width:300px; height:30px;" styleClass="inputx" />
								</a>
							</td>
						</tr>
					</field:display>								
					<field:display name="Resource Category" feature="Resource Columns">
						<tr>
							<td>
								<div class="t_sm">
									<b><span title="<digi:trn>Document Category</digi:trn>">
										<digi:trn>Category</digi:trn>
									</span></b>
								</div>
							</td>
							<td>
								<span title="<digi:trn>Document Category</digi:trn>">
									<html:textarea property="docCategory" cols="" rows="" style="width:300px; height:30px;" styleClass="inputx"/>
								</span>
							</td>
						</tr>
					</field:display>						
						<tr>
							<td>
								<div class="t_sm"><b><digi:trn>Type</digi:trn>:</b></div>
							</td>
							<td>
								<c:set var="translation">
									<digi:trn>Please select a type from below</digi:trn>
								</c:set>
								<category:showoptions  firstLine="${translation}" name="crDocumentManagerForm" property="docType"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="dropdwn_sm" />
							</td>
						</tr>						
						<tr id="tr_path">
							<td>
								<div class="t_sm"><b><digi:trn>Path</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td>
				                <div class="fileinputs"> 
									<input id="fileData" name="fileData" type="file" class="file">
			                	</div>
			                </td>
						</tr>
						<tr id="tr_url" style="display: none">
							<td>
								<div class="t_sm"><b><digi:trn>URL (ex. http://www.example.com)</digi:trn>:</b><font color="red">*</font></div>
							</td>
							<td><html:text property="webLink" size="32"></html:text></td>
						</tr>
						<tr><td colspan="2"><hr/></td> </tr>						
						<tr>
							<td style="text-align:right;">
								<html:submit styleClass="buttonx" style="padding-bottom: 2px; padding-top: 2px;" onclick="return validateAddDocumentLocal()"><digi:trn>Submit</digi:trn></html:submit>&nbsp;
							</td>
							<td>&nbsp;
								<button class="buttonx" type="button" style="padding-bottom: 1px; padding-top: 1px;"  onClick="hidePanel(0)">
									<digi:trn>Cancel</digi:trn>
								</button>
							</td>
						</tr>
					</table>
				</digi:form>
				</div>
		    </div>
<!-- POPUPS END-->

<!-- MAIN CONTENT PART START -->
<table border="0" cellspacing="0" cellpadding="0" width="1100" align="center">	
	<tbody>
		<tr>
		<td align=left valign="top">
			<div style="width:1100px;" class="yui-skin-sam" id="content"> 
				<div id="demo" class="yui-navset">			
				<ul class="yui-nav">
						<feature:display name="My Resources" module="Content Repository">
				        	<c:if  test="${selectedType=='private' || selectedType=='version'}">
				        		<li id="tab1" class="selected"><a href="#my_res"><div><digi:trn>My Resources</digi:trn></div></a></li>
				        	</c:if>
				        	<c:if  test="${selectedType!='private' && selectedType!='version'}">
				        		<li id="tab1"><a href="#my_res"><div> <digi:trn>My Resources</digi:trn></div></a></li>
				        	</c:if>
				        </feature:display>

			        
			        
			        <feature:display name="Team Resources" module="Content Repository">
			        	<c:if  test="${selectedType=='team'}">
			        		<li id="tab2" class="selected"><a href="#team_res"><div class="tab_link"><digi:trn>Team Resources</digi:trn></div></a></li>
			        	</c:if>
			        	<c:if  test="${selectedType!='team'}">
			        		<li id="tab2"><a href="#team_res"><div class="tab_link"><digi:trn>Team Resources</digi:trn></div></a></li>
			        	</c:if>
					</feature:display>
					
					<c:if test="${not empty myForm.sharedDocsTabVisible && myForm.sharedDocsTabVisible}">
						<feature:display name="Shared Resources" module="Content Repository">
							<c:if  test="${selectedType=='shared'}">
				        		<li id="tab3" class="selected"><a href="#shared_res"><div class="tab_link"><digi:trn>Shared Resources</digi:trn></div></a></li>
				        	</c:if>
				        	<c:if  test="${selectedType!='shared'}">
				        		<li id="tab3"><a href="#shared_res"><div class="tab_link"><digi:trn>Shared Resources</digi:trn></div></a></li>
				        	</c:if>
						</feature:display>
					</c:if>			
					
					<c:if test="${not empty myForm.publicDocsTabVisible && myForm.publicDocsTabVisible}">					
						<feature:display name="Public Resources" module="Content Repository">
							<c:set var="selectedClass">
								<c:if test="${selectedType!='private' && selectedType!='version' && selectedType!='team' && selectedType!='shared'}">selected</c:if>
							</c:set>
				        	<li id="tab4" class="${selectedClass}"><a href="#public_res"><div class="tab_link"><digi:trn>Public Resources</digi:trn></div></a></li>
				       </feature:display>
			       </c:if>
			    </ul> 
			     
			    <div class="yui-content" style="border-color: #d0d0d0">
		    		<feature:display name="My Resources" module="Content Repository">
			      		<div id="my_res" class="resource_popin" style="border: none;">
							<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
								<tr>
						        	<td>
							        	<button id="actionsButtonId" type="button" onclick="menuPanelForUser.toggleUserView();fPanel.hide();privateListObj.getFilterPanel('filterButtonId','privateFilterDivId',true);" class="buttonx"><digi:trn>Add Resource</digi:trn>
							        		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
							        	</button>
						        		<input type="text" id="privateSearchStr">
						        		<button id="privateSearchButtonId" type="button" class="buttonx">
							        		<digi:trn>Search</digi:trn>
							        	</button>
							        	<button id="keywordModeButtonId" class="buttonx" type="button" onclick="privateListObj.getKeywordModePanel('keywordModeButtonId','privateKeywordModeDivId');fPanel.hide();menuPanelForUser.hide();">
                                            <digi:trn>Mode</digi:trn>
                                            <img src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
                                        </button>
								    	<button id="filterButtonId" class="buttonx" type="button" onclick="privateListObj.getFilterPanel('filterButtonId','privateFilterDivId', false);fPanel.hide();menuPanelForUser.hide();">
								    		<digi:trn>Filters</digi:trn>
											<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
								    	</button>
								    	<button id="labelButtonId" class="buttonx" type="button" onclick="fPanel.toggleView();menuPanelForUser.hide();privateListObj.getFilterPanel('filterButtonId','privateFilterDivId',true);">
								    		<digi:trn>Labels</digi:trn>
											<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
								    	</button>
									        <button id="privateClearButtonId" type="button" onclick="reset('private', true);"  class="buttonx">
							        		<digi:trn>Clear</digi:trn>
							        	</button>
								    </td>
								</tr>
								<tr><td><hr style="width: 100%;margin-left: 0px; height:2px;"/></td></tr>
								<tr>
									<td>
											<div style="width: 80%; float: left" class="t_sm" id="privateFilterInfo"></div>
											<div class="show_legend" align="right" style="width: 15%; float: left;padding-top: 0px;">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td style="font-size: 11px;font-family: Arial,sans-serif">
															<bean:define id="legendDivId" value="private" toScope="request" />	
									          				<jsp:include page="legendForResources.jsp"/>
									          			</td>
									          		</tr>
									          	</table>
											</div>
											<br /><br />
											<div id="my_markup" align="left" style="clear: both;" >
											</div>
									</td>
								</tr>
							</table>
							<bean:define id="filterDivId" value="privateFilterDivId" toScope="request" />
							<jsp:include page="filters/filters.jsp"/>	   
							<bean:define id="keywordModeDivId" value="privateKeywordModeDivId" toScope="request" />
                            <jsp:include page="keywordMode.jsp"/>     
				        </div>
					</feature:display>			    	
					
					
					<feature:display name="Team Resources" module="Content Repository">
						<div id="team_res" class="resource_popin"  style="border: none;">				        	       
							<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
								<tr>
						        	<td>
									<%if (DocumentManagerRights.hasAddResourceToTeamResourcesRights(request) ) { %>
										<button id="actionsButtonIdTeam" type="button" onclick="menuPanelForTeam.toggleTeamView();teamFPanel.hide();teamListObj.getFilterPanel('teamFilterButtonId','teamFilterDivId', true);" class="buttonx"><digi:trn>Add Resource</digi:trn>
											<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
										</button>
									<%}%>
									
									    <input type="text" id="teamSearchStr">
						        		<button id="teamSearchButtonId" type="button" class="buttonx">
							        		<digi:trn>Search</digi:trn>
							        	</button>
							        	<button id="teamKeywordModeButtonId" class="buttonx" type="button" onclick="teamListObj.getKeywordModePanel('teamKeywordModeButtonId','teamKeywordModeDivId',false);fPanel.hide();menuPanelForTeam.hide();">
                                            <digi:trn>Mode</digi:trn>
                                            <img src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
                                        </button>
										<button id="teamFilterButtonId" class="buttonx" type="button" onclick="teamListObj.getFilterPanel('teamFilterButtonId','teamFilterDivId', false);teamFPanel.hide();menuPanelForTeam.hide();">
								    		<digi:trn>Filters</digi:trn>
								    		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
								    	</button>
								    	<button id="teamLabelButtonId" class="buttonx" type="button" onclick="teamFPanel.toggleView();teamListObj.getFilterPanel('teamFilterButtonId','teamFilterDivId', true);menuPanelForTeam.hide();">
								    		<digi:trn>Labels</digi:trn>
								    		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
								    	</button>
								    	 <button id="teamClearButtonId" type="button" onclick="reset('team', true);"  class="buttonx">
							        		<digi:trn>Clear</digi:trn>
							        	</button>
									</td>
								</tr>						
								<tr><td><hr style="width: 100%;margin-left: 0px; height:2px;"/></td></tr>	
								<tr>
									<td>									
											<div style="width: 80%; float: left" id="teamFilterInfo" class="t_sm"></div>
											<div class="show_legend" align="right" style="width: 15%; float: left;padding-top: 0px;">
												<table border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td style="font-size: 11px;font-family: Arial,sans-serif">
															<bean:define id="legendDivId" value="team" toScope="request" />															
									          				<jsp:include page="legendForResources.jsp"/>
									          			</td>
									          		</tr>
									          	</table>
											</div>
											<br /><br />
											<div id="team_markup" align="left"  class="all_markup">
											</div>
									</td>
								</tr>
							</table>	
							<bean:define id="filterDivId" value="teamFilterDivId" toScope="request" />
							<jsp:include page="filters/filters.jsp"/>
							<bean:define id="keywordModeDivId" value="teamKeywordModeDivId" toScope="request" />
                            <jsp:include page="keywordMode.jsp"/>
				        </div>
					</feature:display>					
					
					<!-- Shared Resources Start  -->
					<feature:display name="Shared Resources" module="Content Repository">
						<c:if test="${not empty myForm.sharedDocsTabVisible && myForm.sharedDocsTabVisible}">
							<div id="shared_res"  class="resource_popin" style="border: none;">				        	       
								<table border="0" cellPadding="1" cellSpacing="0" width="100%"style="position: relative; left: 0px" >
									<tr>
										<td>	
											<input type="text" id="sharedSearchStr">
							        		<button id="sharedSearchButtonId" type="button" class="buttonx">
								        		<digi:trn>Search</digi:trn>
								        	</button>
								            <button id="sharedKeywordModeButtonId" class="buttonx" type="button" onclick="sharedListObj.getKeywordModePanel('sharedKeywordModeButtonId','sharedKeywordModeDivId', false);fPanel.hide();sharedFPanel.hide();">
	                                            <digi:trn>Mode</digi:trn>
	                                            <img src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
                                            </button>							
											<button id="sharedFilterButtonId" class="buttonx" type="button" onclick="sharedListObj.getFilterPanel('sharedFilterButtonId','sharedFilterDivId', false);sharedFPanel.hide();">
									    		<digi:trn>Filters</digi:trn>
									    		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
									    	</button>
									    	<button id="sharedLabelButtonId" class="buttonx" type="button" onclick="sharedFPanel.toggleView();sharedListObj.getFilterPanel('sharedFilterButtonId','sharedFilterDivId', true);">
									    		<digi:trn>Labels</digi:trn>
									    		<img  src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
									    	</button>
									    	<button id="sharedClearButtonId" type="button" onclick="reset('shared', true);"  class="buttonx">
							        		<digi:trn>Clear</digi:trn>
							        	</button>
										</td>
									</tr>						
									<tr><td><hr style="width: 100%;margin-left: 0px; height:2px;"/></td></tr>	
									<tr>
										<td>										
											<div style="width: 80%; float: left" id="sharedFilterInfo" class="t_sm"></div>
											<br />
											<div id="shared_markup" align="left" class="all_markup">
													
											</div>
											<br />
										</td>
									</tr>
								</table>
								<bean:define id="filterDivId" value="sharedFilterDivId" toScope="request" />
								<jsp:include page="filters/filters.jsp"/>
								<bean:define id="keywordModeDivId" value="sharedKeywordModeDivId" toScope="request" />
                                <jsp:include page="keywordMode.jsp"/>		        
					        </div>						
						</c:if>						
					</feature:display>
					
					<!-- Shared Resources end  -->
					
					<!-- Public resources -->
					<feature:display name="Public Resources" module="Content Repository">
						<c:if test="${not empty myForm.publicDocsTabVisible && myForm.publicDocsTabVisible}">
							<div id="public_res"  class="resource_popin" style="border: none;">				        	       
								<table border="0" cellpadding="1" cellspacing="0" width="100%" style="position: relative; left: 0px" >
									<tr>
										<td>	
											<input type="text" id="publicSearchStr">
							        		<button id="publicSearchButtonId" type="button" class="buttonx">
								        		<digi:trn>Search</digi:trn>
								        	</button>
								        	<button id="publicKeywordModeButtonId" class="buttonx" type="button" onclick="publicListObj.getKeywordModePanel('publicKeywordModeButtonId','publicKeywordModeDivId', false);fPanel.hide();menuPanelForPublic.hide();">
                                                <digi:trn>Mode</digi:trn>
                                                <img src="/TEMPLATE/ampTemplate/images/arrow_down_black.gif">
                                            </button>
											<button id="publicClearButtonId" type="button" onclick="reset('public', false);"  class="buttonx">
							        		<digi:trn>Clear</digi:trn>
								        	
								        	
										</td>
									</tr>
									<tr>
										<td>										
											<div id="public_markup" align="left" class="all_markup">
											
											</div>
											<br />
										</td>
									</tr>
								</table>
								<bean:define id="keywordModeDivId" value="publicKeywordModeDivId" toScope="request" />
                                <jsp:include page="keywordMode.jsp"/>	        
					        </div>
						</c:if>				        
			        </feature:display>
					<!--End public Resources-->
				</div>
			</div>
			</div>
		
		
						
		<%-- END -- Table for "My Documents" --%>
        <br />
      </td>
	</tr>
	</tbody>
</table>
<!-- MAIN CONTENT PART END -->
<br/>

<c:set var="publicResourcesWindowName">
	<digi:trn>Public Resources</digi:trn>
</c:set>
<c:set var="teammemberResourcesWindowName">
	<digi:trn>Team Member Resources</digi:trn>
</c:set>
<c:set var="sharedResourcesWindowName">
	<digi:trn>Shared Resources</digi:trn>
</c:set>
	

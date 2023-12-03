<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings"%>

<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="ampModule/aim/scripts/dscript120_ar_style.js"/>"></script>
<script type="text/javascript">
BASE_YEAR	= <globalsettings:value name="Year Range Start" />;
var helpBody=' <digi:trn jsFriendly="true"> to open all reports on one page, please enter the digit "0"</digi:trn>';
var helpBodyAct=' <digi:trn jsFriendly="true"> Please enter a number greater than "1"</digi:trn>';
var wrongNumber = '<digi:trn jsFriendly="true">Wrong number of activities/projects per page. </digi:trn>';
var helpTitle='<digi:trn jsFriendly="true">Report Sheet</digi:trn>';
var helpNumberPages = '<digi:trn jsFriendly="true">Wrong number of pages to display in paginator. The number should be greater than 1.</digi:trn>';
var wrongNumberOfReportsPerPage = '<digi:trn jsFriendly="true">Wrong number of activities/projects per page. </digi:trn>';

function validade(){
	var defReportsPerPage = document.getElementById("defRecsPerPage");
  	if(parseInt(defReportsPerPage.value)<=1){
	  alert(wrongNumber + helpBodyAct);
	  return false;
  	}

  	var numberOfPagesToDisplay = document.getElementById("numberOfPagesToDisplay");
  	if(parseInt(numberOfPagesToDisplay.value)<=1){
	  alert(helpNumberPages);
	  return false;
  	}
  	var  reportsperPage = document.getElementById("repsPerPage").value;
  	var validChars= "0123456789";
 	for (var i = 0;  i < reportsperPage.length;  i++) {
 		var ch = reportsperPage.charAt(i);
  		if (validChars.indexOf(ch)==-1){
  			alert(wrongNumberOfReportsPerPage + '<digi:trn jsFriendly="true">enter correct number</digi:trn>');	   			
   			return false;
  		}
 	}
  	
  	var startYear		= parseInt(aimUpdateAppSettingsForm.reportStartYear.value);
  	var endYear		= parseInt(aimUpdateAppSettingsForm.reportEndYear.value);

  	if ( !checkYear( startYear, BASE_YEAR, 200 ) ) {
		aimUpdateAppSettingsForm.reportStartYear.focus();
		alert("<digi:trn>Chosen report start year is not realistic</digi:trn>");
		return false;
  	}
  	if ( !checkYear( endYear, BASE_YEAR, 200 ) ) {
		aimUpdateAppSettingsForm.reportEndYear.focus();
		alert("<digi:trn>Chosen report end year is not realistic</digi:trn>");
		return false;
  	}
  	if ( startYear > endYear ) {
	  aimUpdateAppSettingsForm.reportStartYear.focus();
	  alert("<digi:trn>Start year greater than end year</digi:trn>");
	  return false;
  	}
	//tms 
	var chosenRights=document.getElementById("publResRights"); //right for publishing docs  
	var tms= document.getElementById("selTeamMembers");
	if(tms!=null && tms.value!='' && tms.value!=0 && chosenRights.value==2){
		document.getElementById('setectedTMs').value=false;
	}else{
		document.getElementById('setectedTMs').value=true;
	}
	var calendar = document.getElementsByName("fisCalendarId")[0];
	if (calendar.options[calendar.options.selectedIndex].value == "") {
		  alert("<digi:trn>You must select a fiscal calendar</digi:trn>");
		  return false;
		
	}
	
  	document.aimUpdateAppSettingsForm.save.value = "save";
  	document.aimUpdateAppSettingsForm.submit();
  	return true;
}

function publishingRigthsChanged(){
	var chosenRights=document.getElementById("publResRights");
	var membersListDiv=document.getElementById("membersDiv"); memDiv
	var memDiv=document.getElementById("memDiv");
	if(chosenRights.value==2){
		memDiv.style.display	= 'block';
		membersListDiv.style.display	= 'block';
	}else{
		memDiv.style.display	= 'none';
		membersListDiv.style.display	= 'none';
	}
}

function checkYear( year, base, range ) {
	if ( year == 0 )
		return true;
	if ( year > base + range || year < base - range )
		return false;
	return true;

}

function loadShareRules(){
	<digi:context name="sel" property="context/ampModule/moduleinstance/defaultSettings.do?shareResAction=getOptions" />;
	url = "<%= sel %>" ;
	document.aimUpdateAppSettingsForm.action = url;
	document.aimUpdateAppSettingsForm.submit();	
}

</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<digi:errors/>
<digi:instance property="aimUpdateAppSettingsForm" />
<digi:form action="/saveApplicationSettings.do" method="post">
<!-- this is for the nice tooltip widgets -->
<!-- we are using this hack to get rid from issue due to "position: relative" in tabs.css -->
<![if !IE]>
 <DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<![endif]>

<html:hidden property="type" />
<html:hidden property="appSettingsId" />
<html:hidden property="save" />
<html:hidden styleId="setectedTMs" value="${aimUpdateAppSettingsForm.resetTeamMembers}" property="resetTeamMembers"/>
<!-- Start include of reportDescriptionSheet.jsp  -->
	<%@include file="reportDescriptionSheet.jsp"%>
<!-- END include of reportDescriptionSheet.jsp  -->

<table cellspacing="0" cellpadding="0" vAlign="top" align="left" width="100%">
	<tr>
		<td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
		</td>
	</tr>
	<tr>
		<td noWrap vAlign="top">
			<c:set var="selectedTab" value="0" scope="request"/>
			<c:set var="selectedSubTab" value="1" scope="request"/>
			
			<c:choose>
				<c:when test="${aimUpdateAppSettingsForm.workspaceType != 'Management' }">
					<c:set var="childWorkspaces" value="disabled" scope="request" />
				</c:when>
				<c:otherwise>
					<c:set var="childWorkspaces" value="enabled" scope="request" />
				</c:otherwise>
			</c:choose>
			
			
			<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
				<tr>
				<td>
					<div class="breadcrump_cont">
						<span class="sec_name">
							<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</span>
						
						<span class="breadcrump_sep">|</span>
						<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
							<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>
						<span class="breadcrump_sep"><b>�</b></span>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
						<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
						<span class="breadcrump_sep"><b>�</b></span>
						<span class="bread_sel"><digi:trn key="aim:customizeTeamSettings">Customize Team Settings</digi:trn></span>
					</div>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
						<jsp:include page="teamSetupMenu.jsp" flush="true" />
						<!-- we are using this hack to get rid from issue due to "position: relative" in tabs.css -->
								<!--[if IE]>
								 <DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
								<![endif]-->
								<table class="inside" width="100%" cellpadding="0" cellspacing="0">
								<tr bgcolor=#C7D4DB>
								<td colspan=2 align=center style="padding:5px; font-size:12px; font-weight:bold;">Application Settings</td>
								</tr>
							<logic:equal name="aimUpdateAppSettingsForm" property="updated" value="true">
								<tr>
									<td class="inside" colspan="2">
										<digi:trn key="aim:updateToAMPComplete">
											Update to AMP Complete
										</digi:trn>
									</td>
								</tr>
							</logic:equal>
							<logic:equal name="aimUpdateAppSettingsForm" property="errors" value="true">
								<tr>
									<td class="inside" colspan="2">
										<font color="red">
											<digi:trn key="aim:wrongNumberOfActivitiesProjectsPerPage">
															Wrong number of Activities/Projects per page. The number should be greater than 1.
											</digi:trn>
										</font>
									</td>
								</tr>
							</logic:equal>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:numberactivitiesprojectsperpage">Number of activities/projects per page</digi:trn></div>
                </td>
                <td class="inside">
									<html:text property="defRecsPerPage" size="5"  styleClass="inputx insidex" styleId="defRecsPerPage"/>
									<a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBodyAct],Style[15])" onmouseout="htm()">
										<img src="../ampTemplate/images/help.gif" alt="" width="10" height=10 border="0">
                  </a>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:numberofpagestodisplay">Number of Pages to display in paginator</digi:trn></div>
				                </td>
                				<td class="inside">
									<html:text property="numberOfPagesToDisplay" size="5"  styleClass="inputx insidex" styleId="numberOfPagesToDisplay"/>
									<a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBodyAct],Style[15])" onmouseout="htm()">
										<img src="../ampTemplate/images/help.gif" alt="" width="10" height=10 border="0">
                 					 </a>
								</td>
							</tr>

							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:numberreportsperpage">Number of reports per page</digi:trn></div>
								</td>
								<td class="inside">
									<html:text property="defReportsPerPage" size="5"  styleClass="inputx insidex" styleId="repsPerPage"/>
										<a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBody],Style[15])" onmouseout="htm()">
											<img src="../ampTemplate/images/help.gif" alt="" width="10" height=10 border="0">
                    </a>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:defLanguage">Language</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="language" styleClass="inputx insidex">
									  	<bean:define id="languages" name="aimUpdateAppSettingsForm" property="languages" type="java.util.Collection" />
									    <c:forEach var="element" items="${aimUpdateAppSettingsForm.languages}">
											<c:set var="trn">
												<digi:trn>${element.name}</digi:trn>
											</c:set>
											<html:option value="${element.code}">
												${trn}
											</html:option>
										</c:forEach>
			                		</html:select>
								</td>
							</tr>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:defCurrency">Currency</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="currencyId" styleClass="inputx insidex">

										<html:optionsCollection name="aimUpdateAppSettingsForm" property="currencies" value="ampCurrencyId" label="currencyName" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:defValidation">Validation</digi:trn></div>
								</td>
								<td class="inside">
									<bean:define id="newOnly" value=""/>
									<bean:define id="allEdits" value=""/>
									<bean:define id="validationOff" value=""/>
									<c:if test="${aimUpdateAppSettingsForm.validation=='newOnly'}">
										<bean:define id="newOnly" value="selected"/>
									</c:if>
									<c:if test="${aimUpdateAppSettingsForm.validation=='allEdits'}">
										<bean:define id="allEdits" value="selected"/>
									</c:if>
									<c:if test="${aimUpdateAppSettingsForm.validation=='validationOff'}">
										<bean:define id="validationOff" value="selected"/>
									</c:if>
									<html:select property="validation" styleClass="inputx insidex">
										<option value="newOnly" <c:out  value="${newOnly}"></c:out> ><digi:trn key="aim:defValidationNewOnly">Validate New Only</digi:trn></option>
										<option value="allEdits" <c:out  value="${allEdits }"></c:out> ><digi:trn key="aim:defValidationAll">Validate All</digi:trn></option>
										<option value="validationOff" <c:out  value="${validationOff}"></c:out> ><digi:trn>Off</digi:trn></option>
									</html:select>
								</td>
							</tr>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:reportsDefaultStartYear">Reports Default Start Year</digi:trn></div>
								</td>
								<td class="inside">
									<html:text property="reportStartYear" size="5"  styleClass="inputx insidex"/>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:reportsDefaultEndYear">Reports Default End Year</digi:trn></div>
								</td>
								<td class="inside">
									<html:text property="reportEndYear" size="5"  styleClass="inputx insidex"/>
								</td>
							</tr>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:defFisCalendar">Fiscal Calendar</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="fisCalendarId" styleClass="inputx insidex">
										<html:option value="">------ <digi:trn key="aim:selFisCalendar">Select Fiscal Calendar</digi:trn> ------</html:option>
										<html:optionsCollection name="aimUpdateAppSettingsForm" property="fisCalendars" value="ampFiscalCalId" label="name" />
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn key="aim:defaultTeamTab">Default Team Tab</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="defaultReportForTeamId" styleClass="inputx insidex"  styleId="defaultReport">
										<html:option value="0">
											------ <digi:trn key="aim:selDefTeamTab">Select Default Team Tab</digi:trn> ------
										</html:option>
										<c:forEach var="reports" items="${aimUpdateAppSettingsForm.reports}">
											<html:option value="${reports.ampReportId}">
												<c:out value="${reports.name}"/>
											</html:option>
										</c:forEach>
									</html:select>
												<br />
									<a style="cursor:pointer;color:#006699" onClick="if(document.getElementById('defaultReport').value == 0) {alert('<digi:trn jsFriendly="true" key="aim:defaultTeamTabDetailsAlertMessage">Please select a default tab</digi:trn>');return false;}else{showMyPanel();}"><digi:trn key="aim:defaultTeamTabDetailsMessage">Click here for details</digi:trn></a>
								</td>
							</tr>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn>Rights for Team Resources</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="allowAddTeamRes" styleClass="inputx insidex" onchange="return loadShareRules()" styleId="allowAddTeamRes">
										<c:forEach var="element" items="${aimUpdateAppSettingsForm.possibleValsAddTR}">
											<c:set var="trn">
												<digi:trn>${element.value}</digi:trn>
											</c:set>
												<html:option value="${element.key}">
													${trn}
												</html:option>
										</c:forEach>
									</html:select>
								</td>
							</tr>
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn>Rights for Sharing Team Resources Across Workspaces</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="allowShareAccrossWRK" styleClass="inputx insidex">
										<c:forEach var="element" items="${aimUpdateAppSettingsForm.shareResAmongWorkspacesPossibleVals}">
											<c:set var="trn">
												<digi:trn>${element.value}</digi:trn>
											</c:set>
											<html:option value="${element.key}">
												${trn}
											</html:option>
										</c:forEach>
									</html:select>
								</td>
							</tr>
							<tr bgcolor=#f8f8f8>
								<td class="inside">
									<div class="bold_12"><digi:trn>Rights For Publishing Resources</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="allowPublishingResources" styleClass="inputx insidex" onchange="publishingRigthsChanged()" styleId="publResRights">
										<c:forEach var="element" items="${aimUpdateAppSettingsForm.publishResourcesPossibleVals}">
											<c:set var="trn">
												<digi:trn>${element.value}</digi:trn>
											</c:set>
											<html:option value="${element.key}">
												${trn}
											</html:option>
										</c:forEach>
									</html:select>
								</td>
							</tr>
							
							
							<tr>
								<td class="inside">
									<div class="bold_12"><digi:trn>Show all set up countries in filters</digi:trn></div>
								</td>
								<td class="inside">
									<html:select property="showAllCountries" styleClass="inputx insidex">
										<html:option value="false">
												Off
										</html:option>
										<html:option value="true">
												On
										</html:option>
									</html:select>
								</td>
							</tr>
							
							
							<tr>
								<c:if test="${aimUpdateAppSettingsForm.allowPublishingResources!=2}">
										<c:set var="displayDiv">display: none;</c:set>
								</c:if>
								<c:if test="${aimUpdateAppSettingsForm.allowPublishingResources==2}">
									<c:set var="displayDiv"></c:set>
								</c:if>
								<td>
									<div class="bold_12"><div style="${displayDiv}"  id="memDiv">
										<digi:trn>Members</digi:trn>
									</div></div>															
								</td>
								<td>
									<div style="${displayDiv}"  id="membersDiv">																	
										<html:select name="aimUpdateAppSettingsForm" property="selTeamMembers" multiple="true" styleId="selTeamMembers" style="width:300px;height:200px" styleClass="inputx insidex">
											<logic:empty name="aimUpdateAppSettingsForm" property="teamMembers">
												<html:option value="0"><digi:trn>Not applicable</digi:trn></html:option>
											</logic:empty>
											<logic:notEmpty name="aimUpdateAppSettingsForm"  property="teamMembers" >
												<html:optionsCollection name="aimUpdateAppSettingsForm" property="teamMembers" value="memberId" label="memberName"/>
											</logic:notEmpty>																		
										</html:select>																
										</div>
									</td>														
								</tr>
							<tr>
								<td colspan="2"><digi:errors/></td>
              </tr>
            </table>
						
						<br>
						                          
            <div align="center">
        			<c:set var="caption">
              	<digi:trn key="aim:btnSave">Save</digi:trn>
               </c:set>
               <input type="button"  class="buttonx_sm btn_save" value="${caption}" onclick="validade();" />    	
						</div>
						
						</div>
						</div>											
												
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>




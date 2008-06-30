<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<script language="JavaScript">
    var continueExecution = true;
    function checkstatus(){

    	var statusDiv = document.getElementById("statusValue");
    	var offsetByServerSide = 44;
    	var xmlhttp; 

    	if(window.ActiveXObject)
    		xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
    	else
    		xmlhttp = new XMLHttpRequest();

		xmlhttp.open("GET","/aim/viewNewAdvancedReport.do~loadstatus=true&"+new Date().getTime(),true);

		xmlhttp.onreadystatechange=function()
		{
		   if(xmlhttp.readyState == 4 && xmlhttp.status == 200)
		   {
 			  	var rowCount = (xmlhttp.responseText.split(",")[1]).replace(/\r\n|\n|\r/g, "");
 			  	if(rowCount == "null"){
 			  		rowCount = -1;
 			  	}
		  		rowCount = Number(rowCount);
 			  	var currentCount = Number(xmlhttp.responseText.split(",")[0]);

 		   		var percentage =  currentCount * 100 / (rowCount + offsetByServerSide);
		   		if(percentage == "") percentage = 0;
			    try{
		    		statusDiv.style.display = "";
		    		if(percentage.toFixed(0) > 0 && percentage.toFixed(0) < 100)
		    		{
			    		if(statusDiv.innerText != undefined)
			    			statusDiv.innerText = "(" + percentage.toFixed(0) + "%)";
			    		else
				    		statusDiv.textContent = "(" + percentage.toFixed(0) + "%)";
				    }
			    	if(percentage>99 || percentage == -1){
			    		continueExecution = false;
			    		//statusDiv.style.display = "none";
			    	}
				}catch(e){}
				if(continueExecution)
		    		setTimeout("checkstatus()", 1000);
		   }
		}
		xmlhttp.send(null);
    }
    
</script>

<script language="JavaScript">
/*	function addActivity() {
		window.location.href="/aim/addActivity.do~pageId=1~reset=true~action=create";	
	}
	*/

function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "Show current settings &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "Hide current settings &lt;&lt;";
	}
}

	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	
	var tabName	= "Tab-By Project";
	<logic:empty name="filterCurrentReport" scope="session">
	<logic:notEmpty name="defaultTeamReport" scope="session">
			tabName	= 'Tab-<bean:write name="defaultTeamReport" scope="session" property="name"/>';
	</logic:notEmpty>
	</logic:empty>
	<logic:notEmpty name="filterCurrentReport" scope="session">
		tabName	= 'Tab-<bean:write name="filterCurrentReport" scope="session" property="name"/>';
	</logic:notEmpty>	
	
</script>

	
<digi:context name="digiContext" property="context" />
<style>

</style>


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>
<br />
<div id="content"  class="yui-skin-sam" style="padding-left:10px;width:98%;min-width:680px;"> 
<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">

<ul id="MyTabs" class="yui-nav"">
<c:set var="counter" value="0"/> 
<logic:present name="myTabs" scope="session">
	<logic:present name="myReports" scope="session">
		<logic:iterate name="myTabs" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
					<logic:equal name="report" property="drilldownTab" value="true">
	                    <c:set var="counter" value="${counter+1}" />
	                    <c:set var="reportNameTrn">
		                    <digi:trn key="aim:clickreport:tabs:${report.nameTrn}">${report.name}</digi:trn>
	                    </c:set>
	                   
	                    <c:if test="${counter < 6}">
	                    <li>
	                    <c:if test="${fn:length(reportNameTrn) > 25}" >
							<a id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${reportNameTrn}" />'><div><c:out value="${fn:substring(reportNameTrn, 0, 25)}" />...</div></a>
	                    </c:if>
	                    <c:if test="${fn:length(reportNameTrn) <= 25}" >
							<a id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${reportNameTrn}" />'><div><c:out value="${reportNameTrn}" /></div></a>
	                    </c:if>
	                                                                
	                    </li>
	                    </c:if>
					</logic:equal>
		</logic:iterate>
	</logic:present>
</logic:present>
</ul>
</div>
<div id="ajaxcontentarea" class="contentstyle" style="border:1px solid black;min-height:410px;_height:410px;padding-left:5px;padding-top:5px;">
<digi:trn key="aim:addATab">
<p/>
Click on one of the tabs to display activities. You can add more tabs by using the Tab Manager.
</digi:trn>
</div>
</div> 

<c:set var="loadstatustext">
	<digi:trn key="aim:loadstatustext">Requesting Content</digi:trn>
</c:set>
<script type="text/javascript">
	loadstatustext='<img src="/repository/aim/view/scripts/ajaxtabs/loading.gif" /> <%=((String) pageContext.getAttribute("loadstatustext")).replaceAll("\r\n"," ")%> <span id="statusValue">...</span>';
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	startajaxtabs("MyTabs");
	if(document.getElementById(tabName)){
		checkstatus(true);
		reloadTab("MyTabs",tabName);
	}
</script>

<div id="debug"></div>

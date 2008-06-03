<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
    var continueExecution = true;
	
	function setRenderingComplete()
	{
    	var statusDiv = document.getElementById("statusValue");
		if(statusDiv.innerText != undefined)
			statusDiv.innerText = "(100%) Rendering page. Please wait...";
		else
			statusDiv.textContent = "(100%) Rendering page. Please wait...";	
			
	}
    function checkstatus(){

    	var statusDiv = document.getElementById("statusValue");
    	var offsetByServerSide = 50;
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
		    		setTimeout("checkstatus()", 250);
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


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

<ul id="MyTabs" class="shadeTabs">
<logic:present name="myReports" scope="session">
	<logic:iterate name="myReports" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
				<logic:equal name="report" property="drilldownTab" value="true">
				<li><a id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea"><digi:trn key="aim:clickreport:tabs:${report.nameTrn}">${report.name}</digi:trn></a></li>
				</logic:equal>
	</logic:iterate>
</logic:present>

</ul>
<div id="ajaxcontentarea" class="contentstyle">
<digi:trn key="aim:clickOnATab">
<p/>
Click on one of the tabs to display activities. You can create more tabs by using the Advanced Reports Manager.
</digi:trn>
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

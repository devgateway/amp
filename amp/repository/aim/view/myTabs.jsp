<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>
<link rel="stylesheet" type="text/css" href="/repository/aim/view/css/filters/filters2.css">
<%
	{
		if (ReportContextData.contextIdExists()){
			ReportContextData.getFromRequest(true); // instantiate a RCD instance - will crash if no ampReportId exists in the context (this is ok), will create a new RCD if not existing (That is ok too - we might render this tab without having gone through viewNewAdvancedReport)
		}
		
		org.digijava.module.aim.dbentity.AmpReports report = (org.digijava.module.aim.dbentity.AmpReports) session.getAttribute(org.digijava.module.aim.helper.Constants.CURRENT_TAB_REPORT);
		//Long currentTabAmpReportId = (report != null) ? report.getAmpReportId() : null;
		if (report != null)
			{
				if (report.getDrilldownTab())
					pageContext.setAttribute("currentTabReport", report);							
				else
					new RuntimeException("Current tab report is not a drill-down tab!").printStackTrace();
			}
		
	}
%>

<script language="JavaScript">
    var continueExecution = true;
    function checkstatus(){

    	var statusDiv = document.getElementById("statusValue");
    	if (!statusDiv) return;
    	 
    	var offsetByServerSide = 44;
    	var xmlhttp; 
	
    	if(window.ActiveXObject)
    		xmlhttp = new ActiveXObject('Microsoft.XMLHTTP');
    	else
    		xmlhttp = new XMLHttpRequest();

    	<logic:notPresent name="currentTabReport">
    		return;
    	</logic:notPresent>
    	
	<logic:present name="currentTabReport">
    	
		xmlhttp.open("GET","/aim/viewNewAdvancedReport.do~loadstatus=true~ampReportId=<bean:write name="currentTabReport" property="ampReportId"/>&"+new Date().getTime(),true);

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
	</logic:present>		
    }
    
</script>

<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings" jsFriendly="true">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings" jsFriendly="true">Hide current settings</digi:trn> 
</c:set>

<script type="text/javascript" src="/repository/aim/view/multilingual/multilingual_scripts.js"></script>

<script language="javascript">
var allTabsPanel = null;
var replaceableTabObject = null;
var myTabsObject;

	function scrollableDivStrips(oddColor, evenColor, hoverColor){
		var divArray = document.getElementById("scrollableDiv").getElementsByTagName("DIV");
		for(a=0; a<divArray.length; a++)
		{
			var currentDiv = divArray[a];
			if(a%2==0) {
				currentDiv.style.backgroundColor = evenColor;
				currentDiv.previousColor = evenColor;
			}
			else
			{
				currentDiv.style.backgroundColor = oddColor;
				currentDiv.previousColor = oddColor;
			}
			currentDiv.hoverColor = hoverColor;
			currentDiv.onmouseout = setHover;
			currentDiv.onmouseover = unsetHover;
		
		}
		
	}
	function setHover()
	{
		this.style.backgroundColor = this.previousColor;
	}
	function unsetHover()
	{
		this.style.backgroundColor = this.hoverColor;
	}

	function initAllTabs() {
		scrollableDivStrips("#dbe5f1","#ffffff","#a5bcf2");
		//Initialize all tabs
		myTabsObject = new YAHOOAmp.widget.TabView("demo"); 
		//Create "More..." tab
		var objeto = document.createElement("DIV");
		objeto.innerHTML = "<digi:trn jsFriendly='true'>More Tabs...</digi:trn>";
		objeto.title = "<digi:trn jsFriendly='true'>Click to see all tabs</digi:trn>";;
		objeto.onclick = changeTab;
		objeto.id = "moreTabs";

		//Add it to the Tab bar
		myTabsObject.addTab( new YAHOOAmp.widget.Tab({ 
			labelEl: objeto
			
		}), myTabsObject.get('tabs').length+1); 

		//Get the position and create the panel
		var region = YAHOOAmp.util.Dom.getRegion("moreTabs");
		var xPos = region.left;
		var yPos = region.bottom;
		allTabsPanel = new YAHOOAmp.widget.Panel("allTabsPanel1", {xy:[xPos,yPos], 
					width:"320px", height:"225px", visible:false,
					effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration: 0.5},
					constraintoviewport:true }  );
		allTabsPanel.setHeader("<digi:trn jsFriendly='true'>Please select from the list below</digi:trn>");
		allTabsPanel.setBody("");
		allTabsPanel.render(document.body);
		var divAllTabs = document.getElementById("allTabs");
		divAllTabs.style.display 	= "block";
		allTabsPanel.setBody(divAllTabs);
		<logic:notEmpty name="currentTabReport" scope="page">
		if(!tabExists('Tab-<c:out value="${currentTabReport.ampReportId}"/>'))
			setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="currentTabReport" property="ampReportId"/>~widget=true", "<c:out value="${currentTabReport.name}" />", "<c:out value="${fn:substring(currentTabReport.name, 0, 25)}" />", "Tab-<c:out value="${currentTabReport.ampReportId}" />");
		</logic:notEmpty>	
	}
	function tabExists(tabCheckName){
		if (document.getElementById(tabCheckName)) return true;
		return false;
	}

	function setNewTab(url, labelComplete,label, id){
		//Replace the tab with the new selection
		if(replaceableTabObject != null)
		{
			myTabsObject.removeTab(replaceableTabObject);
		}

		var objeto = document.createElement("DIV");
	
		objeto.innerHTML = label.replace("<", "&lt;").replace(">", "&gt;")+'...';
		objeto.id = "replaceableTab";

		myTabsObject.addTab( new YAHOOAmp.widget.Tab({ 
			labelEl: objeto
			
		}), myTabsObject.get('tabs').length-1); 
		var tabObject = document.getElementById("replaceableTab");

		if(tabObject.parentNode.tagName == "A")
		{
			tabObject.parentNode.title = labelComplete;
			tabObject.parentNode.rel= "ajaxcontentarea"; 
			tabObject.parentNode.href = url;
			tabObject.parentNode.id = id;
		}
		var len = document.getElementById("scrollableDiv").getElementsByTagName("DIV").length;
		for (var i = 0; i < len; i++) 
			{	
			document.getElementById("scrollableDiv").getElementsByTagName("DIV")[i].style.display='';
			}
		//console.log(labelComplete);
		if(document.getElementById(labelComplete) != null){
			document.getElementById(labelComplete).style.display = 'none';	
		}
		
		replaceableTabObject = myTabsObject.getTab(myTabsObject.get('tabs').length-2);
		allTabsPanel.hide();
		$('#MyTabs .tabs-trigger').click(function(){
			if (tab_loading) return false;
		});
		startajaxtabs("MyTabs");
		reloadTab("MyTabs",id);
		hideMoreTabs();
	}

	function preventTabClickEvent(e){
		if ($(e.target).parents('#MyTabs').length > 0 ) {
			e.stopPropagation();
			e.preventDefault();
		}
	}
	function hideMoreTabs(){
		if(isListEmpty()){
			var l = document.getElementById('MyTabs').getElementsByTagName("li").length;
			document.getElementById('MyTabs').getElementsByTagName("li")[l-1].style.display='none';
		}
	}

	function isListEmpty(){
		var len = document.getElementById("scrollableDiv").getElementsByTagName("DIV").length;
		for (i = 0; i < len; i++){	
			if(document.getElementById("scrollableDiv").getElementsByTagName("DIV")[i].style.display != 'none'){
				return false;
			}
		}
		return true;
	}

    function changeTab(e) {  
		var region = YAHOOAmp.util.Dom.getRegion("moreTabs");
		var xPos = region.left;
		var yPos = region.bottom;
		allTabsPanel.moveTo(xPos,yPos);
		allTabsPanel.show();
    }

	function mouseLeaves (element, evt) {
		if (typeof evt.toElement != 'undefined' && typeof element.contains != 'undefined') {
			return !element.contains(evt.toElement);
		}
		else if (typeof evt.relatedTarget != 'undefined' && evt.relatedTarget) {
			return !containsElement(element, evt.relatedTarget);
		}
	}
	
	function containsElement(container, containee) {
		while (containee) {
			if (container == containee) {
				return true;
			}
			containee = containee.parentNode;
		}
		return false;
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
	var framediv  = document.getElementById('ajaxcontentarea');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings}";
		framediv.style.height=637;
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings}";
		framediv.style.height=700;
	}
}

	function teamWorkspaceSetup(a) {
		if(navigator.appName.indexOf('Microsoft Internet Explorer') > -1){
	 	 	var referLink = document.createElement('a');
 	 	 	referLink.href = "/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";
 	 	 	document.body.appendChild(referLink);
	 	 	referLink.click();
 	 	 } else {
 	 		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";
 	 	 }
	}
	
	var tabName	= "Tab-By Project";
	<logic:empty name="currentTabReport" scope="page">
		<logic:notEmpty name="defaultTeamReport" scope="session">
				tabName	= 'Tab-${defaultTeamReport.ampReportId}';
		</logic:notEmpty>

		<logic:empty name="defaultTeamReport" scope="session">
			<logic:notEmpty name="myTabs" scope="session">
					tabName	= 'Tab-${myTabs[0].ampReportId}';
			</logic:notEmpty>
		</logic:empty>
	</logic:empty>
	<logic:notEmpty name="currentTabReport" scope="page">
		tabName	= 'Tab-<bean:write name="currentTabReport" scope="page" property="ampReportId"/>';
	</logic:notEmpty>	
</script>
	
<digi:context name="digiContext" property="context" />



<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp">
	<jsp:param name="is_a_tab" value="true" />
</jsp:include>

<div id="content"  > 
<div id="demo" class="yui-navset" style="font-family:Arial, Helvetica, sans-serif;font-size:10px;">

<ul id="MyTabs" class="yui-nav"">
<c:set var="counter" value="0"/> 
<logic:present name="myTabs" scope="session">
		<logic:iterate name="myTabs" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
					<logic:equal name="report" property="drilldownTab" value="true">
	                    <c:set var="counter" value="${counter+1}" />
	                    <%-- <c:set var="reportNameTrn">
		                    <digi:trn key="aim:clickreport:tabs:${report.nameTrn}">${report.name}</digi:trn>
	                    </c:set> --%>
	                   
	                    <c:if test="${counter <= 6}">
	                    	<li>
	                    	<c:if test="${fn:length(report.name) > 25}" >
								<a class="tab-trigger" id='Tab-${report.ampReportId}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${report.name}" />'><div><c:out value="${fn:substring(report.name, 0, 25)}" />...</div></a>
	                    	</c:if>
	                    	<c:if test="${fn:length(report.name) <= 25}" >
								<a class="tab-trigger" id='Tab-${report.ampReportId}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${report.name}" />'><div><c:out value="${report.name}" /></div></a>
	                    	</c:if>
	                                                                
	                    </li>
	                    </c:if>
					</logic:equal>
		</logic:iterate>
</logic:present>
	
</ul>									
<div class="yui-content" style="display:none">
</div>
</div>
<div id="ajaxcontentarea" class="contentstyle" style="border:1px solid #D0D0D0; height:637px; overflow:auto; font-size:12px;">
	<digi:trn key="aim:addATab">
		 Click on one of the tabs to display activities. You can add more tabs by using the Tab Manager.
	</digi:trn>
</div>
</div> 

<c:set var="loadstatustext">
	<digi:trn key="aim:loadstatustext">Requesting Content</digi:trn>
</c:set>
<script type="text/javascript">
	loadstatustext='<div align="center" style="font-size: 11px;margin-top:260px;"><img src="/TEMPLATE/ampTemplate/img_2/ajax-loader.gif"/><p><%=((String) pageContext.getAttribute("loadstatustext")).replaceAll("\r\n"," ")%><span id="statusValue">...</span></p><div>';
	//Start Ajax tabs script for UL with id="maintab" Separate multiple ids each with a comma.
	startajaxtabs("MyTabs");
	if(document.getElementById(tabName)){
		checkstatus(true);
		reloadTab("MyTabs",tabName);
	}
</script>

<style>
DIV.panelList {
	cursor:pointer;
	padding:2px 2px 2px 2px;
	color:black;
	background-color:white;
}
</style>
<div id="debug"></div>
<div id="allTabs" style="display: none;" onmouseout="if (mouseLeaves(this, event)) {allTabsPanel.hide();}">
	<logic:present name="myActiveTabs" scope="session">
    	<div id="scrollableDiv" style="width:100%;height:200px;overflow:auto;">
        <c:set var="showMoreTab" value="false"/>
		<logic:iterate name="myActiveTabs" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports">
          	<c:set var="showTab" value="true"/>
			<logic:iterate name="myTabs" id="tab" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
            	<c:if test="${tab.id == report.id}">
                	<c:set var="showTab" value="false"/>
                </c:if>
            </logic:iterate>
			<c:if test="${showTab}">
			        <c:set var="showMoreTab" value="true"/>
					<logic:equal name="report" property="drilldownTab" value="true">
	                   <%--  <c:set var="reportNameTrn"> 
		                    <digi:trn key="aim:clickreport:tabs:${report.nameTrn}">${report.name}</digi:trn>
	                    </c:set> --%>
	                   <c:if test="${fn:length(report.name) > 25}" >
							<div href="#" class="panelList" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${fn:escapeXml(report.name)}" />", "<c:out value="${fn:escapeXml(fn:substring(report.name, 0, 25))}" />", "Tab-<c:out value="${report.ampReportId}" />");' title="<c:out value="${report.name}" />" id="<c:out value="${report.name}" />"><c:out value="${fn:substring(report.name, 0, 25)}" />...</div>
	                    </c:if>
	                    <c:if test="${fn:length(report.name) <= 25}" >
							<div href="#" class="panelList" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${fn:escapeXml(report.name)}" />", "<c:out value="${fn:escapeXml(report.name)}" />", "Tab-<c:out value="${report.ampReportId}" />");'  title="<c:out value="${report.name}" />" id="<c:out value="${report.name}" />"><c:out value="${report.name}" /></div>
	                    </c:if>
					</logic:equal>
            </c:if>
		</logic:iterate>
        </div>
	</logic:present>
</div>
<c:if test="${showMoreTab}">
<script language="javascript">
	YAHOOAmp.util.Event.addListener(window, "load", initAllTabs);
</script>
</c:if>

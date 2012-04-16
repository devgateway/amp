<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<logic:notEmpty name="filterCurrentReport" scope="session">
    <logic:equal name="filterCurrentReport" property="drilldownTab" value="false">
    	<c:remove var="filterCurrentReport" scope="session" />
	</logic:equal>
</logic:notEmpty>

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

<c:set var="showCurrSettings">
	<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> 
</c:set>
<c:set var="hideCurrSettings">
	<digi:trn key="rep:hideCurrSettings">Hide current settings</digi:trn> 
</c:set>


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
		objeto.innerHTML = "<digi:trn key="aim:moretabs">More Tabs...</digi:trn>";
		objeto.title = "<digi:trn key="aim:clickforalltabs">Click to see all tabs</digi:trn>";;
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
		allTabsPanel = new YAHOOAmp.widget.Panel("allTabsPanel1", {xy:[xPos,yPos], width:"320px", height:"225px", visible:false, constraintoviewport:true }  );
		allTabsPanel.setHeader("<digi:trn key="aim:pleaseselect">Please select from the list below</digi:trn>");
		allTabsPanel.setBody("");
		allTabsPanel.render(document.body);
		var divAllTabs = document.getElementById("allTabs");
		divAllTabs.style.display 	= "block";
		allTabsPanel.setBody(divAllTabs);
		<logic:notEmpty name="filterCurrentReport" scope="session">
		if(!tabExists('Tab-<c:out value="${filterCurrentReport.ampReportId}"/>'))
			setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="filterCurrentReport" property="ampReportId"/>~widget=true", "<c:out value="${filterCurrentReport.name}" />", "<c:out value="${fn:substring(filterCurrentReport.name, 0, 25)}" />", "Tab-<c:out value="${filterCurrentReport.ampReportId}" />");
		</logic:notEmpty>	
	}
	function tabExists(tabCheckName){
		if (document.getElementById(tabCheckName)) return true;
		return false;
	}

	function setNewTab(url, label, labelComplete, id){
		//Replace the tab with the new selection
		if(replaceableTabObject != null)
		{
			myTabsObject.removeTab(replaceableTabObject);
		}

		var objeto = document.createElement("DIV");
	
		objeto.innerHTML = label;
		objeto.id = "replaceableTab";

		myTabsObject.addTab( new YAHOOAmp.widget.Tab({ 
			labelEl: objeto
			
		}), myTabsObject.get('tabs').length-1); 
		var tabObject = document.getElementById("replaceableTab");

		if(tabObject.parentNode.tagName == "A")
		{
			tabObject.parentNode.title = labelComplete;
			tabObject.parentNode.rel= "ajaxcontentarea" 
			tabObject.parentNode.href = url;
			tabObject.parentNode.id = id;
		}
		var len = document.getElementById("scrollableDiv").getElementsByTagName("DIV").length;
		for (i = 0; i < len; i++) 
			{	
			document.getElementById("scrollableDiv").getElementsByTagName("DIV")[i].style.display='';
			}
		document.getElementById(label).style.display = 'none';
		replaceableTabObject = myTabsObject.getTab(myTabsObject.get('tabs').length-2);
		allTabsPanel.hide();
		startajaxtabs("MyTabs");
		reloadTab("MyTabs",id);
		hideMoreTabs();
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
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings} &gt;&gt;";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings} &lt;&lt;";
	}
}

	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	
	var tabName	= "Tab-By Project";
	<logic:empty name="filterCurrentReport" scope="session">
		<logic:notEmpty name="defaultTeamReport" scope="session">
				tabName	= 'Tab-${defaultTeamReport.ampReportId}';
		</logic:notEmpty>

		<logic:empty name="defaultTeamReport" scope="session">
			<logic:notEmpty name="myTabs" scope="session">
					tabName	= 'Tab-${myTabs[0].ampReportId}';
			</logic:notEmpty>
		</logic:empty>
	</logic:empty>
	<logic:notEmpty name="filterCurrentReport" scope="session">
		tabName	= 'Tab-<bean:write name="filterCurrentReport" scope="session" property="ampReportId"/>';
	</logic:notEmpty>	
</script>
	
<digi:context name="digiContext" property="context" />



<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>
<br />
<div id="content"  class="yui-skin-sam" style="padding-left:10px;width:98%;min-width:680px;"> 
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
							<a id='Tab-${report.ampReportId}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${report.name}" />'><div><c:out value="${fn:substring(report.name, 0, 25)}" />...</div></a>
	                    </c:if>
	                    <c:if test="${fn:length(report.name) <= 25}" >
							<a id='Tab-${report.ampReportId}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="ajaxcontentarea" title='<c:out value="${report.name}" />'><div><c:out value="${report.name}" /></div></a>
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
							<div href="#" class="panelList" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${report.name}" />", "<c:out value="${fn:substring(report.name, 0, 25)}" />", "Tab-<c:out value="${report.ampReportId}" />");' title="<c:out value="${report.name}" />" id="<c:out value="${report.name}" />"><c:out value="${fn:substring(report.name, 0, 25)}" />...</div>
	                    </c:if>
	                    <c:if test="${fn:length(report.name) <= 25}" >
							<div href="#" class="panelList" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${report.name}" />", "<c:out value="${report.name}" />", "Tab-<c:out value="${report.ampReportId}" />");'  title="<c:out value="${report.name}" />" id="<c:out value="${report.name}" />"><c:out value="${report.name}" /></div>
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

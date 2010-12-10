<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<jsp:include page="/repository/aim/view/ar/reportsScripts.jsp"/>

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
	
	function tabExists(tabCheckName){
		if (document.getElementById(tabCheckName)) {
			return true;
		}
		return false;
	}

	function setNewTab(url, label, labelComplete, id){
		var index = $('#demo').tabs("length");
		//Remove the fake tab before to add a new one
		$("#MoreTabs").parent().remove();
		
		$("#demo").tabs( "select" ,-1);
		$(".ui-tabs-selected").removeClass("ui-state-active").removeClass("ui-tabs-selected");

		$("#replaceableTab").remove();
		$("#demo").tabs("add",url,label,index);
		$("#replaceableTab > a").attr('id', 'Tab-'+label);
		
		$("#MyTabs").append("<li class='desktop_tab ui-state-default ui-corner-top'><a id='MoreTabs' class='tab_link'><digi:trn key="aim:moretabs">More Tabs</digi:trn></></li>");
		MoreTabClickEvent();
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
 function toggleSettings(){
	var currentDisplaySettings = document.getElementById('currentDisplaySettings');
	var displaySettingsButton = document.getElementById('displaySettingsButton');
	if(currentDisplaySettings.style.display == "block"){
		currentDisplaySettings.style.display = "none";
		displaySettingsButton.innerHTML = "${showCurrSettings}";
	}
	else
	{
		currentDisplaySettings.style.display = "block";
		displaySettingsButton.innerHTML = "${hideCurrSettings}";
	}
}

	function teamWorkspaceSetup(a) {
		window.location.href="/aim/workspaceOverview.do~tId="+a+"~dest=teamLead";	
	}
	
	var tabName	= "Tab-By Project";
	<logic:empty name="filterCurrentReport" scope="session">
		<logic:notEmpty name="defaultTeamReport" scope="session">
				tabName	= 'Tab-${defaultTeamReport.name}';
		</logic:notEmpty>
		
		<logic:empty name="defaultTeamReport" scope="session">
			<logic:notEmpty name="myTabs" scope="session">
					tabName	= 'Tab-${myTabs[0].name}';
			</logic:notEmpty>
		</logic:empty>
	</logic:empty>
	<logic:notEmpty name="filterCurrentReport" scope="session">
		tabName	= 'Tab-<bean:write name="filterCurrentReport" scope="session" property="name"/>';
	</logic:notEmpty>	
</script>

<digi:context name="digiContext" property="context" />

<div id="content"> 
<div id="demo" >
	<ul id="MyTabs" class="desktop_tab_base">
	<c:set var="counter" value="0"/> 
		<logic:present name="myTabs" scope="session">
			<logic:iterate name="myTabs" id="report" scope="session" type="org.digijava.module.aim.dbentity.AmpReports"> 
				<logic:equal name="report" property="drilldownTab" value="true">
	            	<c:set var="counter" value="${counter+1}" />
	            	<c:if test="${counter <= 6}">
	                	<li class="desktop_tab">
	                    	<c:if test="${fn:length(report.name) > 25}" >
	                    		<a class="tab_link" id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="Tab_Name" title= "Tab Name"><c:out value="${fn:substring(report.name, 0, 25)}"/></a>
	                    	</c:if>
			                <c:if test="${fn:length(report.name) <= 25}" >
			                	<a class="tab_link" id='Tab-${report.name}' href="/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true" rel="Tab_Name" title= "Tab Name"><c:out value="${report.name}"/></a> 
			                </c:if>
	                    </li>
					</c:if>
				</logic:equal>
			</logic:iterate>
		</logic:present>
	</ul>
	<div  style="display:none">
	</div>
	<div id="Tab_Name">
		<digi:trn key="aim:addATab">
			Click on one of the tabs to display activities. You can add more tabs by using the Tab Manager.
		</digi:trn>
	</div>
</div>
</div>
 
<div id="debug"></div>
<div id="allTabs" style="display: none;width: auto;position: absolute;">
	<div id="scrollableDiv" style="width:250px;height:100px;overflow:auto;">
	<ul>
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
                   	<c:if test="${fn:length(report.name) > 25}" >
						<li style="list-style: none;background-color:#F2F2F2;border-color:#D0D0D0 #D0D0D0 #FFFFFF;border-style:solid solid none;border-width:1px 1px medium;"> 
							<a class="tab_link" style="cursor: pointer;" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${report.name}" />", "<c:out value="${fn:substring(report.name, 0, 25)}" />", "Tab-<c:out value="${report.name}" />");' id="<c:out value="${report.name}" />"><c:out value="${fn:substring(report.name, 0, 25)}" />...</a>
						</li>
                   	</c:if>
                   	<c:if test="${fn:length(report.name) <= 25}" >
						<li style="list-style: none;background-color:#F2F2F2;border-color:#D0D0D0 #D0D0D0 #FFFFFF;border-style:solid solid none;border-width:1px 1px medium;"> 
							<a class="tab_link" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${report.name}" />", "<c:out value="${report.name}" />", "Tab-<c:out value="${report.name}" />");'  id="<c:out value="${report.name}" />"><c:out value="${report.name}" /></a>
						</li>
                   	</c:if>
					</logic:equal>
				</c:if>
		</logic:iterate>
	</ul>
	</div>
</div>
<script language="javascript">

	$(document).ready(function() {
		$("#demo").tabs();
		$("#demo").tabs("option", "tabTemplate", '<li id="replaceableTab" class="desktop_tab"><a class="tab_link" href="\#{href}" rel="Tab_Name" title= "Tab Name">\#{label}</a></li>' );
		
		<c:if test="${showMoreTab}">
			$("#MyTabs").append("<li class='desktop_tab ui-state-default ui-corner-top'><a id='MoreTabs' class='tab_link'><digi:trn key="aim:moretabs">More Tabs</digi:trn></></a></li>");
			MoreTabClickEvent();
		</c:if>
		
		<logic:notEmpty name="filterCurrentReport" scope="session">
		
		if(!tabExists('Tab-<c:out value="${filterCurrentReport.name}"/>'))
			setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="filterCurrentReport" property="ampReportId"/>~widget=true", "<c:out value="${filterCurrentReport.name}" />", "<c:out value="${fn:substring(filterCurrentReport.name, 0, 25)}" />", "Tab-<c:out value="${filterCurrentReport.name}" />");
		</logic:notEmpty>	
		
	});
	
	$("#demo").tabs({
		   load: function(event, ui) { 
			   try
				{	var reporTable=new scrollableTable("reportTable",400);
						reporTable.debug=false;
						reporTable.usePercentage=false;
						reporTable.maxRowDepth=4;
						reporTable.useFixForDisplayNoneRows=true;
						reporTable.scroll();
						continueExecution = false;
				}catch(e)
				{
					//alert(e);
				}
			}
		});
		
	$("#demo").tabs({
		   select: function(event, ui) {
			 $('#Tab_Name').html(loadstatustext);
		   }
		});
	
	var $tabs = $("#demo").tabs({
	    add: function(event, ui) {
	        $tabs.tabs('select',$("#demo").tabs("length")-1);
	    }
	});
	var MoreTabClickEvent = function() {
		$('#MoreTabs').click(function() {
			showMenu();
		});
	}
	
	 $("#allTabs").mouseleave(function(){
		 $('#allTabs').toggle('slow', function() {
			  });
	    })
	
	var showMenu = function(ev) {
	  //get the position of the placeholder element
	  var pos = $("#MoreTabs").offset();  
	  var width = $("#MoreTabs").width();
	  //show the menu directly over the placeholder
	  $('#allTabs').css( { "left": (pos.left+(width/2)) + "px", "top":pos.top + "px" } );
	  $('#allTabs').toggle('slow', function() {
		  });
	}
	 
	
	</script>


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
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>


<digi:errors />


<digi:instance property="crDocTabManagerForm" />
<bean:define id="myForm" toScope="request" name="crDocTabManagerForm" />

<%@include file="../documentManagerJsHelper.jsp" %>

<script type="text/javascript">
	var replaceableTabObject = null;
	function setNewTab(tabName,filterId){
		//Replace the tab with the new selection
		if(replaceableTabObject != null)
		{
			myTabsObject.removeTab(replaceableTabObject);
		}
		var labelDiv = document.createElement("DIV");
		labelDiv.id	 		= "labelDivId";
		labelDiv.innerHTML	= tabName;

		myTabsObject.addTab( new YAHOO.widget.Tab({ 
			labelEl: labelDiv,
			content: "<div id='replaceableDiv' class='resource_box'><div  style='padding-left:10px' id='replaceableFilterInfoDiv'></div><div id='replaceableDiv_markup' align='left'></div></div>",
			active: true
		}), myTabsObject.get('tabs').length-1); 
		
		//var tabObject = document.getElementById("replaceableTab");
		
		replaceableTabObject = myTabsObject.getTab(myTabsObject.get('tabs').length-2);
		allTabsPanel.hide();
		replaceableListObj.containerEl		= document.getElementById("replaceableDiv_markup");
		replaceableListObj.filterInfoDivId	= "replaceableFilterInfoDiv";
		YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/publicDocTabManager.do?action=jsonfilter&filterId='+filterId, 
				new RetrieveFilters(replaceableListObj) );
		
	}

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


	
	function initTabs( ) {
		scrollableDivStrips("#dbe5f1","#ffffff","#a5bcf2");

		
		myTabsObject 	= new YAHOO.widget.TabView("demo");
		<logic:notEmpty name="myForm" property="publicFiltersUnpositioned">
			var tabs		= myTabsObject.get("tabs");
			var moreTab		= myTabsObject.getTab(tabs.length-1);
			moreTab.set("disabled", true);
		</logic:notEmpty>
	
		var region = YAHOO.util.Dom.getRegion("moreTabsTab");
		var xPos = region.left;
		var yPos = region.bottom;
		allTabsPanel = new YAHOO.widget.Panel("allTabsPanel1", {xy:[xPos,yPos], width:"320px", height:"225px", visible:false, constraintoviewport:true }  );
		allTabsPanel.setHeader("Please select from the list below");
		allTabsPanel.setBody("");
		allTabsPanel.render(document.body);
		var divAllTabs = document.getElementById("allTabs");
		divAllTabs.style.display 	= "block";
		allTabsPanel.setBody(divAllTabs);

	}
	
	function afterPageLoad(e) {
		initTabs();
		publicListObj			= new PublicDynamicList(document.getElementById("allPublicResources_markup"), "publicListObj",null);
		publicListObj.sendRequest();
		<c:forEach var="filter" items="${myForm.publicFiltersPositioned}">
			public${filter.id}ListObj	= new PublicDynamicList(document.getElementById("${filter.id}_markup"), "public${filter.id}ListObj",null);
			//if(public${filter.id}ListObj.setKeywordTextboxInformationPublic)
				public${filter.id}ListObj.setKeywordTextboxInformationPublic("tabSearchStr${filter.id}","tabSearchButtonId${filter.id}","${filter.id}","public${filter.id}ListObj");
			public${filter.id}ListObj.filterInfoDivId	= "FilterInfoDiv${filter.id}";
			YAHOO.util.Connect.asyncRequest('GET', '/contentrepository/publicDocTabManager.do?time='+ new Date().getTime()+'&action=jsonfilter&filterId=${filter.id}', 
					new RetrieveFilters(public${filter.id}ListObj) );
		</c:forEach>
		
		replaceableListObj			= new PublicDynamicList(document.getElementById("replaceableDiv_markup"), "replaceableListObj",null);
	}

YAHOO.util.Event.on(window, "load", afterPageLoad); 

</script>

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

.resource_box {
    background-color: #FFFFFF;
    border: 1px solid #CCCCCC;
    font-size: 12px;
    padding: 10px;
}
</style>


			
			<table border="0" cellspacing="0" cellpadding="0" width="1000" align="center">
			<tr><td>
			<div style="width:1000px;" class="yui-skin-sam" id="content">
				<div id="demo" class="yui-navset">			
				<ul class="yui-nav">
					<li id="allPublicResourcesTab" class="selected"><a href="#allPublicResourcesRef"><div><digi:trn>All Public Documents</digi:trn></div></a>
					<c:forEach var="filter" items="${myForm.publicFiltersPositioned}">
						<li id="Tab${filter.id}"><a href="#Ref${filter.id}"><div>${filter.name}</div></a> </li>
					</c:forEach>
					<logic:notEmpty name="myForm" property="publicFiltersUnpositioned">
						<li id="moreTabsTab"><a><div onclick="allTabsPanel.show()"><digi:trn>More Tabs</digi:trn>...</div></a></li>
					</logic:notEmpty>
			    </ul>            
			    <div class="yui-content" style="border-color: #d0d0d0">
			    	<div id="allPublicResourcesRef">
			    		<div id="allPublicResources_markup" align="left"></div>
			    	</div>
			    	<c:forEach var="filter" items="${myForm.publicFiltersPositioned}">
			    		<div id="Ref${filter.id}">
			    			<input type="text" id="tabSearchStr${filter.id}">
			        		<button id="tabSearchButtonId${filter.id}" type="button" class="buttonx button_resources">
				        		<digi:trn>Search</digi:trn>
				        	</button>
			    			<br />
			    			<br />
			    			<div id="FilterInfoDiv${filter.id}" style="padding-left: 10px;"> </div>
			    			<div id="${filter.id}_markup" align="left"></div>
			    		</div>
			    	</c:forEach>
			    	
					<!--End public Resources-->
				</div>
			</div>
			</div>
			
			
		</td></tr></table>
		

<br/>

<%-- TODO-CONSTANTIN: where does this "report" come from? if it is ReportContextData.generatedReport, where is the current context coming from?  --%>
<div id="allTabs" style="display: none;" onmouseout="if (mouseLeaves(this, event)) {allTabsPanel.hide();}">
    <div id="scrollableDiv" style="width:100%;height:200px;overflow:auto;">
		<logic:iterate name="myForm" property="publicFiltersUnpositioned" id="filter">
          
			<c:if test="${fn:length(report.name) > 25}" >
				<div href="#" class="panelList" onclick='setNewTab("/aim/viewNewAdvancedReport.do~view=reset~viewFormat=foldable~ampReportId=<bean:write name="report" property="ampReportId"/>~widget=true", "<c:out value="${report.name}" />", "<c:out value="${fn:substring(report.name, 0, 25)}" />", "Tab-<c:out value="${report.name}" />");' title="<c:out value="${report.name}" />" id="<c:out value="${report.name}" />"><c:out value="${fn:substring(report.name, 0, 25)}" />...</div>
			</c:if>
			<c:if test="${fn:length(report.name) <= 25}" >
				<div href="#" onclick="setNewTab('${filter.name}', ${filter.id})" class="panelList" id="Div${filter.id}"> ${filter.name}</div>
			</c:if>
					
		</logic:iterate>
	</div>	
</div>

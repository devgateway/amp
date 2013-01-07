<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>

<style type="text/css">

.clear,.innerTab {
	clear: both;
}

div#filterContainer {
	width: 740px;
	min-height: 350px;
	margin: 0 auto;
	background: #fff;
}

div.filterContainerHeader {
	background: #e9e9fb;
	border-bottom: 1px solid #bfd4e0;
	height: 26px;
}

div.filterContainerHeader h2 {
	line-height: 26px;
	font-size: 12px;
	font-weight: bold;
	text-indent: 11px;
	width: 90%;
	float: left;
}



div.filterContainerTabs {
	width: 740px;
	margin: 0 auto;
	margin-top: 11px;
}

div.groupingSelector {
	width: 311px;
	float: left;
	border-bottom: 1px solid #cccccc;
	border-right: none;
	height: 155px;
}

.grouped-paragraphs {
	background-color: F5F5F5;
	border: 1px dotted black;
	margin-bottom: 1.1em;
	margin-right: 1.5em;
}

.grouped-paragraphs p{
	margin-bottom: 0.4em !important;
	padding-bottom: 5px !important;
	font-weight: bold;
}

div.membersSelector {
	width: 425px;
	float: right;
	border-left: 1px solid #cccccc;
	border-right: 1px solid #cccccc;
	border-bottom: 1px solid #cccccc;
	height: 155px;
}

div.innerTabHeader {
	border-bottom: 1px solid #cccccc;
	background: url(/TEMPLATE/ampTemplate/img_2/ins_header.gif);
	height: 31px;
}

div.innerTabHeader h3 {
	height: 31px;
	line-height: 31px;
	text-indent: 11px;
	float: left;
	font-size: 11px;
	color: #767676;
	margin: 0;
	padding: 0;
}

div.membersSelector div.memsearch {
	padding: 1px 0px 0px 0px;
	width: 230px;
	float: right;
	text-align: right;
}



.innerTabHeader input.buttonx {
	font-size: 11px;
}

.groupingSelector ul {
	margin: 0 auto;
	margin-bottom: 15px;
	width: 250px;
}

.groupingSelector ul li {
	list-style: none;
}

.groupingSelector ul li a {
	padding: 5px;
	font-size: 12px;
	display: block;
	background: #fff;
	color: #000;
	text-decoration: none;
}

.groupingSelector ul li a:hover {
	background: #bfd2df;
}


div.innerTab {
	height: 115px;
	overflow: auto;
	margin: 2px;
	width: 99%;
}


.otherCriteriaFoofet {
	padding: 15px 0px 0px 0px;
	clear: both;
}

.otherCriteriaFoofet #calendarForm label {
	width: 100px;
	font-size: 11px;
	display: inline-block;
}

p {
	padding: 0px 0px 10px 0px;
}

.otherCriteriaFoofet select {
	background-color: #FFFFFF;
	border: 1px solid #D0D0D0;
	color: #000;
	font-size: 11px;
	padding: 0px;
	width: 150px;
}


.tabSubmit {
	text-align: center;
	padding: 0px 0px 5px 0px;
}

.groupingSelectors li a span {
	display: inline-block;
	float: right;
	font-size: 11px;
}
.groupingSelector,  .groupingSelectors{
	margin: 0;
	padding: 0;
}


label {
	text-indent: 10px;
}
li table{
	visibility:visible; 
}

</style>
<script type="text/javascript">
	var tabView = new YAHOO.widget.TabView('demo');
</script>

<script type="text/javascript">

/*	function showOnlyWorkspaceClicked()
	{
		var checked = $("#curWorkspaceOnlyCB").attr('checked');
		if (checked)
			$('#child_workspaces_input').show();
		else
			$('#child_workspaces_input').hide();
	}
	
	$(document).ready(function()
	{
		$("#curWorkspaceOnlyCB").change(function()
		{
			showOnlyWorkspaceClicked();
		});
		
		showOnlyWorkspaceClicked(); //set visibility of the "also show children" checkbox
	}); */
</script>


<digi:instance property="gisDashboardForm" />
<%--
<html:hidden property="defaultStartYear"/>
<html:hidden property="defaultEndYear"/>
<html:hidden property="defaultCalendar"/>
<html:hidden property="defaultCurrency"/>
<bean:define id="sy" name="gisDashboardForm" property="startYears"
	type="int[]"></bean:define>
<bean:define id="ey" name="gisDashboardForm" property="endYears"
	type="int[]"></bean:define>
 --%>
<bean:define id="form" name="gisDashboardForm" type="org.digijava.module.gis.form.GisDashboardForm"></bean:define>
<bean:define id="reqBeanSetterObject" toScope="request"	name="gisDashboardForm" />
<div id="filterContainer"  class="yui-skin-sam" style="display: none;">
	<form id="gisFilterForm" action="../../gis/getFoundingDetails.do"	>
		<div class="filterContainerTabs">
			<div id="demo" class="yui-navset">
				<table width="100%" border="0" cellpadding="0" cellspacing="0"><tr><td>
				<ul class="yui-nav" style="float:bottom;">
					<li class="selected">
						<a href="#tab1" style="border-radius:0;"><div><digi:trn>Donor Agencies</digi:trn></div></a>
					</li>
					<li>
						<a href="#tab2" style="border-radius:0;"><div><digi:trn>Sectors</digi:trn></div></a>
					</li>
					<li>
						<a href="#tab3" style="border-radius:0;"><div><digi:trn>Programs</digi:trn></div></a>
					</li>
				</ul>
				</td></tr>
				<tr><td>
				<div class="yui-content" id="gisfilters" style="background-color: #FFFFFF; float:right;">
					<div>
						<div id="tabDonorAgency">
							<bean:define name="gisDashboardForm" id="element" property="donorElements" toScope="request" />
							<table width="100%" height="100%" cellpadding="0" cellspacing="0"><tr><td>
							<div class="groupingSelector">
								<div class="innerTabHeader">
									<h3>
										<digi:trn>Grouping Selector</digi:trn>
									</h3>
								</div>
								<span class="clear"></span>
								<div class="innerTab">
									<ul class="groupingSelectors">
										<c:forEach var="element" items="${gisDashboardForm.donorElements}">
											<li><a href="#" onclick="showFilterDiv('${element.htmlDivId}','donorTab_search');return false;"><digi:trn>${element.name}</digi:trn>
													(${element.rootHierarchyListable.countDescendants-1}) </a></li>
										</c:forEach>
									</ul>
								</div>
							</div>
						</td><td>
							<div class="membersSelector">
								<div class="innerTabHeader">
									<h3><digi:trn>Members selector</digi:trn></h3>
									 <div class="memsearch">
										<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="donorTab_search" type="text"   class="inputx" />
										 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('donorTab_search').findPrev()"  value='&lt;&lt;' />
										 <input type="button" onclick="getSearchManagerInstanceById('donorTab_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
									</div>
								</div>
								<c:forEach var="element" items="${gisDashboardForm.donorElements}">
									<div class="innerTab" style="display: none;" id="${element.htmlDivId}">
										<bean:define id="reqEntityList" name="element" property="rootHierarchyListable.children" toScope="request" />
										<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
										<ul style="list-style-type: none;margin: 0; padding: 0;">
											<li><input type="checkbox"	onclick="toggleCheckChildren(this)" class="root_checkbox" />
												<span style="font-family: Arial; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
											</span> <jsp:include
													page="/repository/aim/view/filters/hierarchyLister.jsp" />
											</li>
										</ul>
									</div>
								</c:forEach>
							</div>
						</td></tr></table>
						</div>
						<%--
							<div class="datesCurrenceyFormFooter">
						<div id="calendarForm">
							<div style="width: 40%; float: left;">
								<p>
									<label><digi:trn>Calendar</digi:trn>
									</label>
									<html:select property="selectedCalendar"
										styleId="selectedCalendar">
										<logic:notEmpty name="gisDashboardForm" property="calendars">
											<html:optionsCollection property="calendars"
												value="ampFiscalCalId" label="name" />
										</logic:notEmpty>
									</html:select>
								</p>
								<p>
									<label>Currency type</label>
									<html:select property="selectedCurrency"
										name="gisDashboardForm" styleId="selectedCurrency">
										<logic:notEmpty name="gisDashboardForm"
											property="currencyTypes">
											<html:optionsCollection name="gisDashboardForm"
												property="currencyTypes" value="currencyCode"
												label="currencyName" />
										</logic:notEmpty>
									</html:select>
								</p>
							</div>
							<div style="width: 60%; float: left;">
								<p>
									<label><digi:trn>Start Year</digi:trn> </label>
									<%
											String selected = "";
										%>
									<html:select property="selectedStartYear"
										styleId="selectedStartYear">
										<%
												for (int i = 0; i < sy.length; i++) {
											%>
										<%
												selected = (sy[i] == form.getSelectedStartYear()) ? "selected='selected'"
																: "";
											%>
										<option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
										<%
												}
											%>
									</html:select>
								</p>
								<p>
									<label><digi:trn>End Year</digi:trn> </label>
									<html:select property="selectedEndYear"
										styleId="selectedEndYear">
										<%
												for (int i = 0; i < sy.length; i++) {
											%>
										<%
												selected = (sy[i] == form.getSelectedEndYear()) ? "selected='selected'"
																: "";
											%>
										<option value='<%=sy[i]%>' <%=selected%>><%=sy[i]%></option>
										<%
												}
											%>
									</html:select>
								</p>
	
	
							</div>
							<div class="clear"></div>
							<div class="tabSubmit">
								<input type="button" class="buttonx" 
									value="<digi:trn>Apply Filters to the Report</digi:trn>" onclick="submitFilters();"/>
								<input type="button"
									class="buttonx" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
							</div>
						</div>
						</div>
						 --%>
						
						<span class="clear"></span>
					</div>
					<div>
						<div>
							<bean:define name="gisDashboardForm" id="element" property="sectorElements" toScope="request" />
							<div class="groupingSelector">
								<div class="innerTabHeader">
									<h3>
										<digi:trn>Grouping Selector</digi:trn>
									</h3>
								</div>
								<span class="clear"></span>
								<div class="innerTab">
									<ul class="groupingSelectors">
										<c:forEach var="element" items="${gisDashboardForm.sectorElements}">
											<li><a  href="#" onclick="showFilterDiv('${element.htmlDivId}','sectorTab_search');return false;"><digi:trn>${element.name}</digi:trn>
													(${element.rootHierarchyListable.countDescendants-1}) </a>
											</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<div class="membersSelector">
							<div class="innerTabHeader">
									<h3><digi:trn>Members selector</digi:trn></h3>
									 <div class="memsearch">
										<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="sectorTab_search" type="text"   class="inputx" />
										 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('sectorTab_search').findPrev()"  value='&lt;&lt;' />
										 <input type="button" onclick="getSearchManagerInstanceById('sectorTab_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
										 </div>
								</div>
								<c:forEach var="element" items="${gisDashboardForm.sectorElements}">
									<div class="innerTab" style="display: none;" id="${element.htmlDivId}">
										<bean:define id="reqEntityList" name="element" property="rootHierarchyListable.children" toScope="request" />
										<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
										<ul style="list-style-type: none;margin: 0; padding: 0;">
											<li><input type="checkbox" onclick="toggleCheckChildren(this)" class="root_checkbox" />
												<span style="font-family: Arial; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
											</span> <jsp:include page="/repository/aim/view/filters/hierarchyLister.jsp" />
											</li>
										</ul>
									</div>
								</c:forEach>
							</div>
							<%--
								<div class="clear"></div>
							<div class="tabSubmit">
								<input type="button" class="buttonx"
									value="<digi:trn>Apply Filters to the Report</digi:trn>"
									onclick="submitFilters();" />
									<input type="button"
									class="buttonx" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
							</div>
							 --%>
						</div>
						<span class="clear"></span>
	
					</div>
	
					<div>
					<div>
							<bean:define name="gisDashboardForm" id="element" property="programElements" toScope="request" />
							<div class="groupingSelector">
								<div class="innerTabHeader">
									<h3>
										<digi:trn>Grouping Selector</digi:trn>
									</h3>
								</div>
								<span class="clear"></span>
								<div class="innerTab">
									<ul class="groupingSelectors">
										<c:forEach var="element"
											items="${gisDashboardForm.programElements}">
											<li><a href="#" onclick="showFilterDiv('${element.htmlDivId}','programTab_search');return false;"><digi:trn>${element.name}</digi:trn>
													(${element.rootHierarchyListable.countDescendants-1}) </a>
											</li>
										</c:forEach>
									</ul>
								</div>
							</div>
							<div class="membersSelector">
									<div class="innerTabHeader">
									<h3><digi:trn>Members selector</digi:trn></h3>
									 <div class="memsearch">
										<input onkeypress="getSearchManagerInstanceByEl(this).clear()" id="programTab_search" type="text"   class="inputx" />
										 <input type="button" class="buttonx" onclick="getSearchManagerInstanceById('programTab_search').findPrev()"  value='&lt;&lt;' />
										 <input type="button" onclick="getSearchManagerInstanceById('programTab_search').findNext()"  class="buttonx" value="&gt;&gt;"/>
										 </div>
								</div>
								<c:forEach var="element"
									items="${gisDashboardForm.programElements}">
									<div class="innerTab" style="display: none;"
										id="${element.htmlDivId}">
										<bean:define id="reqEntityList" name="element"
											property="rootHierarchyListable.children" toScope="request" />
										<bean:define id="reqSelectedEntityIds" toScope="request">${element.actionFormProperty}</bean:define>
										<ul style="list-style-type: none;margin: 0; padding: 0;">
											<li><input type="checkbox"
												onclick="toggleCheckChildren(this)" class="root_checkbox" />
												<span style="font-family: Arial; font-size: 12px;"> <digi:trn>${element.rootHierarchyListable.label}</digi:trn>
											</span> <jsp:include
													page="/repository/aim/view/filters/hierarchyLister.jsp" />
											</li>
										</ul>
									</div>
								</c:forEach>
							</div>
						</div>
						<span class="clear"></span>
						
						
						
					</div>
					<div class="otherCriteriaFoofet">
						<div id="calendarForm" style="width:100%">
						
						<div style="width: 40%; float: left;">
								<p>
									<label><digi:trn>Calendar</digi:trn>
									</label>
									<html:select property="selectedCalendar" styleId="selectedCalendar">
										<logic:notEmpty name="gisDashboardForm" property="calendars">
											<html:optionsCollection property="calendars"
												value="ampFiscalCalId" label="name" />
										</logic:notEmpty>
									</html:select>
								</p>
								<p>
									<label><digi:trn>Currency type</digi:trn></label>
									<html:select property="selectedCurrency" name="gisDashboardForm" styleId="selectedCurrency">
										<logic:notEmpty name="gisDashboardForm"
											property="currencies">
											<html:optionsCollection name="gisDashboardForm"
												property="currencies" value="currencyCode"
												label="currencyName" />
										</logic:notEmpty>
									</html:select>
								</p>
								<p>
									<label><digi:trn>Map Level</digi:trn> </label>
									<select id="mapLevel" name="mapLevel">
						            	<option value="2"><digi:trn>Regional</digi:trn></option>
						            	<option value="3"><digi:trn>District</digi:trn></option>
						        	</select>
								</p>
							</div>
							<div style="width: 60%; float: left;">
								<field:display name="Source of Data" feature="GIS DASHBOARD">
									<p>
										<label><digi:trn>Source Of Data</digi:trn> </label>
										<select id="mapModeFin" name="mapModeFin" value="commitment">
							            	<option value="fundingData"><digi:trn>Activity Funding Data</digi:trn></option>
							            	<option value="pledgesData"><digi:trn>Pledges Data</digi:trn></option>
							        	</select>
									</p>
								</field:display>
								<p>
									<label><digi:trn>Funding type</digi:trn> </label>
									<select id="fundingType" name="fundingType" value="commitment" class="fundType">
							            <field:display name="Measure Commitment" feature="GIS DASHBOARD">
							            	<option value="commitment"><digi:trn>Commitment</digi:trn></option>
							            </field:display>
							            <field:display name="Measure Disbursement" feature="GIS DASHBOARD">
							            	<option value="disbursement"><digi:trn>Disbursement</digi:trn></option>
							            </field:display>
							            <field:display name="Measure Expenditure" feature="GIS DASHBOARD">
							            	<option value="expenditure"><digi:trn>Expenditure</digi:trn></option>
							            </field:display>
						        	</select>
								</p>
								
								<field:display name="Show Type of assistance" feature="GIS DASHBOARD">
									<p>
										<label><digi:trn>Type of assistance</digi:trn> </label>
										<select name="selectedTypeOfAssistance" class="selectedCurrency">
											<option value="-1" selected><digi:trn>All</digi:trn></option>
											<logic:notEmpty name="gisDashboardForm" property="typeOfAssistanceVals">
												<logic:iterate name="gisDashboardForm" property="typeOfAssistanceVals" id="typeOfAssistanceItem">
												<option value="<bean:write name="typeOfAssistanceItem" property="id"/>"><bean:write name="typeOfAssistanceItem" property="value"/></option>
												</logic:iterate>
											</logic:notEmpty>
										</select>
									</p>
								</field:display>
								
								<c:if test="${empty param.public}">
									<div class="grouped-paragraphs">
										<p>
											<label style="width:230px"><digi:trn>Show data for selected workspace and children only</digi:trn></label>
											&nbsp;&nbsp;<input id="curWorkspaceOnlyCB" type="checkbox" name="curWorkspaceOnly">
										</p>
										<!-- <p id="child_workspaces_input">
											<label style="width:200px; padding-left: 30px;"><digi:trn>Also include child workspaces</digi:trn></label>
											&nbsp;&nbsp;<input type="checkbox" name="childWorkspacesToo">
										</p>
										-->
									</div>
								</c:if>
							</div>				 
							
							<div class="clear"></div>
							<div class="tabSubmit">
								<input type="button" class="buttonx" 
									value="<digi:trn>Apply Filters</digi:trn>" onclick="applySectorFilter(this);"/>
								<input type="button"
									class="buttonx" value="<digi:trn>Reset and Start Over</digi:trn>" onclick="resetPIFilters();"/>
							</div>
						</div>
						</div>
				</div>
			</td></tr></table>
			</div>
		</div>
		<input type="hidden" name="filterStartYear" id="filterStartYear">
		<input type="hidden" name="filterEndYear" id="filterEndYear">
		<input type="hidden" name="filterAllSectors" id="filterAllSectors">
	</form>
</div>



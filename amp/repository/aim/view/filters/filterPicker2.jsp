<%@page import="org.digijava.module.aim.util.time.StopWatch"%>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

<%@page import="org.digijava.module.aim.util.FeaturesUtil"%>
<%@page import="org.digijava.module.aim.dbentity.AmpGlobalSettings"%>
<%@page import="java.util.Collections"%>
<%@page import="org.dgfoundation.amp.ar.ArConstants"%>
<%@ page import="org.dgfoundation.amp.ar.AmpARFilter"%>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />
<link type="text/css" href="css_2/tabs.css" rel="stylesheet" />
<link href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/yui_datatable.css" rel="stylesheet" type="text/css"></link>
<link href="/TEMPLATE/ampTemplate/css_2/desktop_yui_tabs.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/tabview/assets/tabview-core.css"> 
<link href="/TEMPLATE/ampTemplate/css_2/yui_popins.css" rel="stylesheet" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css"> 
<digi:instance property="aimReportsFilterPickerForm" />
<digi:form action="/reportsFilterPicker.do" style="height: 100%">
<bean:define id="reqBeanSetterObject" toScope="request" name="aimReportsFilterPickerForm"/>

<% 
StopWatch.next("Filters", true);
%>

<html:hidden property="text"/>
<html:hidden property="sourceIsReportWizard"/>
<div id="tabview_container" class="yui-navset" style="display: block; overflow: hidden; height: 80%; padding-bottom: 0px;margin-top: 15px;margin-left: 5px;margin-right: 5px">
	<ul class="yui-nav" style="border-bottom: 1px solid #CCCCCC">
		<li class="selected"><a href="#donorsTab"><div><digi:trn>Donor Agencies</digi:trn></div></a></li>
		<logic:notEqual name="is_pledge_report" value="true" scope="request">
			<li><a href="#relAgenciesTab"><div><digi:trn>Related Agencies</digi:trn></div></a></li>
		</logic:notEqual>
		<li><a href="#sectorsTab"><div><digi:trn>Sectors</digi:trn></div></a></li>
		<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
			<li><a href="#programsTab"><div><digi:trn>Programs</digi:trn></div></a></li>
		</module:display>
		<li><a href="#financingLocTab"><div><digi:trn>Financing & Location</digi:trn></div></a></li>
		<li><a href="#otherCriteriaTab"><div><digi:trn>Other Criteria</digi:trn></div></a></li>
	</ul>
	<div class="yui-content" style="background-color: #f6faff; height: 92%;margin-top: 10px;background: white;" >
		<div id="donorsTab" class="clrfix" style="height: 91%;">
			<div class="grayBorder clrfix">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="donorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="donorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="donorsTab_search" />
				<%@include file="bigFilterTable.jsp" %>
			</div>
		</div>
		<logic:notEqual name="is_pledge_report" value="true" scope="request">
            <div id="relAgenciesTab" class="yui-hidden clrfix" style="height: 91%;">
                <div class="grayBorder clrfix">
					<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="relatedAgenciesElements" />
					<bean:define id="reqPropertyObj" toScope="request" value="relAgenciesPropertyObj" />
					<bean:define id="reqSearchManagerId" toScope="request" value="relAgenciesTab_search" />
					<%@include file="bigFilterTable.jsp" %>
				</div>
			</div>
		</logic:notEqual>
		<div id="sectorsTab" class="yui-hidden clrfix"  style="height: 91%;">
			<div class="grayBorder clrfix">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="sectorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="sectorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="sectorsTab_search" />
				<%@include file="bigFilterTable.jsp" %>
			</div>
		</div>
		<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
			<div id="programsTab" class="yui-hidden clrfix"  style="height: 91%;" >
				<div class="grayBorder clrfix">
					<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="programElements" />
					<bean:define id="reqPropertyObj" toScope="request" value="programsPropertyObj" />
					<bean:define id="reqSearchManagerId" toScope="request" value="programsTab_search" />
					<%@include file="bigFilterTable.jsp" %>
				</div>
			</div>
		</module:display>
		<div id="financingLocTab" class="yui-hidden clrfix"  style="height: 91%;" >
			<div class="grayBorder clrfix" style="width: 95%; float: left;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="financingLocationElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="financingLocPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="financingLocTab_search" />
				<bean:define id="reqSearchFieldWidth" toScope="request" value="80px" />
				<jsp:include page="bigFilterTable.jsp"/>
				<bean:define id="reqSearchFieldWidth" toScope="request" value="" />
			</div>
		</div>
		<div id="otherCriteriaTab" class="yui-hidden clrfix"  style="height: 91%;">
			<div class="grayBorder clrfix">
				<logic:notEqual name="is_pledge_report" value="true" scope="request">
					<c:set var="reqSelectorHeaderSize" scope="request" value="13" />
					<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="otherCriteriaElements" />
					<bean:define id="reqPropertyObj" toScope="request" value="otherCriteriaPropertyObj" />
					<bean:define id="reqSearchManagerId" toScope="request" value="otherCriteriaTab_search" />
					<div class="otherCriteriaBigTable">
						<%@include file="bigFilterTable.jsp" %>
					</div>
					<c:set var="reqSelectorHeaderSize" scope="request" value="" />
				</logic:notEqual>
				<div style="width: 55%; height:30%; padding: 10px; float: left;">
					<b><digi:trn>Date Filter</digi:trn> </b>
					
					<div style="margin-top:7px;">
					<table style="font-family: Arial; font-size: 1em;">
						<tr>
							<td align="left">
							<digi:trn key="rep:filer:From"> From </digi:trn> <html:text  property="fromDate" size="10" styleId="fromDate" styleClass="inp-text" readonly="true" />
							<a id="date1" style="background-color: #F6FAFF;" href='javascript:pickDateById("date1","fromDate")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
							</a>
							<a id="clearDate1" style="background-color: #F6FAFF;" href='javascript:clearDate("fromDate")' title="<digi:trn>Clear</digi:trn>">
								<img src="/TEMPLATE/ampTemplate/images/trash_16.gif" alt="Clear" border="0">
							</a>
							</td>
							<td align="left">&nbsp;&nbsp;
							<digi:trn key="rep:filer:To"> To </digi:trn> <html:text  property="toDate" size="10" styleId="toDate" styleClass="inp-text" readonly="true" />
							<a id="date2" style="background-color: #F6FAFF;" href='javascript:pickDateById("date2","toDate")'>
								<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
							</a>
							<a id="clearDate2" style="background-color: #F6FAFF;" href='javascript:clearDate("toDate")' title="<digi:trn>Clear</digi:trn>">
								<img src="/TEMPLATE/ampTemplate/images/trash_16.gif" border="0">
							</a>
							</td>
						</tr>
					</table></div>
				</div>
				<%-- See details in https://jira.dgfoundation.org/browse/AMP-14926. --%>
				<feature:display name="Actual Approval Year" module="Filter Section">
					<logic:notEqual name="is_pledge_report" value="true" scope="request">
						<div style="width: 17%; height: 30%; padding: 10px; float: left;">
							<span style="white-space: nowrap"><b><digi:trn>Actual Approval Year</digi:trn> </b></span>
							<div style="margin-top:10px;">
								<html:select property="actualAppYear"  style="width: 100px" styleClass="inp-text">
									<option value="-1"><digi:trn key="aim:filters:actualAppYear">Year</digi:trn></option>
	                       			<html:optionsCollection property="actualAppYearsRange" label="wrappedInstance" value="wrappedInstance" />
								</html:select>
							</div>
						</div>
					</logic:notEqual>
				</feature:display>
				<feature:display name="Computed Columns Filters" module="Filter Section">
					<div style="margin-left: 80%;width: 20%; height: 30%; padding: 10px; ">
						<span style="white-space: nowrap"><b><digi:trn>Computed Columns</digi:trn> </b></span>
							<div style="margin-top:10px;">
							<html:select property="computedYear"  style="width: 100px" styleClass="inp-text">
								<option value="-1"><digi:trn key="aim:filters:currentYear">Current Year</digi:trn></option>
	                         	<html:optionsCollection property="computedYearsRange" label="wrappedInstance" value="wrappedInstance" />
							</html:select>
							</div>
					</div>
				</feature:display>
			</div>
		</div>
	</div>
</div>
<logic:notEqual name="is_pledge_report" value="true" scope="request">
<div class="clrfix" style="height: 15%;">
	<div style="width:60%; float:left; font-size: 12px;text-align: center;">
			<c:set var="tooltip_translation">
				<digi:trn>Specify keywords to look for in the project data.</digi:trn>
			</c:set>
			<digi:trn>Keyword Search</digi:trn>
			<img onmouseout="UnTip()" onmouseover="Tip('${tooltip_translation}')" height="15px" 
				src="/TEMPLATE/ampTemplate/images/info.png" alt="Click to View Calendar" border="0" />
				<html:text property="indexString" style="width: 150px"	styleClass="inp-text"  />
				&nbsp;
				<html:select property="searchMode" styleClass="inp-text" style="width: 150px;">
					<option value="0"><digi:trn>Any keyword</digi:trn></option>
					<option value="1"><digi:trn>All keywords</digi:trn></option>
				</html:select>
				
	</div>
	<div style="display: block;width:40%; float:left; font-size: 12px">
		<html:checkbox property="justSearch" value="true" />&nbsp;
		<digi:trn>Use filter as advanced search</digi:trn>
	</div>
	
	<div style="clear:both;text-align:center;padding:2px 0px 0px 0px;margin-top: 20px;">
					<html:hidden property="ampReportId" />
	
					<html:hidden property="defaultCurrency" />
					<input class="buttonx_sm" id="filterPickerSubmitButton" name="apply" type="button" onclick="text.value='';submitFilters()"
					value="<digi:trn key='rep:filer:ApplyFiltersToReport'>Apply Filters</digi:trn>" /> 
					<html:button onclick="resetFilter();" styleClass="buttonx_sm" property="reset" styleId="filterPickerResetButton">
						<digi:trn key="rep:filer:ResetAndStartOver">Reset and Start Over</digi:trn>
					</html:button>
    </div>
</div>
</logic:notEqual>
<%AmpARFilter arf = (AmpARFilter) session.getAttribute("ReportsFilter");%>
<%if ((arf != null) && (arf.isPublicView()==false)){%>
	<c:if test="${aimReportsFilterPickerForm.reporttype eq '5'}">
		<div style="display: block; overflow:hidden;width:40%; float:left; font-size: 12px">
			<html:checkbox property="workspaceonly" styleId="workspace_only"/>&nbsp;
			<digi:trn>Show Only Activities From This Workspace</digi:trn>
		</div>
	</c:if>
<%} %>

<html:hidden property="workspaceonly" styleId="workspaceOnly"/>
</digi:form>

<% 
StopWatch.next("Filters", true);
%>

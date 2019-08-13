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
<%@page import="org.dgfoundation.amp.ar.ReportContextData"%>

<%
	pageContext.setAttribute("reportCD", ReportContextData.getFromRequest());
%>

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
	StopWatch.next("Filters", true, "begin rendering filterPicker2");
%>

<html:hidden property="text"/>
<html:hidden property="sourceIsReportWizard"/>
<div id="tabview_container" class="content-direction yui-navset" style="display: block; overflow: hidden;
height: 80%; padding-bottom: 0px;margin-top: 15px;margin-left: 5px;margin-right: 5px">
	<ul class="yui-nav" style="border-bottom: 1px solid #CCCCCC">
		<li class="selected"><a href="#donorsTab"><div><digi:trn>Donor Agencies</digi:trn></div></a></li>
		<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
			<logic:notEmpty name="aimReportsFilterPickerForm" property="relatedAgenciesElements">
				<li><a href="#relAgenciesTab"><div><digi:trn>Related Agencies</digi:trn></div></a></li>
			</logic:notEmpty>
		</logic:notEqual>
		<li><a href="#sectorsTab"><div><digi:trn>Sectors</digi:trn></div></a></li>
		<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
			<li><a href="#programsTab"><div><digi:trn>Programs</digi:trn></div></a></li>
		</module:display>
		<li><a href="#financingLocTab"><div><digi:trn>Financing & Location</digi:trn></div></a></li>
		<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
			<li><a href="#otherCriteriaTab"><div><digi:trn>Other Criteria</digi:trn></div></a></li>
		</logic:notEqual>
	</ul>
	<div class="yui-content" style="background-color: #f6faff; height: 92%;margin-top: 10px;background: white;" >
	<% 
		StopWatch.next("Filters", true, "donor tab begin");
	%>
		<div id="donorsTab" class="clrfix" style="height: 91%;">
			<div class="grayBorder clrfix">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="donorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="donorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="donorsTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
	<% 
		StopWatch.next("Filters", true, "related agencies tab begin");
	%>
        <logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
        	<logic:notEmpty name="aimReportsFilterPickerForm" property="relatedAgenciesElements">
            	<div id="relAgenciesTab" class="yui-hidden clrfix" style="height: 91%;">
                	<div class="grayBorder clrfix">
                    	<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="relatedAgenciesElements" />
                    	<bean:define id="reqPropertyObj" toScope="request" value="relAgenciesPropertyObj" />
                    	<bean:define id="reqSearchManagerId" toScope="request" value="relAgenciesTab_search" />
						<jsp:include page="bigFilterTable.jsp"/>
                	</div>
            	</div>
        	</logic:notEmpty>    
        </logic:notEqual>
	<% 
		StopWatch.next("Filters", true, "sectors tab begin");
	%>		
		<div id="sectorsTab" class="yui-hidden clrfix"  style="height: 91%;">
			<div class="grayBorder clrfix">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="sectorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="sectorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="sectorsTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
		<module:display name="National Planning Dashboard" parentModule="NATIONAL PLAN DASHBOARD">
		<% 
			StopWatch.next("Filters", true, "programs tab begin");
		%>
			<div id="programsTab" class="yui-hidden clrfix"  style="height: 91%;" >
				<div class="grayBorder clrfix">
					<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="programElements" />
					<bean:define id="reqPropertyObj" toScope="request" value="programsPropertyObj" />
					<bean:define id="reqSearchManagerId" toScope="request" value="programsTab_search" />
					<jsp:include page="bigFilterTable.jsp"/>
				</div>
			</div>
		<% 
			StopWatch.next("Filters", true, "programs tab end");
		%>			
		</module:display>
		<% 
			StopWatch.next("Filters", true, "financing location tab begin");
		%>		
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
		<% 
			StopWatch.next("Filters", true, "financing location tab end");
		%>				
		<div id="otherCriteriaTab" class="yui-hidden clrfix"  style="height: 91%;">
			<div class="grayBorder clrfix">
				<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
				<c:set var="reqSelectorHeaderSize" scope="request" value="13" />
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="otherCriteriaElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="otherCriteriaPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="otherCriteriaTab_search" />
		<% 
			StopWatch.next("Filters", true, "other criteria tab begin");
		%>						
		
		<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
			<div class="otherCriteriaBigTable">
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</logic:notEqual>
		
		<% 
			StopWatch.next("Filters", true, "other criteria tab end");
		%>						
				<c:set var="reqSelectorHeaderSize" scope="request" value="" />
				</logic:notEqual>
				<%-- See details in https://jira.dgfoundation.org/browse/AMP-14926. --%>
				<feature:display name="Actual Approval Year" module="Filter Section">
				<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
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
<div class="clrfix content-direction" style="height: 15%;">
<logic:notEqual name="aimReportsFilterPickerForm" property="pledgeReport" value="true">
	<div class="panel-one" style="width:60%; font-size: 12px;text-align: center;">
		<c:set var="tooltip_translation">
			<digi:trn>Specify keywords to look for in the project data.</digi:trn>
		</c:set>
		<digi:trn>Keyword Search</digi:trn>

        <c:set var="trnAnyKeyword">
            <digi:trn>Any keyword</digi:trn>
        </c:set>

        <c:set var="trnAnyKeywords">
            <digi:trn>All keywords</digi:trn>
        </c:set>

            <span>
                <img onmouseout="UnTip()" onmouseover='Tip("${tooltip_translation}")' height="15px"
                    src="/TEMPLATE/ampTemplate/images/info.png" alt="Click to View Calendar" border="0" />
            </span>
			<html:text property="indexString" style="width: 150px"	styleClass="inp-text"  />
			&nbsp;
			<html:select property="searchMode" styleClass="inp-text" style="width: 150px;">
				<option value="0"><c:out value="${trnAnyKeyword}" /></option>
				<option value="1"><c:out value="${trnAnyKeywords}" /></option>
			</html:select>
			
	</div>
</logic:notEqual>
<div class="filter-just-search">
	<html:checkbox property="justSearch" value="true" />&nbsp;
	<digi:trn>Use filter as advanced search</digi:trn>
</div>

<div style="clear:both;text-align:center;padding:2px 0px 0px 0px;margin-top: 20px;height: 15%;">
				<input type="hidden" name="ampReportId" value="${reportCD.ampReportId}" />
				<input type="hidden" name="reportContextId" value="${reportCD.contextId}" />

				<html:hidden property="defaultCurrency" />
				<!-- notice that in case of queryEngine filters, this file (included by queryEngine.jsp) has its submit button action overrided by submitQuery() -->
				<input class="buttonx_sm" id="filterPickerSubmitButton" name="apply" type="button" onclick="text.value='';submitFilters('<%=ReportContextData.getCurrentReportContextId(request, true)%>');"
					value="<digi:trn key='rep:filer:ApplyFiltersToReport'>Apply Filters</digi:trn>" />

                <input class="buttonx_sm" id="filterPickerResetButton" type="button" name="reset" onclick="resetFilter('${reportCD.contextId}')"
                       value="<digi:trn key='rep:filer:ResetAndStartOver'>Reset and Start Over</digi:trn>" />

</div>
</div>

<html:hidden property="workspaceonly" styleId="workspaceOnly"/>
</digi:form>

<% 
StopWatch.next("Filters", true, "end rendering filter picker2");
%>

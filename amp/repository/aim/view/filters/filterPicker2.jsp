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

<%-- <bean:define id="reportMeta" name="reportMeta"
	type="org.digijava.module.aim.dbentity.AmpReports" scope="session"
	toScope="page" /> --%>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />

<digi:instance property="aimReportsFilterPickerForm" />

<digi:form action="/reportsFilterPicker.do">


<html:hidden property="text"/>
<html:hidden property="sourceIsReportWizard"/>
<div id="tabview_container" class="yui-navset" style="display: block; overflow: hidden; height: 88%;">
	<ul class="yui-nav" style="height: 10%">
		<li class="selected"><a href="#donorsTab"><div><digi:trn>Donor Agencies</digi:trn></div></a> </li>
		<li><a href="#relAgenciesTab"><div><digi:trn>Related Agencies</digi:trn></div></a> </li>
		<li><a href="#sectorsTab"><div><digi:trn>Sectors</digi:trn></div></a> </li>
		<li><a href="#programsTab"><div><digi:trn>Programs</digi:trn></div></a> </li>
		<li><a href="#financingLocTab"><div><digi:trn>Financing & Location</digi:trn></div></a> </li>
		<li><a href="#otherCriteriaTab"><div><digi:trn>Other Criteria</digi:trn></div></a> </li>
	</ul>
	<div class="yui-content" style="background-color: #f6faff; height: 88%;">
		<div id="donorsTab" style="height: 91%;">
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="donorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="donorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="donorsTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
		<div id="relAgenciesTab" class="yui-hidden" style="height: 91%;">
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="relatedAgenciesElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="relAgenciesPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="relAgenciesTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
		<div id="sectorsTab" class="yui-hidden"  style="height: 91%;">
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="sectorElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="sectorsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="sectorsTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
		<div id="programsTab" class="yui-hidden"  style="height: 91%;" >
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="programElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="programsPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="programsTab_search" />
				<jsp:include page="bigFilterTable.jsp"/>
			</div>
		</div>
		<div id="financingLocTab" class="yui-hidden"  style="height: 91%;" >
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="financingLocationElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="financingLocPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="financingLocTab_search" />
				<bean:define id="reqSearchFieldWidth" toScope="request" value="90px" />
				<div style="width: 70%; height: 100%; padding: 0px; float: left;">
					<jsp:include page="bigFilterTable.jsp"/>
				</div>
				<bean:define id="reqSearchFieldWidth" toScope="request" value="" />
				<div style="width: 30%; margin-left: 70%; height: 100%;">
					<field:display name="Joint Criteria" feature="Budget">
							<html:checkbox property="jointCriteria" value="true" /> &nbsp;
							<digi:trn>Display Only Projects Under Joint Criteria.</digi:trn>
					</field:display>
					<br />
					<field:display name="Government Approval Procedures" feature="Budget">
						<html:checkbox property="governmentApprovalProcedures" value="true" />&nbsp;<digi:trn
							key="rep:filter:govAppProcCheck"> Display Only Projects Having Government Approval Procedures. </digi:trn>
					</field:display>
				</div>
			</div>
		</div>
		<div id="otherCriteriaTab" class="yui-hidden"  style="height: 91%;">
			<div style="margin: 10px; padding: 8px; border: 1px solid #e5e8e6; height: 100%;">
				<bean:define id="reqElements" toScope="request" name="aimReportsFilterPickerForm" property="otherCriteriaElements" />
				<bean:define id="reqPropertyObj" toScope="request" value="otherCriteriaPropertyObj" />
				<bean:define id="reqSearchManagerId" toScope="request" value="otherCriteriaTab_search" />
				<div style="padding: 0px; height: 70%;">
					<jsp:include page="bigFilterTable.jsp"/>
				</div>
				<div style="width: 45%; height: 30%; padding: 10px; float: left; ">
					<c:set var="tooltip_translation">
						<digi:trn>Specify keywords to look for in the project data.</digi:trn>
					</c:set>
					<b><digi:trn>Keyword Search</digi:trn> </b>
					<img onmouseout="UnTip()" onmouseover="Tip('${tooltip_translation}')" height="15px" 
						src="/TEMPLATE/ampTemplate/imagesSource/common/info.png" alt="Click to View Calendar" border="0" />
						<br />
						<br />
						<html:text property="indexString" style="width: 250px"	styleClass="inp-text"  />
				</div>
				<div style="margin-left: 50%; width: 45%; height:30%; padding: 10px;">
					<b><digi:trn>Date Filter</digi:trn> </b>
					<br />
					<table style="font-family: Arial; font-size: 1em;">
						<tr>
							<td align="left" colspan="2">
								<digi:trn key="rep:filer:From"> From </digi:trn>
							</td>
							<td align="left" colspan="2">
								<digi:trn key="rep:filer:To"> To </digi:trn>
							</td>
						</tr>
						<tr bgcolor="#F6FAFF">
							<td colspan="2" align="left">
							<html:text  property="fromDate" size="10" styleId="fromDate" styleClass="inp-text" readonly="true" />
							<a id="date1" style="background-color: #F6FAFF;" href='javascript:pickDateById("date1","fromDate")'>
								<img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
							</a>
							</td>
							
							
							<td colspan="2" align="left">
							<html:text  property="toDate" size="10" styleId="toDate" styleClass="inp-text" readonly="true" />
							<a id="date2" style="background-color: #F6FAFF;" href='javascript:pickDateById("date2","toDate")'>
								<img src="/TEMPLATE/ampTemplate/imagesSource/calendar/show-calendar.gif" alt="Click to View Calendar" border=0>
							</a>
							</td>
						</tr>
					</table>
				</div>
			</div>
		</div>
	</div>
</div>
<div style="background-color: #f6faff; display: block; padding: 3px; overflow:hidden; height: 7%; ">
	<table width="100%" style="height: 100%;">
		<tr>
			<td width="30%">&nbsp;</td>
			<td width="40%" align="center">
			<html:hidden property="ampReportId" />
			<html:hidden property="defaultCurrency" />
			<input class="dr-menu" id="filterPickerSubmitButton" name="apply" type="button" onclick="text.value='';submitFilters()"
				value="<digi:trn key='rep:filer:ApplyFiltersToReport'>Apply Filters</digi:trn>" /> 
			<html:button onclick="resetFilter();" styleClass="dr-menu"
				property="reset">
				<digi:trn key="rep:filer:ResetAndStartOver">Reset and Start Over</digi:trn>
			</html:button> </td>
			<td width="30%">
				<html:checkbox property="justSearch" value="true" />&nbsp;
				<digi:trn>Use filter as advanced search</digi:trn>
			</td>
		</tr>
	</table>
</div>

</digi:form>

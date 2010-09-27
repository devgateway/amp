<%@ page pageEncoding="UTF-8"%>

<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/struts-nested" prefix="nested"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<%@ taglib uri="/taglib/fmt" prefix="fmt"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<%@ page import="java.util.*"%>
<%@ page import="org.digijava.module.aim.dbentity.*"%>
<%@ page import="org.digijava.module.aim.helper.*"%>
<%@ page import="org.digijava.module.categorymanager.util.*"%>
<%@ page import="org.digijava.module.categorymanager.dbentity.*"%>
<%@ page import="org.digijava.module.parisindicator.util.*"%>

<link rel="stylesheet" href="<digi:file src="/repository/parisindicator/view/css/pi_styles.css"/>">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/parisindicator/script/pi_scripts.js"/>"></script>

<digi:errors />

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<%String reportId = request.getParameter("reportId");%>
<digi:form action="/parisindicator.do" type="org.digijava.module.parisindicator.form.PIForm" name="parisIndicatorForm">
    
    <jsp:include page="viewParisIndicatorPopupFilter.jsp" />
    <jsp:include page="viewParisIndicatorPopupScripts.jsp" />
    
    <html:hidden property="selectedEndYear"/>
    <html:hidden property="selectedStartYear"/>
    <html:hidden property="selectedCalendar"/>
    <html:hidden property="selectedCurrency"/>
    <html:hidden property="selectedDonors"/>
    <html:hidden property="selectedDonorGroups"/>
    <html:hidden property="selectedStatuses"/>
    <html:hidden property="selectedSectors"/>
    <html:hidden property="selectedFinancingIstruments"/>
    
    <html:hidden property="reset" value="false"/>
    <html:hidden property="printPreview" value="false"/>
	<html:hidden property="exportPDF" value="false"/>
	<html:hidden property="exportXLS" value="false"/>
    <html:hidden property="reportId" value="<%=reportId%>"/>
    
	<table border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="980px" align="left" valign="top" border="1" style="padding-left: 5px;">
				<table width="100%" border="0" cellpadding="5" cellspacing="0">
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<div style="margin-left: 5px; margin-right: 5px; background-color: #ccdbff; padding: 2px 2px 2px 2px; Font-size: 8pt; font-family: Arial;">
								<span style="cursor: pointer; font-style: italic; float: right;" onClick="toggleSettings();" id="displaySettingsButton"><digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> &gt;&gt;</span>
								<span style="cursor: pointer; float: left;"> 
								    <a class="settingsLink" onClick="showFilter(); "><digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn></a>
								</span>
								<br>
								<div style="display: none; background-color: #FFFFCC; padding: 2px 2px 2px 2px;" id="currentDisplaySettings">
									<table border="0" cellspacing="0" cellpadding="0" width="100%">
                                        <tr>
                                            <td style="font-size:11px;" valign="top">
                                            <strong><digi:trn key="rep:pop:SelectedFilters">Selected Filters</digi:trn>:</strong>
                                                <i><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year</digi:trn>:</i>&nbsp;<bean:write name="parisIndicatorForm" property="selectedStartYear"/>&nbsp;&nbsp;|
                                                <i><digi:trn key="rep:pop:SelectedRangeEndYear">End Year</digi:trn>:</i>&nbsp;<bean:write name="parisIndicatorForm" property="selectedEndYear"/>&nbsp;&nbsp;|
                                                <i><digi:trn key="rep:pop:CalendarType">Calendar Type</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedCalendar == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedCalendar != null}">
                                                        <digi:trn><bean:write name="parisIndicatorForm" property="selectedCalendar"/></digi:trn>&nbsp;|
                                                    </c:if>
                                                <i><digi:trn key="rep:pop:CurrencyType">Currency Type</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency != null}">
                                                        <digi:trn><bean:write name="parisIndicatorForm" property="selectedCurrency"/></digi:trn>&nbsp;|
                                                    </c:if>
                                                <i><digi:trn key="rep:pop:Donors">Donors</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedDonors == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedDonors != null}">
                                                        <logic:iterate id="idDonors" property="selectedDonors" name="parisIndicatorForm">
                                                            <%=org.digijava.module.aim.util.DbUtil.getOrganisation(new Long(idDonors.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <i><digi:trn key="rep:pop:DonorGroups">Donor Groups</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedDonorGroups == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedDonorGroups != null}">
                                                        <logic:iterate id="idDonorsGrp" property="selectedDonorGroups" name="parisIndicatorForm">
                                                            <%=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(new Long(idDonorsGrp.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <i><digi:trn>Status</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedStatuses == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedStatuses != null}">
                                                        <logic:iterate id="idStatus" property="selectedStatuses" name="parisIndicatorForm">
                                                            <%=org.digijava.module.categorymanager.util.CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(idStatus.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <i><digi:trn>Financing Instrument</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedFinancingIstruments == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedFinancingIstruments != null}">
                                                        <logic:iterate id="idFunding" property="selectedFinancingIstruments" name="parisIndicatorForm">
                                                            <%=org.digijava.module.categorymanager.util.CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(idFunding.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>                                              
                                                <i><digi:trn>Sectors</digi:trn>:</i>
                                                    <c:if test="${parisIndicatorForm.selectedSectors == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedSectors != null}">
                                                        <logic:iterate id="idSector" property="selectedSectors" name="parisIndicatorForm">
                                                            <%=org.digijava.module.aim.util.SectorUtil.getAmpSector(new Long(idSector.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>                                                
                                            </td>
                                        </tr>
                                    </table>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>
							<div class="toolbar" align="center">
								<table border="0" align="center" bgcolor="#addadd" class="toolbartable">
									<tr>
                                        <td noWrap="nowrap" align="left" valign="center">
									       <a onclick="javascript:exportPDFs(); resetExport(); return false;" target="_blank" style="cursor: pointer" title="<digi:trn>Export to PDF</digi:trn>">
									           <digi:img width="17" height="20" hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/module/aim/images/pdf.gif" border="0" />
                                           </a>
                                        </td>
										<td noWrap="nowrap" align="left" valign="center">
									       <a onclick="javascript:exportXLSs(); resetExport(); return false;" paramName="indcId" paramId="indcId" target="_blank" style="cursor: pointer" title="<digi:trn>Export to Excel</digi:trn>">
									           <digi:img width="17" height="20" hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/imagesSource/common/excel.gif" border="0" />
									       </a>
									    </td>
										<td noWrap="nowrap" align="left" valign="center">
										   <a onclick="javascript:openPrinter(); resetExport(); return false;" target="_blank" style="cursor: pointer" title="<digi:trn>Printer Friendly</digi:trn>">
										      <digi:img width="17" height="20" hspace="2" vspace="2" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif" border="0" /> 
										   </a>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="subtitle-blue-3" style="width: 70%; text-align: left;">
                            <digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> <digi:trn key="aim:report">Report</digi:trn>&nbsp;<bean:write name="parisIndicatorForm" property="piReport.indicatorCode"/>
                        </td>
                    </tr>
					<tr>
						<td colspan="3" class="box-title" align="center"></td>
					</tr>
					<tr>
                        <td>
                            <logic:notEmpty name="parisIndicatorForm" property="availablePIReports">
                                <div id="content" class="yui-skin-sam" style="padding-left:10px;width:98%;width:980px;">
                                    <div id="demo" class="yui-navset">
                                        <ul id="MyTabs" class="yui-nav"">
                                            <logic:iterate id="element" name="parisIndicatorForm" property="availablePIReports" indexId="index">
                                                <%String selected = ""; %>
                                                <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="${element.indicatorCode}">
                                                    <%selected = "selected"; %>
                                                </logic:equal>
                                                <LI class="<%=selected%>">
                                                    <span>
                                                        <digi:link href="/parisindicator.do?reportId=${element.indicatorCode}&reset=true&header=true" target="_self">
                                                            <div style="max-width: 80px">
                                                                <digi:trn key="Paris Indicator">Paris Indicator</digi:trn>&nbsp;<bean:write name="element" property="indicatorCode"/>
                                                            </div>
                                                        </digi:link>
                                                    </span>
                                                </LI>
                                            </logic:iterate>
                                        </ul>
                                    </div>
                                </div>
                            </logic:notEmpty>
                        </td>
                    </tr>
                    <tr>
	                    <td width="90%" class="td_top1" style="padding-left:2px;" >
	                        <table border="0" cellpadding="0" cellspacing="0" width="100%">
	                            <tr>
	                                <td colspan="2" ></td>
	                            </tr>
	                            <tr>
	                                <td style="padding:5">
	                                    <strong><p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15">&nbsp;<bean:write name="parisIndicatorForm" property="piReport.name"/></p></strong>
	                                </td>
	                            </tr>
	                        </table>
	                    </td>
	                </tr>
	                <tr>
                        <td class="td_right_left1">
	                       <div id="reportContent">
		                       <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="3">
		                           <jsp:include page="parisindicator_3.jsp"></jsp:include>
		                       </logic:equal>
		                       <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="4">
	                               <jsp:include page="parisindicator_4.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="5a">
	                               <jsp:include page="parisindicator_5a.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="5b">
	                               <jsp:include page="parisindicator_5b.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="6">
	                               <jsp:include page="parisindicator_6.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="7">
	                               <jsp:include page="parisindicator_7.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="9">
	                               <jsp:include page="parisindicator_9.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="10a">
	                               <jsp:include page="parisindicator_10a.jsp"></jsp:include>
	                           </logic:equal>
	                           <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="10b">
	                               <jsp:include page="parisindicator_10b.jsp"></jsp:include>
	                           </logic:equal>
                           </div>
	                    </td>
	                </tr>
					<tr>
						<td class="td_bottom1">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</digi:form>
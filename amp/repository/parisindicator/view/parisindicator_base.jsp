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

<%@ page import="java.util.*"%>
<%@ page import="org.digijava.module.aim.dbentity.*"%>
<%@ page import="org.digijava.module.aim.helper.*"%>
<%@ page import="org.digijava.module.categorymanager.util.*"%>
<%@ page import="org.digijava.module.categorymanager.dbentity.*"%>

<link rel="stylesheet" href="<digi:file src="/repository/parisindicator/view/css/pi_styles.css"/>">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/parisindicator/script/pi_scripts.js"/>"></script>

<digi:errors />

<div id="myFilter2" style="display: none;"></div>
<div id="myFilter" style="display: none;"></div>

<jsp:include page="/repository/aim/view/viewParisIndicatorPopupScripts.jsp" />
<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />
<jsp:include page="/repository/aim/view/saveReports/dynamicSaveReportsAndFilters.jsp" />

<digi:form action="/parisindicator" type="org.digijava.module.parisindicator.form.PIForm" name="parisIndicatorForm">
	<table border="0" cellpadding="10" cellspacing="0" bgcolor="#FFFFFF">
		<tr>
			<td width="1000" align="left" valign="top" border="1" style="padding-left: 5px;">
				<table width="100%" border="0" cellpadding="5" cellspacing="0">
					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td>
							<div style="margin-left: 5px; margin-right: 5px; background-color: #ccdbff; padding: 2px 2px 2px 2px; Font-size: 8pt; font-family: Arial, Helvetica, sans-serif;">
								<span style="cursor: pointer; font-style: italic; float: right;" onClick="toggleSettings();" id="displaySettingsButton"><digi:trn key="rep:showCurrSettings">Show current settings</digi:trn> &lt;&lt;</span>
								<span style="cursor: pointer; float: left;"> 
								    <a class="settingsLink" onClick="showFilter(); "><digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn></a>
								</span>
								<br>
								<div style="display: none; background-color: #FFFFCC; padding: 2px 2px 2px 2px;" id="currentDisplaySettings">
									<table border="0" cellspacing="0" cellpadding="0" width="100%">
                                        <tr>
                                            <td style="font-size:10px;font-family:Arial,Helvetica,sans-serif" valign="top">
                                                <strong><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year:</digi:trn></strong>&nbsp;<bean:write name="parisIndicatorForm" property="selectedStartYear"/>&nbsp;&nbsp;|
                                                <strong><digi:trn key="rep:pop:SelectedRangeEndYear">End Year:</digi:trn></strong>&nbsp;<bean:write name="parisIndicatorForm" property="selectedEndYear"/>&nbsp;&nbsp;|
                                                <strong><digi:trn key="rep:pop:CalendarType">Calendar Type:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedCalendar == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedCalendar != null}">
                                                        <bean:write name="parisIndicatorForm" property="selectedCalendar"/>&nbsp;|
                                                    </c:if>
                                                <strong><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency != null}">
                                                        <bean:write name="parisIndicatorForm" property="selectedCurrency"/>&nbsp;|
                                                    </c:if>
                                                <strong><digi:trn key="rep:pop:Donors">Donors:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedDonors == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedDonors != null}">
                                                        <bean:write name="parisIndicatorForm" property="selectedDonors"/>&nbsp;|
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
									       <a href="" onclick="javascript:popup_pdf(''); return false;" paramName="indcId" paramId="indcId" target="_blank">
									           <digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/pdf.gif" border="0" alt="Export to PDF" />
                                           </a>
                                        </td>
										<td noWrap="nowrap" align="left" valign="center">
									       <a href="" onclick="javascript:popup_xls(''); return false;" paramName="indcId" paramId="indcId" target="_blank">
									           <digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/excel.gif" border="0" alt="Export to Excel" />
									       </a>
									    </td>
										<td noWrap="nowrap" align="left" valign="center">
										   <a href="#" paramName="indcId" paramId="indcId" onclick="javascript:openPrinter(); return false;" paramName="indcId" paramId="indcId" target="_blank">
										      <digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly" /> 
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
                                <div id="content" class="yui-skin-sam" style="padding-left:10px;width:98%;width:1000px;">
                                    <div id="demo" class="yui-navset">
                                        <ul id="MyTabs" class="yui-nav"">
                                            <logic:iterate id="element" name="parisIndicatorForm" property="availablePIReports" indexId="index">
                                                <LI>
                                                    <span>
                                                        <digi:link href="/parisindicator/parisindicator.do" target="_self">
                                                            <div style="max-width: 90px">
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
	                   <td>
	                       contenido
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
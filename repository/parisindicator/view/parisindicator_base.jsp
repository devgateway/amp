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
    
    <div id="myFilter" style="display: none;">
        <div id="tabview_container" class="yui-navset">
        <ul class="yui-nav">
            <li class="selected">
                <a href="#keyword"><div><digi:trn key="aim:filter:tab:Calendar">Calendar</digi:trn></div></a>
            </li>
            <li>
                <a href="#financing"><div><digi:trn key="aim:filter:tab:Financing">Financing & Indicators</digi:trn></div></a> 
            </li>
            <li>
                <a href="#sectorsgroups"><div><digi:trn key="aim:filter:tab:SectorsGroups">Sectors & Groups</digi:trn></div></a>
            </li>
            <li>
                <a href="#donorstatus"><div><digi:trn key="aim:filter:tab:DonorsStatus">Donors & Status</digi:trn></div></a>
            </li>
        </ul>
        <div class="yui-content" style="background-color: #EEEEEE">
            <div id="keyword" >
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <c:set var="tooltip_translation">
                                    <digi:trn key="rep:filter:timePeriod">Specify the time period to limit your search within.</digi:trn>
                                </c:set>
                                <table align="center" cellpadding="2" cellspacing="2" onmouseout="UnTip()" onmouseover="Tip('${tooltip_translation}');">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:CalendarTitle">Calendar</digi:trn></b>
                                        </td>
                                        <td colspan="4">
                                            <html:select property="selectedCalendar" styleClass="dr-menu">
                                                <logic:notEmpty name="parisIndicatorForm" property="calendars">
                                                    <html:optionsCollection property="calendars" value="ampFiscalCalId" label="name"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:StartYear">Start Year</digi:trn></b>&nbsp;
                                        </td>
                                        <td colspan="4">
                                            <select name="auxStartYear">
                                                <option value="2006">2006</option>
                                                <option value="2007">2007</option>
                                                <option value="2008">2008</option>
                                                <option value="2009">2009</option>
                                                <option value="2010">2010</option>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr>
                                        <td colspan="5">
                                            <b><digi:trn key="rep:filter:EndYear">End Year</digi:trn></b>&nbsp;
                                        </td>
                                        <td colspan="4">
                                            <select name="auxEndYear">
                                                <option value="2006">2006</option>
                                                <option value="2007">2007</option>
                                                <option value="2008">2008</option>
                                                <option value="2009">2009</option>
                                                <option value="2010" selected="selected">2010</option>
                                            </select>
                                        </td>
                                    </tr>
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                            <td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
                        </tr>
                </table>
            </div>
            <div id="financing">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">
                                            <b><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></b>
                                        </td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="4">
                                            <html:select property="selectedCurrency" name="parisIndicatorForm" styleClass="dr-menu" >
                                                <logic:notEmpty name="parisIndicatorForm" property="currencyTypes">
                                                    <html:optionsCollection name="parisIndicatorForm" property="currencyTypes" value="currencyCode" label="currencyName"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                    <tr><td>&nbsp;</td></tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn key="rep:filer:financingInstrument">Financing Instrument</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5">

                                        </td>
                                    </tr>
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                            <td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
                        </tr>
                </table>
            </div>
            <div id="sectorsgroups">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn key="rep:pop:sectors">Sectors:</digi:trn></b></td>
                                        <td>&nbsp;</td>
                                        <td colspan="5"><b><digi:trn key="rep:pop:Groups">Groups:</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select  property="selectedSectors" size="8" style="width: 300px" styleClass="inp-text" multiple="true">
                                                <html:option value="All"><digi:trn key="aim:allSectors">All Sectors</digi:trn></html:option>
                                                <html:optionsCollection property="sectors" value="ampSectorId" label="name" />
                                            </html:select>
                                        </td>
                                        <td>&nbsp;</td>                                 
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select property="selectedDonorGroups" name="parisIndicatorForm" styleClass="dr-menu" size="8" multiple="true">
                                                <html:option value="all"><digi:trn key="aim:allGroups">All Groups</digi:trn></html:option>
                                                <logic:notEmpty name="parisIndicatorForm" property="donorGroups">
                                                    <html:optionsCollection name="parisIndicatorForm" property="donorGroups" value="ampOrgGrpId" label="orgGrpName"/>
                                                </logic:notEmpty>
                                            </html:select>
                                        </td>
                                    </tr>
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                            <td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
                        </tr>
                </table>
            </div>
            <div id="donorstatus">
                <br />
                    <table width="100%" style="vertical-align: top;" align="center" cellpadding="7px" cellspacing="7px" >
                        <tr valign="top">
                            <td align="center">
                                <table align="center" cellpadding="2" cellspacing="2">
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5"><b><digi:trn key="rep:pop:Donors">Donors:</digi:trn></b></td>
                                        <td>&nbsp;</td>
                                        <td colspan="5"><b><digi:trn key="rep:pop:StatusType">Status Type:</digi:trn></b></td>
                                    </tr>
                                    <tr bgcolor="#EEEEEE">
                                        <td colspan="5" styleClass="inp-text">
                                            <html:select property="selectedDonors" size="6" style="width: 300px" styleClass="inp-text" multiple="true">
                                                <html:option value="all"><digi:trn key="aim:allDonors">All Donors</digi:trn></html:option>
                                                <html:optionsCollection property="donors" value="ampOrgId" label="acronym" />
                                            </html:select>
                                        </td>
                                        <td>&nbsp;</td>                                 
                                        <td colspan="5" styleClass="inp-text">
                                            <category:showoptions firstLine="All" styleClass="dr-menu" property="selectedStatuses" name="parisIndicatorForm"
                                                keyName="<%=org.digijava.module.categorymanager.util.CategoryConstants.ACTIVITY_STATUS_KEY %>" multiselect="true" size="6"/>
                                        </td>
                                    </tr>
                                </table>
                            </td> 
                        </tr>
                        <tr bgcolor="#EEEEEE">
                            <td colspan="5" styleClass="inp-text">&nbsp;<br/><br/></td>
                        </tr>
                </table>
            </div>
        </div>
    </div>
    <div style="background-color: #EEEEEE; ">
        <br />
        <table width="100%">
            <tr>
                <td align="center" colspan="5">
                    <input type="button" value="<digi:trn key="rep:filer:ApplyFiltersToReport">Apply Filters to the Report</digi:trn>" class="dr-menu" onclick="submitFilters();">
                </td>
            </tr>
        </table>
    </div>
    </div>
        
    <jsp:include page="viewParisIndicatorPopupScripts.jsp" />
    
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
                                                        <digi:trn><bean:write name="parisIndicatorForm" property="selectedCalendar"/></digi:trn>&nbsp;|
                                                    </c:if>
                                                <strong><digi:trn key="rep:pop:CurrencyType">Currency Type:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedCurrency != null}">
                                                        <digi:trn><bean:write name="parisIndicatorForm" property="selectedCurrency"/></digi:trn>&nbsp;|
                                                    </c:if>
                                                <strong><digi:trn key="rep:pop:Donors">Donors:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedDonors == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedDonors != null}">
                                                        <logic:iterate id="idDonors" property="selectedDonors" name="parisIndicatorForm">
                                                            <%=org.digijava.module.aim.util.DbUtil.getOrganisation(new Long(idDonors.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <strong><digi:trn key="rep:pop:DonorGroups">Donor Groups:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedDonorGroups == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedDonorGroups != null}">
                                                        <logic:iterate id="idDonorsGrp" property="selectedDonorGroups" name="parisIndicatorForm">
                                                            <%=org.digijava.module.aim.util.DbUtil.getAmpOrgGroup(new Long(idDonorsGrp.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <strong><digi:trn>Status:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedStatuses == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedStatuses != null}">
                                                        <logic:iterate id="idStatus" property="selectedStatuses" name="parisIndicatorForm">
                                                            <%=org.digijava.module.categorymanager.util.CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(idStatus.toString()))%>&nbsp;|
                                                        </logic:iterate>
                                                    </c:if>
                                                <strong><digi:trn>Financing Instrument:</digi:trn></strong>
                                                    <c:if test="${parisIndicatorForm.selectedFinancingIstruments == null}">
                                                        <digi:trn key="All">All</digi:trn>&nbsp;|
                                                    </c:if>
                                                    <c:if test="${parisIndicatorForm.selectedFinancingIstruments != null}">
                                                        <digi:trn><bean:write name="parisIndicatorForm" property="selectedFinancingIstruments"/></digi:trn>&nbsp;|
                                                    </c:if>                                              
                                                <strong><digi:trn>Sectors:</digi:trn></strong>
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
                                                <%String selected = ""; %>
                                                <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="${element.indicatorCode}">
                                                    <%selected = "selected"; %>
                                                </logic:equal>
                                                <LI class="<%=selected%>">
                                                    <span>
                                                        <digi:link href="/parisindicator.do?reportId=${element.indicatorCode}&reset=true" target="_self">
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
	                   <td class="td_right_left1">
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
	                   </td>
	                </tr>
					<tr>
						<td class="td_bottom1">&nbsp;</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<html:hidden property="selectedEndYear"/>
	<html:hidden property="selectedStartYear"/>
	<html:hidden property="selectedCalendar"/>
	<html:hidden property="selectedCurrency"/>
	<html:hidden property="selectedDonors"/>
	<html:hidden property="selectedDonorGroups"/>
	<html:hidden property="selectedStatuses"/>
	<html:hidden property="selectedSectors"/>
	
	<html:hidden property="reset" value="false"/>
	<html:hidden property="reportId" value="<%=reportId%>"/>
</digi:form>
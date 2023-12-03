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
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/globalsettings" prefix="gs"%>
<%@ taglib uri="/taglib/aim" prefix="aim" %>

<%@ page import="java.util.*"%>
<%@ page import="org.digijava.ampModule.aim.dbentity.*"%>
<%@ page import="org.digijava.ampModule.aim.helper.*"%>
<%@ page import="org.digijava.ampModule.categorymanager.util.*"%>
<%@ page import="org.digijava.ampModule.categorymanager.dbentity.*"%>
<%@ page import="org.digijava.ampModule.parisindicator.util.*"%>

<!-- BREADCRUMP START -->
<div class="breadcrump">
	<div class="centering">
		<div class="breadcrump_cont" style="visibility: hidden">
			<span class="sec_name"></span>
		</div>
	</div>
</div>
<!-- BREADCRUMP END --> 

<table width="100%">
	<tr align="center">
		<td>
			<!-- CSS -->
			<link href='TEMPLATE/ampTemplate/css_2/amp.css' rel='stylesheet' type='text/css'>
			<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/css/yui/tabview.css" />
			<link type="text/css" rel="stylesheet" href="/TEMPLATE/ampTemplate/css_2/yui_tabs.css">
			
			<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/aim/scripts/common.js"/>"></script>
			<script language="JavaScript" type="text/javascript" src="<digi:file src="ampModule/parisindicator/script/pi_scripts.js"/>"></script>
			
			<digi:errors />
		
			
			<%String reportId = request.getParameter("reportId");%>
			<digi:form action="/parisindicator.do" type="org.digijava.ampModule.parisindicator.form.PIForm" name="parisIndicatorForm">
				<c:choose>
			    <c:when test="${not empty parisIndicatorForm.piReport}">  
			     <jsp:include page="viewParisIndicatorPopupScripts.jsp" />
			    <jsp:include page="viewParisIndicatorPopupFilter.jsp" />
			   
			    
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
			    
				<table width="1000" border="0" cellpadding="10" cellspacing="0" bgcolor="#F2F2F2" style="border-color: #D0D0D0; border-width: 1px 1px 1px 1px; border-style: solid;" align="center">
					<tr>
						<td align="left" valign="top" border="1" style="padding-left: 5px; padding-right: 5px; padding-bottom: 5px;">
							<table width="100%" border="0" cellpadding="5" cellspacing="0">
								<tr>
									<td style="padding-top: 10px;">
										<table border="0" cellspacing="0" cellpadding="0" width="100%" bgcolor="#FFFFFF" 
										style="border-width: 1px; border-color: #D0D0D0; border-style: solid; padding: 5px;">
											<tr>
												<td noWrap="nowrap" align="left" style="color: #376091; font-weight: bold;">
													<a style="cursor: pointer;" class="settingsLink" onClick="showFilter(); "><digi:trn key="rep:pop:ChangeFilters">Change Filters</digi:trn></a>&nbsp;
													<a onclick="javascript:exportPDFs(); resetExport(); return false;" target="_blank" style="cursor: pointer;" title="<digi:trn>Export to PDF</digi:trn>">
												    	<digi:img width="17" height="20" src="/TEMPLATE/ampTemplate/ampModule/aim/images/pdf.gif" style="vertical-align: middle; border-color:#FFFFFF;" border="3" />
												    	<digi:trn>Export to PDF</digi:trn>
													</a>|&nbsp;
												    <a onclick="javascript:exportXLSs(); resetExport(); return false;" paramName="indcId" paramId="indcId" target="_blank" style="cursor: pointer" title="<digi:trn>Export to Excel</digi:trn>">
												    	<digi:img width="17" height="20" src="/TEMPLATE/ampTemplate/imagesSource/common/excel.gif" border="3" style="vertical-align: middle; border-color:#FFFFFF;"/>
												    	<digi:trn>Export to Excel</digi:trn>
												    </a>|&nbsp;
												    <a onclick="javascript:openPrinter(); resetExport(); return false;" target="_blank" style="cursor: pointer" title="<digi:trn>Printer Friendly</digi:trn>">
														<digi:img width="17" height="20" src="/TEMPLATE/ampTemplate/imagesSource/common/printer.gif" border="3" style="vertical-align: middle; border-color:#FFFFFF;"/>
														<digi:trn>Print</digi:trn> 
													</a>|&nbsp;
												</td>
												<td noWrap="nowrap" align="right" style="color: #376091;font-weight: bold;">													
													<a style="cursor: pointer;" onClick="toggleSettings();" id="displaySettingsButton">
														<digi:trn key="rep:showCurrSettings">Show current settings</digi:trn>
													</a>&nbsp;&nbsp;
												</td>
											</tr>
											<tr>
												<td>
													<div style="margin-left: 5px; margin-right: 5px; padding: 2px 2px 2px 2px; Font-size: 8pt; font-family: Arial;">
														<div style="display: none; padding: 2px 2px 2px 2px; border-style: solid; border-width: 1px; border-color: #D0D0D0; font-size: 11px; background:#f2f2f2;padding:5px;width:100%;" id="currentDisplaySettings" >
														
						                                        
						                                            	 <h3><digi:trn key="rep:pop:SelectedRangeStartYear">Start Year</digi:trn><bean:write name="parisIndicatorForm" property="selectedStartYear"/></h3>
						                                              <ul style="margin:0px;padding:0px;"><li><digi:trn key="rep:pop:SelectedRangeEndYear">End Year</digi:trn><bean:write name="parisIndicatorForm" property="selectedEndYear"/>
						                                                </li><li><digi:trn key="rep:pop:CalendarType">Calendar Type</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedCalendar == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedCalendar != null}">
						                                                	<digi:trn><bean:write name="parisIndicatorForm" property="selectedCalendar"/></digi:trn>
						                                                </c:if>
						                                                </li><li><digi:trn key="rep:pop:CurrencyType">Currency Type</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedCurrency == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedCurrency != null}">
						                                                	<digi:trn><bean:write name="parisIndicatorForm" property="selectedCurrency"/></digi:trn>
						                                                </c:if>
						                                                </li><li><digi:trn key="rep:pop:Donors">Donors</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedDonors == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedDonors != null}">
						                                                	<logic:iterate id="idDonors" property="selectedDonors" name="parisIndicatorForm">
						                                                    	<%=org.digijava.ampModule.aim.util.DbUtil.getOrganisation(new Long(idDonors.toString()))%>
						                                                    </logic:iterate>
						                                                </c:if>
						                                                </li><li><digi:trn key="rep:pop:DonorGroups">Donor Groups</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedDonorGroups == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedDonorGroups != null}">
						                                                	<logic:iterate id="idDonorsGrp" property="selectedDonorGroups" name="parisIndicatorForm">
						                                                    	<%=org.digijava.ampModule.aim.util.DbUtil.getAmpOrgGroup(new Long(idDonorsGrp.toString()))%>
						                                                    </logic:iterate>
						                                                </c:if>
						                                                </li><li><digi:trn>Status</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedStatuses == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedStatuses != null}">
						                                                	<logic:iterate id="idStatus" property="selectedStatuses" name="parisIndicatorForm">
						                                                    	<%=org.digijava.ampModule.categorymanager.util.CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(idStatus.toString()))%>
						                                                    </logic:iterate>
						                                                </c:if>
						                                                </li><li><digi:trn>Financing Instrument</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedFinancingIstruments == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedFinancingIstruments != null}">
						                                                	<logic:iterate id="idFunding" property="selectedFinancingIstruments" name="parisIndicatorForm">
						                                                    	<%=org.digijava.ampModule.categorymanager.util.CategoryManagerUtil.getAmpCategoryValueFromDb(new Long(idFunding.toString()))%>
						                                                    </logic:iterate>
						                                                </c:if>                                              
						                                                </li><li><digi:trn>Sectors</digi:trn>:
						                                                <c:if test="${parisIndicatorForm.selectedSectors == null}">
						                                                	<digi:trn key="All">All</digi:trn>
						                                                </c:if>
						                                                <c:if test="${parisIndicatorForm.selectedSectors != null}">
						                                                	<logic:iterate id="idSector" property="selectedSectors" name="parisIndicatorForm">
						                                                    	<%=org.digijava.ampModule.aim.util.SectorUtil.getAmpSector(new Long(idSector.toString()))%>
						                                                    </logic:iterate>
						                                                </c:if>  </li></ul>  
                                                                         <div style="clear:both;"></div>                                         
						                                     
													</div>
                                                     <div style="clear:both;"></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
							&nbsp;
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
			                        <td>
			                        	<div id="content" class="yui-skin-sam">
			                        	<div id="demo" class="yui-navset">
				                            <logic:notEmpty name="parisIndicatorForm" property="availablePIReports">
			                                	<ul class="yui-nav" id="MyTabs">
			                                    	<logic:iterate id="element" name="parisIndicatorForm" property="availablePIReports" indexId="index">
														<%String selected = ""; 
														String style = "background: none; background-color: #E0E0E0;";
														String aStyle = "color: #376091;";%>
			                                            <logic:equal name="parisIndicatorForm" property="piReport.indicatorCode" value="${element.indicatorCode}">
			                                            	<%selected = "selected"; %>
			                                            	<%style = ""; %>
			                                            	<%aStyle = ""; %>
			                                            </logic:equal>
			                                            <li class="<%=selected%>">
			                                                <digi:link href="/parisindicator.do?reportId=${element.indicatorCode}&reset=false&header=true" target="_self">
			                                                   	<div style="max-width: 80px; font-size: 11px; font-weight: bold;">
			                                                       	<digi:trn>Indicator</digi:trn>&nbsp;<bean:write name="element" property="indicatorCode"/>
			                                                    </div>
			                                                </digi:link>
			                                            </li>
													</logic:iterate>
			                                 	</ul>
			                            	</logic:notEmpty>		                        
						                	<table style="border:1px solid #D0D0D0;" border="0" width="100%">
						                		<tr bgcolor="#FFFFFF">
						                			<td>
									                	<table width="100%">
									                		<tr>
											                    <td width="40%">
											                        <p style="font-size: 20px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;"><strong><digi:trn key="aim:parisIndicator">Paris Indicator</digi:trn> <digi:trn key="aim:report">Report</digi:trn>&nbsp;<bean:write name="parisIndicatorForm" property="piReport.indicatorCode"/></strong></p>
											                    </td>
									                    		<td width="60%" align="right" style="font-size: 11px; padding-right: 15px;">
								                           			<p><img src="/TEMPLATE/ampTemplate/images/info.png" width="15" height="15" style="vertical-align: bottom;">&nbsp;<bean:write name="parisIndicatorForm" property="piReport.name"/></p>
									                    		</td>
									                    	</tr>
									                  	</table>
									        
								                       	<div id="reportContent" style="padding-left: 10px; padding-right: 10px; padding-bottom: 10px;">
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
							           		</table>
			                           </div>
			                           </div>
				                    </td>
				                </tr>
							</table>
						</td>
					</tr>
				</table>
				</c:when>
			<c:otherwise>
				<digi:trn>No indicator(s) found</digi:trn>
			</c:otherwise>
			</c:choose>
			</digi:form>
			
		</td>
	</tr>
</table>
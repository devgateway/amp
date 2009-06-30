<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="globalsettings"%>

<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<script language="JavaScript1.2" type="text/javascript" src="<digi:file src="module/aim/scripts/dscript120.js"/>"></script>
<script language="JavaScript1.2" type="text/javascript"  src="<digi:file src="module/aim/scripts/dscript120_ar_style.js"/>"></script>
<script type="text/javascript">
BASE_YEAR	= <globalsettings:value name="Year Range Start" />;
var helpBody=' <digi:trn jsFriendly="true"> to open all reports on one page, please enter the digit "0"</digi:trn>';
var helpBodyAct=' <digi:trn jsFriendly="true"> Please enter a number greater than "1"</digi:trn>';
var helpTitle='<digi:trn jsFriendly="true">Report Sheet</digi:trn>';
function validade(){
  var defReportsPerPage = document.getElementById("defRecsPerPage");
  if(parseInt(defReportsPerPage.value)<1){
	  alert(helpBodyAct);
	  return false;
  }

  var defaultReportViewablePages = document.getElementById("defaultReportViewablePages");
  if(parseInt(defaultReportViewablePages.value)<1){
	  var msg='<digi:trn>Please enter a number greater then 1 for number of pages per Report or Tab.</digi:trn>';
	  alert(msg);
	  return false;
  }

  var startYear		= parseInt(aimUpdateAppSettingsForm.reportStartYear.value);
  var endYear		= parseInt(aimUpdateAppSettingsForm.reportEndYear.value);
   
  if ( !checkYear( startYear, BASE_YEAR, 200 ) ) {
	aimUpdateAppSettingsForm.reportStartYear.focus();
	alert("<digi:trn>Chosen report start year is not realistic</digi:trn>");
	return false;
  }
  if ( !checkYear( endYear, BASE_YEAR, 200 ) ) {
	aimUpdateAppSettingsForm.reportEndYear.focus();
	alert("<digi:trn>Chosen report end year is not realistic</digi:trn>");
	return false;
  }
  if ( startYear > endYear ) {
	  aimUpdateAppSettingsForm.reportStartYear.focus();
	  alert("<digi:trn>Start year greater than end year</digi:trn>");
	  return false;
 }
  
  document.aimUpdateAppSettingsForm.save.value = "save";
  document.aimUpdateAppSettingsForm.submit();
  return true;
}
function checkYear( year, base, range ) {
	if ( year == 0 )
		return true;
	if ( year > base + range || year < base - range )
		return false;
	return true;
	
}
</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>

<digi:errors/>
<digi:instance property="aimUpdateAppSettingsForm" />
<digi:form action="/saveApplicationSettings.do" method="post">
 <!-- this is for the nice tooltip widgets -->
 <DIV id="TipLayer"  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<html:hidden property="type" />
<html:hidden property="appSettingsId" />
<html:hidden property="save" />

<!-- Start include of reportDescriptionSheet.jsp  -->
	<%@include file="reportDescriptionSheet.jsp"%>
<!-- END include of reportDescriptionSheet.jsp  -->

<table cellSpacing=0 cellPadding=0 vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td>
<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="780">
	<tr>
		<td width="14">&nbsp;</td>
		<td align="left" vAlign="top" width="750">
			<table cellPadding="5" cellSpacing="0" width="100%">
				<tr>
					<td height="33">
                    <span class="crumb">
						<c:set var="translation">
							<digi:trn>Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
							<digi:trn>Portfolio</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn>Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
							<digi:trn>Team Workspace Setup</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn>Customize Team Settings</digi:trn>
                    </span>
					</td>
				</tr>
				<tr>
					<td height="16" vAlign="middle" width="571">
                    	<span class="subtitle-blue">
                    		<digi:trn>Team Workspace Setup</digi:trn>
                    	</span>
					</td>
				</tr>
				<tr>
					<td noWrap vAlign="top">
						<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="100%" valign="top" border="0" align="left">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="0" scope="request"/>
									<c:set var="selectedSubTab" value="1" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr>
								<td valign="top">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">
										<tr>
											<td style="padding-left:78px">
												<table width="500" border="0" cellspacing="1" cellpadding="3" bgcolor="#f4f4f2">
													<logic:equal name="aimUpdateAppSettingsForm" property="updated" value="true">
													<tr>
														<td colspan="2" align="center">
															<b>
                                                            <font color="blue">
															<digi:trn>Update to AMP Complete</digi:trn>
															</font>
                                                            </b>
														</td>
													</tr>
													</logic:equal>
                                                    <tr>
                                                        <td bgcolor="#f4f4f2" colspan="2"><digi:errors/>
                                                        </td>
                                                    </tr>
                                                    
                                                    <logic:equal name="aimUpdateAppSettingsForm" property="errors" value="true">
													<tr>
														<td colspan="2" align="center">
															<b>
                                                            <font color="red">
															<digi:trn>Wrong number of Activities/Projects per page. The number should be greater than 1.</digi:trn>
															</font>
                                                            </b>
														</td>
													</tr>
													</logic:equal>
                                                    
													<tr>
														<td bgcolor="#f4f4f2" align="right" width="30%">
															<digi:trn>Number of activities/projects per page</digi:trn>
														</td>
														<td align="left" width="70%" bgcolor="#f4f4f2">
															<html:text property="defRecsPerPage" size="5"  styleClass="inp-text" styleId="defRecsPerPage"/>
                                                            <a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBodyAct],Style[15])" onmouseout="htm()">
														    	<img src="../ampTemplate/images/help.gif" alt="Click to get help on Status" width=10 height=10 border=0>
                                                            </a>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2" align="right" width="30%">
															<digi:trn>Number of pages per report/tab</digi:trn>
														</td>
														<td align="left" width="70%" bgcolor="#f4f4f2">
															<html:text property="defaultReportViewablePages" size="5"  styleClass="inp-text" styleId="defaultReportViewablePages"/>
                                                            <a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBodyAct],Style[15])" onmouseout="htm()">
														    	<img src="../ampTemplate/images/help.gif" alt="Click to get help on Status" width=10 height=10 border=0>
                                                            </a>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2" align="right" width="50%">
															<digi:trn>Number of reports per page</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:text property="defReportsPerPage" size="5"  styleClass="inp-text"/>
														    
                                                            <a style="cursor:pointer;color:#006699" onmouseover="stm([helpTitle,helpBody],Style[15])" onmouseout="htm()">
														    	<img src="../ampTemplate/images/help.gif" alt="Click to get help on Status" width=10 height=10 border=0>
                                                            </a>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn>Language</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="language" styleClass="inp-text">
												                <bean:define id="languages" name="aimUpdateAppSettingsForm" property="languages" type="java.util.Collection" />
												                <html:options collection="languages" property="code" labelProperty="name" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn>Currency</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="currencyId" styleClass="inp-text">
																<html:option value="">------ <digi:trn>Select Currency</digi:trn> ------</html:option>
																<html:optionsCollection name="aimUpdateAppSettingsForm"	property="currencies" value="ampCurrencyId" label="currencyCode" />
															</html:select>
														</td>
													</tr>
 													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn>Validation</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<bean:define id="newOnly" value=""/>
															<bean:define id="allEdits" value=""/>
															<c:if test="${aimUpdateAppSettingsForm.validation=='newOnly'}">
																<bean:define id="newOnly" value="selected"/>
															</c:if>
															<c:if test="${aimUpdateAppSettingsForm.validation=='allEdits'}">
																<bean:define id="allEdits" value="selected"/>
															</c:if>
															<html:select property="validation" styleClass="inp-text">
																<option value="newOnly" <c:out  value="${newOnly}"></c:out> ><digi:trn>Validate New Only</digi:trn></option>
																<option value="allEdits" <c:out  value="${allEdits }"></c:out> ><digi:trn>Validate All</digi:trn></option>
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2" align="right" width="50%">
															<digi:trn>Reports Default Start Year</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:text property="reportStartYear" size="5"  styleClass="inp-text"/>
														</td>
													</tr>
 													<tr>
														<td bgcolor="#f4f4f2" align="right" width="50%">
															<digi:trn>Reports Default End Year</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:text property="reportEndYear" size="5"  styleClass="inp-text"/>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn>Fiscal Calendar</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="fisCalendarId" styleClass="inp-text">
															<html:option value="">------ <digi:trn>Select Fiscal Calendar</digi:trn> ------</html:option>
															<html:optionsCollection name="aimUpdateAppSettingsForm" property="fisCalendars" value="ampFiscalCalId" label="name" />
															</html:select>
														</td>
													</tr>
													<tr>
														<td bgcolor="#f4f4f2"  align="right" width="50%">
															<digi:trn>Default Team Tab</digi:trn>
														</td>
														<td align="left" width="50%" bgcolor="#f4f4f2">
															<html:select property="defaultReportForTeamId" styleClass="inp-text" onchange="changePanel()" styleId="defaultReport">
															<html:option value="0">
															------ <digi:trn>Select Default Team Tab</digi:trn> ------
															</html:option>
															<c:forEach var="reports" items="${aimUpdateAppSettingsForm.reports}">
															<c:set var="trn">
																<digi:trn>${reports.name}</digi:trn>
															</c:set>
																<html:option value="${reports.ampReportId}">
																	${trn}
																</html:option>
															</c:forEach>
															</html:select>
															<br />
															<a style="cursor:pointer;color:#006699" onClick="if(document.getElementById('defaultReport').value == 0) {alert('<digi:trn key="aim:defaultTeamReportDetailsAlertMessage">Please select a default report</digi:trn>');return false;}else{showMyPanel();}"><digi:trn key="aim:defaultTeamReportDetailsMessage">Click here for details</digi:trn></a>
														</td>
													</tr>
													<tr>
														<td colspan="2" align="center" bgcolor="#f4f4f2" >
															<table cellspacing="10">
																<tr>
																	<td align="right" colspan="2">
                                                                      <c:set var="caption">
                                                                        <digi:trn>Save</digi:trn>
                                                                      </c:set>
                                                                      <input type="button"  class="dr-menu" value="${caption}" onclick="validade();" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
                                </div>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;</td></tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>
</digi:form>




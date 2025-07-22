<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<DIV id="TipLayer" style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>
<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/jquery/jquery-min.js"/>"></script>
<script type="text/javascript">
<!--
	function validate() {
		if (document.aimTeamReportsForm.selReports.checked != null) {
			if (document.aimTeamReportsForm.selReports.checked == false) {
				alert("Please choose a report to add");
				return false;
			}				  
		} else {
			var length = document.aimTeamReportsForm.selReports.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimTeamReportsForm.selReports[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a report to add");
				return false;					  
			}				  
		}
		document.aimTeamReportsForm.submit();
		return true;			  
	}

	function resetSearch() {
		var addrepVal="List of Unassigned Reports";
		<digi:context name="searchOrg" property="context/module/moduleinstance/updateTeamReports.do"/>     
		url = "<%= searchOrg %>?reset=true&addReport="+addrepVal;
		//document.aimTeamReportsForm.addReport.value="List of Unassigned Reports";
		document.aimTeamReportsForm.action = url;
		document.aimTeamReportsForm.submit();
		 return true;

	}

	function searchActivity(teamId) {
		var addrepVal="List of Unassigned Reports";
			 <digi:context name="searchOrg" property="context/module/moduleinstance/updateTeamReports.do"/>			 
		     url = "<%= searchOrg %>?addReport="+addrepVal;
		     //document.aimTeamReportsForm.addReport.value="List of Unassigned Reports";
		     document.aimTeamReportsForm.action = url;
		     document.aimTeamReportsForm.submit();
			 return true;
	}
	
	function doSearchWhenEnter (event) {
    	var key = (document.all) ? event.keyCode : event.which;
    	  if (key==13) {
    		  $('#goButtonAdd').trigger( "click" );
    		  return false;
    	  }
    	
    }

-->

</script>


<digi:errors/>
<digi:instance property="aimTeamReportsForm" />
<digi:form action="/updateTeamReports.do" method="post">
<html:hidden property="teamId" />
<table width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />

									<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="selectedTab" value="3" scope="request"/>
									</c:if>
									<c:if test="${aimTeamReportsForm.showReportList == false}">
										<c:set var="selectedTab" value="8" scope="request"/>
									</c:if>	
									<c:set var="selectedSubTab" value="1" scope="request"/>
										
									<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
										<tr>
											<td>
												<div class="breadcrump_cont">
													<span class="sec_name">
														<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
													</span>
													
													<span class="breadcrump_sep">|</span>
													<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
														<digi:trn key="aim:portfolio">Portfolio</digi:trn>
													</digi:link>
													<span class="breadcrump_sep"><b>�</b></span>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
													</c:set>
													<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
													<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
													<span class="breadcrump_sep"><b>�</b></span>
													
													<c:set var="translation">
													<digi:trn key="aim:clickToViewReportList">Click here to view Report List</digi:trn>
													</c:set>
													<digi:link href="/teamReportList.do" styleClass="l_sm" title="${translation}" >
													<digi:trn key="aim:reportList">
													Report List
													</digi:trn>
													</digi:link>
													
													<span class="breadcrump_sep"><b>�</b></span>
													
													<span class="bread_sel">
														<c:if test="${aimTeamReportsForm.showReportList == true}">
															<digi:trn key="aim:addReports">
																Add Reports
															</digi:trn>
														</c:if>
														<c:if test="${aimTeamReportsForm.showReportList == false}">
															<digi:trn key="aim:addTabs">
																Add Tabs
															</digi:trn>
														</c:if>
													</span>
												</div>
											
											</td>
										</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">	
										
									<jsp:include page="teamSetupMenu.jsp"  />								

                                	<div class="contentbox_border" style="border-top:0px;padding: 10px 0px 10px 0px;">
									<div align="center">
									<table class="normal" width="100%" cellpadding="0" cellspacing="0" style="background:#fff;">
										<tr>
											<td >
												<table>
													<tr>
														<td nowrap="nowrap">
															<digi:trn>Keyword</digi:trn>&nbsp;
															<html:text property="keyword" styleClass="inp-text" onkeypress="doSearchWhenEnter(event);" />
														</td>
														<td nowrap="nowrap">
															<digi:trn>Results</digi:trn>&nbsp;
															<html:select property="tempNumResults" styleClass="inp-text" onchange="return searchActivity('${aimTeamReportsForm.teamId }')">
																<c:if test="${aimTeamReportsForm.tempNumResults!=-1}">
																	<html:option value="${aimTeamReportsForm.tempNumResults}"><digi:trn>Current</digi:trn>: ${aimTeamReportsForm.tempNumResults}</html:option>
																</c:if>
																<html:option value="10">10</html:option>
																<html:option value="20">20</html:option>
																<html:option value="50">50</html:option>
																<html:option value="-1"><digi:trn>All</digi:trn></html:option>
															</html:select>
														</td>
														<td>
															<c:set var="trnResetBtn">
																<digi:trn>Reset</digi:trn>
															</c:set>
															<input type="button" value="${trnResetBtn}" class="dr-menu" onclick="return resetSearch()">
														</td>
														<td>					
															<c:set var="trnGoBtn">
																<digi:trn> GO </digi:trn>
															</c:set>
															<input type="button" id="goButtonAdd" value="${trnGoBtn}" class="dr-menu" onclick="return searchActivity('${aimTeamReportsForm.teamId }')">
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
                                        <td colspan="7" style="width:100%; border-top:1px solid #b8b7b7; ">
                                        	<table cellpadding="1" cellspacing="1" border="0" style="background:#b8b7b7;">
                                            	<tr>
                                                <td style="5%" align="center"class="tdInsideHeader">
									  		<input type="checkbox" id="checkAll" >
									  	</td>
									    <td width="35%"class="tdInsideHeader">
									    	<b class="ins_title">
									    		<c:if test="${aimTeamReportsForm.showReportList == true}">
														<digi:trn key="aim:reportListUnassignedReports">
															List of unassigned reports
														</digi:trn>
													</c:if>
													<c:if test="${aimTeamReportsForm.showReportList == false}">
														<digi:trn key="aim:reportListUnassignedTabs">
															List of unassigned tabs
														</digi:trn>
													</c:if>
									    	</b>
									    </td>
									    <td width=15% class="tdInsideHeader">
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportOwnerName">Owner</digi:trn>
									    	</b>
									    </td>
									    <td  width=15% class="tdInsideHeader">
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportType">Type</digi:trn>
									    	</b>
									    </td>
									    <td    width=15% class="tdInsideHeader">
									    	<b class="ins_title">
									    		<digi:trn key="aim:hierarchies">Hierarchies</digi:trn>
									    	</b>
									    </td>
									    <td    width=18% class="tdInsideHeader">&nbsp;
									    	
									    </td>
										</tr>
										<logic:empty name="aimTeamReportsForm" property="reports">
											<tr>
												<td   class="inside1"   colspan="7">
													<c:if test="${aimTeamReportsForm.showReportList == true}">
														<digi:trn key="aim:noReportsPresent">No reports present</digi:trn>
													</c:if>
													<c:if test="${aimTeamReportsForm.showReportList == false}">
														<digi:trn key="aim:noTabsPresent">No tabs present</digi:trn>
													</c:if>															
												</td>
											</tr>
										</logic:empty>
										<logic:notEmpty name="aimTeamReportsForm" property="reports">
											<logic:iterate name="aimTeamReportsForm" property="reports" id="reports" type="org.digijava.module.aim.dbentity.AmpReports">
													<tr>
														<td style="5%" align="center"   class="inside1">
															<html:multibox property="selReports" >
																<bean:write name="reports" property="ampReportId" />
															</html:multibox>
														</td>
														<td   class="inside1">
															<span title="<c:out value="${reports.name}"/>">
															<c:choose>
																<c:when test="${fn:length(reports.name) > 25}" >
																	<c:out value="${fn:substring(reports.name, 0, 25)}" />...
																</c:when>
																<c:otherwise>
																	<c:out value="${reports.name}" /> 
																</c:otherwise>
															</c:choose>
														</span>
														</td>
														<td   class="inside1">
															<logic:present name="reports" property="ownerId">
                                 <bean:write name="reports" property="ownerId.user.name" />
                              </logic:present>
														</td>
														<td   class="inside1" >
															<li class="bullet">
                                  <%
                                    if (reports.getType()!=null && reports.getType().equals(new Long(1))) {
                                  %>
                                      <digi:trn key="aim:donorType">donor</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long (3))){
                                %>
                                      <digi:trn key="aim:regionalType">regional</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long(2))){
                                %>
                                      <digi:trn key="aim:componentType">component</digi:trn>
                                <%
                                    }
                                    else if (reports.getType()!=null && reports.getType().equals(new Long(4))){
                                %>
                                      <digi:trn key="aim:contributionType">contribution</digi:trn>
                                <%}%>
                            </li>
                              <logic:equal name="reports" property="drilldownTab" value="true">
                                <li class="bullet">
                                  <digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="publicReport" value="true">
                                <li class="bullet">
                                  <digi:trn key="aim:typePublicReport">Public Report</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="hideActivities" value="true">
                                <li class="bullet">
                                  <digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
                                </li>
                              </logic:equal>                                  
                              <logic:equal name="reports" property="options" value="A">
                                <li class="bullet">
                                	<digi:trn key="aim:annualreport">Annual</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="options" value="Q">
                                <li class="bullet">
                                	<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="reports" property="options" value="M">
                                <li class="bullet">
                                	<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
                                </li>
                              </logic:equal>
														</td>
														<td   class="inside1" >
															<logic:iterate name="reports" property="hierarchies" id="hierarchy" >
																<li class="bullet">
										            	${hierarchy.column.columnName}
										            </li>
										          </logic:iterate>
														</td>
														<td   class="inside1">
															<div style='position:relative;display:none;' id='report-<bean:write name="reports" property="ampReportId"/>'> 
                                <logic:iterate name="reports" property="columns" id="column" indexId="index"  >
                                  <%if (index.intValue()%2==0){ %>
                                    <li class="bullet">
                                        <digi:colNameTrn>
                                            <bean:write name="column" property="column.columnName" />
                                        </digi:colNameTrn>
                                  <% } else {%>
                                    ,
                                        <digi:colNameTrn>
                                            <bean:write name="column" property="column.columnName" />
                                        </digi:colNameTrn>
                                    </li>
                                  <%} %>
                                </logic:iterate>
                              </div>
                              <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn jsFriendly="true" key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="reports" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
                              </span>

                              <div style='position:relative;display:none;' id='measure-<bean:write name="reports" property="measures"/>'> 
                                <logic:iterate name="reports" property="measures" id="measure" indexId="index"  >
                                  <li class="bullet">
                                  	<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">                                      
                                    		${measure.measure.aliasName}
                                    	</digi:trn>
                                  </li>
                                </logic:iterate>
                              </div>										                                
                              <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn jsFriendly="true" key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="reports" property="measures"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
                              </span>
														</td>
                                                </tr>
                                                	</logic:iterate>
										</logic:notEmpty>


										<tr><td colspan="7"><digi:errors /></td></tr>
									</table>
									<!-- Pagination -->
									<logic:notEmpty name="aimTeamReportsForm" property="totalPages">
										<div class="paging" style="font-size:11px;">
											<digi:trn>Pages</digi:trn>:
												<c:forEach var="page" begin="1" end="${aimTeamReportsForm.totalPages}">
												  	<c:if test="${aimTeamReportsForm.currentPage==page}">
				                                	    <b class="paging_sel"><c:out value="${page}"/></b>
				                                    </c:if>
				                                    <c:if test="${aimTeamReportsForm.currentPage!=page}">
				                                    	<c:set var="translation">
															<digi:trn>Click here to goto Next Page</digi:trn>
														</c:set>																														
														<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
														<c:set target="${urlParams}" property="addReport" value="List of Unassigned Reports"/>
														<c:set target="${urlParams}" property="keyword" value="${aimTeamReportsForm.keyword}"/>
														<digi:link href="/updateTeamReports.do?currentPage=${page}&tempNumResults=${aimTeamReportsForm.tempNumResults}" name="urlParams">
					                                    	<c:out value="${page}"/>
					                                    </digi:link>
				                                     </c:if>																												  	
													|&nbsp;
												</c:forEach>
											</div>
										</logic:notEmpty>
										<!-- end of Pagination -->
									
									<br>
									<div class="buttons" align="center">
														</td>
													</tr>
													
													<tr>
														<td align="center" bgcolor=#ffffff>
                                                          <a style="cursor:pointer;" onclick="window.scrollTo(0,0); return false"><digi:trn key="aim:backtotop">Back to Top</digi:trn> <span style="font-size: 10pt; font-family: Tahoma;">&uarr;</span></a>
														</td>
													</tr>
													<tr>
														<td align="center" bgcolor=#ffffff>
															<table cellspacing="5">
																<tr>
																	<td>
										
										<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="assignReportsText">
											<digi:trn key="btn:addReportsToTheWorkspace">Add Reports to the Workspace</digi:trn>
										</c:set>
										<html:hidden property="assignReports" value="${assignReportsText}"/>
										<input type="button"  class="buttonx_sm btn" onclick="return validate()" value="${assignReportsText}"/>
										</c:if>
									
										<c:if test="${aimTeamReportsForm.showReportList == false}">
											<c:set var="assignReportsText">
												<digi:trn key="btn:addTabsToTheWorkspace">Add Tabs to the Workspace</digi:trn> 
											</c:set>
											<html:hidden property="assignReports" value="${assignReportsText}"/>
											<input type="button"  class="buttonx_sm btn"  value="${assignReportsText}" onclick="return validate()"/>
										</c:if>
										</td></tr></table>
									</div>										
										
										
								
										
										
										</div>
										</div>											
												
											</td>
										</tr>
									</table>	
										
</td></tr>
</table>
</digi:form>
</tr></td></table></div>
<script language="javascript">
$("#checkAll").bind("change", function (obj){
$("input[name=selReports]").attr("checked", $("#checkAll").is(":checked"));
}
);
</script>

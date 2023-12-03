<%@ page pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<DIV id="TipLayer"
  style="visibility:hidden;position:absolute;z-index:1000;top:-100;"></DIV>


<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>


<digi:instance property="aimTeamReportsForm" />

<digi:form action="/updateTeamReports.do" method="post">
<html:hidden property="addReport"/>
<html:hidden property="showReportList" styleId="showReportList"/>
<table width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr>
<td>

									<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="selectedTab" value="3" scope="request"/>
									</c:if>
									<c:if test="${aimTeamReportsForm.showReportList == false}">
										<c:set var="selectedTab" value="8" scope="request"/>
									</c:if>									
									<c:set var="selectedSubTab" value="0" scope="request"/>
										
										<table width="1000px" border="0" cellspacing="0" cellpadding="0" align="center">
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
													<span class="bread_sel">
														<c:if test="${aimTeamReportsForm.showReportList == true}">
															<digi:trn key="aim:reportList">Report List</digi:trn>
														</c:if>
														<c:if test="${aimTeamReportsForm.showReportList == false}">
															<digi:trn key="aim:tabsList">Tab List</digi:trn>
														</c:if>	
													</span>
												</div>
											</td>
										</tr>
										<tr>
											<td valign="top">
												<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
										
										
									<jsp:include page="teamSetupMenu.jsp"  />
									
									<div>
										<table>
													<tr>
														<td nowrap="nowrap">
															<digi:trn>Keyword</digi:trn>&nbsp;
															<html:text property="keyword" styleClass="inp-text" onkeypress="doSearchWhenEnter(event)" />
														</td>
														<td nowrap="nowrap" >
															<digi:trn>Results</digi:trn>&nbsp;ss
															<html:select property="tempNumResults" style="width:100px" styleClass="inp-text" onchange="return searchActivity('${aimTeamReportsForm.teamId }')">
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
															<input type="button" value="${trnGoBtn}" class="dr-menu" onclick="return searchActivity('${aimTeamReportsForm.teamId }')">
														</td>
													</tr>
												</table>
									</div>
									
									<br>
									<div id="private">
										<img src= "/repository/contentrepository/view/images/make_public.gif">
										<c:if test="${aimTeamReportsForm.showReportList == true}">
											<digi:trn key="aim:teamReportListViewableByAllWorkspaceMembers">
												Indicates the report is viewable by all team members.
											</digi:trn>
										</c:if>
										<c:if test="${aimTeamReportsForm.showReportList == false}">
											<digi:trn key="aim:teamTabListViewableByAllWorkspaceMembers">
												Indicates the tab is viewable by all workspace members.
											</digi:trn>
										</c:if>
										<br/>
										<digi:trn key="aim:clicktomakethisprivate">Click here to make this team-invisible</digi:trn>	
									</div>
									<div id="private">
										<img src= "/repository/contentrepository/view/images/make_private.gif">
										<c:if test="${aimTeamReportsForm.showReportList == true}">
											<digi:trn>Indicates the report is only viewable by the owner</digi:trn>
										</c:if>
										<c:if test="${aimTeamReportsForm.showReportList == false}">
											<digi:trn>Indicates the tab is only viewable by the owner</digi:trn>
										</c:if>
										<br>
										<digi:trn key="aim:clicktomakethispublic">Click here to make this team-visible</digi:trn>
									</div>
						
									<table class="inside normal" cellpadding="0" cellspacing="0" width=970>									
									
									
										<tr style="border-top:1px solid #cccccc;border-bottom:1px solid #cccccc;">
									  	<td width="5" align="center" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									  		<input type="checkbox" id="checkAll">
									  	</td>
									    <td width=25% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<c:if test="${aimTeamReportsForm.showReportList == true}">
														<digi:trn key="aim:reportListWorkspace">List of Reports in the Workspace</digi:trn>
													</c:if>
													<c:if test="${aimTeamReportsForm.showReportList == false}">
														<digi:trn key="aim:tabListWorkspace">List of Tabs in the Workspace</digi:trn>
													</c:if>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportOwnerName">Owner</digi:trn>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<digi:trn key="aim:reportType">Type</digi:trn>
									    	</b>
									    </td>
									    <td width=15% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<digi:trn key="aim:hierarchies">Hierarchies</digi:trn>
									    	</b>
									    </td>
									    <td width=18% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">&nbsp;
									    	
									    </td>
									     <td width=7% background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class="inside">
									    	<b class="ins_title">
									    		<digi:trn key="aim:visibility">Visibility</digi:trn>
									    	</b>
									    </td>
										</tr>
										<logic:empty name="aimTeamReportsForm" property="reports">
											<tr>
												<td class="inside" colspan="7">
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
											<logic:iterate name="aimTeamReportsForm" property="reports" id="reports" type="org.digijava.ampModule.aim.helper.ReportsCollection">
												<bean:define id="ampReports" name="reports" property="report" type="org.digijava.ampModule.aim.dbentity.AmpReports" />
													<tr>
														<td width="5" align="center" class="inside">
															<html:multibox property="selReports" >
																<bean:write name="ampReports" property="ampReportId" />
															</html:multibox>
														</td>
														<td class="inside">
															<span  title="<c:out value="${ampReports.name}"/>">
																<c:choose>
																	<c:when test="${fn:length(ampReports.name) > 25}" >
																		<c:out value="${fn:substring(ampReports.name, 0, 25)}" />...
																	</c:when>
																	<c:otherwise>
																		<c:out value="${ampReports.name}" /> 
																	</c:otherwise>
																</c:choose>
															</span>														
														</td>
														<td class="inside">
															<logic:present name="ampReports" property="ownerId">
								                              	<bean:write name="ampReports" property="ownerId.user.name" />
								                              </logic:present>
														</td>
														<td class="inside">
															<li class="bullet">
                                  <%
                                    if (ampReports.getType()!=null && ampReports.getType().equals(new Long(1))) {
                                  %>
                                      <digi:trn key="aim:donorType">donor</digi:trn>
                                <%
                                    }
                                    else if (ampReports.getType()!=null && ampReports.getType().equals(new Long (3))){
                                %>
                                      <digi:trn key="aim:regionalType">regional</digi:trn>
                                <%
                                    }
                                    else if (ampReports.getType()!=null && ampReports.getType().equals(new Long(2))){
                                %>
                                      <digi:trn key="aim:componentType">component</digi:trn>
                                <%
                                    }
                                    else if (ampReports.getType()!=null && ampReports.getType().equals(new Long(4))){
                                %>
                                      <digi:trn key="aim:contributionType">contribution</digi:trn>
                                <%}%>
                            </li>
                              <logic:equal name="ampReports" property="drilldownTab" value="true">
                                <li class="bullet" >
                                  <digi:trn key="aim:typeDrilldownTab">Desktop Tab</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="ampReports" property="publicReport" value="true">
                                <li class="bullet">
                                  <digi:trn key="aim:typePublicReport">Public Report</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="ampReports" property="hideActivities" value="true">
                                <li class="bullet">
                                  <digi:trn key="aim:typeSummaryReport">Summary Report</digi:trn>
                                </li>
                              </logic:equal>                                  
                              <logic:equal name="ampReports" property="options" value="A">
                                <li class="bullet">
                                	<digi:trn key="aim:annualreport">Annual</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="ampReports" property="options" value="Q">
                                <li style="margin-left: 10px;">
                                	<digi:trn key="aim:quarterlyreport">Quarterly</digi:trn>
                                </li>
                              </logic:equal>
                              <logic:equal name="ampReports" property="options" value="M">
                                <li class="bullet">
                                	<digi:trn key="aim:monthlyreport">Monthly</digi:trn>	
                                </li>
                              </logic:equal>
														</td>
														<td class="inside">
															<logic:iterate name="ampReports" property="hierarchies" id="hierarchy" >
                                <li class="bullet">
                                    <digi:colNameTrn>
                                		${hierarchy.column.columnName}
                                    </digi:colNameTrn>
                                </li>
                              </logic:iterate>
														</td>
														<td class="inside">
															<div style='position:relative;display:none;' id='report-<bean:write name="ampReports" property="ampReportId"/>'> 
                                <logic:iterate name="ampReports" property="columns" id="column" indexId="index"  >
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
                              <span align="center" style="text-transform: capitalize;" onMouseOver="stm(['<digi:trn jsFriendly="true" key="aim:teamreports:columns">columns</digi:trn>',document.getElementById('report-<bean:write name="ampReports" property="ampReportId"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u style="text-transform:capitalize;" ><digi:trn key="aim:teamreports:columns">Columns</digi:trn></u> ]&nbsp;
                              </span>

                              <div style='position:relative;display:none;' id='measure-<bean:write name="ampReports" property="measures"/>'> 
                                <logic:iterate name="ampReports" property="measures" id="measure" indexId="index"  >
                                  <li class="bullet">
                                  	<digi:trn key="aim:reportBuilder:${measure.measure.aliasName}">                                      
                                    		${measure.measure.aliasName}
                                    	</digi:trn>
                                  </li>
                                </logic:iterate>
                              </div>										                                
                              <span align="center" style="text-transform: capitalize;white-space: no-wrap;"  onMouseOver="stm(['<digi:trn jsFriendly="true" key="aim:teamreports:measures">measures</digi:trn>',document.getElementById('measure-<bean:write name="ampReports" property="measures"/>').innerHTML],Style[1])" onMouseOut="htm()">[ <u><digi:trn key="aim:teamreports:measures">Measures</digi:trn></u> ]<br />
                              </span>
														</td>
														<td class="inside">
															
															
															<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams}" property="id">
																<bean:write name="ampReports" property="ampReportId" />
															</c:set>
															<logic:equal name="reports" property="teamView" value="false">
																<c:set target="${urlParams}" property="status" value="team" />
																<c:set target="${urlParams}" property="currentPage" value="${aimTeamReportsForm.currentPage}" />
																	<c:set var="translation">
																		<digi:trn key="aim:clickToMakeThisPublic">Click here to make this team-visible</digi:trn>
																	</c:set>
																	
																	<c:if test="${aimTeamReportsForm.showReportList == true}">
																		<c:set target="${urlParams}" property="returnPage">teamReportList</c:set>
																	</c:if>	
																	<c:if test="${aimTeamReportsForm.showReportList == false}">
																		<c:set target="${urlParams}" property="returnPage">teamDesktopTabList</c:set>
																	</c:if>	
																	
																	<c:set target="${urlParams}" property="tempNumResults">${aimTeamReportsForm.tempNumResults}</c:set>
																	
																	<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="${translation}" >
																		<img hspace="2" title="<digi:trn key="aim:teamReportListMakePublic">Make this team-visible</digi:trn>" src= "/repository/contentrepository/view/images/make_private.gif" border="0">
																	</digi:link>
															</logic:equal>
															
															<logic:equal name="reports" property="teamView" value="true">
																<c:set target="${urlParams}" property="status" value="member" />
																<c:set target="${urlParams}" property="currentPage" value="${aimTeamReportsForm.currentPage}" />
																	<c:set var="translation">
																		<digi:trn key="aim:clickToMakeThisPrivate">Click here to make this private</digi:trn>
																	</c:set>

																	<c:if test="${aimTeamReportsForm.showReportList == true}">
																		<c:set target="${urlParams}" property="returnPage">teamReportList</c:set>
																	</c:if>	
																	<c:if test="${aimTeamReportsForm.showReportList == false}">
																		<c:set target="${urlParams}" property="returnPage">teamDesktopTabList</c:set>
																	</c:if>	

																	<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="${translation}" >
																		<img hspace="2" title="<digi:trn key="aim:teamReportListMakePrivate">Make this team-invisible</digi:trn>" src= "/repository/contentrepository/view/images/make_public.gif" border="0">
																	</digi:link>
															</logic:equal>
															
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
				                                    	<c:if test="${aimTeamReportsForm.showReportList == true}">
															<digi:link href="/teamReportList.do?currentPage=${page}&tempNumResults=${aimTeamReportsForm.tempNumResults}&keyword=${aimTeamReportsForm.keyword}" >
							                                   	<c:out value="${page}"/>
							                                </digi:link>
														</c:if>
														<c:if test="${aimTeamReportsForm.showReportList == false}">
															<digi:link href="/teamDesktopTabList.do?currentPage=${page}&tempNumResults=${aimTeamReportsForm.tempNumResults}&keyword=${aimTeamReportsForm.keyword}" >
							                                   	<c:out value="${page}"/>
							                                </digi:link>
														</c:if>
				                                    </c:if>				                                    
													|&nbsp;
												</c:forEach>
											</div>
										</logic:notEmpty>
										<!-- end of Pagination -->
									
									<br>
									<div class="buttons" align="center">
									<c:if test="${aimTeamReportsForm.showReportList == true}">
										<c:set var="removeReportsText">
											<digi:trn key="btn:removeSelectedReports">Remove selected reports</digi:trn>
										</c:set>
										<html:hidden property="removeReports" value="${removeReportsText}"/>
										<input type="text"  class="buttonx_sm btn" value="${removeReportsText} " onclick="return confirmDelete() "/>
										</c:if>
										<c:if test="${aimTeamReportsForm.showReportList == false}">
											<c:set var="removeReportsText">
												<digi:trn key="btn:removeSelectedTabs">Remove selected tabs</digi:trn>
											</c:set>
											<html:hidden property="removeReports" value="${removeReportsText}"/>
											<input type="text"  class="buttonx_sm btn" value="${removeReportsText} " onclick="return confirmDelete() "/>
										
										</c:if>																			
									
									</div>
							
										
										</div>
										</div>											
												
											</td>
										</tr>
									</table>	
									
</td></tr>
</table>

</digi:form>

<script type="text/javascript">


function listReports()
{
	document.aimTeamReportsForm.addReport.value="List of Unassigned Reports";
	document.aimTeamReportsForm.action="/updateTeamReports.do?reset=true";
	$('[name="removeReports"]').attr('disabled','disabled');
	document.aimTeamReportsForm.submit();
}


function validate() {

	<c:if test="${aimTeamReportsForm.showReportList == true}">
	   <c:set var="message" scope="request">
          <digi:trn>Please choose a report to remove</digi:trn>
      </c:set>
  </c:if>
  <c:if test="${aimTeamReportsForm.showReportList == false}">
      <c:set var="message" scope="request">	      
       <digi:trn>Please choose a tab to remove</digi:trn>
   </c:set>
  </c:if>	
	
  if(document.aimTeamReportsForm.selReports){
	if(document.aimTeamReportsForm.selReports.checked != null) {
		if (document.aimTeamReportsForm.selReports.checked == false) {				
			alert("${message}");
			return false;
		};
	} else {
		var length = document.aimTeamReportsForm.selReports.length;
		var flag = 0;
		for (var i = 0; i < length; i ++) {
			if (document.aimTeamReportsForm.selReports[i].checked == true) {
				flag = 1;
				break;
			};
		}
		if (flag == 0) {
			alert("${message}");
			return false;
		};
	}
	return true;
  }else{
	  	alert("<digi:trn>There are no selection to remove</digi:trn>");
		return false;
  };
}
    function confirmDelete() {
	var valid = validate();
	if (valid == true) {

		<c:if test="${aimTeamReportsForm.showReportList == true}">
		   <c:set var="message">
            <digi:trn>Are you sure you want to remove selected reports?</digi:trn>
          </c:set>
      </c:if>
	  <c:if test="${aimTeamReportsForm.showReportList == false}">
	      <c:set var="message">
              <digi:trn>Are you sure you want to remove selected tabs?</digi:trn>
          </c:set>
	  </c:if>																			

		var flag = confirm("${message}");
		if(flag == false)
		  return false;
		else
			{
			$('[name="removeReports"]').removeAttr('disabled');
			document.aimTeamReportsForm.submit();
			return true;
			}
	} else {
		return false;
	}
}
    
    function resetSearch() {
    	var showReports = document.getElementById("showReportList").value;    		
		if(showReports == "true"){
			return submitReports('yes');
		}else if(showReports == "false"){
			return  submitTabs('yes');
		}
	}

    function doSearchWhenEnter (event) {
    	var key = (document.all) ? event.keyCode : event.which;
    	  if (key==13) {
    		  searchActivity ('${aimTeamReportsForm.teamId }');
    	  }
    	
    }
	function searchActivity(teamId) {    		
		var showReports = document.getElementById("showReportList").value;    		
		if(showReports == "true"){
			return submitReports('no');
		}else if(showReports == "false"){
			return  submitTabs('no');
		}	 
	}
	
	function submitTabs(reset){
		<digi:context name="lala" property="context/ampModule/moduleinstance/teamDesktopTabList.do"/>
		url = "<%= lala %>";
		if(reset=='yes'){
			url +="?reset=true";
		}
		document.aimTeamReportsForm.action = url;   		    
		    document.aimTeamReportsForm.submit();
		 	return true;
	}
	
	function submitReports(reset){
		 <digi:context name="lala" property="context/ampModule/moduleinstance/teamReportList.do"/>
		 url = "<%= lala %>";
		 if(reset=='yes'){
 			url +="?reset=true";
 		 }
		 document.aimTeamReportsForm.action = url;   		    
		     document.aimTeamReportsForm.submit();
		 return true;
	}

</script>


<script language="javascript">
    $("#checkAll").bind("change", function (obj){
        $("input[name=selReports]").attr("checked", $("#checkAll").is(":checked"));
    });
</script>

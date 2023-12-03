<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<script type="text/javascript">

<!--

	function checkall() {
		var selectbox = document.aimMemberReportsForm.checkAll;
		var items = document.aimMemberReportsForm.selReports;
		if (document.aimMemberReportsForm.selReports.checked == true || 
							 document.aimMemberReportsForm.selReports.checked == false) {
				  document.aimMemberReportsForm.selReports.checked = selectbox.checked;
		} else {
			for(i=0; i<items.length; i++){
				document.aimMemberReportsForm.selReports[i].checked = selectbox.checked;
			}
		}
	}

	function validate() {
		if (document.aimMemberReportsForm.selReports.checked != null) {
			if (document.aimMemberReportsForm.selReports.checked == false) {
				alert("Please choose a report to remove");
				return false;
			}				  
		} else {
			var length = document.aimMemberReportsForm.selReports.length;	  
			var flag = 0;
			for (i = 0;i < length;i ++) {
				if (document.aimMemberReportsForm.selReports[i].checked == true) {
					flag = 1;
					break;
				}
			}		

			if (flag == 0) {
				alert("Please choose a report to remove");
				return false;					  
			}				  
		}
		return true;			  
	}

	function confirmDelete() {
		var valid = validate();
		if (valid == true) {
			var flag = confirm("Are you sure you want to remove the selected reports");
			if(flag == false)
				return false;
			else 
				return true;
		} else {
			return false;
		}		  

	}

-->

</script>



<digi:errors/>
<digi:instance property="aimMemberReportsForm" />
<digi:form action="/updateMemberReports.do" method="post">

<html:hidden property="memberId" />

<jsp:include page="teamPagesHeader.jsp"  />
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>

			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<td height=33><span class=crumb>
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</c:set>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:portfolio">
						Portfolio
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</c:set>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToViewMembers">Click here to view Members</digi:trn>
						</c:set>
						<digi:link href="/teamMemberList.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:members">
						Members
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:memberReports">
						Member Reports
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp"  />
								</td>
							</tr>
							<tr bgcolor="#f4f4f2">
								<td vAlign="top" width="100%">
									<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams}" property="id">
										<bean:write name="aimMemberReportsForm" property="memberId" />
									</c:set>
									<table bgColor=#f4f4f2 align="center" valign="top" cellpadding="0" cellspacing="1">
										<tr>
											<td bgColor=#222e5d noWrap>
												&nbsp;
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberActivities">Click here to view Member Activities</digi:trn>
												</c:set>
												<digi:link href="/teamMemberActivities.do" name="urlParams" 
												styleClass="sub-nav2" title="${translation}" >
													<digi:trn key="aim:memberActivities">
														Member Activities
													</digi:trn>
												</digi:link>
												&nbsp;
											</td>											
											<td bgColor=#222e5d noWrap>
												&nbsp;
												<c:set var="translation">
													<digi:trn key="aim:clickToViewMemberReports">Click here to view Member Reports</digi:trn>
												</c:set>
												&nbsp;
												<digi:link href="/teamMemberReports.do" name="urlParams" styleClass="sub-nav2" 
												title="${translation}" >
													<digi:trn key="aim:memberRepor">
														Member Reports
													</digi:trn>
												</digi:link>												
												&nbsp;
											</td>										
										</tr>
									</table>									
								</td>
							</tr>							
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellpadding="0" cellspacing="0" width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:reportListManager">
															Report List Manager
															</digi:trn>
														</td>
														<td background="ampModule/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>


										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border="0" cellpadding="1" cellspacing="1" class=box-border width="100%">
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center">
															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#dddddd">
																	<c:if test="${!empty aimMemberReportsForm.reports}">	
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																	</c:if>
																</td>
																<td valign="center" align="center" bgcolor="#dddddd">
																	<b><digi:trn key="aim:reportList">Report List</digi:trn>
																	for the member <bean:write name="aimMemberReportsForm" 
																	property="memberName" /></b>
																</td></tr>
															</table>														
														</td>
													</tr>
													<logic:empty name="aimMemberReportsForm" property="reports">
													<tr>
														<td align="center">
															<span class="note">
															<digi:trn key="aim:noReportsPresent">
															No reports present
															</digi:trn></span>
														</td>
													</tr>	
													<tr>
														<td align="center">
															<html:submit  styleClass="dr-menu" property="addReport">
																<digi:trn key="btn:addReport">Add report</digi:trn> 
															</html:submit>
															
														</td>
													</tr>	
													</logic:empty>

													<logic:notEmpty name="aimMemberReportsForm" property="reports">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellspacing="1" cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd">
															<logic:iterate name="aimMemberReportsForm" property="reports" id="reports" 
															type="org.digijava.ampModule.aim.dbentity.AmpReports">
																<tr><td width=3 bgcolor="#f4f4f2">
																<html:multibox property="selReports">
																	<bean:write name="reports" property="ampReportId" />
																</html:multibox>
																</td>
																<td bgcolor="#f4f4f2">
																	<bean:write name="reports" property="name" />
																</td></tr>
															</logic:iterate>
															</table>
														</td>
													</tr>													
													<tr>
														<td align="center" colspan=2>
															<table cellspacing="5">
																<tr>
																	<td>
																		<html:submit  styleClass="dr-menu" property="addReport">
																			<digi:trn key="btn:addReport">Add report</digi:trn> 
																		</html:submit>
																	</td>
																	<td>	
																		<html:submit  styleClass="dr-menu" property="submitButton"  onclick="return confirmDelete()">
																			<digi:trn key="btn:removeSelectedReports">Remove selected reports</digi:trn> 
																		</html:submit>
																		
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>
								&nbsp;
							</td></tr>
						</table>			
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

</digi:form>





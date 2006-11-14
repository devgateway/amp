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
	function listReports()
	{
		document.aimTeamReportsForm.addReport.value="List of Unassigned Reports";
		document.aimTeamReportsForm.action="/updateTeamReports.do";		
		document.aimTeamReportsForm.submit();
	}


	function checkall() {
		var selectbox = document.aimTeamReportsForm.checkAll;
		var items = document.aimTeamReportsForm.selReports;
		if (document.aimTeamReportsForm.selReports.checked == true || 
							 document.aimTeamReportsForm.selReports.checked == false) {
				  document.aimTeamReportsForm.selReports.checked = selectbox.checked;
		} else {
			for(i=0; i<items.length; i++){
				document.aimTeamReportsForm.selReports[i].checked = selectbox.checked;
			}
		}
	}

	function validate() {
		if (document.aimTeamReportsForm.selReports.checked != null) {
			if (document.aimTeamReportsForm.selReports.checked == false) {
				alert("Please choose a report to remove");
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
				alert("Please choose a report to remove");
				return false;					  
			}				  
		}
		return true;			  
	}

-->

</script>

<digi:instance property="aimTeamReportsForm" />
<digi:form action="/updateTeamReports.do" method="post">
<html:hidden property="addReport"/>
<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>

			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
						</bean:define>
						<digi:link href="/viewMyDesktop.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:portfolio">Portfolio</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here to view Team Workspace Setup</digi:trn>
						</bean:define>
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:reportList">Report List</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>Team Workspace Setup</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
									<jsp:include page="teamSetupMenu.jsp" flush="true" />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr><td>
										<digi:errors />
										</td></tr>
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width=167>
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title width=150>
															<digi:trn key="aim:reportListWorkspace">List of Reports in the Workspace</digi:trn>
														</td>
														<td background="module/aim/images/corner-r.gif" height="17" width=17>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border valign="top">
												<table border=0 cellPadding=0 cellSpacing=1 class=box-border-nopadding width="100%">
													<tr>
														<td align="left" width="100%" valign="center">
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#ffffff">
																<tr><td width=3 bgcolor="#dddddd">
																	<input type="checkbox" name="checkAll" onclick="checkall()">
																</td>
																<td valign="center" align="center" bgcolor="#dddddd">
																	<b><digi:trn key="aim:reportListWorkspace">List of Reports in the Workspace</digi:trn></b>
																</td>
															</table>
														</td>
													</tr>
													<logic:empty name="aimTeamReportsForm" property="reports">
													<tr bgColor=#f4f4f2>
														<td align="center">
															<digi:trn key="aim:noReportsPresent">No reports present</digi:trn>
														</td>
													</tr>	
													<tr><td>&nbsp;</td></tr>
													<tr>
														<td align="center">
															<html:submit styleClass="dr-menu" value="List of Unassigned Reports" property="addReport" />
														</td>
													</tr>	
													</logic:empty>							
													<logic:notEmpty name="aimTeamReportsForm" property="reports">
													<tr>
														<td>
															<table width="100%" cellSpacing=1 cellPadding=2 vAlign="top" align="left"
															bgcolor="#dddddd">
															<logic:iterate name="aimTeamReportsForm" property="reports" id="reports" 
															type="org.digijava.module.aim.helper.ReportsCollection">
																<bean:define id="ampReports" name="reports" property="report" 
																type="org.digijava.module.aim.dbentity.AmpReports" />
																<tr bgColor=#f4f4f2>
																	<td width="3%">
																		<html:multibox property="selReports" >
																		<bean:write name="ampReports" property="ampReportId" />
																		</html:multibox>
																	</td>
																	<td width="73%">
																		<bean:write name="ampReports" property="name" />
																	</td>
																	<jsp:useBean id="urlParams" type="java.util.Map" 
																	class="java.util.HashMap"/>
																	<c:set target="${urlParams}" property="id">
																		<bean:write name="ampReports" property="ampReportId" />
																	</c:set>
																	<logic:equal name="reports" property="teamView" value="false">
																		<td width="3%">
																			<font color="blue">
																			<digi:trn key="aim:reportMemberSpecific">*M</digi:trn>
																			</font>
																		</td>
																		<c:set target="${urlParams}" property="status" value="team" />
																		<td width="24%" align="center">
																			[
																			<bean:define id="translation">
																				<digi:trn key="aim:clickToMakeThisPublic">Click here to make this public</digi:trn>
																			</bean:define>
																			<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="<%=translation%>" >Make this public</digi:link>
																			]
																		</td>
																	</logic:equal>
																	<logic:equal name="reports" property="teamView" value="true">
																		<td width="3%">
																			<font color="blue">
																			<digi:trn key="aim:reportTeamSpecific">*T</digi:trn>
																			</font>
																		</td>
																		<c:set target="${urlParams}" property="status" value="member" />
																		<td width="24%" align="center">
																			[
																			<bean:define id="translation">
																				<digi:trn key="aim:clickToMakeThisPrivate">Click here to make this private</digi:trn>
																			</bean:define>
																			<digi:link href="/changeTeamReportStatus.do" name="urlParams" title="<%=translation%>" >
																			Make this private</digi:link>
																			]
																		</td>
																	</logic:equal>
																	</td>
																</tr>
															</logic:iterate>
															</table>
														</td>
													</tr>
													<tr>
														<td align="left">
															<table cellspacing="5" width="100%">
																<tr>
																	<td align="left">
													                    <a href="javascript:listReports()"> List of Unassigned Reports </a>
																	</td>
																	<td >	
																		<html:submit styleClass="dr-menu" value="Remove selected reports" 
																		property="removeReports" onclick="return validate()"/>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													</logic:notEmpty>
												</table>
											</td>

										</tr>
										<tr>
											<td valign="top">
												<table width="100%">
													<tr>
														<td>
															<font color="blue">
															<digi:trn key="aim:reportTeamSpecific">*T</digi:trn>
															Indicates the report is viewable by all team members.
															</font>
														</td>
													</tr>
													<tr>
													<td>
															<font color="blue">
															<digi:trn key="aim:reportMemberSpecific">*M</digi:trn>
															Indicates the report is viewable by selected team members, 
															whom the team leader assigns.
															</font>
														</td>
													</tr>
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
</td></tr>
</table>

</digi:form>


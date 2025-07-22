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

<table width="100%" cellpadding="0" cellspacing="0" border="0">
<tr>
	<td valign="top" align="left" width="100%">
		<jsp:include page="teamPagesHeader.jsp"  />
	<td>
</tr>
<tr>
	<td>
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
						<digi:trn key="aim:configureTeam">
						Configure Team
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=650><span class=subtitle-blue>
						<digi:trn key="aim:configureTeam">
						Configure Team
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=650 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%">	
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=7 cellSpacing=7 width="100%" >
													<tr><td>
													<c:set var="translation">
														<digi:trn key="aim:clickToViewTeamWorkspaceSetup">Click here view Team Workspace Setup</digi:trn>
													</c:set>
													<digi:link href="/workspaceOverview.do" name="bcparams" title="${translation}" >
														<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
													</digi:link>												
													</td></tr>												
													<tr><td>													
													<c:set var="translation">
														<digi:trn key="aim:clickToManageActivityList">Click here to Manage Activity List</digi:trn>
													</c:set>
													<digi:link href="/teamActivityList.do" title="${translation}" >
														<digi:trn key="aim:manageActivityList">Manage activity list</digi:trn>
													</digi:link>													
													</td></tr>
													<tr><td>
			<c:set var="translation">
				<digi:trn key="aim:clickToManageReports">Click here to Manage Reports</digi:trn>
			</c:set>
			                  				<digi:link href="/teamReportList.do" title="${translation}" >
									  					<digi:trn key="aim:manageReports">Manage reports</digi:trn>
		            	      				</digi:link>
													</td></tr>
													<tr><td>
		<c:set var="translation">
			<digi:trn key="aim:clickToManageDocuments">Click here to Manage Documents</digi:trn>
		</c:set>
		                  					<digi:link href="/workspaceOverview.do" title="${translation}" >
			                						Manage documents
						              			</digi:link>
													</td></tr>
													<tr><td>													
													<c:set var="translation">
														<digi:trn key="aim:clickToManageMembers">Click here to Manage Members</digi:trn>
													</c:set>
													<digi:link href="/teamMemberList.do" title="${translation}" >
														Manage members
													</digi:link>							
													</td></tr>
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
	</td>
</tr>
</table>




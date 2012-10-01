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

<digi:instance property="aimTeamPagesForm" />

<table cellspacing="0" cellpadding="0" vAlign="top" align="left" width="100%">
<tr><td width="100%">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>
<tr><td>
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
						<digi:trn key="aim:teamPagesConfigure">
						Configure Team Pages
						</digi:trn>
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" width="100%"
						valign="top" align="left">
							<tr>
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="5" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp"  />
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td>&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align="center" bgColor=#f4f4f2 cellpadding="0" cellspacing="0" width="90%">
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border="0" cellPadding=3 cellspacing="1" class=box-border width="100%" >
													<tr bgColor=#dddddb>
														<td bgColor=#dddddb align="center" height="20">
															<b>
															<digi:trn key="aim:configurablePages">
															List of configurable team pages
															</digi:trn></b>
														</td>
													</tr>
														<logic:notEmpty name="aimTeamPagesForm" property="pages">
															<logic:iterate name="aimTeamPagesForm" id="pages" property="pages"
															type="org.digijava.module.aim.dbentity.AmpPages">
															<tr bgColor=#f4f4f2><td height="20">
																<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																<c:set target="${urlParams}" property="pageId">
																	<bean:write name="pages" property="ampPageId" />
																</c:set>
																<c:set var="translation">
																	<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Page</digi:trn>
																</c:set>
																<digi:link href="/showConfigureTeam.do" name="urlParams" title="${translation}" >
																<digi:trn key="aim:${pages.pageName}"><bean:write name="pages" property="pageName" /></digi:trn></digi:link>
															</td></tr>
															</logic:iterate>
														</logic:notEmpty>
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




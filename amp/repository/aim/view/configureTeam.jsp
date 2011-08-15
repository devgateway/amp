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

<digi:form action="/saveTeamPageConfiguration.do">
<html:hidden property="pageId" />

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
						<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}">
						<digi:trn key="aim:teamWorkspaceSetup">
						Team Workspace Setup
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<c:set var="translation">
							<digi:trn key="aim:clickToConfigureTeamPages">Click here to Configure Team Pages</digi:trn>
						</c:set>
						<digi:link href="/configureTeamPage.do" styleClass="comment" title="${translation}" >
						<digi:trn key="aim:teamPagesConfigure">
						Configure Team Pages
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:write name="aimTeamPagesForm" property="pageName" />
					</td>
				</tr>
				<tr>
					<td height=16 valign="center" width=571><span class=subtitle-blue><digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></span>
					</td>
				</tr>
				<tr>
					<td noWrap width=571 vAlign="top">
						<table bgColor=#ffffff cellpadding="0" cellspacing="0" class=box-border-nopadding width="100%"
						valign="top" align="left">
							<tr bgColor=#3754a1>
								<td vAlign="top" width="100%">
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
														<td bgColor=#dddddb align="center" height="20" colspan="2">
															<b>
															<digi:trn key="aim:configureFiltersFor">
															Configure Filters for
															</digi:trn>&nbsp;<digi:trn key="aim:${aimTeamPagesForm.pageName}"> <bean:write name="aimTeamPagesForm" property="pageName" /></digi:trn></b>
														</td>
													</tr>

													<logic:equal name="aimTeamPagesForm" property="updated" value="true">
													<tr>
														<td align="center" colspan="2">
															<font color="blue"><b><digi:trn key="aim:updateToAMPComplete">
																Update to AMP complete
															</digi:trn></b></font>
														</td>
													</tr>
													</logic:equal>

														<logic:notEmpty name="aimTeamPagesForm" property="filters">
															<logic:iterate name="aimTeamPagesForm" id="filters" property="filters"
															type="org.digijava.module.aim.dbentity.AmpFilters">
															<tr bgColor=#f4f4f2>
																<td width="3%">
																	<html:multibox property="selFilters">
																		<bean:write name="filters" property="ampFilterId" />
																	</html:multibox>
																</td>
																<td height="20" width="97%">
                                                                                                                                <digi:trn key="aim:${filters.filterName}">
																	<bean:write name="filters" property="filterName" />
                                                                                                                                        </digi:trn>
																</td>
															</tr>
															</logic:iterate>

															<tr><td colspan="2" align="center">
																<table cellPadding="3" cellPadding="3">
																	<tr>
																		<td>
																			<html:submit value="  Save  " styleClass="dr-menu" />
																		</td>
																		<td>
																			<html:reset value="Cancel" styleClass="dr-menu" onclick="javascript:history.go(-1)" />
																		</td>
																	</tr>
																</table>
															</td></tr>
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
</digi:form>




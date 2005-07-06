	<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimTeamActivitiesForm" />
<digi:context name="digiContext" property="context" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=872 border=0>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=850>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to go to Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
						</bean:define>
						<digi:link href="/workspaceManager.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:workspaceManager">
						Workspace Manager
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:activities">
						Activities
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<bean:write name="aimTeamActivitiesForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=700 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
							<tr bgColor=#f4f4f2>
								<td vAlign="top" width="100%">
									&nbsp;
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top">
									<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="95%" border=0>	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2>
														<td bgColor=#c9c9c7 class=box-title height=20 align="center">
															<!-- Table title -->
															<digi:trn key="aim:activitiesFor">Activities for </digi:trn>
															<bean:write name="aimTeamActivitiesForm" property="teamName" />
															<!-- end table title -->
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<logic:empty name="aimTeamActivitiesForm" property="activities">
													<table width="100%" border="0">
														<tr>
															<td>
																<b>No activity present</b>
															</td>
														</tr>
													</table>	
												</logic:empty>
												<logic:notEmpty name="aimTeamActivitiesForm" property="activities">
													<table width="100%" border="0">
														<tr>
															<td bgColor=#dddddb colspan="2" height="20">
															<b>
																<digi:trn key="aim:activity">
																Activity
																</digi:trn>
															</b>
															</td>
														</tr>
													<logic:iterate name="aimTeamActivitiesForm" property="activities" id="activities" 
													type="org.digijava.module.aim.helper.Activity">
													<tr>
														<td bgcolor=#f4f4f2 height="20" width="80%">
															<bean:write name="activities" property="name" />
														</td>
														<td bgcolor=#f4f4f2 height="20" align="center">
															<jsp:useBean id="urlParams1" type="java.util.Map" class="java.util.HashMap"/>
															<c:set target="${urlParams1}" property="id">
																<bean:write name="activities" property="activityId" />
															</c:set>
															<bean:define id="translation">
																<digi:trn key="aim:clickToRemoveActivity">Click here to Remove Activity</digi:trn>
															</bean:define>
															[ <digi:link href="/removeTeamActivity.do" name="urlParams1" title="<%=translation%>" >
															<digi:trn key="aim:removeActivity">Remove activity</digi:trn>
															</digi:link> ]
														</td>
													</tr>
													</logic:iterate>
													</table>
												</logic:notEmpty>
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
					<td noWrap width=100% vAlign="top">
						<table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>	
							<tr>
								<td>
									<!-- Other Links -->
									<table cellPadding=0 cellSpacing=0 width=100>
										<tr>
											<td bgColor=#c9c9c7 class=box-title>
												<digi:trn key="aim:otherLinks">
												Other links
												</digi:trn>
											</td>
											<td background="module/aim/images/corner-r.gif" height="17" width=17>
												&nbsp;
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td bgColor=#ffffff class=box-border>
									<table cellPadding=5 cellSpacing=1 width="100%">
										<tr>
											<td>
												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams}" property="id">
												<bean:write name="aimTeamActivitiesForm" property="teamId" />
												</c:set>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToAssignActivity">Click here to Assign Activity</digi:trn>
												</bean:define>
												<digi:link href="/assignActivity.do" name="urlParams" title="<%=translation%>" >
													<digi:trn key="aim:assignAnActivity">Assign an activity</digi:trn>
												</digi:link>					
											</td>
										</tr>
										<tr>
											<td>
												<digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
												<bean:define id="translation">
													<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
												</bean:define>
												<digi:link href="/admin.do" title="<%=translation%>" >
												<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
												</digi:link>
											</td>
										</tr>
										<!-- end of other links -->
									</table>
								</td>
							</tr>
						</table>
					</td></tr>
					
					</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

	function deleteIndicator()
	{
		return confirm("Do you want to delete the Activity ?");
	}
	function load() {}

	function unload() {}

-->
</script>

<digi:instance property="aimActivityForm" />
<digi:form action="/activityManager.do" method="post">

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top width=750>
			<table cellPadding=5 cellSpacing=0 width="100%" border=0>
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb>
						<bean:define id="translation">
							<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
						</bean:define>
						<digi:link href="/admin.do" styleClass="comment" title="<%=translation%>" >
						<digi:trn key="aim:AmpAdminHome">
							Admin Home
						</digi:trn>
						</digi:link>&nbsp;&gt;&nbsp;
						<digi:trn key="aim:activityManager">
							Activity Manager
						</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<tr>
					<td height=16 vAlign=center width=571>
						<span class=subtitle-blue>
						<digi:trn key="aim:activityManager">
							Activity Manager
						</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1 border=0>
					<tr><td noWrap width=600 vAlign="top">
						<table bgColor=#d7eafd cellPadding=1 cellSpacing=1 width="100%" valign="top">
							<tr bgColor=#ffffff>
								<td vAlign="top" width="100%">
									
									<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
										<tr><td bgColor=#d7eafd class=box-title height="20" align="center">
											<!-- Table title -->
											<digi:trn key="aim:activityList">
												Activity List
											</digi:trn>
											<!-- end table title -->										
										</td></tr>
										<tr><td>
											<table width="100%" cellspacing=1 cellpadding=4 valign=top align=left bgcolor="#ffffff">

													<logic:notEmpty name="aimActivityForm" property="activityList">
														<tr><td>
															<table width="100%" cellspacing=1 cellpadding=3 bgcolor="#d7eafd">
																<tr bgcolor="#ffffff">
																		<td width="9" height="15">&nbsp;</td>
																		<td><b>
																				Activity Name</b>
																		</td>
																		<td width="100"><b>
																				Activity Id</b>
																		</td>
																		<td align="left" width="12">&nbsp;</td>
																</tr>
															
																<logic:iterate name="aimActivityForm" property="activityList" id="activities"
																type="org.digijava.module.aim.dbentity.AmpActivity">	

																	<tr bgcolor="#ffffff">
																	<td width="9" height="15">
																		<img src= "../ampTemplate/images/arrow_right.gif" border=0>
																	</td>																	
																	<td>
																		<bean:write name="activities" property="name"/>
																	</td>
																	<td width="100">
																		<bean:write name="activities" property="ampId"/>
																	</td>																	
																	<td align="left" width="12">
																		<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
																		<c:set target="${urlParams}" property="id">
																			<bean:write name="activities" property="ampActivityId"/>
																		</c:set>
																		<c:set target="${urlParams}" property="action" value="delete"/>
																		<bean:define id="translation">
																			<digi:trn key="aim:clickToDeletetheActivity">Click here to Delete Activity</digi:trn>
																		</bean:define>
																		<digi:link href="/activityManager.do" name="urlParams" 
																		onclick="return deleteIndicator()" title="<%=translation%>" >
																		<img src= "../ampTemplate/images/trash_12.gif" border=0>
																		</digi:link>
																	</td>
																	</tr>
																</logic:iterate>
															</table>
														</td></tr>
													</logic:notEmpty>
													<logic:empty name="aimActivityForm" property="activityList">
														<tr align="center" bgcolor="#ffffff"><td><b>
															No activities present</b></td>
														</tr>
													</logic:empty>
											</table>
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

</digi:form>

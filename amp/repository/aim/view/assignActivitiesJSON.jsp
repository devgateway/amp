<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="aimAssignActivityForm" />
<digi:form action="/assignActivity.do" method="post" onsubmit="return validate()">

<html:hidden property="teamId" />

<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->




<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=450 border=0>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=450>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=16 vAlign=top width=450><span class=subtitle-blue>
						<bean:write name="aimAssignActivityForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=450 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">&nbsp;
									
								</td>
							</tr>
							<tr>
								<td valign="top">
									<table align=center cellPadding=0 cellSpacing=0 width="95%" border=0 style="table-layout: fixed">	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2>
														<td width="5%" height=20 align="center" bgColor=#d7eafd class=box-title>
															<!-- Table title -->
													    <input type="checkbox" name="checkAll" onclick="checkall()">
															<!-- end table title -->
														</td>
													    <td width="95%" align="center" bgColor=#d7eafd class=box-title><digi:trn key="aim:assignActivityTo">Assign Activities to</digi:trn>
                                                          <bean:write name="aimAssignActivityForm" property="teamName" /></td>
													</tr>
												</table>
										  </td>
										</tr>
										<tr>
											<td align="center" bgColor=#ffffff class=box-border>
												<logic:empty name="aimAssignActivityForm" property="activities">
													<b><digi:trn key="aim:noActivitiesToAssign">No activities to assign</digi:trn>
													</b>
											  </logic:empty>
												<logic:notEmpty name="aimAssignActivityForm" property="activities">
														<div id="demo" style="overflow: auto; width: 100%; height: 220px; max-height: 220px;">
														<table width="100%" cellpadding=5 cellspacing=0 border=0>
														<logic:iterate name="aimAssignActivityForm" property="activities" id="activities">
															<tr>
																<td align="right" width="10%">
																	<html:multibox property="selectedActivities" >
																		<bean:write name="activities" property="ampActivityId" />
																	</html:multibox>
																</td>
																<td align="left" width="90%" style="word-wrap: break-word;">
																	<bean:write name="activities" property="name" />
																</td>
															</tr>
														</logic:iterate>
														</table>
														</div>
												</logic:notEmpty>
											</td>
									  	</tr>
									  	<tr>
									  	<td>
											<table width="100%" cellpadding=5 cellspacing=0 border=0>
											<tr>
												<td align="center" colspan="2">
													<input class="dr-menu" type="button" onclick="assignActivityList();" value="<digi:trn>Assign</digi:trn>"/>
													<input class="dr-menu" type="button" onclick="myPanel.hide();" value="<digi:trn>Cancel</digi:trn>"/>
												</td>
											</tr>
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
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</digi:form>





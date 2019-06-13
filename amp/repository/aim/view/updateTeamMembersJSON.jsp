<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<digi:instance property="aimTeamMemberForm" />
<digi:form action="/updateTeamMember.do" method="post" onsubmit="return validate()">

<html:hidden property="teamId" />
<html:hidden property="teamMemberId" />
<html:hidden property="action" />
<html:hidden property="userId" />
<html:hidden property="name" />
<div id="someError"><digi:errors/></div>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=380>
	<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=380 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%" style="border-bottom:2px solid #ccc;"valign="middle" >
									<span class="subtitle-blue member-form-title" >
                                    <bean:write name="aimTeamMemberForm" property="teamName" /></span>
                                    <div class="adminicon"><img src="/TEMPLATE/ampTemplate/img_2/adminicons/usermanager.jpg"/></div>
								</td>
							</tr>
							<tr >
								<td valign="top" style="padding:10px 0px;border-bottom:2px solid #ccc;">
									
								<logic:equal name="aimTeamMemberForm" property="action" value="edit"><digi:trn key="aim:editTeamMembers">Edit Members</digi:trn></logic:equal>
								<h3><logic:equal name="aimTeamMemberForm" property="action" value="delete"><digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn></logic:equal></h3>
								</td></tr>
								<tr>
									<td bgColor=#ffffff class=box-border colspan="2">
												<table border=0 cellPadding=5 cellSpacing=1 class=box-border width="100%">
													<tr>
														<td class="left-align" width="40%" style="font-weight:bold;">
															<digi:trn key="aim:memberName">Name</digi:trn> &nbsp;&nbsp; &nbsp;
														</td>
														<td class="left-align"" width="60%">
															<bean:write name="aimTeamMemberForm" property="name" />
															<html:hidden name="aimTeamMemberForm" property="someError" />
														</td>
													</tr>
													<tr>
														<td class="left-align" width="40%" style="font-weight:bold;">
															<digi:trn key="aim:memberRole">Role</digi:trn>&nbsp;&nbsp; &nbsp;&nbsp;	
														</td>
														<td class="left-align" width="60%">
															<html:select property="role">
																<%@include file="teamMemberRolesDropDown.jsp" %>
															</html:select>
														</td>
													</tr>
													<tr>
														<td colspan="2" width="40%"  style="border-top:2px solid #ccc;">
															<logic:equal name="aimTeamMemberForm" property="action" value="delete">
															<table width="100%" cellspacing="5">
																<tr>
																	<td width="50%" class="right-align">
<!--																		<html:submit styleClass="dr-menu"	property="apply">-->
<!--																			<digi:trn key="btn:delete">Delete</digi:trn>-->
<!--																		</html:submit>						-->
																		<input class="dr-menu" type="button" value="<digi:trn>Delete</digi:trn>" name="apply" onclick="confirmActionMember()"/>
																	</td>
																	<td width="50%" class="left-align">
																		<html:reset  styleClass="dr-menu" property="submitButton" onclick="hideWindow()">
																			<digi:trn key="btn:cancel">Cancel</digi:trn>
																		</html:reset>																					</td>
																	</td>
																</tr>
															</table>
															</logic:equal>
															<logic:equal name="aimTeamMemberForm" property="action" value="edit">
															<table width="100%" cellspacing="5">
																<tr>
																	<td width="50%" class="right-align">
<!--																		<html:submit styleClass="dr-menu" property="apply">-->
<!--																			<digi:trn key="btn:save">Save</digi:trn>-->
<!--																		</html:submit>						-->
																		<input class="dr-menu" type="button" value="<digi:trn>Save</digi:trn>" name="apply" onclick="confirmActionMember()"/>
																	</td>
																	<td width="50%" class="left-align"">
																		<html:reset  styleClass="dr-menu" property="submitButton" onclick="hideWindow()">
																			<digi:trn key="btn:cancel">Cancel</digi:trn>
																		</html:reset>
																	</td>
																</tr>
															</table>
															</logic:equal>
														</td>
													</tr>

												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr><td>&nbsp;
								
							</td></tr>
						</table>
					</td>
</tr>
					</table>
					</td>
		
	</tr>
</table>
</digi:form>


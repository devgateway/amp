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
<digi:errors/>
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=350>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=350>
			<table cellPadding=5 cellSpacing=0 width="100%">
				<tr>
					<td height=16 vAlign=center width=350><span class=subtitle-blue>
						<bean:write name="aimTeamMemberForm" property="teamName" />
					</span></td>
				</tr>
				<tr>
					<td noWrap width=100% vAlign="top">
					<table width="100%" cellspacing=1 cellSpacing=1>
					<tr><td noWrap width=350 vAlign="top">
						<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="100%">
							<tr>
								<td vAlign="top" width="100%">&nbsp;
									
								</td>
							</tr>
							<tr>
								<td valign="top">
									<table align=center cellPadding=0 cellSpacing=0 width="95%" border=0>	
										<tr bgColor=#f4f4f2>
											<td bgColor=#f4f4f2>
												<table border="0" cellPadding=0 cellSpacing=0 width="100%">
													<tr bgColor=#f4f4f2 height="20">
														<td bgColor=#c9c9c7 class=box-title width=100 align="center">
																<logic:equal name="aimTeamMemberForm" property="action" value="edit">
																	<digi:trn key="aim:editTeamMembers">Edit Members</digi:trn>	
																</logic:equal>
																<logic:equal name="aimTeamMemberForm" property="action" value="delete">
																	<digi:trn key="aim:deleteTeamMembers">Delete Members</digi:trn>	
																</logic:equal>
														</td>
                                                        <td bgColor=#c9c9c7 class=box-title align="right">
                                                        	<div class="adminicon" style="float: right;"><img src="/TEMPLATE/ampTemplate/img_2/adminicons/usermanager.jpg"/></div>
                                                        </td>
                                                    </tr>
												</table>
											</td>
										</tr>
										<tr>
											<td bgColor=#ffffff class=box-border>
												<table border=0 cellPadding=5 cellSpacing=1 class=box-border width="100%">
													<tr>
														<td align="right" width="50%">
															<digi:trn key="aim:memberName">Name &nbsp;&nbsp; &nbsp;</digi:trn>
														</td>
														<td align="left" width="50%">
															<bean:write name="aimTeamMemberForm" property="name" />
															<html:hidden name="aimTeamMemberForm" property="someError" />
														</td>
													</tr>
													<tr>
														<td align="right" width="50%">
															<digi:trn key="aim:memberRole">Role&nbsp;&nbsp; &nbsp;&nbsp;</digi:trn>	
														</td>
														<td align="left" width="50%">
															<html:select property="role">
																<%@include file="teamMemberRolesDropDown.jsp" %>
															</html:select>
														</td>
													</tr>
													<tr>
														<td colspan="2" width="60%">
															<logic:equal name="aimTeamMemberForm" property="action" value="delete">
															<table width="100%" cellspacing="5">
																<tr>
																	<td width="50%" align="right">
<!--																		<html:submit styleClass="dr-menu"	property="apply">-->
<!--																			<digi:trn key="btn:delete">Delete</digi:trn>-->
<!--																		</html:submit>						-->
																		<input class="dr-menu" type="button" value="<digi:trn>Delete</digi:trn>" name="apply" onclick="confirmActionMember()"/>
																	</td>
																	<td width="50%" align="left">
																		<html:reset  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
																			<digi:trn key="btn:cancel">Cancel</digi:trn>
																		</html:reset>																					</td>
																	</td>
																</tr>
															</table>
															</logic:equal>
															<logic:equal name="aimTeamMemberForm" property="action" value="edit">
															<table width="100%" cellspacing="5">
																<tr>
																	<td width="50%" align="right">
<!--																		<html:submit styleClass="dr-menu" property="apply">-->
<!--																			<digi:trn key="btn:save">Save</digi:trn>-->
<!--																		</html:submit>						-->
																		<input class="dr-menu" type="button" value="<digi:trn>Save</digi:trn>" name="apply" onclick="confirmActionMember()"/>
																	</td>
																	<td width="50%" align="left">
																		<html:reset  styleClass="dr-menu" property="submitButton" onclick="closeWindow()">
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
		</td>
	</tr>
</table>
</digi:form>


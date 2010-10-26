<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		750px;
	background-color: #f4f4f2;
}
</style>
<script language="javascript">
  function checkRole(  ) {
    var selectedRole = aimTeamMemberForm.role.value;
    var wmRole = aimTeamMemberForm.workspaceManId.value;
    var idWMMmemeber = aimTeamMemberForm.headId.value;
    var idCurrentMember = aimTeamMemberForm.teamMemberId.value;
    var idUpdaterMember = aimTeamMemberForm.memberId.value;
    var selectedRole=document.getElementById("selRole");
    if(isEmpty(selectedRole.value) == true){
        var msg='<digi:trn>Please Select Role</digi:trn>';
    	alert(msg);
    	return false;
    }
    if(idWMMmemeber!= idUpdaterMember){
        <c:set var="translation">
        <digi:trn>The Workspace Member does not have rights to change the role.</digi:trn>
        </c:set>   			 
        alert("${translation}");
        return false;
    } else if(selectedRole== wmRole && idWMMmemeber!="" && idWMMmemeber!=idCurrentMember ){
        <c:set var="translation">
        <digi:trn key="aim:OneManagerPerWorkspace">Only one Manager is allowed per workspace. Please choose another role or keep the existing one.</digi:trn>
       </c:set>   			 
        alert("${translation}");
        return false;
    } else {
        var indice = aimTeamMemberForm.role.selectedIndex;
        var valor = aimTeamMemberForm.role.options[indice].text;
        <c:set var="translation">
        <digi:trn key="aim:AreYouSureToChangerole">Are you sure you want to change this role to</digi:trn>
        </c:set>				 
        return confirm("${translation} \""+valor+"\"?");
    }
  }
</script>

<jsp:useBean id="bcparams" type="java.util.Map" class="java.util.HashMap"/>
<c:set target="${bcparams}" property="tId" value="-1"/>
<c:set target="${bcparams}" property="dest" value="teamLead"/>			

<digi:errors/>
<digi:instance property="aimTeamMemberForm" />
<digi:form action="/saveMemberDetails.do" method="post">
<digi:context name="digiContext" property="context" />

<html:hidden property="teamId" />
<html:hidden property="teamMemberId" />
<html:hidden property="action" />
<html:hidden property="userId" />
<html:hidden property="name" />
<html:hidden property="headId"/>
<html:hidden property="workspaceManId"/>
<html:hidden name="currentMember" property="memberId"/>



<table width="100%" cellPadding=0 cellSpacing=0 vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp" flush="true" />
</td></tr>
<tr><td width="100%" vAlign="top" align="left">
<table cellPadding=0 cellSpacing=0 width=780>
	<tr>
		<td width=14>&nbsp;</td>
		<td align=left vAlign=top width=750>

			<table cellPadding="5" cellSpacing="0" width="100%">
				<tr>
					<td height="33">
						<span class="crumb">
							<c:set var="translation">
								<digi:trn>Click here to view MyDesktop</digi:trn>
							</c:set>
							<digi:link href="/viewMyDesktop.do" styleClass="comment" title="${translation}" >
								<digi:trn>Portfolio</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<c:set var="translation">
								<digi:trn>Click here to view Team Workspace Setup</digi:trn>
							</c:set>
							<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="comment" title="${translation}" >
							<digi:trn>Team Workspace Setup</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<c:set var="translation">
								<digi:trn>Click here to view Members</digi:trn>
							</c:set>
							<digi:link href="/teamMemberList.do" styleClass="comment" title="${translation}" >
							<digi:trn>Members</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp;
							<digi:trn>Member Details</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td height=16 vAlign="middle" width="571">
						<span class="subtitle-blue">
							<digi:trn>Team Workspace Setup</digi:trn>
						</span>
					</td>
				</tr>
				<tr>
					<td noWrap width="571" vAlign="top">
						<table bgColor="#ffffff" cellPadding="0" cellSpacing="0" width="100%">
							<tr >
								<td vAlign="top" width="100%">
									<c:set var="selectedTab" value="1" scope="request"/>
									<jsp:include page="teamSetupMenu.jsp" flush="true" />								
								</td>
							</tr>
							<tr bgColor=#f4f4f2>
								<td valign="top" align="center">
                                <div class="contentbox_border" style="border-top:0px;padding: 20px 0px 20px 0px;">
									<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams}" property="id">
										<bean:write name="aimTeamMemberForm" property="teamMemberId" />
									</c:set>
										<table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%">	
										<tr>
											<td valign="top" align="left">
												<table border="0" cellPadding="3" cellSpacing="0" width="35%">
													<tr>
														<td bgcolor="#f4f4f2">
															<table width="100%" border="0" cellspacing="0" cellPadding="3" >
																<tr>
																	<td align="right" width="50%" bgcolor="#f4f4f2">
																		<digi:trn>Name</digi:trn>
																	</td>
																	<td align="left" width="50%" bgcolor="#f4f4f2">
																		<bean:write name="aimTeamMemberForm" property="name" />
																	</td>
																</tr>
																<tr>
																	<td align="right" width="50%" bgcolor="#f4f4f2">
																		<digi:trn>Role</digi:trn>
																	</td>
																	<td align="left" width="50%" bgcolor="#f4f4f2">
																		<html:select property="role" styleId="selRole">
																			<%@include file="teamMemberRolesDropDown.jsp" %>
																			<%-- <html:optionsCollection name="aimTeamMemberForm" 
																			property="ampRoles" value="ampTeamMemRoleId" label="role" /> --%>
																		</html:select>
																	</td>
																</tr>
																<tr>
                                                                	<td>&nbsp;
                                                                  	</td>
																</tr>
																<tr >
																	<td align="center" colspan="2">
																		<table width="100%" cellspacing="5">
																			<tr>
																				<td width="50%" align="right">
																					<c:set var="translation"><digi:trn>Save</digi:trn> </c:set>
																					<html:submit value="${translation}" styleClass="dr-menu" onclick="return checkRole()"/>
																				</td>	
																				<td width="50%" align="left">	
																					<c:set var="translation"><digi:trn>Cancel</digi:trn> </c:set>
																					<html:reset value="${translation}" styleClass="dr-menu" onclick="javascript:history.go(-1)"/>
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
                                </div>
								</td>
							</tr>
							<tr><td bgColor=#f4f4f2>&nbsp;
								
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





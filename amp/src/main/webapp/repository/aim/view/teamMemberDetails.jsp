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
        var msg='<digi:trn jsFriendly="true">Please Select Role</digi:trn>';
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



<table width="100%" cellpadding="0" cellspacing="0" vAlign="top" align="left">
<tr><td width="100%" vAlign="top" align="left">
<jsp:include page="teamPagesHeader.jsp"  />
</td></tr>

				<tr>
					<td noWrap vAlign="top">
						<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
							<tr>
								<td >
									<div class="breadcrump_cont">
										<span class="sec_name">
											<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn>
										</span>
										
										<span class="breadcrump_sep">|</span>
										<digi:link href="/viewMyDesktop.do" title="${translation}" styleClass="l_sm">
											<digi:trn key="aim:portfolio">Portfolio</digi:trn>
										</digi:link>
										<span class="breadcrump_sep"><b>�</b></span>
										<c:set var="translation">
											<digi:trn key="aim:clickToViewWorkspaceOverview">Click here to view Workspace Overview</digi:trn>
										</c:set>
										<digi:link href="/workspaceOverview.do" name="bcparams" styleClass="l_sm" title="${translation}">
										<digi:trn key="aim:teamWorkspaceSetup">Team Workspace Setup</digi:trn></digi:link>
										<span class="breadcrump_sep"><b>�</b></span>
										<c:set var="translation">
											<digi:trn>Click here to view Members</digi:trn>
										</c:set>
										<digi:link href="/teamMemberList.do" styleClass="l_sm" title="${translation}" >
											<digi:trn>Members</digi:trn>
										</digi:link>
										<span class="breadcrump_sep"><b>�</b></span>
										<span class="bread_sel"><digi:trn>Member Details</digi:trn></span>
									</div>
									
								</td>
							</tr>
							<tr >
								<td vAlign="top" width="100%">
									
									<c:set var="selectedTab" value="1" scope="request"/>
										
										<div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all">
									<jsp:include page="teamSetupMenu.jsp"  />								
									
										
								
									
									
                                
									<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams}" property="id">
										<bean:write name="aimTeamMemberForm" property="teamMemberId" />
									</c:set>
									<div align="left">
												<table border="0" cellPadding="3" cellSpacing="0" width="200px" style="border:0px;" class="inside">
													<tr>
														<td class="inside" style="border:0px;">
															<digi:trn>Name</digi:trn>
														</td>
														<td class="inside" style="border:0px;padding:0px 0px 0px 5px;text-indent:5px;" align="left">
															<bean:write name="aimTeamMemberForm" property="name" />
														</td>
													</tr>
													<tr>
														<td class="inside" style="border:0px;">
															<digi:trn>Role</digi:trn>
														</td>
														<td class="inside" style="border:0px;" align="left">
															<html:select property="role" styleId="selRole" styleClass="inputx insidex">
																<%@include file="teamMemberRolesDropDown.jsp" %>
															</html:select>
														</td>
													</tr>
													<tr >
														<td align="center" colspan="2">
																		<c:set var="translation"><digi:trn>Save</digi:trn> </c:set>
																		<html:submit value="${translation}" styleClass="buttonx_sm btn_save" onclick="return checkRole()"/>

																		<c:set var="translation"><digi:trn>Cancel</digi:trn> </c:set>
																		<html:reset value="${translation}" styleClass="buttonx_sm btn_save" onclick="javascript:history.go(-1)"/>
														</td>
													</tr>
												</table>
									</div>
															
															
															
</td>
							</tr>
						</table>
						</div>
						</div>												
											
											
											
		</td>
	</tr>
</table>
</td></tr>
</table>

</digi:form>





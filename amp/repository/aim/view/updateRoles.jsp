<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
<!--

function permissionCheck()
{
  if(document.aimUpdateRoleForm.readPermission.checked == true)
  return true;
  else
  {
    alert("Read Permission is default all members. Please tick the box for 'Read'.");
    return false;
  }
}

function cancel(){
  aimUpdateRoleForm.action="${contextPath}/aim/roles.do";
  aimUpdateRoleForm.target = "_self";
  aimUpdateRoleForm.submit();
}

function load() {}

function unload() {}


-->
</script>

<html:javascript formName="aimUpdateRoleForm"/>

<digi:context name="digiContext" property="context" />
<digi:instance property="aimUpdateRoleForm" />

<digi:form action="/updateRole.do" method="post" name="aimUpdateRoleForm" type="org.digijava.module.aim.form.UpdateRoleForm" onsubmit="return validateAimUpdateRoleForm(this);">

  <html:hidden property="roleId" />
  <html:hidden property="action" />

  <jsp:include page="teamPagesHeader.jsp"  />
  <c:set var="btnSave">
    <digi:trn key="btn:save">Save</digi:trn>
  </c:set>
  <c:set var="btnCancel">
    <digi:trn key="btn:cancel">Cancel</digi:trn>
  </c:set>


  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
    <tr>
      <td class=r-dotted-lg width=14>&nbsp;</td>
      <td align=left class=r-dotted-lg vAlign=top width=750>
        <table cellPadding=5 cellSpacing=0 width="100%">
          <tr>
            <td height=33><span class=crumb>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <c:set var="translation">
                <digi:trn key="aim:clickToViewWorkspaceManager">Click here to view Workspace Manager</digi:trn>
              </c:set>
              <digi:link href="/workspaceManager.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:workspaceManager">
                Workspace Manager
                </digi:trn>
              </digi:link>&nbsp;&gt;&nbsp;
              <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                <digi:trn key="aim:addRole">Add Role</digi:trn>
              </logic:equal>
              <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                <digi:trn key="aim:editRole">Edit Role</digi:trn>
              </logic:equal>
              <logic:equal name="aimUpdateRoleForm" property="action" value="delete">
                <digi:trn key="aim:deleteRole">Delete Role</digi:trn>
              </logic:equal>
              <logic:equal name="aimUpdateRoleForm" property="action" value="view">
                <digi:trn key="aim:viewRole">View Role</digi:trn>
              </logic:equal>
            </td>
          </tr>
          <tr>
            <td height=16 vAlign=center width=571><span class=subtitle-blue>Workspace Manager</span>
      </td>
          </tr>
          <tr>
            <td noWrap width=100% vAlign="top">
              <table width="100%" cellspacing=1 cellSpacing=1>
                <tr><td noWrap width=600 vAlign="top">
                  <table bgColor=#ffffff cellPadding=0 cellSpacing=0 class=box-border-nopadding width="100%">
                    <tr bgColor=#f4f4f2>
                      <td vAlign="top" width="100%">&nbsp;
                      
                      </td>
                    </tr>
                    <tr bgColor=#f4f4f2>
                      <td valign="top">
                        <table align=center bgColor=#f4f4f2 cellPadding=0 cellSpacing=0 width="90%" border=0>
                          <tr bgColor=#f4f4f2>
                            <td bgColor=#f4f4f2>
                              <digi:errors />
                            </td>
                          </tr>
                          <tr bgColor=#f4f4f2>
                            <td bgColor=#f4f4f2>
                              <table border="0" cellPadding=0 cellSpacing=0 width=117>
                                <tr bgColor=#f4f4f2>
                                  <td bgColor=#c9c9c7 class=box-title width=100>
                                    <digi:trn key="aim:roles">
                                    Roles
                                    </digi:trn>
                                  </td>
                                  <td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
                                  
                                  </td>
                                </tr>
                              </table>
                            </td>
                          </tr>
                          <tr>
                            <td bgColor=#ffffff class=box-border>
                              <table border=0 cellPadding=3 cellSpacing=2 class=box-border width="100%">

                                <tr bgColor=#dddddb>
                                  <td bgColor=#dddddb align="center" colspan="2">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <digi:trn key="aim:addRole">Add Role</digi:trn>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <digi:trn key="aim:editRole">Edit Role</digi:trn>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="delete">
                                      <digi:trn key="aim:deleteRole">Delete Role</digi:trn>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="view">
                                      <digi:trn key="aim:viewRole">View Role</digi:trn>
                                    </logic:equal>
                                  </td>
                                </tr>



                                <tr>
                                  <td width="30%" align="right">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <FONT color=red>*</FONT>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <FONT color=red>*</FONT>
                                    </logic:equal>
                                    <digi:trn key="aim:roleName">Role</digi:trn>
                                  </td>
                                  <td width="30%" align="left">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="delete">
                                      <b><bean:write name="aimUpdateRoleForm" property="role" /></b>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="view">
                                      <b><bean:write name="aimUpdateRoleForm" property="role" /></b>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <html:text property="role" size="20" />
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <html:text property="role" size="20" />
                                    </logic:equal>
                                  </td>
                                </tr>
                                <tr>
                                  <td width="30%" align="right">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <FONT color=red>*</FONT>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <FONT color=red>*</FONT>
                                    </logic:equal>
                                    <digi:trn key="aim:description">Description</digi:trn>
                                  </td>
                                  <td width="30%" align="left">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="delete">
                                      <b><bean:write name="aimUpdateRoleForm" property="description" /></b>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="view">
                                      <b><bean:write name="aimUpdateRoleForm" property="description" /></b>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <html:text property="description" size="30" />
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <html:text property="description" size="30" />
                                    </logic:equal>
                                  </td>
                                </tr>

                                <tr>
                                  <td colspan="2" align="center">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                      <table>
                                        <tr>
                                          <td>
                                            <digi:trn key="aim:defaultPrivilegeForTheRole">
                                            Default privileges for the role.
                                            All members of this role will inherit these privileges
                                            </digi:trn>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="readPermission">
                                              <digi:trn key="aim:readPerms">
                                              Read
                                              </digi:trn>
                                            </html:checkbox><FONT color=red>*</FONT>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="writePermission">
                                              <digi:trn key="aim:writePerms">
                                              Add / Update
                                              </digi:trn>
                                            </html:checkbox>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="deletePermission">
                                              <digi:trn key="aim:deletePerms">
                                              Delete
                                              </digi:trn>
                                            </html:checkbox>
                                          </td>
                                        </tr>
                                      </table>
                                    </logic:equal>
                                  </td>
                                </tr>
                                <tr>
                                  <td colspan="2" align="center">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                      <table>
                                        <tr>
                                          <td>
                                            <digi:trn key="aim:defaultPrivilegeForTheRole">
                                            Default privileges for the role.
                                            All members of this role will inherit these privileges
                                            </digi:trn>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="readPermission">
                                              <digi:trn key="aim:readPerms">
                                              Read
                                              </digi:trn>
                                            </html:checkbox><FONT color=red>*</FONT>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="writePermission">
                                              <digi:trn key="aim:writePerms">
                                              Add / Update
                                              </digi:trn>
                                            </html:checkbox>
                                          </td>
                                        </tr>
                                        <tr>
                                          <td>
                                            <html:checkbox property="deletePermission">
                                              <digi:trn key="aim:deletePerms">
                                              Delete
                                              </digi:trn>
                                            </html:checkbox>
                                          </td>
                                        </tr>
                                      </table>
                                    </logic:equal>
                                    <logic:equal name="aimUpdateRoleForm" property="teamHeadFlag" value="no">
                                      <table>
                                        <tr>
                                          <td><br></td>
                                            <td>
                                              <html:checkbox property="teamHead">
                                                <digi:trn key="aim:checkIfTeamHead">
                                                Check this box if you want to set this role as the team lead.
                                                </digi:trn>
                                              </html:checkbox>
                                            </td>
                                        </tr>
                                      </table>
                                    </logic:equal>
                                          </td>
                                </tr>
                                <tr>
                                  <td colspan="2" align="center">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="view">
                                      <table>
                                        <tr>
                                          <td>
                                            <digi:trn key="aim:defaultRolePrivileges">
                                            Default privileges for the role :
                                            </digi:trn>
                                          </td>
                                        </tr>
                                        <tr align="center">
                                          <br>
                                          <td>
                                            <logic:equal name="aimUpdateRoleForm" property="readPermission" value="on">
                                              <b>Read</b>
                                            </logic:equal>
                                          </td>
                                        </tr>
                                        <tr align="center">
                                          <td>
                                            <logic:equal name="aimUpdateRoleForm" property="writePermission" value="on">
                                              <b>Write</b>
                                            </logic:equal>
                                          </td>
                                        </tr>
                                        <tr align="center">
                                          <td>
                                            <logic:equal name="aimUpdateRoleForm" property="deletePermission" value="on">
                                              <b>Delete</b>
                                            </logic:equal>
                                          </td>
                                        </tr>
                                      </table>
                                    </logic:equal>
                                  </td>
                                </tr>
                                <tr>
                                  <td colspan="2" width="60%" align="center">
                                    <logic:equal name="aimUpdateRoleForm" property="action" value="delete">
                                      <logic:equal name="aimUpdateRoleForm" property="flag"
                                        value="membersAssigned"><b>
                                          <digi:trn key="aim:cannotDeleteRole">Cannot Delete Role</digi:trn>
                                          <digi:trn key="aim:membersAssignedToRole">
                                          Members are assigned the roles
                                          </digi:trn>
                                        </b></logic:equal>
                                        <logic:equal name="aimUpdateRoleForm" property="flag"
                                        value="delete">
                                        <table width="100%" cellspacing="5">
                                          <tr>
                                            <c:set var="trnDeleteBtn">
                                              <digi:trn key="aim:btnDelete">delete</digi:trn>&nbsp;
                                            </c:set>
                                            <td width="50%" align="right">
                                              <html:submit value="${trnDeleteBtn}" styleClass="dr-menu"/>
                                            </td>
                                            <td width="50%" align="left">
                                              <c:set var="trnCancelBtn">
                                                <digi:trn key="aim:btnCancel">Cancel</digi:trn>&nbsp;
                                              </c:set>
                                              <html:reset value="${trnCancelBtn}"  styleClass="dr-menu"  onclick="cancel()"/>
                                            </td>
                                          </tr>
                                        </table>
                                        </logic:equal>
                                      </logic:equal>
                                      <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                        <table width="100%" cellspacing="5">
                                          <tr>
                                            <td width="50%" align="right">
                                              <html:submit value="${btnSave}" styleClass="dr-menu" onclick="return permissionCheck()" />
                                            </td>
                                            <td width="50%" align="left">
                                              <html:reset value="${btnCancel}" styleClass="dr-menu"  onclick="cancel()" />
                                            </td>
                                          </tr>
                                        </table>
                                      </logic:equal>
                                      <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                        <table width="100%" cellspacing="5">
                                          <tr>
                                            <td width="50%" align="right">

                                              <html:submit value="${btnSave}" styleClass="dr-menu" onclick="return permissionCheck()" />
                                            </td>
                                            <td width="50%" align="left">

                                              <html:reset value="${btnCancel}" styleClass="dr-menu"  onclick="cancel()" />
                                            </td>
                                          </tr>
                                        </table>
                                      </logic:equal>
                                  </td>
                                </tr>
                                <tr><td>
                                  <c:set var="mandatoryFields">
                                    <digi:trn key="aim:updateRoles:MandatoryFields">Mandatory Fields</digi:trn>
                                  </c:set>
                                  <logic:equal name="aimUpdateRoleForm" property="action" value="edit">
                                    <FONT color=red>*&nbsp;<c:out value="${mandatoryFields}"/></FONT>
                                  </logic:equal>
                                  <logic:equal name="aimUpdateRoleForm" property="action" value="add">
                                    <FONT color=red>*&nbsp;<c:out value="${mandatoryFields}"/></FONT>
                                  </logic:equal>
                                  </td></tr>
                              </table>
                            </td>
                                </tr>

                        </table>
                      </td>
                          </tr>
                          <tr><td bgColor=#f4f4f2>&nbsp;
                          
            </td></tr>
                  </table>
</td>
<td noWrap width=100% vAlign="top">
  <table align=center cellPadding=0 cellSpacing=0 width="90%" border=0>
    <tr>
      <td>
        <table cellPadding=0 cellSpacing=0 width=100>
          <tr>
            <td bgColor=#c9c9c7 class=box-title>
              <digi:trn key="aim:otherLinks">
              Other links
              </digi:trn>
            </td>
            <td background="module/aim/images/corner-r.gif" height="17" width=17>&nbsp;
            
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
      <td bgColor=#ffffff class=box-border>
        <table cellPadding=5 cellSpacing=1 width="100%">
          <tr>
            <td class="inside">
              <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
              <c:set var="translation">
                <digi:trn key="aim:clickToViewTeams">Click here to view Teams</digi:trn>
              </c:set>
              <digi:link href="/workspaceManager.do" title="${translation}" >
                <digi:trn key="aim:teams">
                Teams
                </digi:trn>
              </digi:link>
            </td>
          </tr>
          <tr>
            <td class="inside">
              <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
              <c:set var="translation">
                <digi:trn key="aim:clickToAddTeams">Click here to Add Teams</digi:trn>
              </c:set>
              <digi:link href="/updateWorkspace.do" title="${translation}" >
                <digi:trn key="aim:addTeam">
                Add Teams
                </digi:trn>
              </digi:link>
            </td>
          </tr>
          
          <tr>
            <td class="inside">
              <digi:img src="module/aim/images/arrow-014E86.gif" width="15" height="10"/>
              <c:set var="trnViewAdmin">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <digi:link href="/admin.do" title="${trnViewAdmin}" >
                <digi:trn key="aim:AmpAdminHome">
                Admin Home
                </digi:trn>
              </digi:link>
            </td>
          </tr>
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
</digi:form>

<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnRemoveUser(id) {
      <digi:context name="removeUrl" property="context/ampModule/moduleinstance/removeMemberUser.do" />
      document.groupMembersForm.action = "<%= removeUrl %>?userId=" + id;
      document.groupMembersForm.submit();
  }

  function refreshUserList() {
      alert();
      <digi:context name="refreshUrl" property="context/ampModule/moduleinstance/showGroupMembers.do" />
      document.groupMembersForm.action= "<%= refreshUrl %>";
      document.groupMembersForm.submit();
  }

  function selectUsers(users) {
      document.groupMembersForm.usersToAdd.value = users;
      <digi:context name="refreshUrl" property="context/ampModule/moduleinstance/addUsersToGroup.do" />
      document.groupMembersForm.action= "<%= refreshUrl %>";
      document.groupMembersForm.submit();
  }

</script>

<digi:errors/>
<digi:form action="/showGroupMembers.do">
<html:hidden name="groupMembersForm" property="groupId" />
<html:hidden name="groupMembersForm" property="usersToAdd" />
<table>
  <tr><td align="center" colspan="2"><h3><b><digi:trn key="admin:groupMembrsManagementFor">Group Members Management for</digi:trn></b></h3></td></tr>
  <tr><td width="20%">Group:</td><td><b>
  <bean:define id="groupName" name="groupMembersForm" property="groupName" type="java.lang.String" />
     <digi:trn key='<%= "groups:" + groupName %>'>
        <c:out value="${groupMembersForm.groupName}" />
     </digi:trn></b></td></tr>
</table>
<BR><BR>
<div align="center">
<table>
<tr>
<td noWrap class="text" width="40%" align="center"><b><digi:trn key="admin:email">Email</digi:trn></b></td>
<td noWrap class="text" width="20%" align="center"><b><digi:trn key="admin:firstNames">First Names</digi:trn></b></td>
<td noWrap class="text" width="20%" align="center"><b><digi:trn key="admin:lastName">Last Name</digi:trn></b></td>
<td class="text" width="5%" align="center">&nbsp;</td>
<td class="text" width="15%" align="center">&nbsp;</td>
</tr>
<logic:iterate name="groupMembersForm" property="users" id="user" indexId="index" type="org.digijava.kernel.user.User">
<tr  bgColor="#f0f0f0">
<td class="text" width="40%" ><c:out value="${user.email}" /></td>
<td class="text" width="20%" ><c:out value="${user.firstNames}" /></td>
<td class="text" width="20%" ><c:out value="${user.lastName}" /></td>
<td noWrap class="text" width="5%" ><a href="javascript:fnOnRemoveUser(<%= user.getId() %>)"> <digi:trn key="admin:remove">Remove</digi:trn> </a></td>
<td noWrap class="text" width="15%" >
<digi:context name="digiContext" property="context" />
<a href="<%= digiContext %>/um/user/showUserProfile.do?activeUserId=<%= user.getId() %>" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index"><digi:trn key="admin:viewProfile">View Profile</digi:trn></a>
</td>
</tr>
</logic:iterate>
</table>
</div>
<HR>
<table>
<tr><td>
<digi:context name="selectUsers" property="context/ampModule/moduleinstance/showSelectUsers.do" />
<digi:context name="addUsersToGroup" property="context/ampModule/moduleinstance/addUsersToGroup.do" />
<a href="<%= selectUsers %>?targetAction=<%= addUsersToGroup %>" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index"><digi:trn key="admin:addUsers">Add Users</digi:trn></a>
</td></tr>
</table>
</digi:form>
<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnDelete( param ) {
      <digi:context name="deleteUrl" property="context/ampModule/moduleinstance/deleteGroup.do" />
      document.groupsForm.groupId.value = param;
      document.groupsForm.action = "<%= deleteUrl %>";

      document.groupsForm.submit();
  }

  function pickupGroup(id) {
      <digi:context name="showPerms" property="context/ampModule/moduleinstance/showPermissions.do" />
      window.location="<%= showPerms %>?groupId=" + id;
  }

</script>
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="ampModule/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:groupAdministration">Group Administration</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">
		
<digi:errors/>
<digi:form action="/addGroup.do" method="post">
<table>
<html:hidden property="groupId" />
<tr><td>
<table border="0">
<tr><td noWrap><b><digi:trn key="admin:groupName">Group Name</digi:trn></b></td><td noWrap><b><digi:trn key="admin:default">Default</digi:trn></b></td><td>&nbsp;</td></tr>
<logic:iterate id="group" name="groupsForm" property="groups" type="org.digijava.ampModule.admin.form.GroupsForm.GroupInfo">
<tr><td><digi:trn key='<%= "groups:" + group.getName() %>'><c:out value="${group.name}" /></digi:trn></td>

<c:if test="${group.defaultGroup}"><td noWrap><digi:trn key="admin:yes">Yes</digi:trn></td><td>&nbsp;</td></c:if>
<c:if test="${! group.defaultGroup}">
<td noWrap><digi:trn key="admin:no">No</digi:trn></td><td>
<logic:equal name="group" property="empty" value="true">
<a href="javascript:fnOnDelete('<%= group.getId().toString() %>')"><digi:trn key="admin:delete">Delete</digi:trn></a></td>
</logic:equal>
<logic:equal name="group" property="empty" value="false">
&nbsp;
</logic:equal>
</c:if>
<td noWrap><digi:link href="/showPermissions.do" paramId="groupId" paramName="group" paramProperty="id"><digi:trn key="admin:permissions">Permissions</digi:trn></digi:link></td>
<td noWrap><digi:link href="/showGroupMembers.do" paramId="groupId" paramName="group" paramProperty="id"><digi:trn key="admin:members">Members</digi:trn></digi:link></td></tr>
</logic:iterate>
</table>
<td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td><b><digi:trn key="admin:addNewGroup">Add new group</digi:trn></b></td></tr>
<tr><td>
<html:text property="groupName" />
<a href="javascript:groupsForm.submit()"><digi:trn key="admin:add">Add</digi:trn></a>
</td><tr>
<tr><td>&nbsp;</td></tr>
<tr><td noWrap align="center"><b><digi:trn key="admin:groupsOwnedByOtherSites">Groups, owned by other sites</digi:trn></b></td></tr>
<tr><td>
<table>
<tr><td noWrap><b><digi:trn key="admin:siteName">Site Name</digi:trn></b></td><td noWrap><b><digi:trn key="admin:groupName">Group Name</digi:trn></b></td><td>&nbsp;</td></tr>
<logic:iterate id="otherGroup" name="groupsForm" property="otherGroups">
<bean:define id="keySiteId" name="otherGroup" property="site.siteId" type="java.lang.String" />
<tr>
  <td><digi:trn siteId="<%=keySiteId%>" key="<%= \"groups:\" + keySiteId %>"><c:out value="${otherGroup.site.name}" /></digi:trn></td>
  <bean:define id="keyGroupName" name="otherGroup" property="groupName" />
  <td><digi:trn siteId="<%=keySiteId%>" key="<%= \"groups:\" + keyGroupName %>"><c:out value="${otherGroup.groupName}" /></digi:trn></td>
  <td><digi:link href="/showPermissions.do" paramId="groupId" paramName="otherGroup" paramProperty="id"><digi:trn key="admin:permissions">Permissions</digi:trn></digi:link></td>
</tr>
</logic:iterate>
</table>
</td></tr>
<tr><td noWrap>
<digi:context name="selectGroup" property="context/ampModule/moduleinstance/showPickupGroup.do" />
<digi:context name="addGroup" property="context/ampModule/moduleinstance/showPermissions.do" />
<a href="<%= selectGroup %>?targetAction=<%= addGroup %>" onclick="window.open(this.href, 'users', 'HEIGHT=500,resizable=yes,scrollbars=yes,WIDTH=500');return false;" target="user" paramId="id" paramName="index"><digi:trn key="admin:addPermToOtherSiteGroup">Add permission to other site's group</digi:trn></a>
<td></tr>
</table>
</digi:form>

</td>
	</tr>
</table>
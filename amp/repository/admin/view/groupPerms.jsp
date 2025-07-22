<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>

  function fnOnAddPerm() {
      <digi:context name="addUrl" property="context/module/moduleinstance/addPermission.do" />
      document.groupPermissionsForm.action = "<%= addUrl %>";
      document.groupPermissionsForm.submit();
  }

  function fnOnDeletePerm(index) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/deletePermission.do" />
      document.groupPermissionsForm.action = "<%= deleteUrl %>?index=" + index;
      document.groupPermissionsForm.submit();
  }

</script>

<digi:errors/>
      <digi:form action="/savePermissions.do" method="post" >
<html:hidden property="groupId" />
<table>
<tr ><td width="100%" colspan="2"align="center" >
   <h3><b><digi:trn key="admin:permManagementFor">Permission Management for</digi:trn></h3></b></td>
   <c:if test="${ ! empty groupPermissionsForm.siteName}">
   <tr>
      <td noWrap width="10%" align="right"><b><digi:trn key="admin:site">Site:</digi:trn></b></td>
      <td width="90%" align="left">
        <html:hidden property="siteName" />
        <c:out value="${groupPermissionsForm.siteName}" />
	    <c:out value="${groupPermissionsForm.siteId}" />
      </td>
   </c:if></td></tr>
   <tr>
      <td noWrap width="10%" align="right"><b><digi:trn key="admin:group">Group:</digi:trn></b></td>
      <td width="90%" align="left"> 
     <bean:define id="groupName" name="groupPermissionsForm" property="groupName" type="java.lang.String" />
      <digi:trn key='<%= "groups:" + groupName %>'>
        <c:out value="${groupPermissionsForm.groupName}" />
      </digi:trn>   
      </td>
   </td></tr>
</td></tr>
</table>
<br>
<table>
<logic:iterate name="groupPermissionsForm" property="permissions" id="permission" indexId="index" type="org.digijava.module.admin.form.GroupPermissionsForm.PermissionInfo">
<tr>
<td><html:hidden name="permission" property="id" indexed="true" />
 <html:select name="permission" property="resourceId" indexed="true" style="text">
    <c:set var="resources" value="${groupPermissionsForm.resources}" scope="page" />
    <html:options collection="resources" property="id" labelProperty="name" />
 </html:select>
</td>
<td>
<html:select name="permission" property="action" indexed="true" style="text">
   <html:options name="groupPermissionsForm" property="actions"style="text" />
</html:select>
</td>
<td><a href="javascript:fnOnDeletePerm(<%= index %>)"><digi:trn key="admin:delete">Delete</digi:trn></a></td>
</tr>
</logic:iterate>
</table>
<table>
<tr><td><a href="javascript:fnOnAddPerm()"><digi:trn key="admin:addPermission">Add permission</digi:trn></a></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td><html:submit value="Save"/></td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td><digi:trn key="admin:permOnOtherSiteModuleInstances">Permissions on other site's module instances</digi:trn></td></tr>
<tr><td>
<table>
<tr bgColor=#f0f0f0><td noWrap><digi:trn key="admin:siteName">Site Name</digi:trn></td><td noWrap><digi:trn key="admin:moduleName">Module Name</digi:trn></td><td noWrap><digi:trn key="admin:moduleInstance">Module Instance</digi:trn></td><td noWrap><digi:trn key="admin:permActions">Permitted action(s)</digi:trn></td></tr>
<c:forEach var="perm" items="${groupPermissionsForm.foreignPerms}">
<tr>
   <td><c:out value="${perm.site.name}" /></td>
   <td><c:out value="${perm.module}" /></td>
   <td><c:out value="${perm.instance}" /></td>
   <td><c:out value="${perm.actions}" /></td>
</tr>
</c:forEach>
</table>
</td></tr>
<tr><td>&nbsp;</td></tr>
<tr><td><digi:trn key="admin:inheritedPerms">Inherited permissions</digi:trn></td></tr>
<tr><td>
<table>
<tr bgColor=#f0f0f0><td noWrap><digi:trn key="admin:siteName">Site Name</digi:trn></td><td noWrap><digi:trn key="admin:moduleName">Module Name</digi:trn></td><td noWrap><digi:trn key="admin:moduleInstance">Module Instance</digi:trn></td><td noWrap><digi:trn key="admin:permActions">Permitted action(s)</digi:trn></td></tr>
<c:forEach var="perm" items="${groupPermissionsForm.inheritedPermissions}">
<tr>
   <td><c:out value="${perm.site.name}" /></td>
   <td><c:out value="${perm.module}" /></td>
   <td><c:out value="${perm.instance}" /></td>
   <td><c:out value="${perm.actions}" /></td>
</tr>
</c:forEach>
</table>
</td></tr>
<tr><td><digi:link href="/showGroups.do"><digi:trn key="admin:backToGroupAdmin">Back to group administration</digi:trn></digi:link></td></tr>
</table>
</digi:form>
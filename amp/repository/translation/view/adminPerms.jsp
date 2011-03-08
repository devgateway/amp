<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language=JavaScript type=text/javascript>
  function fnOnAddPerm() {
      <digi:context name="addPerm" property="context/module/moduleinstance/addPermission.do" />
      document.translationPermissionsForm.action = "<%= addPerm%>";
      document.translationPermissionsForm.submit();
  }

  function fnOnDeletePerm(index) {
      <digi:context name="deletePerm" property="context/module/moduleinstance/deletePermission.do" />
      document.translationPermissionsForm.action = "<%= deletePerm%>?index=" + index;
      document.translationPermissionsForm.submit();
  }
</script>

<digi:errors/>
<digi:form action="/saveGroupPermissions.do" method="post" >
<html:hidden property="groupId" />
<table width="100%" border="0" bgcolor="#FFFFF0">
<tr><td colspan="2" align="center" >
   <h3><b><digi:trn key="translation:permAdministrationFor">Permission Administration for</digi:trn></h3></b></td>
   <tr>
      <td noWrap width="10%" align="right"><b><digi:trn key="translation:site">Site:</digi:trn></b></td>
      <td width="90%" align="left">
        <html:hidden property="siteName" />
        <c:out value="${translationPermissionsForm.siteName}" />
      </td>
   </td></tr>
   <tr>
      <td noWrap width="10%" align="right"><b><digi:trn key="translation:group">Group:</digi:trn></b></td>
      <td width="90%" align="left"> 
      <bean:define id="groupName" name="translationPermissionsForm" property="groupName" type="java.lang.String" />
      <digi:trn key='<%= "groups:" + groupName %>'>
        <c:out value="${translationPermissionsForm.groupName}" />
      </digi:trn>   
      </td>
   </td></tr>
</td></tr>
</table>
<br>

<table width="50%" border="0" bgcolor="#FFFAF0">
<logic:iterate name="translationPermissionsForm" property="permissions" id="permission" indexId="index" type="org.digijava.module.translation.form.TranslationPermissionsForm.PermissionInfo">
 <tr><td align="left">
  <html:hidden name="permission" property="id" indexed="true" /> 
  <html:select name="permission" property="siteId" indexed="true" style="text">
    <c:set var="sites" value="${translationPermissionsForm.sites}" scope="page" />
    <html:options collection="sites" property="id" labelProperty="name" />
  </html:select>
 </td>
 <td align="left">
  <html:select name="permission" property="localeId" indexed="true" style="text">
    <c:set var="languages" value="${translationPermissionsForm.languages}" scope="page" />
    <html:options collection="languages" property="code" labelProperty="name" />
  </html:select>
 </td>
 <td align="left">
     <a href="javascript:fnOnDeletePerm(<%= index %>)"><digi:trn key="translation:delete">Delete</digi:trn></a>
 </td>
 </tr>
</logic:iterate>
</table>
<table>
   <tr><td>
      <a href="javascript:fnOnAddPerm()"><digi:trn key="translation:addPermission">Add permission</digi:trn></a>
   </td></tr>
   <tr><td>&nbsp;</td></tr>
   <tr><td><html:submit value="Save"/></td></tr>
   <tr><td>&nbsp;</td></tr>
   <tr><td>
    <digi:link href="/showEditPermissions.do">
      <digi:trn key="translation:backToAdmin">Back to translation administration</digi:trn>
    </digi:link>  
   </td></tr>
   <digi:context name="indexUrl" property="context" />
    <tr><td>&nbsp;</td></tr>
    <tr>
     <td colspan="4" align="left">
     <digi:trn key="translation:goTo">Go to</digi:trn> 
      <a href="<%= indexUrl%>">
       <b><digi:trn key="translation:indexPage">Index Page</digi:trn></b>
      </a> 
     </td>
   </tr>
</table>
</digi:form>
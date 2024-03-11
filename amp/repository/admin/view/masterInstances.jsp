<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script>
  function fnOnSave( form ) {
      window.opener.setMapping(form.index.value, form.instanceId.value);
      window.returnValue = "";	
      window.close();
  }
</script>

<digi:form method="post" action="/showMasterInstances.do">
<html:hidden property="module" />
<html:hidden property="index" />
<table width="100%">
<tr><td colspan="2" align="center" class="pageTitle" noWrap><digi:trn key="admin:setMasterInstance">Set Master Instance</digi:trn></td></tr>
<tr><td noWrap >&nbsp;</td></tr>
<tr><td noWrap align="left" class=text><digi:trn key="admin:module">Module</digi:trn></td><td align="left" noWrap class="text"><c:out value="${referencedInstForm.module}" /></td></tr>
<tr><td noWrap align="left" class=text><digi:trn key="admin:instance">Instance</digi:trn></td><td align="left" noWrap class="text">
<script>
   document.write(opener.document.all("instances[<%= request.getParameter("id")%>].instanceName").value);
</script>
</td></tr>
<tr><td align="left" noWrap class=text><digi:trn key="admin:master">Master</digi:trn></td><td align="left" noWrap class="text">
<html:select name="referencedInstForm" property="siteId" onchange="javascript:referencedInstForm.submit()">
  <c:set var="sites" value="${referencedInstForm.sites}" scope="page" />
  <html:options collection="sites" property="id" labelProperty="name" />
</html:select>
</td></tr>
<tr><td align="left" noWrap class=text><digi:trn key="admin:master">Master</digi:trn></td><td align="left" noWrap class="text">
<html:select name="referencedInstForm" property="instanceId">
  <bean:define id="instances" name="referencedInstForm" property="instances" />
  <html:options collection="instances" property="moduleInstanceId" labelProperty="instanceName" />
</html:select>
</td></tr>
<tr><td align="center">
<input type="button" onclick="fnOnSave(referencedInstForm)" value="Save"/></td>
<td><input type="button" onclick="window.close()" value="Close" /></td>
</tr>
</table>
</digi:form>
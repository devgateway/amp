<%@ page language="java" %>
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script>
  function fnOnAdd() {
      <digi:context name="addUrl" property="context/module/moduleinstance/addInstance.do" />
      document.siteInstancesForm.action = "<%= addUrl %>";
      document.siteInstancesForm.submit();
  }
  function fnOnDelete( index) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/deleteInstance.do" />
      document.siteInstancesForm.action = "<%= deleteUrl %>?index=" + index;
      document.siteInstancesForm.submit();
  }
  function fnChangeModule( index ) {
      <digi:context name="changeUrl" property="context/module/moduleinstance/changeInstanceModule.do" />
      document.siteInstancesForm.action = "<%= changeUrl %>?index=" + index;
      document.siteInstancesForm.submit();
  }
  function fnOnClearMaster( index) {
      <digi:context name="deleteUrl" property="context/module/moduleinstance/clearMasterInstance.do" />
      document.siteInstancesForm.action = "<%= deleteUrl %>?index=" + index;
      document.siteInstancesForm.submit();
  }
  function setMapping(index, instanceId) {
      <digi:context name="setMappingUrl" property="context/module/moduleinstance/setMasterInstance.do" />
      document.siteInstancesForm.action = "<%= setMappingUrl %>?index=" + index + "&mapId=" + instanceId;
      document.siteInstancesForm.submit();
  }

</script>

<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="module/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:moduleInstances">MODULE INSTANCES</digi:trn>
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

<digi:errors />
<digi:form method="post" action="/saveInstances.do">
<digi:instance property="siteInstancesForm" />
<table border="1" cellspacing="0" cellpadding="3" bordercolor="#E3DDC1" style="border-collapse: collapse;">
<tr>
	<td noWrap class="text">Default</td>
	<td noWrap class="text"><digi:trn key="admin:module">Module</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:instance">Instance</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:numOfItemsInTeaser">Number of items in teaser</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:mastersSite">Master's site</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:masterInstance">Master Instance</digi:trn></td>
	<td noWrap class="text"><digi:trn key="admin:permitted">Permitted?</digi:trn></td>
	<td colspan="3">&nbsp;</td>
</tr>
<logic:iterate indexId="index" name="siteInstancesForm" id="instance" property="instances" type="org.digijava.module.admin.form.SiteInstancesForm.InstanceInfo">
<tr>
<td align="right">
	<html:radio name="siteInstancesForm" property="defaultModeuleIndex" value="<%=String.valueOf(instance.getId())%>"/>
</td>
<td>
<html:hidden name="instance" property="id" indexed="true"/>
<c:if test="${!empty instance.mappingId}">
<html:hidden name="instance" property="mappingId" indexed="true"/>
<html:hidden name="instance" property="mappingSite" indexed="true"/>
<html:hidden name="instance" property="mappingInstance" indexed="true"/>
<html:hidden name="instance" property="permitted" indexed="true"/>
</c:if>
<html:select name="instance" property="module" indexed="true" onchange='<%= "fnChangeModule(" + index + ")" %>'>
   <html:options property="modules" />
</html:select>
</td>
<td><html:text name="instance" property="instance" indexed="true" /></td>
<td>  <html:select name="instance" property="selectedNumOfItemsInTeaser" indexed="true">
         <c:set var="numberItems" value="${siteInstancesForm.numOfItemsInTeaser}" scope="page" />
         <html:options  collection="numberItems" property="value" labelProperty="value" />
      </html:select>

</td>
<c:if test="${! empty instance.mappingId}">
<td><c:out value="${instance.mappingSite}" /></td>
<td><c:out value="${instance.mappingInstance}" /></td>
<c:if test="${instance.permitted}">
<td noWrap><digi:trn key="admin:yes">Yes</digi:trn></td>
</c:if>
<c:if test="${!instance.permitted}">
<td>&nbsp;</td>
</c:if>
</c:if>
<c:if test="${empty instance.mappingSite}">
<td colspan="3">&nbsp;</td>
</c:if>
</td>
<td noWrap>
<digi:context name="masterUrl" property="context/module/moduleinstance/showMasterInstances.do" />
<a href="<%= masterUrl + "?module=" + instance.getModule() + "&index=" + index %>" onclick="window.open(this.href, 'masterInstances', 'HEIGHT=300,resizable=yes,scrollbars=no,WIDTH=300');return false;" target="masterInstances"><digi:trn key="admin:assignMapping">Assign Mapping</digi:trn></a></td>
<td noWrap>
	<a href='javascript:fnOnClearMaster("<%= index %>")'>
		<digi:trn key="admin:clearMapping">Clear Mapping</digi:trn>
	</a>
<td noWrap>
	<a href='javascript:fnOnDelete("<%= index %>")'>
		<digi:trn key="admin:delete">Delete</digi:trn>
	</a>
</td>
</tr>
</logic:iterate>
<tr>
<td noWrap class="text">
	None<html:radio name="siteInstancesForm" property="defaultModeuleIndex" value="0"/>
</td>
<td colspan="10">&nbsp;</td>
</tr>

</table>
<a href='javascript:fnOnAdd()' ><digi:trn key="admin:add">Add</digi:trn></a>
<br/>
<br/>
<br/>
<table>
<tr>
<td noWrap><digi:trn key="admin:permit">Permit</digi:trn></td>
<td noWrap><digi:trn key="admin:module">Module</digi:trn></td>
<td noWrap><digi:trn key="admin:instance">Instance</digi:trn></td>
<td noWrap><digi:trn key="admin:mappingSite">Mapping site</digi:trn></td>
<td noWrap><digi:trn key="admin:mappedInstance">Mapped instance</digi:trn></td>
</tr>
<c:forEach var="instance" items="${siteInstancesForm.refInstances}">
<tr>
<td align="right"><html:multibox name="siteInstancesForm" property="permittedInstances"><c:out value="${instance.moduleInstanceId}" /></html:multibox></td>
<td><c:out value="${instance.realInstance.moduleName}" /></td>
<td><c:out value="${instance.realInstance.instanceName}" /></td>
<td><c:out value="${instance.site.name}" /></td>
<td><c:out value="${instance.instanceName}" /></td>
</tr>
</c:forEach>
</table>
<html:submit />
</digi:form>

</td>
	</tr>
</table>
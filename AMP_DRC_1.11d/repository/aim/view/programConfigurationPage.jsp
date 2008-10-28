<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<script language="JavaScript">

 function cancel() {
   <digi:context name="cancel" property="context/module" />
   document.aimActivityProgramSettingsForm.action = "<%=cancel%>";

   document.aimActivityProgramSettingsForm.target =  "_self";

   document.aimActivityProgramSettingsForm.submit();

   }

</script>


<digi:instance  property="aimActivityProgramSettingsForm"/>



<digi:errors/>


<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp" flush="true" />
<!-- End of Logo -->
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
<tr>
<td height=16 vAlign=center width=571><span class=subtitle-blue>
						<digi:trn key="aim:adminconfigurationsettings">Configuration Settings
						</digi:trn>
						</span>
					</td>
</tr>
<tr>
					<td height=16 vAlign=center width=571>
						<digi:errors />
					</td>
				</tr>
<tr>
<tr>
<td>
<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" >
<tr>
<td><b><digi:trn key="aim:programNameSetting">The Name of the Setting</digi:trn></b></td>
<td><b><digi:trn key="aim:defaultHierarchy">Default Hierarchy</digi:trn></b></td>
<td><b><digi:trn key="aim:allowMultiple">Allow Multiple</digi:trn></b></td>
</tr>
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsList" id="setting">
<tr>
<td>
<digi:trn key="aim:${setting.name}"> <c:out value="${setting.name}"/></digi:trn>
</td>
<td>
<c:if test="${empty setting.defaultHierarchy}">
<digi:trn key="aim:none"> None</digi:trn>
</c:if>
<c:if test="${!empty setting.defaultHierarchy}">
<c:out value="${setting.defaultHierarchy.name}"/>
</c:if>
</td>
<td>
<c:if test="${setting.allowMultiple}">
<digi:trn key="aim:yes"> Yes</digi:trn>
</c:if>
<c:if test="${!setting.allowMultiple}">
<digi:trn key="aim:no">No</digi:trn>
</c:if>
</td>
</tr>
</logic:iterate>
</table>
</td>
</tr>
<td>
<digi:form action="/programConfigurationPage.do" method="post">

<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" >
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsList" id="settingsList">
<tr>
<td colspan="2" bgColor=#d7eafd class=box-title height="20" align="left" >
<digi:trn key="aim:${settingsList.name}"> <c:out value="${settingsList.name}"/></digi:trn>
</td>
</tr>
<tr>
<td width="20%">
<digi:trn key="aim:defaultHierarchy">
Default Hierarchy
</digi:trn>
</td>
<td>

<html:select name="settingsList" property="defaultHierarchyId" indexed="true">
  <html:option value="-1"><digi:trn key="aim:selprogram">Select Program</digi:trn></html:option>
	<html:optionsCollection  property="programList" value="ampThemeId" label="name" />
</html:select>
</td>
</tr>
<tr>
<td colspan="2" >
<digi:trn key="aim:allowMultiple">Allow Multiple</digi:trn>? <html:checkbox name="settingsList" property="allowMultiple" indexed="true" />
</td>
</tr>
</logic:iterate>

<tr>
<td colspan="2">
<c:set var="trn"><digi:trn key="aim:btnsave">Save</digi:trn></c:set>
<html:submit property="save" value="${trn}"/>
<c:set var="tran"><digi:trn key="aim:btncancel">Cancel</digi:trn></c:set>
<html:cancel property="cancel" value="${tran}" onclick="return cancel();"/>
</td>
</tr>
</table>
</td>
</tr>
</table>


</digi:form >




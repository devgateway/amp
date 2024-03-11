<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>
<script language="JavaScript">

 function cancelSaving() {

	window.location="/aim/admin.do";
	return true;

   }
   function validateSave(){
        var valid=false;
        var hierarchyIDs=$("select[name$='.defaultHierarchyId']");
        if(hierarchyIDs!=null){
            for(var i=0;i < hierarchyIDs.length; i++){
                for(var j=i+1;j<hierarchyIDs.length;j++){
                    if(hierarchyIDs[i].value==hierarchyIDs[j].value && hierarchyIDs[j].value!=-1){
                        alert('<digi:trn jsFriendly="true">two or more porgrams with same hierarchy!</digi:trn>');
                        return valid;
                    }
                }
            }
        }
        valid=true;
        return valid;
    }

   var enterBinder	= new EnterHitBinder('saveMPCBtn');

</script>
<h1 class="admintitle"><digi:trn>Configuration settings</digi:trn></h1>
<digi:instance  property="aimActivityProgramSettingsForm"/>






<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=1000 align=center style="font-size:12px;">
<!-- <tr> -->
	<!-- Start Navigation -->
	<!-- <td height=33><span class=crumb>
		<c:set var="translation">
			<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
		</c:set>
		<digi:link href="/admin.do" styleClass="comment" title="${translation}" >
		<digi:trn key="aim:AmpAdminHome">
			Admin Home
		</digi:trn>
		</digi:link>&nbsp;&gt;&nbsp;
		<digi:trn key="aim:multiProgramConf">
			Multi Program Configuration
		</digi:trn>
	</td>-->
	<!-- End navigation -->
<!-- </tr> -->



<tr>
					<td height=16 valign="center" width=571>
						<digi:errors />
					</td>
  </tr>
<tr>
<tr>
<td style="border-top:1px solid #cccccc;">
<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" class="inside">
<tr>
<td class="inside"><b><digi:trn key="aim:programNameSetting">The Name of the Setting</digi:trn></b></td>
<td class="inside"><b><digi:trn key="aim:defaultHierarchy">Default Hierarchy</digi:trn></b></td>
<td class="inside"><b><digi:trn key="aim:allowMultiple">Allow Multiple</digi:trn></b></td>
</tr>
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsList" id="setting">
<tr>
<td class="inside">
<digi:trn key="aim:${setting.name}"> <c:out value="${setting.name}"/></digi:trn>
</td>
<td class="inside">
<c:if test="${empty setting.defaultHierarchy}">
<digi:trn key="aim:none"> None</digi:trn>
</c:if>
<c:if test="${!empty setting.defaultHierarchy}">
	<c:if test="${setting.defaultHierarchy.ampThemeId ne -1}">
		<c:out value="${setting.defaultHierarchy.name}"/>
	</c:if>
	<c:if test="${setting.defaultHierarchy.ampThemeId eq -1}">
		<digi:trn key="aim:noHierarchy">No Hierarchy</digi:trn>
	</c:if>
</c:if>
</td>
<td class="inside">
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

<table width="100%" cellspacing="1" cellpadding="4" valign="top" align="left" style="margin-top:15px;" class="inside">
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsList" id="settingsList">
<tr>
<td colspan="2" bgColor=#f2f2f2 class="inside" height="20" align="center" style="font-weight:bold;">
<digi:trn key="aim:${settingsList.name}"> <c:out value="${settingsList.name}"/></digi:trn>
</td>
</tr>
<tr>
<td width="50%" class="inside" align=right>
<digi:trn key="aim:defaultHierarchy">
Default Hierarchy
</digi:trn>
</td>
<td class="inside">

<html:select name="settingsList" property="defaultHierarchyId" indexed="true">
  <html:option value="-1"><digi:trn key="aim:selprogram">Select Program</digi:trn></html:option>
	<html:optionsCollection  property="programList" value="ampThemeId" label="name" />
</html:select>
</td>
</tr>
<tr>
<td colspan="2" class="inside" align=center>
<digi:trn key="aim:allowMultiple">Allow Multiple</digi:trn>? <html:checkbox name="settingsList" property="allowMultiple" indexed="true" />
</td>
</tr>
</logic:iterate>

<tr>
<td colspan="2" class="inside" align=center>
<c:set var="trn"><digi:trn key="aim:btnsave">Save</digi:trn></c:set>
<html:submit property="save" value="${trn}" styleClass="buttonx" onclick="return validateSave()"  styleId="saveMPCBtn"/>
<c:set var="tran"><digi:trn key="aim:btncancel">Cancel</digi:trn></c:set>
<c:set var="resetTrn"><digi:trn key="aim:btnreset">Reset</digi:trn></c:set>
  <html:reset property="reset" styleClass="buttonx" value="${resetTrn}" />
  <html:button property="cancel" styleClass="buttonx" value="${tran}" onclick="return cancelSaving();"/>
</td>
</tr>
</table>
</td>
</tr>
</table>


</digi:form >




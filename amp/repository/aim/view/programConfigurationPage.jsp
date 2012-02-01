<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
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


<digi:instance  property="aimActivityProgramSettingsForm"/>



<digi:errors/>


<!--  AMP Admin Logo -->
<jsp:include page="teamPagesHeader.jsp"  />
<!-- End of Logo -->
<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width=772>
<tr>
	<!-- Start Navigation -->
	<td height=33><span class=crumb>
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
	</td>
	<!-- End navigation -->
</tr>


<tr>
<td height=16 vAlign=center width=571>

<span class=subtitle-blue style="padding-top:0px;padding-bottom:0px">
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
	<c:if test="${setting.defaultHierarchy.ampThemeId ne -1}">
		<c:out value="${setting.defaultHierarchy.name}"/>
	</c:if>
	<c:if test="${setting.defaultHierarchy.ampThemeId eq -1}">
		<digi:trn key="aim:noHierarchy">No Hierarchy</digi:trn>
	</c:if>
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
<html:submit property="save" value="${trn}" onclick="return validateSave()" styleId="saveMPCBtn"/>
<c:set var="tran"><digi:trn key="aim:btncancel">Cancel</digi:trn></c:set>
<c:set var="resetTrn"><digi:trn key="aim:btnreset">Reset</digi:trn></c:set>
  <html:reset property="reset" value="${resetTrn}" />
  <html:button property="cancel" value="${tran}"  onclick="return cancelSaving();"/>
</td>
</tr>
</table>
</td>
</tr>
</table>


</digi:form >




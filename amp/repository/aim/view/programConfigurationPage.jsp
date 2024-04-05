<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/common.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/element/element-min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="/TEMPLATE/ampTemplate/js_2/yui/container/assets/container.css">

<jsp:include page="scripts/newCalendar.jsp" flush="true" />

<script language="JavaScript">
	$(document).ready(function () {
		var hierarchyIDs=$("select[name$='.defaultHierarchyId']");

		if (hierarchyIDs != null) {
			for (var i = 0; i < hierarchyIDs.length; i++) {
				handleHierarchyChange(i);
			}
		}
	});

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

	function saveProgramConfiguration(){
		if(validateSave()){
			document.forms[0].submit();
		}

	}

	function handleHierarchyChange(hierarchyIndex){
	    if (hierarchyIndex == null) {
	        hierarchyIndex = -1;
	    }

		const hierarchyIdClass = 'settingsListDTO[' + hierarchyIndex + '].defaultHierarchyId';
		const hierarchyId = document.getElementsByName(hierarchyIdClass)[0].selectedOptions[0].value;


		if (hierarchyId.toString() === "-1") {
			$('.mpc-startDate' + hierarchyIndex).hide();
			$('#startDate' + hierarchyIndex).val('');
			$('.mpc-endDate' + hierarchyIndex).hide();
			$('#endDate' + hierarchyIndex).val('');
		} else {
			$('.mpc-startDate' + hierarchyIndex).show();
			$('.mpc-endDate' + hierarchyIndex).show();
		}
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
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsListDTO" id="setting">
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
<logic:iterate name="aimActivityProgramSettingsForm" property="settingsListDTO" id="settingsListDTO" indexId="index">
<tr>
<td colspan="2" bgColor=#f2f2f2 class="inside" height="20" align="center" style="font-weight:bold;">
<digi:trn key="aim:${settingsListDTO.name}"> <c:out value="${settingsListDTO.name}"/></digi:trn>
</td>
</tr>
<tr>
<td width="50%" class="inside" align=right>
<digi:trn key="aim:defaultHierarchy">
Default Hierarchy
</digi:trn>
</td>
<td class="inside">

<html:select onchange="return handleHierarchyChange(${index})" name="settingsListDTO" property="defaultHierarchyId" styleId="settingsListDTO${index}" indexed="true" value="${settingsListDTO.defaultHierarchy.ampThemeId}">
  <html:option value="-1"><digi:trn key="aim:selprogram" >Select Program</digi:trn></html:option>
	<html:optionsCollection property="programList" value="ampThemeId" label="name" />
</html:select>
</td>
</tr>
<tr>
<td colspan="2" class="inside" align=center>
<digi:trn key="aim:allowMultiple">Allow Multiple</digi:trn>? <html:checkbox name="settingsListDTO" property="allowMultiple" indexed="true" />
</td>
<tr class="mpc-startDate${index}">
		<td width="50%" class="inside" align="right">
			<digi:trn key="aim:startDate">Start Date</digi:trn>

		</td>
		<td class="inside">
			<html:text property="startDate" styleId="startDate${index}" name="settingsListDTO" readonly="true" indexed="true" />
			<a id="date${index}" href='javascript:pickDateById("date${index}", "startDate${index}")'>
				<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
			</a>
		</td>
</tr>
	<tr class="mpc-endDate${index}">
	<td width="50%" class="inside" align="right">
		<digi:trn key="aim:endDate">End Date</digi:trn>
	</td>
	<td class="inside">
		<html:text property="endDate" styleId="endDate${index}" name="settingsListDTO" readonly="true" indexed="true"  />
		<a id="date1${index}" href='javascript:pickDateById("date1${index}", "endDate${index}")'>
			<img src="../ampTemplate/images/show-calendar.gif" alt="Click to View Calendar" border="0">
		</a>
	</td>
	</tr>
</logic:iterate>

<tr>
<td colspan="2" class="inside" align=center>
<c:set var="trn"><digi:trn key="aim:btnsave">Save</digi:trn></c:set>
<html:submit property="save" value="${trn}" styleClass="buttonx" onclick="return saveProgramConfiguration()"  styleId="saveMPCBtn"/>
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




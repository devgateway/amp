<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<script language="JavaScript">

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampEditTemplateVisibility">
		AMP Edit Template Visibility
	</digi:trn>&nbsp;:::&nbsp;
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<logic:notEmpty name="aimVisibilityManagerForm" property="templateModules">
	<logic:iterate name="aimVisibilityManagerForm" property="templateModules" id="module"
		type="org.digijava.module.aim.dbentity.AmpModulesVisibility">
		<tr bgcolor="#ffffff">
			<td width="9">
					<img src= "../ampTemplate/images/bullet_green.gif" border="0">
			</td>																	
			<td width="100%"> <bean:write name="module" property="moduleName"/> </td>
			<td>
			<select name='<%="module:"+module.getModuleNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#0000ff; ">
				<option value="disable" >-- Disable --</option>
				<option value="enable" selected>-- Enabled --</option>											
			</select>
			</td>
		</tr>
	</logic:iterate>
	</logic:notEmpty>
	<logic:notEmpty name="aimVisibilityManagerForm" property="templateModulesNotActive">
	<logic:iterate name="aimVisibilityManagerForm" property="templateModulesNotActive" id="module"
		type="org.digijava.module.aim.dbentity.AmpModulesVisibility">
		<tr bgcolor="#ffffff">
			<td width="9">
					<img src= "../ampTemplate/images/bullet_red.gif" border="0">
			</td>																	
			<td width="100%"> <bean:write name="module" property="name"/> </td>
			<td>
			<select name='<%="module:"+module.getModuleNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#ff0000;">
				<option value="disable" selected>-- Disabled --</option>
				<option value="enable" >-- Enable --</option>
			</select>
			</td>
		</tr>
	</logic:iterate>
	</logic:notEmpty>
	<tr><td>&nbsp;</td><td colspan="2">
	<digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn> <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/>
	</td></tr>
	
	<tr><td>&nbsp;</td><td colspan="2">
		<html:submit style="dr-menu" value="Save Template" property="saveEditTemplate"/>
	</td></tr>
	</digi:form>
	
</table>

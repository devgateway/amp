<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

</script>


<digi:instance property="aimFeatureManagerForm" />
<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampEditTemplateFeatures">
		AMP Edit Template Features
	</digi:trn>&nbsp;:::&nbsp;<%=session.getAttribute("templateName")%>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/featureManager.do" method="post" >
	<logic:notEmpty name="aimFeatureManagerForm" property="templateFeatures">
	<logic:iterate name="aimFeatureManagerForm" property="templateFeatures" id="feature"
		type="org.digijava.module.aim.dbentity.AmpFeature">	
		<tr bgcolor="#ffffff">
			<html:hidden property="ampFeatures" value="<%=feature.getNameTrimmed() %>" />
			<td width="9">
					<img src= "../ampTemplate/images/bullet_green.gif" border=0>
			</td>																	
			<td width="100%"> <bean:write name="feature" property="name"/> </td>
			<td>
			<select name='<%="feature:"+feature.getNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#0000ff; ">
				<option value="disable" >-- Disable --</option>
				<option value="enable" selected>-- Enabled --</option>											
			</select>
			</td>
		</tr>
	</logic:iterate>
	</logic:notEmpty>
	<logic:notEmpty name="aimFeatureManagerForm" property="templateFeaturesNotActive">
	<logic:iterate name="aimFeatureManagerForm" property="templateFeaturesNotActive" id="feature"
		type="org.digijava.module.aim.dbentity.AmpFeature">	
		<tr bgcolor="#ffffff">
			<html:hidden property="ampFeatures" value="<%=feature.getNameTrimmed() %>" />
			<td width="9">
					<img src= "../ampTemplate/images/bullet_red.gif" border=0>
			</td>																	
			<td width="100%"> <bean:write name="feature" property="name"/> </td>
			<td>
			<select name='<%="feature:"+feature.getNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#ff0000;">
				<option value="disable" selected>-- Disabled --</option>
				<option value="enable" >-- Enable --</option>
			</select>
			</td>
		</tr>
	</logic:iterate>
	</logic:notEmpty>
	<tr><td>&nbsp;</td><td colspan="2">
	Template Name: <input type="text" name="templateName" size="30" value="<%=session.getAttribute("templateName")%>"/>
	</td></tr>
	
	<tr><td>&nbsp;</td><td colspan="2">
		<html:submit style="dr-menu" value="Save Template" property="saveEditTemplate"/>
	</td></tr>
	</digi:form>
	
</table>

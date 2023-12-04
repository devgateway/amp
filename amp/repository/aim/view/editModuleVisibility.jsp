<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampEditModuleVisibility">
		AMP Edit Module Visibility
	</digi:trn>&nbsp;:::&nbsp;<%=session.getAttribute("moduleName")%>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<logic:notEmpty name="aimVisibilityManagerForm" property="featuresModule">
	<logic:iterate name="aimVisibilityManagerForm" property="featuresModule" id="visibility"
		type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility">
		<logic:equal name="visibility" property="visible" value="true">
		<tr bgcolor="#ffffff">
			<td width="9">
					<img src= "../ampTemplate/images/bullet_green.gif" border="0">
			</td>																	
			<td width="100%"> <bean:write name="visibility" property="name"/> </td>
			<td>
			<select name='<%="feature:"+visibility.getNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#0000ff; ">
				<option value="disable" >-- Disable --</option>
				<option value="enable" selected>-- Enabled --</option>											
			</select>
			</td>
		</tr>
		</logic:equal>
	</logic:iterate>
	</logic:notEmpty>
	<logic:notEmpty name="aimVisibilityManagerForm" property="featuresModule">
	<logic:iterate name="aimVisibilityManagerForm" property="featuresModule" id="visibility"
		type="org.digijava.module.aim.dbentity.AmpFeaturesVisibility">
		<logic:equal name="visibility" property="visible" value="false">
		<tr bgcolor="#ffffff">
			<td width="9">
					<img src= "../ampTemplate/images/bullet_red.gif" border="0">
			</td>																	
			<td width="100%"> <bean:write name="visibility" property="name"/> </td>
			<td>
			<select name='<%="feature:"+visibility.getNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#ff0000;">
				<option value="disable" selected>-- Disabled --</option>
				<option value="enable" >-- Enable --</option>
			</select>
			</td>
		</tr>
		</logic:equal>
	</logic:iterate>
	</logic:notEmpty>
	<html:hidden name="aimVisibilityManagerForm" property="moduleId" value="<%=request.getParameter("moduleId")%>"/>
	<tr><td>&nbsp;</td><td colspan="2">
		<html:submit style="dr-menu" value="Save Module" property="saveEditModule"/>
	</td></tr>
	</digi:form>
	
</table>

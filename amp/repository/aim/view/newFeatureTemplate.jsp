<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">

	function toggleFeature(id) {
		<digi:context name="urlVal" property="context/ampModule/moduleinstance/featureManager.do" />
		document.aimFeatureManagerForm.action = "<%= urlVal %>?toggle=true&fId="+id;
		document.aimFeatureManagerForm.submit();		
	}

</script>


<digi:instance property="aimFeatureManagerForm" />
<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampNewTemplateFeatures">
		AMP New Template Features
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/featureManager.do" method="post" >
	<logic:iterate name="aimFeatureManagerForm" property="features" id="feature"
		type="org.digijava.ampModule.aim.dbentity.AmpFeature">
		<tr bgcolor="#ffffff">
			<html:hidden property="ampFeatures" value="<%=feature.getNameTrimmed() %>" />
			<td width="9">
					<img src= "../ampTemplate/images/bullet_red.gif" border="0">
			</td>																	
			<td width="100%"> <bean:write name="feature" property="name"/> </td>
			<td>
			<select name='<%="feature:"+feature.getNameTrimmed()%>' style="width:130px; border:1px solid #000; color:#ff0000;">
				<c:set var="translation">
					<digi:trn key="aim:newFeatureTemplateDisabled">Disabled</digi:trn>
				</c:set>			
				<option value="disable" selected>-- ${translation} --</option>
				<c:set var="translation">
					<digi:trn key="aim:newFeatureTemplateEnable">Enable</digi:trn>
				</c:set>				
				<option value="enable">-- ${translation} --</option>											
			</select>
			</td>
		</tr>
	</logic:iterate>
	<tr><td>&nbsp;</td><td colspan="2">
	<digi:trn key="aim:newFeatureTemplateNameBbl">Template Name:</digi:trn><input type="text" name="templateName" size="30"/>
	</td></tr>
	
	<tr><td>&nbsp;</td><td colspan="2">
		<c:set var="translation">
			<digi:trn key="aim:newFeatureTemplateSaveNewTemplate">Save New Template</digi:trn>
		</c:set>	
		<html:submit style="dr-menu" value="${translation}" property="newTemplate"/>
	</td></tr>
	</digi:form>
	
</table>

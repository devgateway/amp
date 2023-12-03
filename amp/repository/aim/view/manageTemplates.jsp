<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>

<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>

<%@ taglib uri="/taglib/struts-html" prefix="html" %>

<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ taglib uri="/taglib/jstl-core" prefix="c" %>



<script language="JavaScript">

	function onDelete() {
		var flag = confirm("Delete this Template?");
		return flag;
	}

    function deleteTemplate(id) {
    	if(!onDelete) return false;
        <digi:context name="url" property="context/ampModule/moduleinstance/featureManager.do?delete=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}
	
	function editTemplate(id) {
    	// if(!onDelete) return false;
        <digi:context name="url" property="context/ampModule/moduleinstance/featureManager.do?edit=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}



</script>





<digi:instance property="aimFeatureManagerForm" />

<table width="100%" cellspacing="1" cellpadding="1" valign="top" align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampTemplatesManager">
		AMP Templates Manager
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/featureManager.do" method="post" >
	<tr>
		<th>Template name</th>
		
		<th> Options</th>
	</tr>
	<jsp:useBean id="urlParams10" type="java.util.Map" class="java.util.HashMap"/>
	<jsp:useBean id="urlParams11" type="java.util.Map" class="java.util.HashMap"/>
	
	<logic:iterate name="aimFeatureManagerForm" property="templates" id="template"
	
		type="org.digijava.ampModule.aim.dbentity.FeatureTemplates">
		<tr bgcolor="#ffffff">
		<c:set target="${urlParams10}" property="event" value="edit"/>
		<c:set target="${urlParams10}" property="templateId" value="<%=template.getTemplateId() %>"/>
		<c:set target="${urlParams11}" property="event" value="delete"/>
		<c:set target="${urlParams11}" property="templateId" value="<%=template.getTemplateId() %>"/>
		
			<td width="70%"> <bean:write name="template" property="featureTemplateName"/> &nbsp;&nbsp;&nbsp; 
			<%@ page import="org.digijava.ampModule.aim.util.FeaturesUtil" %>
			<font color="#ff0000"> <%= FeaturesUtil.isDefault(template.getTemplateId())?"in use":""%> </font></td>

			<td width="30%" align="center">
			<c:set var="translation">
				<digi:trn key="aim:clickToDeleteTemplate">Click here to Delete Template</digi:trn>
			</c:set>
			[ <digi:link href="/featureManager.do" name="urlParams10" 
				title="${translation}" >Edit</digi:link> ]&nbsp;&nbsp;&nbsp;
			[ <digi:link href="/featureManager.do" name="urlParams11" 
				title="${translation}" onclick="return onDelete()">Delete</digi:link> ]
			</td>
		</tr>
	</logic:iterate>
	</digi:form>
	<tr>
		<td><br/>
		</td>
	</tr>
	<tr>
		<td>
			<c:set var="translation">
					<digi:trn key="aim:setTemplateInUse">Click here to Set the Template in use</digi:trn>
			</c:set>
			<digi:link href="/GlobalSettings.do" title="${translation}" ><digi:trn key="aim:GlobalSettings">Global Settings</digi:trn></digi:link>
		</td>
	</tr>
	
</table>





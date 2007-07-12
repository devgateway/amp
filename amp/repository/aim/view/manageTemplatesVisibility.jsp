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
        <digi:context name="url" property="context/module/moduleinstance/visibilityManager.do?delete=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}
	
	function editTemplate(id) {
    	// if(!onDelete) return false;
        <digi:context name="url" property="context/module/moduleinstance/visibilityManager.do?edit=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing=1 cellpadding=1 valign=top align=left>	
	<tr><td bgColor=#d7eafd class=box-title height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampTemplatesVisibilityManager">
		AMP Templates Visibility Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" >
	<tr>
		<th>Template name</th>
		
		<th> Options</th>
	</tr>
	<jsp:useBean id="urlParams10" type="java.util.Map" class="java.util.HashMap"/>
	<jsp:useBean id="urlParams11" type="java.util.Map" class="java.util.HashMap"/>
	
	<logic:iterate name="aimVisibilityManagerForm" property="templates" id="template"
	
		type="org.digijava.module.aim.dbentity.AmpTemplatesVisibility">	
		<tr bgcolor="#ffffff">
		<c:set target="${urlParams10}" property="action" value="edit"/>
		<c:set target="${urlParams10}" property="templateId" value="<%=template.getId() %>"/>
		<c:set target="${urlParams11}" property="action" value="delete"/>
		<c:set target="${urlParams11}" property="templateId" value="<%=template.getId() %>"/>
			<bean:define id="translation">
				<digi:trn key="aim:clickToEditTemplate">Click here to Edit Template</digi:trn>
			</bean:define>	
			<td width="70%"> <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="<%=translation%>" ><bean:write name="template" property="name"/></digi:link> &nbsp;&nbsp;&nbsp; 
				<%= template.isDefault()?"in use":"" %>
			</td>

			<td width="30%" align="center">

			[ <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="<%=translation%>" >Edit</digi:link> ]&nbsp;&nbsp;&nbsp;
			<bean:define id="translation">
				<digi:trn key="aim:clickToDeleteTemplate">Click here to Delete Template</digi:trn>
			</bean:define>
			[ <digi:link href="/visibilityManager.do" name="urlParams11" 
				title="<%=translation%>" onclick="return onDelete()">Delete</digi:link> ]
			</td>
		</tr>
	</logic:iterate>
	</digi:form>
	<tr>
		<td><br/>
		</td>
	</tr>
	<tr>
		<td><br/>
		</td>
	</tr>
	
		<tr>
		<td>
			<bean:define id="translation">
					<digi:trn key="aim:setTemplateInUse">Click here to Add a New Template </digi:trn>
			</bean:define>
			<jsp:useBean id="urlParams12" type="java.util.Map" class="java.util.HashMap"/>
			<c:set target="${urlParams12}" property="action" value="add"/>
			 <digi:link href="/visibilityManager.do" name="urlParams12" 
				title="<%=translation%>" ><digi:trn key="aim:addVisibilityTemplate">Add a New Template</digi:trn></digi:link>
		</td>
	</tr>
	
	
	<tr>
		<td>
				<bean:define id="translation">
					<digi:trn key="aim:setTemplateInUse">Click here to Set the Template in use</digi:trn>
			</bean:define>
			<digi:link href="/GlobalSettings.do" title="<%=translation%>" ><digi:trn key="aim:GlobalSettings">Global Settings</digi:trn></digi:link>
		</td>
	</tr>
	
</table>

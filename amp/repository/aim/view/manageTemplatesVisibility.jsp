<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script language="JavaScript">
	function onDelete() {
		var flag = confirm("<digi:trn key="aim:deletetemplates">Delete this Template?</digi:trn>");
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
	<digi:trn key="aim:ampFeatureManager">
		Feature Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" enctype="multipart/form-data">
	<tr>
		<th><digi:trn key="aim:featureManagerTemplateName">Template name</digi:trn></th>
		
		<th><digi:trn key="aim:featureManagerOptions">Options</digi:trn></th>
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
			<c:set var="translation">
				<digi:trn key="aim:clickToEditTemplate">Click here to Edit Template</digi:trn>
			</c:set>	
			<c:set var="translation1">
				<digi:trn key="aim:InUse">In use</digi:trn>
			</c:set>	
			
			<td width="70%"> <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}"><bean:write name="template" property="name"/></digi:link> &nbsp;&nbsp;&nbsp; 
				
				<%if (template.isDefault()){%>
					${translation1}
				<%}%>
				
				<%//=template.isDefault()?"'in use":""%>
			</td>

			<td width="30%" align="center">

			[ <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}" ><digi:trn key="aim:featureManagerEditLink">Edit</digi:trn></digi:link> ]&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
				<digi:trn key="aim:clickToDeleteTemplate">Click here to Delete Template</digi:trn>
			</c:set>
			[ <digi:link href="/visibilityManager.do" name="urlParams11"  
				title="${translation}" onclick="return onDelete()"><digi:trn key="aim:featureManagerDeleteLink">Delete</digi:trn></digi:link> ]
			</td>
		</tr>
	</logic:iterate>
	<tr>
		<td>
			<br/>
			<br/>
			<br/>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="3">
			<c:set var="translation">
				<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>
			</c:set>
			<html:submit style="dr-menu" value="${translation}" property="exportTreeVisibility" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:file property="uploadFile" size="20"/>
			
			<c:set var="translation">
				<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>
			</c:set>
			<html:submit style="dr-menu" value="${translation}" property="importTreeVisibility" />
		</td>
	</tr>
	</digi:form>
</table>

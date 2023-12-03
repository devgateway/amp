<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<style type="text/css">
<!--
div.fileinputs {
	position: relative;
	height: 30px;
	width: 300px;
}
input.file {
	width: 300px;
	margin: 0;
}
input.file.hidden {
	position: relative;
	text-align: right;
	-moz-opacity:0 ;
	filter:alpha(opacity: 0);
	width: 300px;
	opacity: 0;
	z-index: 2;
}

div.fakefile {
	position: absolute;
	top: 0px;
	left: 0px;
	width: 300px;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile input {
	margin-bottom: 5px;
	margin-left: 0;
	width: 217px;
}
div.fakefile2 {
	position: absolute;
	top: 0px;
	left: 217px;
	width: auto;
	padding: 0;
	margin: 0;
	z-index: 1;
	line-height: 90%;
}
div.fakefile2 input{
	width: 83px;
}
-->
</style>
<script language="JavaScript">
	function onDelete() {
		var flag = confirm("<digi:trn jsFriendly="true" key="aim:deletetemplates">Delete this Template?</digi:trn>");
		return flag;
	}
    function deleteTemplate(id) {
    	if(!onDelete) return false;
        <digi:context name="url" property="context/ampModule/moduleinstance/visibilityManager.do?delete=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}
	
	function editTemplate(id) {
    	// if(!onDelete) return false;
        <digi:context name="url" property="context/ampModule/moduleinstance/visibilityManager.do?edit=true" />
        document.aimFlagUploaderForm.action = "<%=url%>&templateId="+id;
        document.aimFlagUploaderForm.submit();
	}

</script>


<digi:instance property="aimVisibilityManagerForm" />
<table width="100%" cellspacing="0" cellpadding="0" valign="top" align=left class="inside">	
	<tr style="#C7D4DB"><td bgColor=#C7D4DB class="inside" height="20" align="center" colspan="3">
	<!-- Table title -->
	<digi:trn key="aim:ampFeatureManager">
		Feature Manager 
	</digi:trn>
	<!-- end table title -->										
	</td></tr>
	<digi:form action="/visibilityManager.do" method="post" enctype="multipart/form-data">
	<tr>
		<th class="inside" height=32 bgcolor=#F5F5F5><digi:trn key="aim:featureManagerTemplateName">Template name</digi:trn></th>
		
		<th class="inside" bgcolor=#F5F5F5 style="border-right:1px solid #ccc;"><digi:trn key="aim:featureManagerOptions">Options</digi:trn></th>
	</tr>
	<jsp:useBean id="urlParams10" type="java.util.Map" class="java.util.HashMap"/>
	<jsp:useBean id="urlParams11" type="java.util.Map" class="java.util.HashMap"/>
	
	<logic:iterate name="aimVisibilityManagerForm" property="templates" id="template"
	
		type="org.digijava.ampModule.aim.dbentity.AmpTemplatesVisibility">
		<tr bgcolor="#ffffff">
		<c:set target="${urlParams10}" property="action" value="edit"/>
		<c:set target="${urlParams10}" property="templateId" value="<%=template.getId() %>"/>
		<c:set target="${urlParams11}" property="action" value="delete"/>
		<c:set target="${urlParams11}" property="templateId" value="<%=template.getId() %>"/>
			<c:set var="translation">
				<digi:trn key="aim:clickToEditTemplate">Click here to Edit Template</digi:trn>
			</c:set>	

			<td width="70%" class="inside"> 
				<digi:link href="/visibilityManager.do" name="urlParams10" title="${translation}">
					<bean:write name="template" property="name"/>
				</digi:link> 
				&nbsp;&nbsp;&nbsp; 

				<logic:equal name="template" property="default" value="true">
					<digi:trn key="aim:InUse">In use (Default template)</digi:trn>
				</logic:equal>

				<logic:notEqual name="template" property="default" value="true">
					<logic:present name="template" property="usedByTeamsNames">
						Assigned to
							<logic:iterate name="template" property="usedByTeamsNames" id="team">
								"<bean:write name="team"/>"&nbsp;
							</logic:iterate>
							workspace(s)
					</logic:present>
				</logic:notEqual>
				
				
				
				
			</td>

			<td width="30%" align="center" class="inside">

			[ <digi:link href="/visibilityManager.do" name="urlParams10" 
				title="${translation}" ><digi:trn key="aim:featureManagerEditLink">Edit</digi:trn></digi:link> ]&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
				<digi:trn key="aim:clickToDeleteTemplate">Click here to Delete Template</digi:trn>
			</c:set>
			<%if (!template.isDefault() && template.getUsedByTeamsNames() == null){%>
					
			[ <digi:link href="/visibilityManager.do" name="urlParams11"  
				title="${translation}" onclick="return onDelete()"><digi:trn key="aim:featureManagerDeleteLink">Delete</digi:trn></digi:link> ]
				<%}
				else{%>
				[ <digi:trn key="aim:featureManagerDeleteLink">Delete</digi:trn>]
				<%}
				%>
				
			</td>
		</tr>
	</logic:iterate>
	<tr>
		<td colspan=2 class="inside">	
			
			<div style="width: 600px;">
				<div class="fileinputs" style="float: left;">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
					<!-- CSS content must be put in a separated file and a class must be generated -->
					<input id="uploadFile" name="uploadFile" type="file" class="inputx"  style="margin-top:7px;">
				</div>
				<div id="importButton" style="float: left;padding-left: 10px;">
					<c:set var="translation">
						<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>
					</c:set>
					<html:submit style="dr-menu" value="${translation}" styleClass="buttonx" property="importTreeVisibility" />
				</div>
			</div>
			<div>
				<br><br><br>
				<c:set var="translation">
					<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>
				</c:set>
				<html:submit value="${translation}" styleClass="buttonx" property="exportTreeVisibility" />
			</div>	
				
				
			<%--
				<div class="fileinputs">
				<table>
				<tr>
				<td><input id="uploadFile" name="uploadFile" type="file" style="font-size:12px; width:200px;" class="file"/></td>
				<td><c:set var="translation">
				<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>
			</c:set>
			<html:submit style="dr-menu" value="${translation}" styleClass="buttonx" property="importTreeVisibility" /></td>
				<td style="padding-left:15px;"><c:set var="translation">
				<digi:trn key="aim:translationmanagerexportbutton">Export</digi:trn>
			</c:set>
			<html:submit value="${translation}" styleClass="buttonx" property="exportTreeVisibility" /></td>
				</tr>
				</table>
				
				  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
					
						
				</div>
			 --%>
				
			
			
		</td>
	</tr>
	</digi:form>
</table>

<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>
   	

<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
	if (document.forms['crDocumentManagerForm'] && document.forms['crDocumentManagerForm'].pageCloseFlag.value == "true" ) {
        window.opener.location.replace(window.opener.location.href);
        window.close();
	}
</script>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
	width: 300px;
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

<script type="text/javascript">
	var W3CDOM = (document.createElement && document.getElementsByTagName);

	function initFileUploads() {
		if (!W3CDOM) return;
		var fakeFileUpload = document.createElement('div');
		fakeFileUpload.className = 'fakefile';
		fakeFileUpload.appendChild(document.createElement('input'));

		var fakeFileUpload2 = document.createElement('div');
		fakeFileUpload2.className = 'fakefile2';


		var button = document.createElement('input');
		button.type = 'button';

		button.value = '<digi:trn key="aim:browse">Browse...</digi:trn>';
		fakeFileUpload2.appendChild(button);

		fakeFileUpload.appendChild(fakeFileUpload2);
		var x = document.getElementsByTagName('input');
		for (var i=0;i<x.length;i++) {
			if (x[i].type != 'file') continue;
			if (x[i].parentNode.className != 'fileinputs') continue;
			x[i].className = 'file hidden';
			var clone = fakeFileUpload.cloneNode(true);
			x[i].parentNode.appendChild(clone);
			x[i].relatedElement = clone.getElementsByTagName('input')[0];

 			x[i].onchange = x[i].onmouseout = function () {
				this.relatedElement.value = this.value;
			}
		}
	}
</script>

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
			<%if (!template.isDefault()){%>
					
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
			<hr>
		</td>
	</tr>
	<tr>
		<td>	
			<a title="<digi:trn key="aim:FileLocation">Location of the document to be attached</digi:trn>">
				<div class="fileinputs">  <!-- We must use this trick so we can translate the Browse button. AMP-1786 -->
					<input id="uploadFile" name="uploadFile" type="file" class="file"/>
				</div>
			</a>
			<c:set var="translation">
				<digi:trn key="aim:translationmanagerimportbutton">Import</digi:trn>
			</c:set>
			<html:submit style="dr-menu" value="${translation}" property="importTreeVisibility" />
		</td>
	</tr>
	</digi:form>
</table>

<script type="text/javascript">
	initFileUploads();
	if ( document.crDocumentManagerForm.pageCloseFlag.value == "true" ) {
			window.opener.location.replace(window.opener.location.href); 
			window.close();
		}
</script>

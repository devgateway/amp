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


<!-- Yahoo Panel --> 


<script langauage="JavaScript">
	function onDelete() {
		var flag = confirm('<digi:trn key="admin:workSpaceManager.deleteQuestion" jsFriendly="true">Delete this workspace?</digi:trn>');
		return flag;
	}
	
</script>

<digi:instance property="messageForm" />
<table width="100%" cellspacing="2" cellpadding="2" valign="top" align="center" border="0">
	<tr>
	<!-- Start Navigation -->
		<td height=33><span class=crumb>
			<c:set var="translation">
				<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
			</c:set>
		    <digi:link ampModule="aim" href="/admin.do" styleClass="comment" title="${translation}" >
				<digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;													
			<c:set var="gotoTemplateAlertsPage">
				<digi:trn>Click here to goto Template Alert Page</digi:trn>
			</c:set>
			<digi:link href="/templatesManager.do~actionType=viewTemplates" styleClass="comment" title="${gotoTemplateAlertsPage}" >
				<digi:trn>Templates Manager</digi:trn>
			</digi:link>&nbsp;&gt;&nbsp;
			<digi:trn>Export/Import Templates</digi:trn>
		</td>
	<!-- End navigation -->
	</tr>
	<tr>
		<th width="50%"><digi:trn key="ie:import">Import</digi:trn></th>
		<th width="50%"><digi:trn key="ie:export">Export</digi:trn></th>
	</tr>
	<tr>
		<td align="center">
			<digi:errors/>
			<digi:form action="/exportImportTemplates.do?actionType=importTemplates" method="post" enctype="multipart/form-data">
				<table cellpadding="3" cellspacing="3">
					<tr>
						<td><digi:trn key="contentrepository:addEdit:Path">Path:</digi:trn><font color="red">*</font></td>
						<td>
						  	<div class="fileinputs">
								<input id="fileUploaded" name="fileUploaded" type="file" class="file">
						    </div>
						</td>						
					</tr>
					<tr><td></td></tr>
					<tr>
						<td>&nbsp;</td>
						<td>
							<html:submit style="dr-menu" value="Import"/>				
						</td>
					</tr>
				</table>
			</digi:form>	
		</td>
		<td align="center">
			<digi:form action="/exportImportTemplates.do?actionType=exportTemplates">
				<html:submit style="dr-menu" value="Export"/>
			</digi:form>	
		</td>
	</tr>
</table>

<script  type="text/javascript" src="<digi:file src="ampModule/aim/scripts/fileUpload.js"/>"></script>
<script type="text/javascript">
	initFileUploads('<digi:trn jsFriendly="true" key="aim:browse">Browse...</digi:trn>');
</script>

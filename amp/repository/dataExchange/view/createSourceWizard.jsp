<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:ref href="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/treeview.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="<digi:file src="js_2/yui/yahoo-dom-event.js"/>"></script> 


<script language="JavaScript" type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/js_2/yui/treeview/treeview-min.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/aim/scripts/tree/jktreeview.js"/>"></script>

<script type="text/javascript" src="/repository/dataExchange/view/scripts/TaskNode.js"></script>
<script type="text/javascript">

function cancelImportManager() {
    <digi:context name="url" property="/aim/admin.do" />
    window.location="<%= url %>";
}

 function importActivities(sourceId){
	 if(checkAttachment()){
		 var form = document.getElementById('form');
	      form.action = "/dataExchange/createSource.do?saveImport=true";
	      if(sourceId != -1){
	    	  form.action ="/dataExchange/editSource.do?action=saveSource&sourceId="+sourceId;
	      }
	      form.target="_self"
	      form.submit();
	 }
     
}
 function removeAttachment (attachmentOrder) {
	 var form = document.getElementById('form');
	 form.action = "/dataExchange/editSource.do?action=removeAttachment&attachmentOrder="+attachmentOrder;	 
	 form.target = "_self";
	 form.submit();
 }
 
 function checkAttachment (){
	 var attachmentDiv = document.getElementById("attachmentDiv");
	 if(attachmentDiv != null){
		 var currentFileToAttach = document.getElementById("uploadedFile");
		 if(currentFileToAttach.value != null && currentFileToAttach.value != ''){
			 alert('Please remove old attachment first');
			 return false;
		 }		 
	 }
	 return true;
 }
</script>

<digi:instance property="createSourceForm" />

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<!-- MAIN CONTENT PART START -->
<digi:form action="/createSource.do?saveImport=true" styleId="form" method="post" enctype="multipart/form-data">

<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center >
	<!-- BREADCRUMP START -->
	<tr>
		<td height="33">
			<div class="breadcrump_cont"> 
				<span class="sec_name"><digi:trn>Partial Data Import Manager</digi:trn></span>
				<span class="breadcrump_sep">|</span> <a href="/admin.do" class="l_sm"><digi:trn>Admin Home</digi:trn></a>
				<span class="breadcrump_sep"><b>»</b></span><a href="/dataExchange/manageSource.do" class="l_sm"><digi:trn>Import Manager</digi:trn></a>
				<span class="breadcrump_sep"><b>»</b></span>
				<span class="bread_sel"><digi:trn>Create/Edit Source</digi:trn></span>
			</div>
			<br>
		</td>
	</tr>
	<!-- BREADCRUMP END -->
	<tr>
    	<td class="main_side_1">
			<div class="wht">		
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
					    <td width=49% valign="top" class="inside" style="border: none;">
					    	<fieldset>
								<legend><span class=legend_label><digi:trn>General Details</digi:trn></span></legend>								
								<b>Name:</b> 
								<html:text property="name" styleClass="inputx"/>
								<br /><br />
								
								<b><digi:trn>Please choose the language(s) that exist in imported file</digi:trn>:</b><br />
								<logic:iterate name="createSourceForm" property="languages" id="lang">
        							<html:multibox property="selectedLanguages" >
        								<bean:write name="lang"/>
        							</html:multibox>
        							<bean:write name="lang"/>&nbsp;&nbsp;&nbsp;        						
        						</logic:iterate>
								<br /><br />
								
								<b><digi:trn>Please choose the type of source</digi:trn>:</b> <br>
								<logic:iterate name="createSourceForm" property="sourceValues" id="srcVal">  									
        							<html:radio property="source" value="${srcVal.key}">
		        						<digi:trn>${srcVal.value}</digi:trn>
		        					</html:radio>
		        					&nbsp;&nbsp;
        						</logic:iterate>				
								<br><br>
								
								<b>Please choose the workspace that will be used:</b><br />
								<html:select property="teamId" styleClass="inputx" style="margin-top:5px;">
        								<html:optionsCollection property="teamValues" label="name" value="ampTeamId"/>
        							</html:select>
        							<br /><br />
								
								<b><digi:trn>Please choose import strategy</digi:trn>:</b><br />
								<logic:iterate name="createSourceForm" property="importStrategyValues" id="impVal">
       								<html:radio property="importStrategy" value="${impVal.key}">
       									<digi:trn>${impVal.value}</digi:trn>&nbsp;&nbsp;
       								</html:radio>
        						</logic:iterate>
							</fieldset>
							<br />
							<fieldset>
								<legend><span class=legend_label>Filter and Identifier</span></legend>
								<b><digi:trn>Type unique identifier (title,id,ampid,ptip) separatd by '|'</digi:trn> </b>:
								<html:text property="uniqueIdentifier" styleClass="inputx" style="width:95%; margin-top:5px;"/><br /><br />
								<b>Select the approval status that the new activities will have:</b><br />
								
								<logic:iterate id="appStatus" name="createSourceForm" property="approvalStatusValues">
		                    		<html:radio property="approvalStatus" value="${appStatus.key}"><digi:trn>${appStatus.value}</digi:trn></html:radio> <br />
                    			</logic:iterate>								
							</fieldset>
							<br />							
							<fieldset>
								<legend><span class=legend_label>Upload a file</span></legend>
								<c:if test="${not empty createSourceForm.sdmDocument}">
									<c:forEach var="attachedDoc" items="${createSourceForm.sdmDocument.items}">
										<div id="attachmentDiv">
											<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
												<c:if test="${not empty createSourceForm.sdmDocument.id}">
													<c:set target="${urlParamsSort}" property="documentId" value="${createSourceForm.sdmDocument.id}"/>
												</c:if>																					
												<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${attachedDoc.paragraphOrder}" name="urlParamsSort">
													<img src="/repository/message/view/images/attachment.png" border="0" />
													${attachedDoc.contentTitle}
												</digi:link>
												<a href="javascript:removeAttachment(${attachedDoc.paragraphOrder})" title="Click Here To Remove Attachment" >
												<img  src="/TEMPLATE/ampTemplate/imagesSource/common/trash_16.gif" border="0"/></a>
										</div>
									</c:forEach>
								</c:if>
								<input name="uploadedFile" type="file" style="margin-top:7px;" class="inputx" id="uploadedFile">
							</fieldset>														
						</td>
						<td width=2%>&nbsp;</td>
				    	<td width=49% valign="top">
				    		<fieldset>
								<legend><span class=legend_label><digi:trn>Field Selection</digi:trn></span></legend>
								<a href=# class="t_sm" id="expand"><b><digi:trn>Expand all</digi:trn></b></a>&nbsp; | &nbsp;
								<a href=# class="t_sm" id="collapse"><b><digi:trn>Collapse all</digi:trn></b></a>&nbsp; | &nbsp;
								<a href=# class="t_sm" id="check"><b><digi:trn>Check all</digi:trn></b></a>&nbsp; | &nbsp;
								<a href=# class="t_sm" id="uncheck"><b><digi:trn>Uncheck all</digi:trn></b></a>
								<bean:define id="fieldsModuleTree" name="createSourceForm" property="activityTree" toScope="request" />
								<c:if test="${not empty createSourceForm.sourceId}">
									<bean:define id="sourceId" name="createSourceForm" property="sourceId" type="java.lang.Long" toScope="request" />
								</c:if>
								<jsp:include page="fieldsModule.jsp"></jsp:include>
							</fieldset>
						</td>
					</tr>
				</table>
				<br />
				<center>
					<input type="button" value="Save" class="buttonx" onclick="return importActivities('${createSourceForm.sourceId}')"/> &nbsp;&nbsp;
					<input type="button" value="Cancel" class="buttonx" onclick="cancelImportManager()"/>
				</center>
			</div>	
		</td>
	</tr>
</table>
</digi:form>
</body>

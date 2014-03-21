<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>

<digi:instance property="manageSourceForm" />

<script language="JavaScript">
function viewLog(sourceId) {
	var form = document.getElementById('manageForm');
	form.action = "/dataExchange/showLogs.do?htmlView=true&selectedSourceId="+sourceId+"&reset=true";
	form.target="_self"
	form.submit();	
}

function deleteSource1(sourceId){
	var msg = '<digi:trn>Delete this source?</digi:trn>';
	var result = confirm(msg);
	if (result==true) {
	//Logic to delete the item
		var form = document.getElementById('manageForm');
		form.action = "/dataExchange/manageSource.do?action=delete&selectedSourceId="+sourceId;
		form.target="_self"
		form.submit();
	}
}

function deleteSource(sourceId) {
	var msg = '<digi:trn>Delete this source?</digi:trn>';
	var result = confirm(msg);
	if (result==true) {
		var form = document.getElementById('manageForm');
		form.action = "/dataExchange/manageSource.do?action=delete&selectedSourceId="+sourceId;
		form.target="_self"
		form.submit();
	}
}

function execute(sourceId){
	var loadingImgDiv = document.getElementById("loadingImg");
	loadingImgDiv.style.display="block";
	var form = document.getElementById('manageForm');
	form.action = "/dataExchange/manageSource.do?action=executeIATI&executingSourceId="+sourceId;
	form.target="_self"
	form.submit();
}

function editSource(sourceId) {
	 var form = document.getElementById('manageForm');
	form.action = "/dataExchange/createEditSource.do?action=gotoEditPage&sourceId="+sourceId+"&displaySource=true";
	form.target="_self"
	form.submit();
}

function editConfiguration(sourceId){
	 var form = document.getElementById('manageForm');
		form.action = "/dataExchange/createEditSource.do?action=gotoEditPage&sourceId="+sourceId+"&displaySource=false";
		form.target="_self"
		form.submit();
}

function toggleGroup(group_id){
	var strId='#'+group_id;
	$(strId+'_minus').toggle();
	$(strId+'_plus').toggle();
	$('#source_'+group_id).toggle('fast');
}

function sortByVal(sortBy){
	var form = document.getElementById('manageForm');
	form.action = "/dataExchange/manageSource.do?sort="+sortBy;
	form.target="_self"
	form.submit();
}

function page (page){
	var form = document.getElementById('manageForm');
	form.action = "/dataExchange/manageSource.do?page="+page;
	form.target="_self";
	form.submit();	
}

</script>

<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<digi:form action="/manageSource.do" styleId="manageForm">
<h1 class="admintitle" style="text-align:left;">Data Import Manager</h1>
	<c:if test="${manageSourceForm.showResources==false}">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<!-- BREADCRUMP START -->
		<tr>
			<td height="33">
				<digi:errors/>
			</td>
		</tr>
		<!-- BREADCRUMP END -->
	  <tr>
	    <td class="main_side_1">
		<table width="980" border="0" cellspacing="0" cellpadding="0" style="margin:10px;">
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">&nbsp;</td>
	    <td align=right><a href="/dataExchange/mapFields.do" class="t_sm">
	    	<b><digi:trn>Mapping Tool</digi:trn></b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    	<a href="/dataExchange/importActionNew.do" class="t_sm"><b>[+] <digi:trn>IATI XML File</digi:trn></b></a>
	    	<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true&displaySource=false" class="t_sm"><b>[+] <digi:trn>Create New Configuration</digi:trn></b></a>
	    	<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true&displaySource=true" class="t_sm"><b>[+] <digi:trn>Create New Source</digi:trn></b></a>
	    	<a href="/dataExchange/manageSource.do?showResources=true" class="t_sm"><b><digi:trn>List Of Sources</digi:trn></b></a>
	    </td>
	  </tr>
	</table>
	<div style="text-align: center; display: none;" id="loadingImg" class="inside">
		<img src="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/loading.gif" border="0" height="17px"/>&nbsp;&nbsp; 
    	<b class="ins_title"><digi:trn>Loading, please wait ...</digi:trn></b>
	</div>
	<table class="inside" width=980 cellpadding="0" cellspacing="0" style="margin:10px;">
		<tr>
		<td colspan="5" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>			
			<b class="ins_header"><digi:trn>List of configurations</digi:trn></b></td>
		</tr>
		<tr>
		    <td width="300" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort!='name'}">
					<a href="javascript:sortByVal('name')">
						<b class="ins_title"><digi:trn>Name</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort=='name'}">
					<a href="javascript:sortByVal('name_desc')">
						<b class="ins_title"><digi:trn>Name</digi:trn></b> 
					</a>
				</c:if> 
				<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort=='name'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='name_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>		    	
		    </td>
		    <%-- <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort!='source'}">
					<a href="javascript:sortByVal('source')">
						<b class="ins_title"><digi:trn>Source</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source'}">
					<a href="javascript:sortByVal('source_desc')">
						<b class="ins_title"><digi:trn>Source</digi:trn></b>
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>		    	
		    </td>
		    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort!='workspace'}">
					<a href="javascript:sortByVal('workspace')">
						<b class="ins_title"><digi:trn>Workspace Used</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace'}">
					<a href="javascript:sortByVal('workspace_desc')">
						<b class="ins_title"><digi:trn>Workspace Used</digi:trn></b>
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>
		    </td>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title"><digi:trn>Show/Hide Details</digi:trn></b>
		    </td> --%>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center>
		    	<b class="ins_title"><digi:trn>Actions</digi:trn></b>
		    </td>
		</tr>
		<logic:empty name="manageSourceForm" property="pagedSources">
			<tr>
				<td colspan="5"><digi:trn>No records found</digi:trn> </td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="manageSourceForm" property="pagedSources">
			<logic:iterate id="source" name="manageSourceForm" property="pagedSources">
				<tr>
				    <td bgcolor="#FFFFFF" class="inside">${source.name}</td>
				    <%-- <td bgcolor="#FFFFFF" class="inside">${source.source}</td>
				    <td bgcolor="#FFFFFF" class="inside">${source.importWorkspace.name }</td>
				    <td bgcolor="#FFFFFF" class="inside" align="center" nowrap="nowrap">
				    	<img src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif" id="${source.id}_plus" onClick="toggleGroup('${source.id}')" style="cursor: pointer;"/>
				    	<div id="source_${source.id}" style="display: none;height: 355px;width:500px; overflow: auto;">				    		
				    		<table border="0">
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" colspan="2" rowspan="8" class="inside">				    				
				    				<img src="/TEMPLATE/ampTemplate/img_2/ico_blue_minus.gif"  id="${source.id}_minus" style="display: none;cursor: pointer; " onClick="toggleGroup('${source.id}')"/>	
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">				    				
				    				<strong><digi:trn>Name</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				${source.name}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Source</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.source}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Strategy</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.importStrategy}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Language</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.languageId}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Unique Identifier</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.uniqueIdentifier}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Approval Status</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.approvalStatus}
				    			</td>
				    		</tr>
				    		
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Attached File</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				<c:if test="${not empty source.attachedFile}">
										<c:forEach var="attachedDoc" items="${source.attachedFile.items}">
											<div>
												${attachedDoc.contentTitle}
											</div>											
										</c:forEach>
									</c:if>
				    			</td>
				    		</tr>
				    		
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Fields</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				<logic:notEmpty name="source" property="fields">
										<ul>
											<logic:iterate id="field" name="source" property="fields">
												<li>${field}</li>
											</logic:iterate>
										</ul>
									</logic:notEmpty>
				    			</td>
				    		</tr>
				    	</table>
				    	</div>
				    </td> --%>
				    <td bgcolor="#FFFFFF" class="inside" align="center">
				    	 <img src="/TEMPLATE/ampTemplate/img_2/ico_edit_perm.gif"  onclick="editConfiguration(${source.id});" style="cursor:pointer;"/> &nbsp;
				    	 <%-- <img src="/TEMPLATE/ampTemplate/img_2/execute_icon.gif" onClick="execute(${source.id});" style="cursor:pointer;" title="<digi:trn>Execute</digi:trn>"/>
				    	 <img src="/TEMPLATE/ampTemplate/img_2/view_log_icon.gif" onClick="viewLog(${source.id});" style="cursor:pointer;" title="<digi:trn>View Log</digi:trn>"/>
				    	 --%>
				    	 <img src="/TEMPLATE/ampTemplate/img_2/ico_del_perm.gif" onClick="deleteSource1(${source.id});" style="cursor:pointer;" title="<digi:trn>Delete</digi:trn>"/>
				    </td>
				</tr>		
			</logic:iterate>
		</logic:notEmpty>
	</table>
	<!-- MAIN CONTENT PART END -->
	<!-- Pagination -->
		<div class="paging" style="font-size:11px;margin:10px;">
			<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
			<c:forEach var="page" begin="1" end="${manageSourceForm.lastPage}">
				<bean:define id="currPage" name="manageSourceForm" property="currentPage" />
				<c:if test="${manageSourceForm.currentPage == page}">
					<b class="paging_sel">${page}</b>
				</c:if>
				<c:if test="${manageSourceForm.currentPage != page}">
					<c:set var="translation">
						<digi:trn>Click here to goto Next Page</digi:trn>
					</c:set>
					<a href="javascript:page(${page})" title="${translation}" class="l_sm">${page}</a>
				</c:if>
				|&nbsp;
			</c:forEach>
		</div>	
		<!-- end of Pagination -->
		</td>
	  </tr>
	</table>
	</c:if>
	<c:if test="${manageSourceForm.showResources==true}">
	<table width="1000" border="0" cellspacing="0" cellpadding="0" align=center>
		<!-- BREADCRUMP START -->
		<tr>
			<td height="33">
				<digi:errors/>
			</td>
		</tr>
		<!-- BREADCRUMP END -->
	  <tr>
	    <td class="main_side_1">
		<table width="980" border="0" cellspacing="0" cellpadding="0" style="margin:10px;">
	  <tr>
	    <td>&nbsp;</td>
	    <td align="right">&nbsp;</td>
	    <td align=right><a href="/dataExchange/mapFields.do" class="t_sm">
	    	<b><digi:trn>Mapping Tool</digi:trn></b></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	    	<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true&displaySource=false" class="t_sm"><b>[+] <digi:trn>Create New Configuration</digi:trn></b></a>
	    	<a href="/dataExchange/createEditSource.do?action=gotoCreatePage&htmlView=true&displaySource=true" class="t_sm"><b>[+] <digi:trn>Create New Source</digi:trn></b></a>
	    	<a href="/dataExchange/manageSource.do" class="t_sm"><b><digi:trn>List Of Configurations</digi:trn></b></a>
	    </td>
	  </tr>
	</table>
	<div style="text-align: center; display: none;" id="loadingImg" class="inside">
		<img src="/TEMPLATE/ampTemplate/js_2/yui/assets/skins/sam/loading.gif" border="0" height="17px"/>&nbsp;&nbsp; 
    	<b class="ins_title"><digi:trn>Loading, please wait ...</digi:trn></b>
	</div>
	<table class="inside" width=980 cellpadding="0" cellspacing="0" style="margin:10px;">
		<tr>
		<td colspan="5" align=center background="/TEMPLATE/ampTemplate/img_2/ins_header.gif" class=inside>			
			<b class="ins_header"><digi:trn>List of sources</digi:trn></b></td>
		</tr>
		<tr>
		    <td width="300" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort!='name'}">
					<a href="javascript:sortByVal('name')">
						<b class="ins_title"><digi:trn>Name</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort=='name'}">
					<a href="javascript:sortByVal('name_desc')">
						<b class="ins_title"><digi:trn>Name</digi:trn></b> 
					</a>
				</c:if> 
				<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort=='name'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='name_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>		    	
		    </td>
		    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort!='source'}">
					<a href="javascript:sortByVal('source')">
						<b class="ins_title"><digi:trn>Source</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source'}">
					<a href="javascript:sortByVal('source_desc')">
						<b class="ins_title"><digi:trn>Source</digi:trn></b>
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='source_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>		    	
		    </td>
		    <td background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<c:if test="${empty manageSourceForm.sort || manageSourceForm.sort!='workspace'}">
					<a href="javascript:sortByVal('workspace')">
						<b class="ins_title"><digi:trn>Workspace Used</digi:trn></b>	                            	
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace'}">
					<a href="javascript:sortByVal('workspace_desc')">
						<b class="ins_title"><digi:trn>Workspace Used</digi:trn></b>
					</a>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace'}">
					<img  src="/repository/aim/images/up.gif" border="0"/>
				</c:if> 
				<c:if test="${not empty manageSourceForm.sort && manageSourceForm.sort=='workspace_desc'}">
					<img src="/repository/aim/images/down.gif" />
				</c:if>
		    </td>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside>
		    	<b class="ins_title"><digi:trn>Show/Hide Details</digi:trn></b>
		    </td>
		    <td width="100" background="/TEMPLATE/ampTemplate/img_2/ins_bg.gif" class=inside align=center>
		    	<b class="ins_title"><digi:trn>Actions</digi:trn></b>
		    </td>
		</tr>
		<logic:empty name="manageSourceForm" property="pagedSources">
			<tr>
				<td colspan="5"><digi:trn>No records found</digi:trn> </td>
			</tr>
		</logic:empty>
		<logic:notEmpty name="manageSourceForm" property="pagedSources">
			<logic:iterate id="source" name="manageSourceForm" property="pagedSources">
				<tr>
				    <td bgcolor="#FFFFFF" class="inside">${source.name}</td>
				    <td bgcolor="#FFFFFF" class="inside">${source.source}</td>
				    <td bgcolor="#FFFFFF" class="inside">${source.importWorkspace.name }</td>
				    <td bgcolor="#FFFFFF" class="inside" align="center" nowrap="nowrap">
				    	<img src="/TEMPLATE/ampTemplate/img_2/ico_plus.gif" id="${source.id}_plus" onClick="toggleGroup('${source.id}')" style="cursor: pointer;"/>
				    	<div id="source_${source.id}" style="display: none;height: 355px;width:500px; overflow: auto;">				    		
				    		<table border="0">
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" colspan="2" rowspan="8" class="inside">				    				
				    				<img src="/TEMPLATE/ampTemplate/img_2/ico_blue_minus.gif"  id="${source.id}_minus" style="display: none;cursor: pointer; " onClick="toggleGroup('${source.id}')"/>	
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">				    				
				    				<strong><digi:trn>Name</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				${source.name}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Source</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.source}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Strategy</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.importStrategy}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Language</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.languageId}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Unique Identifier</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.uniqueIdentifier}
				    			</td>
				    		</tr>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Approval Status</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				${source.approvalStatus}
				    			</td>
				    		</tr>
				    		<%--
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Attached File</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				<c:if test="${not empty source.attachedFile}">
										<c:forEach var="attachedDoc" items="${source.attachedFile.items}">
											<div>
												${attachedDoc.contentTitle}
											</div>											
										</c:forEach>
									</c:if>
				    			</td>
				    		</tr>
				    		--%>
				    		<tr>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap">
				    				<strong><digi:trn>Fields</digi:trn>:</strong>
				    			</td>
				    			<td style="border: none;vertical-align: text-top;" class="inside" nowrap="nowrap" colspan="2">
				    				<logic:notEmpty name="source" property="fields">
										<ul>
											<logic:iterate id="field" name="source" property="fields">
												<li>${field}</li>
											</logic:iterate>
										</ul>
									</logic:notEmpty>
				    			</td>
				    		</tr>
				    	</table>
				    	</div>
				    </td>
				    <td bgcolor="#FFFFFF" class="inside" align="center">
				    	 <img src="/TEMPLATE/ampTemplate/img_2/ico_edit_perm.gif"  onclick="editSource(${source.id});" style="cursor:pointer;"/> &nbsp;
				    	 <img src="/TEMPLATE/ampTemplate/img_2/execute_icon.gif" onClick="execute(${source.id});" style="cursor:pointer;" title="<digi:trn>Execute</digi:trn>"/>
				    	 <img src="/TEMPLATE/ampTemplate/img_2/view_log_icon.gif" onClick="viewLog(${source.id});" style="cursor:pointer;" title="<digi:trn>View Log</digi:trn>"/>
				    	 <img src="/TEMPLATE/ampTemplate/img_2/ico_del_perm.gif" onClick="deleteSource1(${source.id});" style="cursor:pointer;" title="<digi:trn>Delete</digi:trn>"/>
				    	
				    </td>
				</tr>		
			</logic:iterate>
		</logic:notEmpty>
	</table>
	<!-- MAIN CONTENT PART END -->
	<!-- Pagination -->
		<div class="paging" style="font-size:11px;margin:10px;">
			<b class="ins_title"><digi:trn>Pages :</digi:trn></b>
			<c:forEach var="page" begin="1" end="${manageSourceForm.lastPage}">
				<bean:define id="currPage" name="manageSourceForm" property="currentPage" />
				<c:if test="${manageSourceForm.currentPage == page}">
					<b class="paging_sel">${page}</b>
				</c:if>
				<c:if test="${manageSourceForm.currentPage != page}">
					<c:set var="translation">
						<digi:trn>Click here to goto Next Page</digi:trn>
					</c:set>
					<a href="javascript:page(${page})" title="${translation}" class="l_sm">${page}</a>
				</c:if>
				|&nbsp;
			</c:forEach>
		</div>	
		<!-- end of Pagination -->
		</td>
	  </tr>
	</table>
	</c:if>
</digi:form>
</body>
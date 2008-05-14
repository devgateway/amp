<%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/category" prefix="category"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="org.digijava.module.aim.helper.CategoryConstants"%>

<jsp:include page="/repository/aim/view/teamPagesHeader.jsp" flush="true" />

<%@include file="addDocumentPanel.jsp" %>

<digi:errors />

<digi:instance property="crDocumentManagerForm" />
<bean:define id="myForm" name="crDocumentManagerForm" toScope="page"
	type="org.digijava.module.contentrepository.form.DocumentManagerForm" />

<bean:define id="isTeamLeader" name="myForm" property="teamLeader" />
<bean:define id="meTeamMember" name="myForm" property="teamMember" />

<bean:define id="tMembers" name="myForm" property="teamMembers" />

<%@include file="documentManagerJsHelper.jsp" %>


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

<table bgColor=#ffffff cellPadding=0 cellSpacing=0 width="75%"
	class="box-border-nopadding">
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td valign="bottom" class="crumb" >
			&nbsp;&nbsp;&nbsp;
			<c:set var="translation">
					<digi:trn key="aim:clickToViewMyDesktop">Click here to view MyDesktop</digi:trn>
			</c:set>
			<digi:link href="/../aim/viewMyDesktop.do" styleClass="comment" title="${translation}" >
               	<digi:trn key="aim:MyDesktop">My Desktop</digi:trn>
               </digi:link> &gt; <digi:trn key="contentrepository:contentRepository">Content Repository</digi:trn>
			<br />
			<br />
		</td>
	</tr>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg vAlign=top>
				
		<span class=subtitle-blue> &nbsp;&nbsp; <digi:trn key="contentrepository:contentRepository">
							Content Repository
	</digi:trn> </span> <br />
		<br />
		<%--
		<table border="1" cellPadding=5 cellSpacing=0 width="56%"
			style="position: relative; left: 20px">
			<tr>
				<td bgcolor="#006699" class="textalb">
				<a style="cursor:pointer"  onclick="isMinusPrivate=toggleView('myDocumentstr', 'clipIcon', isMinusPrivate)">
				<img
					border="0" align="absmiddle"
					src="/repository/contentrepository/view/images/dhtmlgoodies_minus.gif" id="clipIcon" />
				<digi:img skipBody="true"
					border="0" align="absmiddle"
					src="module/contentrepository/images/folder_folder.gif" />
				</a>
					<digi:trn key="contentrepository:documentManagerMyDocuments">My Documents</digi:trn>
				</td>
			</tr>
			<tr style="display: table-row" id="myDocumentstr">
				<td colspan="2"><logic:notEmpty name="crDocumentManagerForm"
					property="myPersonalDocuments">
					<ul>
						<logic:iterate name="crDocumentManagerForm"
							property="myPersonalDocuments" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData">
							<%
					int index2;
					String documentName = documentData.getName();
					String iconPath = "";
					index2 = ((String) documentName).lastIndexOf(".");
					if (index2 >= 0) {
						iconPath = "module/cms/images/extensions/"
								+ ((String) documentName).substring(index2 + 1,
										((String) documentName).length())
								+ ".gif";
					}

					%>
							<li><digi:img skipBody="true" src="<%=iconPath%>" border="0"
								align="absmiddle" /> <bean:write name="documentData"
								property="name" /> <bean:define id="translation">
								<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
							</bean:define> <a
								href="/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />"
								title="<%= translation %>"> [<digi:trn
								key="contentrepository:documentManagerDownload">Download</digi:trn>] </a></li>
						</logic:iterate>
					</ul>
				</logic:notEmpty>
				<button type="button" onClick="setType('private');configPanel(0,'','',''); showMyPanel(0, 'addDocumentDiv'); ">Add File ...</button>
			</td>
				
			</tr>
		</table>
		<br />
		<br /> --%>
		<%-- Table for "My Documents" --%>
				<table border="0" cellPadding=5 cellSpacing=0 width="95%"
			style="position: relative; left: 20px">
			<tr>
				<td style="background-image:url(/repository/contentrepository/view/images/left-side.gif); background-repeat: no-repeat; background-position: top right" 
				width="13" height="20"> </td>
				<td bgcolor="#006699" class="textalb" height="20" width="97%" valign="middle" style="font-size: 11px; padding-bottom: 1px; padding-top: 1px">
				<a style="cursor:pointer"  onclick="isMinusPrivate=toggleView('myDocumentstr', 'clipIcon', isMinusPrivate)">
				<img
					border="0" align="absmiddle"
					src="/repository/contentrepository/view/images/dhtmlgoodies_minus.gif" id="clipIcon" />
				<digi:img skipBody="true" height="16"
					border="0" align="absmiddle"
					src="module/contentrepository/images/folder_folder.gif" />
				</a>
					<digi:trn key="contentrepository:documentManagerMyDocuments">My Documents</digi:trn>
				</td>
				<td background="/repository/contentrepository/view/images/right-side.gif" width="13" height="20" 
				style="background-image:url(/repository/contentrepository/view/images/right-side.gif); background-repeat: no-repeat; background-position: top left"> </td>
			</tr>
			<tr style="display: table-row" id="myDocumentstr">
				<td colspan="3" bgcolor="#f4f4f2" style="border-color: #006699; border-left-style: solid; border-left-width: thin; 
					border-bottom-style: solid; border-bottom-width: thin; border-right-style: solid; border-right-width: thin; ">
					<logic:notEmpty name="crDocumentManagerForm" property="myPersonalDocuments">
					<br />
					<div id="my_markup" align="center" class="all_markup">
					<bean:define name="crDocumentManagerForm" property="myPersonalDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
					<jsp:include page="documentTable.jsp" flush="true" />
					<%-- <table id="my_table">
						<thead>
							<tr>
								<th>
                                <digi:trn key="contentrepository:TableHeader:FileName">File Name</digi:trn>   
                                </th>
								<th><digi:trn key="contentrepository:TableHeader:ResourceTitle">Resource Title</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Date">Date</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:ContentType">Content Type</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Description">Description</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Actions">Actions</digi:trn></th>
                             
							</tr>
						</thead>
						<logic:iterate name="crDocumentManagerForm"
							property="myPersonalDocuments" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData">
							<%
					int index2;
					String documentName = documentData.getName();
					String iconPath = "";
					index2 = ((String) documentName).lastIndexOf(".");
					if (index2 >= 0) {
						iconPath = "module/cms/images/extensions/"
								+ ((String) documentName).substring(index2 + 1,
										((String) documentName).length())
								+ ".gif";
					}

					%>
							<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="makePublicCommand">
									<digi:trn key="contentrepository:republish">Rep</digi:trn>
								</c:set>
							</logic:equal>
							<logic:equal name="documentData" property="isPublic" value="false">
								<c:set var="makePublicCommand">
									<digi:trn key="contentrepository:makePublic">Pub</digi:trn>
								</c:set>>
							</logic:equal>
							<tr>
								<td>
									 <bean:write name="documentData" property="name" />
									 <digi:img skipBody="true" src="<%=iconPath%>" border="0"
									align="absmiddle" />
								</td>
								<td>
									<bean:write name="documentData" property="title" />
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								<td>
									<bean:write name="documentData" property="contentType" />
									
								</td>
								<td>
									<bean:write name="documentData" property="description" />
								</td>
								<td> 
								<a style="display:none" id="fake1" href="http://www.yahoo.com">FOR_SILK</a>
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
								</c:set> 
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="D<bean:write name='documentData' property='uuid' />"
								onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"

								title="${translation}">[<digi:trn key="contentrepository:documentManagerDownload">D</digi:trn>]</a>
								

								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasVersioningRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="plus<bean:write name='documentData' property='uuid' />"
								onClick="setType('version'); configPanel(0,'<%=documentData.getTitle() %>','<%=documentData.getDescription() %>','<%=documentData.getUuid() %>');showMyPanel(0, 'addDocumentDiv');"
								title="${translation}">[<digi:trn key="contentrepository:documentManagerAddVersion">+</digi:trn>]</a>
								
								</logic:equal>
								
								<c:set var="translationForWindowTitle">
									<digi:trn key="contentrepository:versionHistoryWindowTitle">Version History</digi:trn>
								</c:set> 
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>
								</c:set> 
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="H<bean:write name='documentData' property='uuid' />"
								onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}' +' - '+ '<%= documentData.getTitle() %>');"
								title="${translation}">[<digi:trn key="contentrepository:documentManagerVersions">H</digi:trn>]</a>
								
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerMakePublicHint">Click here to make this document public</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasMakePublicRights" value="true">
									<c:if test="${ (!documentData.isPublic)||(!documentData.lastVersionIsPublic) }">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
									title="${translation}">[<c:out value="${makePublicCommand}" />]</a>
									</c:if>
								</logic:equal>
								
								
								<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerUnpublishHint">Click here to unpublish this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
									title="${translation}">[<digi:trn key="contentrepository:documentManagerUnpublish">Priv</digi:trn>]</a>
								</logic:equal>
								
								</logic:equal>
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRights" value="true">
									<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
									onClick="deleteRow('<%=documentData.getUuid() %>');"
									title="${translation}">[<digi:trn key="contentrepository:documentManagerDelete">Del</digi:trn>]</a>
								</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table>--%>
					</div>
					<br />
				</logic:notEmpty>
				<button type="button" class="buton" onClick="setType('private');configPanel(0,'','','', false); showMyPanel(0, 'addDocumentDiv'); ">
	               <digi:trn key="contentrepository:addResource">
	 	    	       Add Resource ...
    				</digi:trn>            
                </button>
				</td>
			</tr>
		</table>
		<%-- END -- Table for "My Documents" --%>
		<br />
		<br />
		
		<table border="0" cellPadding=5 cellSpacing=0 width="95%"
			style="position: relative; left: 20px" >
			<tr>
				<td style="background-image:url(/repository/contentrepository/view/images/left-side.gif); background-repeat: no-repeat; background-position: top right" 
				width="13" height="20"> </td>
				<td bgcolor="#006699" class="textalb" height="20" width="97%" valign="middle" style="font-size: 11px; padding-bottom: 1px; padding-top: 1px">
				<a style="cursor:pointer"  onclick="isMinusTeam=toggleView('teamDocumentstr', 'clipIconTD', isMinusTeam)">
				<img
					border="0" align="absmiddle"
					src="/repository/contentrepository/view/images/dhtmlgoodies_minus.gif" id="clipIconTD" />
				<digi:img skipBody="true" height="16"
					border="0" align="absmiddle"
					src="module/contentrepository/images/folder_folder.gif" />
				</a>
					<digi:trn key="contentrepository:documentManagerTeamDocuments">Team Documents</digi:trn>
				</td>
				<td style="background-image:url(/repository/contentrepository/view/images/right-side.gif); background-repeat: no-repeat; background-position: top left" 
				width="13" height="20"> </td>
			</tr>
			<tr style="display: table-row" id="teamDocumentstr">
				<td colspan="3" bgcolor="#f4f4f2" style="border-color: #006699; border-left-style: solid; border-left-width: thin; 
					border-bottom-style: solid; border-bottom-width: thin; border-right-style: solid; border-right-width: thin; ">
					<logic:notEmpty name="crDocumentManagerForm" property="myTeamDocuments">
					<br />
					<div id="team_markup" align="center" class="all_markup">
					<bean:define name="crDocumentManagerForm" property="myTeamDocuments" id="documentDataCollection" type="java.util.Collection" toScope="request" />
					<jsp:include page="documentTable.jsp" flush="true" />
					<%-- <table id="team_table">
						<thead>
							<tr>
							  <th> <digi:trn key="contentrepository:TableHeader:FileName">File Name</digi:trn></th>
							  <th><digi:trn key="contentrepository:TableHeader:ResourceTitle">Resource Title</digi:trn></th>
							  <th><digi:trn key="contentrepository:TableHeader:Date">Date</digi:trn></th>
							  <th><digi:trn key="contentrepository:TableHeader:ContentType">Content Type</digi:trn></th>
							  <th><digi:trn key="contentrepository:TableHeader:Description">Description</digi:trn></th>
								<th><digi:trn key="contentrepository:TableHeader:Actions">Actions</digi:trn></th>
							</tr>
						</thead>
						<logic:iterate name="crDocumentManagerForm"
							property="myTeamDocuments" id="documentData"
							type="org.digijava.module.contentrepository.helper.DocumentData">
							<%
					int index2;
					String documentName = documentData.getName();
					String iconPath = "";
					index2 = ((String) documentName).lastIndexOf(".");
					if (index2 >= 0) {
						iconPath = "module/cms/images/extensions/"
								+ ((String) documentName).substring(index2 + 1,
										((String) documentName).length())
								+ ".gif";
					}

					%>
							<logic:equal name="documentData" property="isPublic" value="true">
								<bean:define id="makePublicCommand">
									<digi:trn key="contentrepository:republish">Rep</digi:trn>
								</bean:define>
							</logic:equal>
							<logic:equal name="documentData" property="isPublic" value="false">
								<bean:define id="makePublicCommand">
									<digi:trn key="contentrepository:makePublic">Pub</digi:trn>
								</bean:define>
							</logic:equal>
							<tr>
								<td>
									 <bean:write name="documentData" property="name" />
									 <digi:img skipBody="true" src="<%=iconPath%>" border="0"
									align="absmiddle" />
								</td>
								<td>
									<bean:write name="documentData" property="title" />
								</td>
								<td>
									<bean:write name="documentData" property="calendar" />
								</td>
								<td>
									<bean:write name="documentData" property="contentType" />
									
								</td>
								<td>
									<bean:write name="documentData" property="description" />
								</td>
								<td> 
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDownloadHint">Click here to download document</digi:trn>
								</c:set> 
								<a style="display:none" id="fake2" href="http://www.yahoo.com">FOR_SILK</a>
								
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="D<bean:write name='documentData' property='uuid' />"
								onClick="window.location='/contentrepository/downloadFile.do?uuid=<bean:write name='documentData' property='uuid' />'"
								
								title="${translation}">[<digi:trn key="contentrepository:documentManagerDownload">D</digi:trn>]</a>

								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerAddVersionHint">Click here to add a new version of this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasVersioningRights" value="true">
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="Plus<bean:write name='documentData' property='uuid' />"
								onClick="setType('version'); configPanel(0,'<%=documentData.getTitle() %>','<%=documentData.getDescription() %>','<%=documentData.getUuid() %>');showMyPanel(0, 'addDocumentDiv');"
								title="${translation }">[<digi:trn key="contentrepository:documentManagerAddVersion">+</digi:trn>]</a>
		
								</logic:equal>
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerVersionsHint">Click here to see a list of versions for this document</digi:trn>
								</c:set> 
								<a style="cursor:pointer; text-decoration:underline; color: blue" id="H<bean:write name='documentData' property='uuid' />"
								onClick="showMyPanelCopy(1,'viewVersions'); requestVersions('<%=documentData.getUuid() %>'); setPanelHeader(1, '${translationForWindowTitle}' +' - '+ '<%= documentData.getTitle() %>');"
								title="${translation }">[<digi:trn key="contentrepository:documentManagerVersions">H</digi:trn>]</a> 
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerMakePublicHint">Click here to make this document public</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasMakePublicRights" value="true">
									<c:if test="${ (!documentData.isPublic)||(!documentData.lastVersionIsPublic) }">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Pub<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.MAKE_PUBLIC %>' ,'<%=documentData.getUuid() %>', true);"
									title="${translation}">[<bean:write name="makePublicCommand"/>]</a>
									</c:if>
								</logic:equal>

								
								<logic:equal name="documentData" property="isPublic" value="true">
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerUnpublishHint">Click here to unpublish this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRightsOnPublicVersion" value="true">
									<a style="cursor:pointer; text-decoration:underline; color: blue" id="Priv<bean:write name='documentData' property='uuid' />"
									onClick="setAttributeOnNode('<%= org.digijava.module.contentrepository.helper.CrConstants.UNPUBLISH %>', '<%=documentData.getUuid() %>');"
									title="${translation }">[<digi:trn key="contentrepository:documentManagerUnpublish">Priv</digi:trn>]</a>
								</logic:equal>

								</logic:equal>
								
								<c:set var="translation">
									<digi:trn key="contentrepository:documentManagerDeleteHint">Click here to delete this document</digi:trn>
								</c:set>
								<logic:equal name="documentData" property="hasDeleteRights" value="true">
									<a  id="a<%=documentData.getUuid() %>" style="cursor:pointer; text-decoration:underline; color: blue"
									onClick="deleteRow('<%=documentData.getUuid() %>');"
									title="${translation }">[<digi:trn key="contentrepository:documentManagerDelete">Del</digi:trn>]</a>
								</logic:equal>
								</td>
							</tr>
						</logic:iterate>
					</table> --%>
					</div>
					<br />
				</logic:notEmpty>
				<c:if test="${isTeamLeader}">
					<button class="buton" type="button" onClick="setType('team'); configPanel(0,'','','', false);showMyPanel(0, 'addDocumentDiv');">						
                            	<digi:trn key="contentrepository:addResource">
	 	    	       				Add Resource ...
    							</digi:trn>            
					</button>
				</c:if>
				</td>
			</tr>
		</table>
		<br />
		<br />
		<c:set var="translation">
			<digi:trn key="contentrepository:newWindowExplanation">Click here to open a new document window</digi:trn>
		</c:set>
		<div id="otherDocumentsDiv"></div>
		&nbsp;&nbsp;<a title="${translation}" style="cursor:pointer; text-decoration:underline; color: blue" onClick="newWindow('<digi:trn key="contentrepository:SelectDocuments">Select Documents</digi:trn>', true,'otherDocumentsDiv')" />
			<digi:trn key="contentrepository:newWindow">New window</digi:trn>
			
			</a>
		</td>
	</tr>
</table>

<div id="addDocumentDiv" style="display: none">
	<div align="center">
	<div id="addDocumentErrorHolderDiv" style="font-size:11px; color: red"></div>
	<digi:form action="/documentManager.do" method="post" enctype="multipart/form-data" >
		<input type="hidden" name="type" id="typeId"/>
		<input type="hidden" name="uuid" id="nodeUUID"/>
		<table cellpadding="3" cellspacing="3" border="0">
			<tr>
				<td> 
					<digi:trn key="contentrepository:addEdit:typeDocument">Document</digi:trn>
					<input name="webResource" type="radio" value="false" onclick="selectResourceType()" />
				</td>
				<td> 
					<digi:trn key="contentrepository:addEdit:typeUrl">URL</digi:trn>
					<input name="webResource" type="radio" value="true" onclick="selectResourceType()"/>
				</td>
			</tr>
			<tr>
			<td><strong><digi:trn key="contentrepository:addEdit:Title">Title:</digi:trn></strong><font color="red">*</font></td>
			<td><html:text property="docTitle" size="30" /></td>
			</tr>
			<tr>
			<td><strong><digi:trn key="contentrepository:addEdit:Description">Description:</digi:trn></strong></td>
			<td><html:textarea property="docDescription" cols="28"/></td>
			</tr>
			<tr>
			<td><strong><digi:trn key="contentrepository:addEdit:Notes">Notes:</digi:trn></strong></td>
			<td><html:textarea property="docNotes" cols="28" /></td>
			</tr>
			<tr>
			<td><strong><digi:trn key="aim:typeOfTheDocument">Type:</digi:trn></strong></td>
			<td>
				<c:set var="translation">
					<digi:trn key="contentrepository:doctype:firstline">Please select a type from below</digi:trn>
				</c:set>
				<category:showoptions firstLine="${translation}" name="crDocumentManagerForm" property="docType"  keyName="<%= CategoryConstants.DOCUMENT_TYPE_KEY %>" styleClass="inp-text" />
			</td>
			</tr>
			<tr id="tr_path">
			<td><strong><digi:trn key="contentrepository:addEdit:Path">Path:</digi:trn><font color="red">*</font></strong></td>
			<td>
                             <!-- <html:file property="fileData"></html:file> -->
                             <div class="fileinputs"> 
				
			<input id="fileData" name="fileData" type="file" class="file">
                        </div></td>
			</tr>
			<tr style="display: none" id="tr_url">
			<td><strong><digi:trn key="contentrepository:addEdit:Url">URL:</digi:trn><font color="red">*</font></strong></td>
			<td><html:text property="webLink" size="32"></html:text></td>
			</tr>
			<tr>
				<td align="right">
					<html:submit styleClass="buton" onclick="return validateAddDocument()"><digi:trn key="contentrepository:addEdit:Submit">Submit</digi:trn></html:submit>&nbsp;
				</td>
				<td align="left">
					&nbsp;<button class="buton" style="font-size: x-small" type="button" onClick="hidePanel(0)"><digi:trn key="contentrepository:addEdit:Cancel">Cancel</digi:trn></button>
				</td>
			</tr>
		</table>
	</digi:form>
	</div>
</div>

<%@include file="documentManagerDivHelper.jsp" %>

<script type="text/javascript">
YAHOO.namespace("YAHOO.amp.table");
YAHOO.amp.table.mytable	= YAHOO.amp.table.enhanceMarkup("team_markup");
YAHOO.amp.table.teamtable	= YAHOO.amp.table.enhanceMarkup("my_markup");
</script>
<script type="text/javascript">
	initFileUploads();
</script>

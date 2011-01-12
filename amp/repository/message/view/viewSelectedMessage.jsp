<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>

<digi:instance property="messageForm" />
<c:set var="contextPath" scope="session">${pageContext.request.contextPath}</c:set>






<div class="message"><div class="message_cont">
	<div style="float:right;">Priority: <b>
		<logic:equal name="messageForm" property="priorityLevel" value="0">None</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="1">low</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="2">medium</logic:equal>
		<logic:equal name="messageForm" property="priorityLevel" value="3">Critical</logic:equal>	
		</b><br />
		Date: <b>${messageForm.creationDate}</b></div>
	From: <b>
		<c:set var="senderInfo" value="${fn:split(messageForm.sender,';')}"/>
																					<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;">
																						<c:out value="${senderInfo[0]}"/>
																					</a>
		
		
		</b><br />
		To: <b>${messageForm.receivesrsNameMail[0].userNeme}</b> (<a href="#" onClick="return false" class="view_all_recipients">view all</a>)<br />

		<div class="msg_all" style="background-color: white; border: 1px solid #EBEBEB; display:none;">
			<table border="0" cellspacing="1" cellpadding="1" width="100%" style="Font-size: 8pt;">
				<tr><td width="50%">
					<b>Member</b>
				</td><td width="50%">
					<b>Workspace</b>
				</td></tr>
				<tr><td colspan=2><hr /></td></tr>
				<c:forEach var="recipient" items="${messageForm.receivesrsNameMail}">
					<tr><td>
						${recipient.userNameFiltered}
					</td><td>
						${recipient.teamName}
					</td></tr>
				</c:forEach>
			</table>
		</div>


<c:if test="${not empty messageForm.objectURL}">

<b><digi:trn key="message:objURL">object URL</digi:trn></b></td>
<a href="${messageForm.objectURL}" target="_blank"><digi:trn key="message:ClickViewDetails">Click here to view details</digi:trn></a>
																				</c:if>


</div>
<div class="message_body">
${messageForm.description}
</div>

	<c:if test="${not empty messageForm.sdmDocument}">
		<%--
		<hr class=hr_3>
		--%>
		<img src="/TEMPLATE/ampTemplate/img_2/ico_attachment.png" width="16" height="16" align=left style="margin-right:3px;"> <b>Attachments</b>:
		<div class="msg_attachments">
			<c:forEach var="item" items="${messageForm.sdmDocument.items}">
					<hr>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_other.gif" align=left style="margin-right:5px;">
					<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
					<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>																																														
					<digi:link module="sdm" href="/showFile.do~activeParagraphOrder=${item.paragraphOrder}" name="urlParamsSort">
						${item.contentTitle}
					</digi:link>
					
				
			</c:forEach>
			</div>
	</c:if>

</div>

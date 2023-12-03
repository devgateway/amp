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

<div class="message">
	<div class="message_cont">
	<div style="float:right;">
		<digi:trn>Priority</digi:trn>:
		<b>
			<logic:equal name="messageForm" property="priorityLevel" value="0"><digi:trn>None</digi:trn></logic:equal>
			<logic:equal name="messageForm" property="priorityLevel" value="1"><digi:trn>low</digi:trn></logic:equal>
			<logic:equal name="messageForm" property="priorityLevel" value="2"><digi:trn>medium</digi:trn></logic:equal>
			<logic:equal name="messageForm" property="priorityLevel" value="3"><digi:trn>Critical</digi:trn></logic:equal>	
		</b><br />
		<digi:trn>Date</digi:trn>: <b>${messageForm.creationDate}</b>
	</div>
	<digi:trn>From</digi:trn>: 
	<b>
		<c:set var="senderInfo" value="${fn:split(messageForm.sender,';')}"/>
		<a  title='<c:out value="${senderInfo[1]}"/>' style="color: #05528B; text-decoration:underline;">
			<c:out value="${senderInfo[0]}"/>
		</a>
		
	</b>
	<br />
	<digi:trn>To</digi:trn>: <b><c:out value="${messageForm.receivesrsNameMail[0].userNeme}"/></b> (<a href="#" onClick="return false" class="view_all_recipients"><digi:trn>view all</digi:trn></a>)<br />
		<div class="msg_all" style="background-color: white; border: 1px solid #EBEBEB; display:none;">
			<table border="0" cellspacing="1" cellpadding="1" width="100%" style="Font-size: 8pt;">
				<tr><td width="50%">
					<b><digi:trn>Member</digi:trn></b>
				</td><td width="50%">
					<b><digi:trn>Workspace</digi:trn></b>
				</td></tr>
				<tr><td colspan=2><hr /></td></tr>
				<c:forEach var="recipient" items="${messageForm.receivesrsNameMail}">
					<tr><td>
						${recipient.userNameFiltered}
					</td><td>
						<c:out value="${recipient.teamName}"/>
					</td></tr>
				</c:forEach>
			</table>
		</div>


<c:if test="${not empty messageForm.objectURL}">
	<b><digi:trn>Related Activity URL</digi:trn></b>
	<a href="${messageForm.objectURL}" target="_blank">
		<digi:trn>Click here to view details</digi:trn>
	</a>
</c:if>

</div>
<div class="message_body">
	${messageForm.description}
</div>
<c:if test="${not empty messageForm.sdmDocument}">
		<%--
		<hr class=hr_3>
		--%>
		<img src="/TEMPLATE/ampTemplate/img_2/ico_attachment.png" width="16" height="16" align=left style="margin-right:3px;"> <b><digi:trn>Attachments</digi:trn></b>:
		<div class="msg_attachments">
			<c:forEach var="item" items="${messageForm.sdmDocument.items}">
					<hr>
					<img src="/TEMPLATE/ampTemplate/img_2/ico_other.gif" align=left style="margin-right:5px;">
					<jsp:useBean id="urlParamsSort" type="java.util.Map" class="java.util.HashMap"/>
					<c:set target="${urlParamsSort}" property="documentId" value="${messageForm.sdmDocument.id}"/>																																														
					<digi:link ampModule="sdm" href="/showFile.do~activeParagraphOrder=${item.paragraphOrder}" name="urlParamsSort">
						<c:out value="${item.contentTitle}"/>
					</digi:link>
					
				
			</c:forEach>
			</div>
	</c:if>

	<logic:notPresent name="messageForm" property="forwardedMessage">
		<logic:present name="messageForm" property="repliedMessage">
			<br>
			<div style="width:100%;" id="msg_body_<bean:write name="messageForm" property="repliedMessage.id"/>">
				<div style="width:100%;">
					<a href="javascript:loadInnerMessage(<bean:write name="messageForm" property="repliedMessage.id"/>)" style="float:right;font-size:10px;">
						<digi:trn>Replied</digi:trn>
					</a>
				</div>
			</div>
		</logic:present>
	</logic:notPresent>
		
	<logic:present name="messageForm" property="forwardedMessage">
		<br>
		<div style="width:100%;" id="msg_body_<bean:write name="messageForm" property="forwardedMessage.id"/>">
			<div style="width:100%;">
				<a href="javascript:loadInnerMessage(<bean:write name="messageForm" property="forwardedMessage.id"/>)" style="float:right;font-size:10px;">
					<digi:trn>Forwarded</digi:trn>
				</a>
			</div>
		</div>
	</logic:present>

</div>

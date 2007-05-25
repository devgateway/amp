<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.forum.util.ForumConstants" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumUserSettings" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumPrivateMessage" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">
	function jumpToPage() {
	     <digi:context name="movePms" property="context/module/moduleinstance/showInbox.do" />
	     document.forumPageForm.action = "<%= movePms %>?startForm=0";
	     document.forumPageForm.submit();
	}
</script>

<digi:form action="/showPm.do">

<digi:errors property="forumGlobalError"/>

<logic:present name="forumPageForm" property="parsedContent">

<table width="100%" cellpadding="0" cellspacing="3" border="0">
	<tr>
		<td align="left">
			<digi:link href="/index.do" styleClass="forum">
				<bean:write name="forumPageForm" property="forum.name"/>
			</digi:link>
		</td>
		<td align="right">
			<digi:trn key="forum:jumpTo">Jump to</digi:trn>
			<html:select name="forumPageForm" property="folderName">
				<html:option value="<%= ForumConstants.PM_FOLDER_INBOX %>">Inbox</html:option>
				<html:option value="<%= ForumConstants.PM_FOLDER_SENTBOX %>">Sent items</html:option>
				<html:option value="<%= ForumConstants.PM_FOLDER_OUTBOX %>">Outbox</html:option>
				<html:option value="<%= ForumConstants.PM_FOLDER_SAVEBOX %>">Savebox</html:option>
			</html:select>
			<input class="forumButtons" type="button" value="Go" onClick="jumpToPage()">
		</td>
	</tr>
</table>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th  colspan="2" align="Left" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:personalMsg">Personal message</digi:trn>
			</th>
		</tr>
		<tr>
			<td width="10%" class="light" nowrap>
				<bean:define id="senderUser" name="forumPageForm" property="forumUserSettings" type="ForumUserSettings"/>

				<b><bean:write name="senderUser" property="nickName"/></b><br>
			</td>
			<td class="dark" width="90%" valign="top">
				<bean:write name="forumPageForm" property="postTitle"/>
			</td>
		</tr>
		<tr>
			<td width="10%" class="light" nowrap valign="top">
				<bean:define id="senderUser" name="forumPageForm" property="forumUserSettings" type="ForumUserSettings"/>
				<logic:present name="senderUser" property="avatarUrl">
					<bean:define id="urlLength" value="<%= String.valueOf(senderUser.getAvatarUrl().trim().length()) %>"/>
						<logic:greaterThan name="urlLength" value="0">
							<img src="<%= senderUser.getAvatarUrl() %>" border="0"/>
						</logic:greaterThan>
					<br>
				</logic:present>
				<font class="verySmallInfoText">
					<digi:trn key="forum:registered">Registered</digi:trn>:
					<%= ForumManager.getFormatedDate(senderUser.getRegisterDate()) %>&nbsp;<br>
					<digi:trn key="forum:posts">Posts</digi:trn>: <bean:write name="senderUser" property="totalPosts"/><br>
					<digi:trn key="forum:location">Location</digi:trn>: xxxx
				</font>
			</td>
			<td class="dark" width="90%" valign="top">
				<bean:write name="forumPageForm" property="parsedContent" filter="false"/>
						<bean:define id="pm" name="forumPageForm"  property="privateMessage" type="ForumPrivateMessage"/>
						<logic:present name="pm"  property="editedOn">
							<hr class="postStatusSeparator">
							<font class="verySmallInfoText">
							<digi:trn key="forum:postEditedBy">post has been edited by</digi:trn>: <bean:write name="pm" property="editedBy.nickName"/>
							&nbsp;<digi:trn key="forum:on">on</digi:trn>&nbsp; <%= ForumManager.getFormatedDateTime(pm.getEditedOn()) %>
						</font>
					</logic:present>				
			</td>
		</tr>
		<tr>
			<td colspan="2" class="light" align="right">
				
				<bean:define id="pmId" name="forumPageForm" property="pmId"/>
				<digi:link href="<%= "/showEditPm.do?pmId=" + String.valueOf(pmId)%>">
					<digi:trn key="forum:edit">Edit</digi:trn>
				</digi:link>
				
				<logic:notEqual name="forumPageForm" property="forumUserSettings.id" value="<%String.valueOf(senderUser.getId())%>">
					<digi:link href="<%= "/showPmDetails.do?userId=" + String.valueOf(senderUser.getId())%>">
						<digi:trn key="forum:reply">Reply</digi:trn>
					</digi:link>
				</logic:notEqual>

				<logic:equal name="forumPageForm" property="forumUserSettings.id" value="<%String.valueOf(senderUser.getId())%>">
					<digi:link href="<%= "/showPmDetails.do?userId=" + String.valueOf(senderUser.getId())%>">
						---
					</digi:link>
				</logic:equal>				
			</td>
		</tr>
	</table>
</logic:present>
</digi:form>
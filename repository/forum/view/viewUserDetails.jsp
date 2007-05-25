<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<digi:form action="/showUserDetails.do">

<digi:errors property="forumGlobalError"/>

<table width="100%" border="0">
	<tr>
		<td align="left">
			<digi:link href="/index.do" styleClass="forum">
				<bean:write name="forumPageForm" property="forum.name"/>
			</digi:link>
		</td>
	</tr>
</table>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:userDetails">User details</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="10%" align="left">
				<logic:present name="forumPageForm" property="forumUserSettings.avatarUrl">
					
					<bean:define id="avatarUrl" name="forumPageForm" property="forumUserSettings.avatarUrl" type="java.lang.String"/>
					<bean:define id="urlLength" value="<%= String.valueOf(avatarUrl.trim().length()) %>"/>

					<logic:greaterThan name="urlLength" value="0">
						<img src="<bean:write name="forumPageForm" property="forumUserSettings.avatarUrl"/>" border="0"/>
					</logic:greaterThan>

				</logic:present>
			</td>
			<td class="dark" width="90%" align="left">
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="light" width="10%" align="left"><digi:trn key="forum:nickName">Nick name</digi:trn></td>
			<td class="dark" width="90%" align="left">
				<b><bean:write name="forumPageForm" property="forumUserSettings.nickName"/></b>
			</td>
		</tr>
		<tr>
			<td class="light" width="10%" align="left"><digi:trn key="forum:registered">Registered</digi:trn></td>
			<td class="dark" width="90%" align="left">
				<bean:define id="regDate" name="forumPageForm" property="forumUserSettings.registerDate" type="java.util.Date"/>
				<%= ForumManager.getFormatedDate(regDate) %> &nbsp;</td>
		</tr>
		<tr>
			<td class="light" width="10%" align="left"><digi:trn key="forum:totalPosts">Total posts</digi:trn></td>
			<td class="dark" width="90%" align="left">
				<bean:write name="forumPageForm" property="forumUserSettings.totalPosts"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="light">
				<bean:define id="userId" name="forumPageForm" property="forumUserSettings.id"/>
				<digi:link href="<%= "/showPmDetails.do?userId=" + String.valueOf(userId)%>">
					<digi:img src="module/forum/images/icon_pm.gif" border="0"/>
				</digi:link>
				<%--<digi:img src="module/forum/images/icon_email.gif" border="0"/>--%> &nbsp;</td>
		</tr>
	</table>
</digi:form>
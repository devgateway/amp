<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumUserSettings" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">

<script language="JavaScript">

</script>


<html>

<head>
</head>

<body>
<digi:link href="/index.do" styleClass="forum">
	<logic:present name="forumPageForm" property="forum">
		<bean:write name="forumPageForm" property="forum.name"/> 
	</logic:present>
	<logic:notPresent name="forumPageForm" property="forum">
		 <digi:trn key="forum:forumMainPage">Forum main page</digi:trn>
	</logic:notPresent>	
</digi:link>
<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
	<tr>
		<th align="center" class="groupHeader" width="99%" nowrap>
			<logic:present name="forumPageForm" property="forum">
				<bean:write name="forumPageForm" property="forum.name"/> 
			</logic:present>
			&nbsp;<digi:trn key="forum:error">error</digi:trn>
		</th>
	</tr>
	<tr>
		<td class="light">
			<digi:errors property="forumGlobalError"/>
		</td>
	</tr>
	<tr>
		<td class="sectionTitle" align="center">
			<input class="forumButtons" type="button" value="Go back" onClick="window.history.back()">
		</td>
	</tr>
</table>


</body>
</html>
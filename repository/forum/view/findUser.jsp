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
	function showInboxOnMainPage(){
      <digi:context name="showInbox" property="context/module/moduleinstance/showInbox.do" />
	  window.opener.location.href="<%= showInbox %>";
	  window.close();
	}
	
	function find() {
	     <digi:context name="findUser" property="context/module/moduleinstance/findUser.do" />
	     document.forumPageForm.action = "<%= findUser %>";
	     document.forumPageForm.submit();
	}
	
	function set() {
		userId = document.getElementsByName("userId")[0].value;
	     <digi:context name="showPmDetails" property="context/module/moduleinstance/showPmDetails.do" />
		  window.opener.location.href="<%= showPmDetails %>?userId=" + userId;
		  window.close();
	}	
</script>


<html>

<head>
</head>

<body>
<digi:form action="/showFindUser.do">
<digi:errors property="forumGlobalError"/>
<table border="0" cellspacing="0" cellpadding="0" align="center" width="100%" height="100%">
	<tr>
		<td>
	
		<table border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td colspan="2" align="center">
					<h2><digi:trn key="forum:findUser">Find user</digi:trn></h2>
				</td>			
			</tr>		
			<tr>
				<td>
					<html:text name="forumPageForm" property="nickName" size="32"/>
				</td>
				<td>
					<input type="Button" value="find" onclick="find()">
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<html:select name="forumPageForm" property="userId" style="width: 250;">
						<logic:present name="forumPageForm" property="userList">
							<logic:iterate id="userSettings" name="forumPageForm" property="userList" type="ForumUserSettings">
								<html:option value="<%= String.valueOf(userSettings.getId()) %>">
									<bean:write name="userSettings" property="nickName"/>
								</html:option>
							</logic:iterate>
						</logic:present>
					</html:select>
				</td>			
			</tr>
			<tr>
				<td colspan="2" nowrap align="center">
						<logic:present name="forumPageForm" property="userList">
							<logic:equal name="forumPageForm" property="userList.empty" value="false">
								<input type="Button" value=" Ok " onclick="set()">
							</logic:equal>
						</logic:present>
						<logic:notPresent name="forumPageForm" property="userList">
								<input type="Button" value=" Ok " disabled>
						</logic:notPresent>
						<logic:present name="forumPageForm" property="userList">
							<logic:equal name="forumPageForm" property="userList.empty" value="true">
									<input type="Button" value=" Ok " disabled>
							</logic:equal>
						</logic:present>
						&nbsp;
					<input type="Button" value="Cancel" onclick="window.close()">
				</td>			
			</tr>			
		</table>
		
		</td>
	</tr>
</table>		
	</digi:form>
</body>
</html>
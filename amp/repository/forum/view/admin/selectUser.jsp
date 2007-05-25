<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<digi:instance property="forumAdminPageForm"/>

<script language="JavaScript">
	function selectUser(userId) {
    	var subsectionId = <bean:write name="forumAdminPageForm" property="subsectionId"/>
      <digi:context name="addModerator" property="context/module/moduleinstance/adminAddModerator.do" />
	  window.opener.location.href = "<%= addModerator %>?subsectionId=" + subsectionId + "&userDigiUserId=" + userId;
	  window.close();
	}
</script>

<digi:form action="/adminShowSearchUser.do">
	<html:hidden name="forumAdminPageForm" property="subsectionId"/>
	<html:text name="forumAdminPageForm" property="userSearchCriteria"/>
	<input type="Submit" vale="Search"/>
	<logic:present name="forumAdminPageForm" property="userSearchResults">
	<table>
	
		<logic:iterate id="user" name="forumAdminPageForm" property="userSearchResults">
		<tr>
			<td>
				<a href="javascript:selectUser(<bean:write name="user" property="id"/>)">
				<bean:write name="user" property="firstNames"/>
				</a>
			</td>
			<td>
				<bean:write name="user" property="lastName"/>
			</td>			
		</tr>
		</logic:iterate>
	</table>
	</logic:present>
</digi:form>

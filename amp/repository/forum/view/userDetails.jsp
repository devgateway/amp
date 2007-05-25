<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">
	function saveSection(){
      <digi:context name="saveUserSettings" property="context/module/moduleinstance/saveUserSettings.do" />
      document.forumPageForm.action = "<%= saveUserSettings %>";
      document.forumPageForm.submit();
	}

	function cancel() {
		window.history.back(1);
	<%--
      <digi:context name="showForum" property="context/module/moduleinstance/index.do" />
      document.forumPageForm.action = "<%= showForum %>";
      document.forumPageForm.submit();
	  --%>
	}

</script>
<digi:form action="/saveUserSettings.do">


<digi:errors property="forumGlobalError"/>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:userSettings">User settings</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:nickName">Nick name</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text name="forumPageForm"
						   property="nickName"
						   styleClass="forumControls"
						   size="30"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:avatarUrl">Avatar url</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text name="forumPageForm"
						   property="avatarUrl"
						   styleClass="forumControls"
						   size="30"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Save"
					   class="forumButtons"
					   onClick="saveSection()">
				&nbsp;
				<input type="button"
					   value="Cancel"
					   class="forumButtons"
					   onClick="cancel()">
			</th>
		</tr>
	</table>
</digi:form>
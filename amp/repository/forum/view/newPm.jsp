<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">

<script language="JavaScript">
	function showInboxOnMainPage(){
      <digi:context name="showInbox" property="context/module/moduleinstance/showInbox.do" />
	  window.opener.location.href="<%= showInbox %>";
	  window.close();
	}
</script>

<digi:form action="/showInbox.do">
<digi:errors property="forumGlobalError"/>

<table border="0" cellpadding="0" cellspacing="10" width="100%" height="100%">
	<tr>
		<td align="center">
			<h3>
			<digi:trn key="forum:youHave">You have</digi:trn> <bean:write name="forumPageForm" property="newPmCount"/> <digi:trn key="forum:newPrivateMsgs">new private messages</digi:trn>
			</h3>		
		</td>
	</tr>
	<tr>
		<td align="center">
			<a href="javascript:showInboxOnMainPage()" class="forum">
				<digi:trn key="forum:showInbox">Show inbox</digi:trn>
			</a>	
		</td>
	</tr>
	<tr>
		<td align="center">
			<a href="javascript:window.close()" class="forum">
				<digi:trn key="forum:close">Close</digi:trn>
			</a>	
		</td>
	</tr>		
</table>
</digi:form>
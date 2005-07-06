<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumPrivateMessage" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<%@ page import="org.digijava.module.forum.form.ForumPageForm" %>
<%@ page import="org.digijava.module.forum.util.ForumPaginationItem" %>
<%@ page import="org.digijava.module.forum.util.ForumConstants" %>

<digi:instance property="forumPageForm"/>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>
<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">

<bean:define id="fpf" name="forumPageForm" type="ForumPageForm"/>

<script language="JavaScript">
	function movePms() {
		if (isAnyChecked(document.getElementsByName("checkboxList"))) {
		     <digi:context name="movePms" property="context/module/moduleinstance/movePmToFolder.do" />
		     document.forumPageForm.action = "<%= movePms %>";
		     document.forumPageForm.submit();
		} else {
			alert ("Select at least one item");
		}
	}

	function jumpToPage() {
	     <digi:context name="movePms" property="context/module/moduleinstance/showInbox.do" />
	     document.forumPageForm.action = "<%= movePms %>?startForm=0";
	     document.forumPageForm.submit();
	}

	function deletePms() {
		if (isAnyChecked(document.getElementsByName("checkboxList"))) {
			if (confirm("Do you really want to delete selected PM(s) ?")) {
			     <digi:context name="delPms" property="context/module/moduleinstance/deletePms.do" />
			     document.forumPageForm.action = "<%= delPms %>";
			     document.forumPageForm.submit();
			 }
		} else {
			alert ("Select at least one item");
		}
	}
</script>

<digi:form action="/showInbox.do">

<digi:errors property="forumGlobalError"/>

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
            S<html:option value="<%= ForumConstants.PM_FOLDER_SAVEBOX %>">avebox</html:option>
			</html:select>
			<input class="forumButtons" type="button" value="Go" onClick="jumpToPage()">
		</td>
	</tr>
</table>

<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
	<tr>
		<td colspan="7" class="sectionTitle">
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_INBOX %>">
				<digi:trn key="forum:inbox">Inbox</digi:trn>
			</logic:equal>
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_DEL_ITEMS %>">
				<digi:trn key="forum:deletedItems">Deleted items</digi:trn>
			</logic:equal>
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SENT %>">
				<digi:trn key="forum:sentItems">Sent items</digi:trn>
			</logic:equal>
		</td>
	</tr>
	<tr>
		<th colspan="2" align="center" class="groupHeader" width="99%" nowrap>
			<digi:trn key="forum:postTitle">Post title</digi:trn>
		</th>

			<%--<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SENTBOX %>">
				<digi:trn key="forum:from">From</digi:trn>
			</logic:equal>
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_OUTBOX %>">
				<digi:trn key="forum:from">From</digi:trn>
			</logic:equal>--%>
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_INBOX %>">
				<th class="groupHeader" align="center" nowrap>
					<digi:trn key="forum:from">From</digi:trn>
				</th>
			</logic:equal>
			<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SAVEBOX %>">
				<th class="groupHeader" align="center" nowrap>
					<digi:trn key="forum:from">From</digi:trn>
				</th>
			</logic:equal>

		<th class="groupHeader" align="center" nowrap>
			<digi:trn key="forum:sendDate">Send date</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="1" nowrap>
			&nbsp;
		</th>
	</tr>

	<logic:iterate id="userPm" name="forumPageForm" property="userPmList" type="ForumPrivateMessage">
		<tr>
			<td class="forumTitle" width="1" align="center">
				<logic:equal  name="userPm" property="isNew" value="true">
					<digi:img src="module/forum/images/f_norm.gif" border="0"/>
				</logic:equal>
				<logic:equal name="userPm" property="isNew" value="false">
					<digi:img src="module/forum/images/f_norm_no.gif" border="0"/>
				</logic:equal>
			</td>
			<td class="forumTitle" width="99%">
				<digi:link href="<%= "/showPm.do?pmId=" + String.valueOf(userPm.getId()) %>" styleClass="forum">
					<bean:write name="userPm" property="title"/>
				</digi:link>
			</td>

				<%--<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SENTBOX %>">
					Form
				</logic:equal>
				<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_OUTBOX %>">
					Form
				</logic:equal>--%>
				<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_INBOX %>">
					<td class="forumInfo" align="center">
						<bean:write name="userPm" property="authorUserSettings.nickName"/>
					</td>
				</logic:equal>
				<logic:equal name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SAVEBOX %>">
					<td class="forumInfo" align="center">
						<bean:write name="userPm" property="authorUserSettings.nickName"/>
					</td>
				</logic:equal>

			<td class="forumInfo" align="center" nowrap>
				<%= ForumManager.getFormatedDateTime(userPm.getPostTime()) %> &nbsp;</td>
			<td class="light">
				<html:multibox name="forumPageForm" property="checkboxList" value="<%= String.valueOf(userPm.getId()) %>"/>
			</td>
		</tr>
	</logic:iterate>
		<tr>
			<td colspan="7" class="sectionTitle" align="right">
				<digi:trn key="forum:moveTo">Move to</digi:trn>
				<html:select name="forumPageForm" property="moveToFolderName">
					<html:option value="<%= ForumConstants.PM_FOLDER_INBOX %>">Inbox</html:option>
					<html:option value="<%= ForumConstants.PM_FOLDER_SAVEBOX %>">Savebox</html:option>
				</html:select>
				<logic:notEqual name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_SENTBOX %>">
					<logic:notEqual name="forumPageForm" property="folderName" value="<%= ForumConstants.PM_FOLDER_OUTBOX %>">
						<input class="forumButtons" type="button" value="Move" onClick="movePms()">
					</logic:notEqual>		
				</logic:notEqual>
				
				<input class="forumButtons" type="button" value="Delete" onClick="deletePms()">
			</td>
		</tr>
		<tr>
			<td colspan="7" class="light" align="right" align="right">
				<digi:link href="/showPmDetails.do"><digi:trn key="forum:newPrivateMsg">New private message</digi:trn></digi:link>
			</td>
		</tr>
</table>


	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<digi:trn key="forum:pages">pages</digi:trn>:
				<logic:present name="forumPageForm" property="paginationItems">
					<logic:iterate id="pageItem"
								   name="forumPageForm"
								   property="paginationItems"
								   type="org.digijava.module.forum.util.ForumPaginationItem">
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.FIRST_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/showInbox.do?startForm=" + String.valueOf(pageItem.getParameter()) + "&folderName=" + fpf.getFolderName()%>">
								<bean:write name="pageItem" property="displayString"/>
							</digi:link>
						</logic:equal>

						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.GENERIC_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/showInbox.do?startForm=" + String.valueOf(pageItem.getParameter()) + "&folderName=" + fpf.getFolderName()%>">
								<bean:write name="pageItem" property="displayString"/>
							</digi:link>
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.CURRENT_PAGE) %>">
								[<bean:write name="pageItem" property="displayString"/>]
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.INTERVAL) %>">
								...
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.LAST_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/showInbox.do?startForm=" + String.valueOf(pageItem.getParameter()) + "&folderName=" + fpf.getFolderName()%>">
								<bean:write name="pageItem" property="displayString"/>
							</digi:link>
						</logic:equal>
					</logic:iterate>
				</logic:present>
			</td>
		</tr>
	</table>
</digi:form>
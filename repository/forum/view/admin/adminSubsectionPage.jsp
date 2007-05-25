<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumThread" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<%@ page import="org.digijava.module.forum.util.ForumPaginationItem" %>
<%@ page import="org.digijava.module.forum.form.AdminPageForm" %>
<%@ page import="org.digijava.module.forum.util.LocationTrailItem" %>

<digi:instance property="forumAdminPageForm"/>

<bean:define id="subsection" name="forumAdminPageForm" property="forumSubsection" type="ForumSubsection"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>
<bean:define id="forumForm" name="forumAdminPageForm" type="AdminPageForm"/>
<script language="JavaScript">

	function showThread(id) {
      <digi:context name="showThread" property="context/module/moduleinstance/showThread.do" />
      document.forumAdminPageForm.action = "<%= showThread %>?threadId=" + id;
      document.forumAdminPageForm.submit();
	}

	function showForum () {
      <digi:context name="showForum" property="context/module/moduleinstance/index.do" />
      document.forumAdminPageForm.action = "<%= showForum %>";
      document.forumAdminPageForm.submit();
	}

	function deleteThread() {
		if (isAnyChecked(document.getElementsByName("checkboxList"))) {
			if (confirm ("Do you realy want to delete selected thread(s) ?")) {
			     <digi:context name="adminDeleteThreads" property="context/module/moduleinstance/adminDeleteThreads.do" />
			     document.forumAdminPageForm.action = "<%= adminDeleteThreads %>";
			     document.forumAdminPageForm.submit();
			  }
		 } else {
		 	alert ("Select at least one item");
		 }
	}

	function moveThread() {
		if (isAnyChecked(document.getElementsByName("checkboxList"))) {
			if (confirm ("Do you realy want to move selected thread(s) ?")) {
			     <digi:context name="adminMoveThreads" property="context/module/moduleinstance/adminShowMoveThreads.do" />
			     document.forumAdminPageForm.action = "<%= adminMoveThreads + "?subsectionId=" + String.valueOf(subsection.getId()) %>";
			     document.forumAdminPageForm.submit();
			  }
		 } else {
		 	alert ("Select at least one item");
		 }
	}

	function lockThreads() {
	     <digi:context name="adminLockThreads" property="context/module/moduleinstance/adminLockThreads.do" />
	     document.forumAdminPageForm.action = "<%= adminLockThreads + "?subsectionId=" + String.valueOf(subsection.getId()) %>";
	     document.forumAdminPageForm.submit();
	}

	function unlockThreads() {
	     <digi:context name="adminUnlockThreads" property="context/module/moduleinstance/adminUnlockThreads.do" />
	     document.forumAdminPageForm.action = "<%= adminUnlockThreads + "?subsectionId=" + String.valueOf(subsection.getId()) %>";
	     document.forumAdminPageForm.submit();
	}


</script>

<digi:form method="post" action="/adminShowSubsection.do">
<html:hidden name="forumAdminPageForm" property="subsectionId"/>


<digi:errors property="forumGlobalError"/>

<table class="forumListContainer" width="96%" cellpadding="3" cellspacing="0" border="1">
			<tr>
				<th colspan="7" align="center" class="groupHeader" width="70%" nowrap>
					<digi:trn key="forum:forumAdministration">Forum administration</digi:trn>
				</th>
			</tr>
		<tr>
		<td colspan="7" class="dark">
			<table border="0" cellpadding="0" cellspacing="3" class="trailContainer">
				<tr>
					<!-- Trail -->
						<logic:iterate id="trailItem"
									   indexId="depthIndex"
									   name="forumAdminPageForm"
									   property="locationTrailItems"
									   type="LocationTrailItem">

							<td nowrap>
							<logic:notEqual name="trailItem" property="itemType" value="<%= String.valueOf(LocationTrailItem.TOP_ITEM) %>">
								::
							</logic:notEqual>
								<logic:present name="trailItem" property="actionName">
									<logic:present name="trailItem" property="paramName">
										<digi:link href="<%= "/" + trailItem.getActionName() + "?" + trailItem.getParamName() + "="  + trailItem.getParamValue()%>" styleClass="forum">
											<bean:write name="trailItem" property="caption"/>
										</digi:link>
									</logic:present>
									<logic:notPresent name="trailItem" property="paramName">
										<digi:link href="<%= "/" + trailItem.getActionName()%>" styleClass="forum">
											<bean:write name="trailItem" property="caption"/>
										</digi:link>
									</logic:notPresent>
								</logic:present>
								<logic:notPresent name="trailItem" property="actionName">
									<bean:write name="trailItem" property="caption"/>
								</logic:notPresent>
							</td>
						</logic:iterate>
						<!-- End of Trail -->
						<td width="100%" align="right" nowrap>
							<digi:link href="/adminShowEditForum.do" styleClass="forum">
									<digi:trn key="forum:globalForumSettings">Global forum settings</digi:trn>
							</digi:link>
						</td>

				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td colspan="7" class="sectionTitle" width="70%">
			<bean:write name="forumAdminPageForm" property="forumSubsection.title"/>
		</td>
	</tr>
	<tr>
		<th colspan="2" align="left" class="groupHeader" width="1">
			<digi:img src="module/forum/images/spacer.gif" width="20" border="0"/>
		</th>
		<th class="groupHeader" align="center" width="70%">
		   <digi:trn key="forum:topicsTitle">Topics title</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:replies">Replies</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:latestPost">Latest post</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:select">Select</digi:trn>
		</th>

	</tr>

	<logic:present name="forumAdminPageForm" property="threadList">
		<logic:iterate name="forumAdminPageForm" property="threadList" id="thread" type="ForumThread">
			<tr>
				<td colspan="2" align="left" class="forumTitle" width="1">
					<logic:notPresent name="thread" property="parentThread">
						<logic:equal name="thread" property="locked" value="false">
							<logic:equal  name="thread" property="hasNewPost" value="true">
								<digi:img src="module/forum/images/f_norm.gif" border="0"/>
							</logic:equal>
							<logic:equal  name="thread" property="hasNewPost" value="false">
								<digi:img src="module/forum/images/f_norm_no.gif" border="0"/>
							</logic:equal>
						</logic:equal>
						<logic:equal name="thread" property="locked" value="true">
							<digi:img src="module/forum/images/f_closed.gif" border="0"/>
						</logic:equal>
					</logic:notPresent>
					<logic:present name="thread" property="parentThread">
						<logic:equal name="thread" property="parentThread.locked" value="false">
							<logic:equal  name="thread" property="parentThread.hasNewPost" value="true">
								<digi:img src="module/forum/images/f_norm.gif" border="0"/>
							</logic:equal>
							<logic:equal  name="thread" property="parentThread.hasNewPost" value="false">
								<digi:img src="module/forum/images/f_norm_no.gif" border="0"/>
							</logic:equal>
						</logic:equal>
						<logic:equal name="thread" property="parentThread.locked" value="true">
							<digi:img src="module/forum/images/f_closed.gif" border="0"/>
						</logic:equal>
					</logic:present>
				</td>
				<td class="forumTitle" align="left" width="70%">

					<logic:notPresent name="thread" property="parentThread">
						<digi:link href="<%= "/adminShowThread.do?threadId=" + String.valueOf(thread.getId()) %>" styleClass="forum">
							<bean:write name="thread" property="title"/>
						</digi:link>
					</logic:notPresent>
					<logic:present name="thread" property="parentThread">
						<digi:img src="module/forum/images/f_moved.gif" border="0"/>

						<digi:link href="<%= "/adminShowThread.do?threadId=" + String.valueOf(thread.getParentThread().getId()) %>" styleClass="forum">
							<bean:write name="thread" property="parentThread.title"/>
						</digi:link>
					</logic:present>
				</td>
				<td class="dark" align="center" width="10%" nowrap>
					<bean:write name="thread" property="postCount"/>
				</td>
				<td class="light" align="center" width="10%" nowrap>
					<font class="smallInfoText">
						<logic:notPresent name="thread" property="parentThread">
							<logic:present name="thread" property="lastPost">
								<%= ForumManager.getFormatedDateTime(thread.getLastPost().getPostTime()) %>
							</logic:present>
						</logic:notPresent>
						<logic:present name="thread" property="parentThread">
							<logic:present name="thread" property="parentThread.lastPost">
								<%= ForumManager.getFormatedDateTime(thread.getParentThread().getLastPost().getPostTime()) %>
							</logic:present>
						</logic:present>
					</font>
				&nbsp;</td>
				<td class="dark" width="10%" align="center">

						<%--<logic:notPresent name="thread" property="parentThread">
							notshad
							<%= ForumManager.getFormatedDateTime(thread.getLastPost().getPostTime()) %>
						</logic:notPresent>
						<logic:present name="thread" property="parentThread">
							shad
						</logic:present>--%>

					<html:multibox name="forumAdminPageForm" property="checkboxList" value="<%= String.valueOf(thread.getId()) %>"/>
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan="7" class="sectionTitle" align="right" width="70%">
				<input class="forumButtons" type="button" value="Delete" onClick="deleteThread()">
				&nbsp;
				<input class="forumButtons" type="button" value="Move" onClick="moveThread()">
				&nbsp;
				<input class="forumButtons" type="button" value="Lock" onClick="lockThreads()">
				&nbsp;
				<input class="forumButtons" type="button" value="Unlock" onClick="unlockThreads()">
			</td>
		</tr>
	</logic:present>
</table>

	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td>
				<digi:trn key="forum:pages">pages</digi:trn>:
				<logic:present name="forumAdminPageForm" property="paginationItems">
					<logic:iterate id="pageItem"
								   name="forumAdminPageForm"
								   property="paginationItems"
								   type="org.digijava.module.forum.util.ForumPaginationItem">
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.FIRST_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/adminShowSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&firstPost=true" %>" styleClass="forum">
								<digi:trn key="forum:first">First</digi:trn>
							</digi:link>
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.GENERIC_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/adminShowSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&startForm=" + String.valueOf(pageItem.getParameter()) %>" styleClass="forum">
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
							<digi:link styleClass="forum" href="<%= "/adminShowSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&lastPost=true" %>" styleClass="forum">
								<digi:trn key="forum:last">Last</digi:trn>
							</digi:link>
						</logic:equal>
					</logic:iterate>
				</logic:present>
			</td>
		</tr>
	</table>

		<table width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td colspan="5" align="center" class="groupHeader" width="70%" nowrap>
					<digi:link href="/index.do" styleClass="forum">
						<digi:trn key="forum:switchToUserMode">Switch to user mode</digi:trn>
					</digi:link>
				</td>
			</tr>
		</table>
</digi:form>
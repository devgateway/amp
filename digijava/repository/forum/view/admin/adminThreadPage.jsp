<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumPostEx" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>

<%@ page import="org.digijava.module.forum.util.ForumManager" %>

<%@ page import="org.digijava.module.forum.form.AdminPageForm" %>
<%@ page import="org.digijava.module.forum.util.ForumPaginationItem" %>
<%@ page import="org.digijava.module.forum.util.LocationTrailItem" %>

<digi:instance property="forumAdminPageForm"/>


<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">

	function splitSelected() {
      <digi:context name="splitSelected" property="context/module/moduleinstance/adminSplitTopic.do" />
      document.forumAdminPageForm.action = "<%= splitSelected %>";
      document.forumAdminPageForm.submit();
	}

	function splitFromSelected() {
      <digi:context name="splitFromSelected" property="context/module/moduleinstance/adminSplitTopic.do" />
      document.forumAdminPageForm.action = "<%= splitFromSelected %>?from=true";
      document.forumAdminPageForm.submit();
	}

</script>

<digi:form action="/adminShowThread.do">
<bean:define id="adminForm" name="forumAdminPageForm" type="AdminPageForm"/>


<digi:errors property="forumGlobalError"/>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
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
			<bean:write name="forumAdminPageForm" property="forumThread.title"/>
		</td>
	</tr>
		<tr>
			<th class="groupHeader" align="left" width="10%">
			   <digi:trn key="forum:author">Author</digi:trn>
			</th>
			<th class="groupHeader" align="left" width="15%" nowrap>
				<digi:trn key="forum:content">Content</digi:trn>
			</th>
		</tr>
		<logic:present name="forumAdminPageForm" property="postList">
		<logic:iterate name="forumAdminPageForm" property="postList" id="post" indexId="postIndex" type="ForumPostEx">
		<bean:define id="oddEven" value="<%= String.valueOf(postIndex.intValue()%2) %>"/>
		<logic:equal name="oddEven" value="1">
			<tr class="postOdd">
		</logic:equal>
			<logic:equal name="oddEven" value="0">
				<tr class="postEven">
			</logic:equal>
				<td class="groupHeader" align="left" width="10%" valign="top" nowrap>
					<logic:present name="post" property="authorUserSettings">
						<bean:write name="post" property="authorUserSettings.nickName"/></b><br>
					</logic:present>
					<logic:notPresent name="post" property="authorUserSettings">
						<bean:write name="post" property="userName"/>
					</logic:notPresent>
				</td>
				<td class="groupHeader" align="left" valign="top" width="90%">
					<table whidth="100%" cellpadding="0" cellspacing="3" border="0">
						<tr>
							<td nowrap>
								<font class="verySmallInfoText">
									Posted on:
									<%= ForumManager.getFormatedDateTime(post.getPostTime()) %>&nbsp;
									Subject:
								<bean:write name="post" property="title"/>
								<a name="post<bean:write name="post" property="id"/>"></a>
								</font>
							</td>
						</tr>
					</table>

					<hr class="postStatusSeparator">
					<bean:write name="post" property="parsedContent" filter="false"/>
				</td>
			</tr>
			<logic:equal name="oddEven" value="1">
			<tr class="postOdd">
			</logic:equal>
			<logic:equal name="oddEven" value="0">
				<tr class="postEven">
			</logic:equal>
					<td>
					<a href="javascript:scrollToTop()" class="forum"><digi:trn key="forum:top">top</digi:trn></a>
					</td>
					<td align="right">
						<html:multibox name="forumAdminPageForm" property="checkboxList" value="<%= String.valueOf(post.getId()) %>"/>
					</td>
				</tr>
				<tr>
					<td class="postSplitter" colspan="2">
					</td>
				</tr>

			</logic:iterate>
		</logic:present>

		<tr>
			<td colspan="5" align="center" class="dark" nowrap>
				<table border="0" cellpadding="0" cellspacing="3" width="100%">

					<tr>
						<td width="30%" align="left"><digi:trn key="forum:newTopicTitle">New topic title</digi:trn></td>
						<td width="70%" align="left">
							<html:text name="forumAdminPageForm"
									   property="threadTitle"
									   styleClass="forumControls"
									   size="30"/>
						</td>
					</tr>
					<tr>
						<td width="30%" align="left"><digi:trn key="forum:forumForNewTopic">Forum for new topic</digi:trn></td>
						<td width="70%" align="left">

							<html:select name="forumAdminPageForm" property="subsectionId">

							<logic:present name="forumAdminPageForm" property="forum">
								<logic:iterate id="section" name="forumAdminPageForm" property="forum.sections" type="ForumSection">
									<logic:iterate id="subsection" name="section" property="subsections" type="ForumSubsection">
										<html:option value="<%= String.valueOf(subsection.getId()) %>">
											<bean:write name="subsection" property="title"/>
										</html:option>
									</logic:iterate>
								</logic:iterate>
							</logic:present>

							</html:select>

						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="5" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Split selected"
					   class="forumButtons"
					   onClick="splitSelected()">
				&nbsp;
				<input type="button"
					   value="Split from selected"
					   class="forumButtons"
					   onClick="splitFromSelected()">
			</td>
		</tr>
	</table><br>




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
							<digi:link styleClass="forum" href="<%= "/adminShowThread.do?threadId=" + String.valueOf(adminForm.getThreadId()) + "&firstPost=true" %>">
								<!--First-->
								<bean:write name="pageItem" property="displayString"/>
							</digi:link>
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.GENERIC_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/adminShowThread.do?threadId=" + String.valueOf(adminForm.getThreadId()) + "&startForm=" + String.valueOf(pageItem.getParameter()) %>">
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
							<digi:link styleClass="forum" href="<%= "/adminShowThread.do?threadId=" + String.valueOf(adminForm.getThreadId()) + "&lastPost=true" %>">
								<!--Last-->
								<bean:write name="pageItem" property="displayString"/>
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
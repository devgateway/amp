<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.Forum" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumThread" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<%@ page import="org.digijava.module.forum.form.ForumPageForm" %>
<%@ page import="org.digijava.module.forum.util.ForumPaginationItem" %>
<%@ page import="org.digijava.module.forum.util.LocationTrailItem" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">
	function init(){
		<logic:greaterThan name="forumPageForm" property="newPmCount" value="0">
			checkNewPms();
		</logic:greaterThan>
	}

	function checkNewPms() {
	  openNewWindow(320, 200);
      <digi:context name="alertNewPm" property="context/module/moduleinstance/alertNewPm.do" />
      document.forumPageForm.action = "<%= alertNewPm %>";
	  document.forumPageForm.target = popupPointer.name;
      document.forumPageForm.submit();
	}
</script>

<digi:form method="post" action="/showSubsection.do">

<bean:define id="subsectionId" name="forumPageForm" property="subsectionId"/>

<digi:errors property="forumGlobalError"/>

<digi:secure authenticated="true">
	<table width="100%" cellpadding="0" cellspacing="3">
		<tr>
			<td>
				<digi:secure authenticated="true">
					<digi:trn key="forum:loggedInAs">Logged In as</digi:trn>: <bean:write name="forumPageForm" property="forumUserSettings.nickName"/>
					&nbsp;(
					<digi:link href="/showEditUserSettings.do" styleClass="forum">
						<digi:trn key="forum:editUserSettings">Edit user settings</digi:trn>
					</digi:link>
					)
					<br>
					<digi:link href="/showInbox.do" styleClass="forum">
						<digi:trn key="forum:youHave">You have</digi:trn>&nbsp;<bean:write name="forumPageForm" property="unreadPmCount"/>&nbsp;<digi:trn key="forum:unreadMsgs">unread messages</digi:trn>.
					</digi:link>
				</digi:secure>
			</td>
		</tr>
	</table>
</digi:secure>

<bean:define id="forumForm" name="forumPageForm" type="ForumPageForm"/>


<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
	<tr>
		<td colspan="7" class="dark">
			<table border="0" cellpadding="0" cellspacing="3" class="trailContainer">
				<tr>
					<!-- Trail -->
					<logic:iterate id="trailItem"
								   indexId="depthIndex"
								   name="forumPageForm"
								   property="locationTrailItems"
								   type="LocationTrailItem">

						<td nowrap>
							<logic:notEqual name="trailItem" property="itemType" value="<%= String.valueOf(LocationTrailItem.TOP_ITEM) %>">
								::
							</logic:notEqual>
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
						</td>
					</logic:iterate>
					<!-- End of Trail -->
					<td width="99%" nowrap>
					</td>

					<digi:secure authenticated="true">
					<td nowrap>
                        <digi:link href="<%= "/showAddThread.do?subsectionId=" + String.valueOf(subsectionId) %>" styleClass="forum">
                            <digi:trn key="forum:newTopic">New topic</digi:trn>
                        </digi:link>
					</digi:secure>
					</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td colspan="7" class="sectionTitle" width="70%">
			<bean:write name="forumPageForm" property="forumSubsection.title"/>
		</td>
	</tr>
	<tr>
		<th colspan="2" align="left" class="groupHeader" width="1">
			<digi:img src="module/forum/images/spacer.gif" width="20" border="0"/>
		</th>
		<th class="groupHeader" align="left" width="50%">
		    <digi:trn key="forum:threadTitle">Thread title</digi:trn>
		</th>
		<th class="groupHeader" align="left" width="15%" nowrap>
		   <digi:trn key="forum:author">Author</digi:trn>
		</th>
		<th class="groupHeader" align="left" width="10%" nowrap>
		   <digi:trn key="forum:replies">Replies</digi:trn>
		</th>
		<th class="groupHeader" align="left" width="10%" nowrap>
		    <digi:trn key="forum:views">Views</digi:trn>
		</th>
		<th class="groupHeader" align="left" width="15%" nowrap>
		   <digi:trn key="forum:latestPost">Latest post</digi:trn>
		</th>
	</tr>

	<logic:present name="forumPageForm" property="threadList">
		<logic:iterate name="forumPageForm" property="threadList" id="thread" type="ForumThread">

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
				<td class="forumTitle" align="left" width="50%">
					<logic:notPresent name="thread" property="parentThread">
						<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(thread.getId()) %>" styleClass="forum">
							<bean:write name="thread" property="title"/>
						</digi:link>
					</logic:notPresent>
					<logic:present name="thread" property="parentThread">
						<digi:img src="module/forum/images/f_moved.gif" border="0"/>
						<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(thread.getParentThread().getId()) %>" styleClass="forum">
							<bean:write name="thread" property="parentThread.title"/>
						</digi:link>
					</logic:present>
				</td>
				<td class="forumInfo" align="left" width="15%" nowrap>
				<logic:present name="thread" property="authorUser">
					<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(thread.getAuthorUser().getId()) %>" styleClass="forum">
						<bean:write name="thread" property="authorUser.nickName"/>
					</digi:link>
				</logic:present>
				<logic:notPresent name="thread" property="authorUser">
					&nbsp;
				</logic:notPresent>				
				</td>
				<td class="forumInfo" align="left" width="10%" nowrap>
					<bean:write name="thread" property="postCount"/>
				</td>
				<td class="forumInfo" align="left" width="10%" nowrap>
				   <digi:trn key="forum:views">Views</digi:trn>
				</td>
				<td class="forumInfo" align="left" width="15%" nowrap>
					<font class="smallInfoText">
						<logic:notPresent name="thread" property="parentThread">
							<logic:present name="thread" property = "lastPost">
								<%= ForumManager.getFormatedDateTime(thread.getLastPost().getPostTime()) %>
							</logic:present>
						</logic:notPresent>
						<logic:present name="thread" property="parentThread">
							<logic:present name="thread" property = "parentThread.lastPost">
								<%= ForumManager.getFormatedDateTime(thread.getParentThread().getLastPost().getPostTime()) %>
							</logic:present>
						</logic:present>
					</font>
					<br>
					<font class="smallLink">
							<logic:notPresent name="thread" property="parentThread">
								<logic:present name="thread" property = "lastPost">
									<logic:present name="thread" property="lastPost.authorUserSettings">
										<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(thread.getLastPost().getAuthorUserSettings().getId()) %>" styleClass="forum">
											<bean:write name="thread" property="lastPost.authorUserSettings.nickName"/>
										</digi:link>
									</logic:present>
									
									<logic:notPresent name="thread" property="lastPost.authorUserSettings">
									<a class="forum" href="mailto:<bean:write name="thread" property="lastPost.unregisteredEmail"/>">
										<bean:write name="thread" property="lastPost.unregisteredFullName"/>
									</a>
									</logic:notPresent>
									
								</logic:present>
							</logic:notPresent>
							<%-- Shadow thread --%>
							<logic:present name="thread" property="parentThread">
								<logic:present name="thread" property = "parentThread.lastPost">
									<logic:present name="thread" property="parentThread.lastPost.authorUserSettings">
										<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(thread.getParentThread().getLastPost().getAuthorUserSettings().getId()) %>" styleClass="forum">
											<bean:write name="thread" property="parentThread.lastPost.authorUserSettings.nickName"/>
										</digi:link>								
									</logic:present>
									
									<logic:notPresent name="thread" property="parentThread.lastPost.authorUserSettings">
										<a class="forum" href="mailto:<bean:write name="thread" property="parentThread.lastPost.unregisteredEmail"/>">
											<bean:write name="thread" property="parentThread.lastPost.unregisteredFullName"/>
										</a>
									</logic:notPresent>									
								</logic:present>
							</logic:present>
					</font>
						<logic:notPresent name="thread" property="parentThread">
							<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(thread.getId()) + "&lastPost=true"%>" styleClass="forum">
								<digi:img src="module/forum/images/icon_latest_reply.gif" border="0"/>
							</digi:link>
						</logic:notPresent>
						<logic:present name="thread" property="parentThread">
							<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(thread.getParentThread().getId()) + "&lastPost=true" %>" styleClass="forum">
								<digi:img src="module/forum/images/icon_latest_reply.gif" border="0"/>
							</digi:link>
						</logic:present>
				</td>
			</tr>
		</logic:iterate>
	</logic:present>
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
							<digi:link styleClass="forum" href="<%= "/showSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&firstPost=true" %>">
								<digi:trn key="forum:first">First</digi:trn>
							</digi:link>
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.GENERIC_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/showSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&startForm=" + String.valueOf(pageItem.getParameter()) %>">
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
							<digi:link styleClass="forum" href="<%= "/showSubsection.do?subsectionId=" + String.valueOf(forumForm.getSubsectionId()) + "&lastPost=true" %>">
								<digi:trn key="forum:last">Last</digi:trn>
							</digi:link>
						</logic:equal>
					</logic:iterate>
				</logic:present>
			</td>
		</tr>
	</table>


	<!-- quick jump -->
	<table border="0" cellpadding="0" cellspacing="3" width="100%">
		<tr>
			<td width="100%" align="right">
				<digi:trn key="forum:jumpTo">Jump to</digi:trn>:
			</td>
			<td align="right">
				<select name="jumpToCombo">
					<logic:iterate id="section" name="forumPageForm" property="forum.sections" type="ForumSection">
						<option style="background-color:#DEE3E7;color:#black;" value="sec<%= String.valueOf(section.getId()) %>">
							<bean:write name="section" property="title"/>
						</option>
							<logic:iterate id="subsection" name="section" property="subsections" type="ForumSubsection">
								<option style="background-color:#EFEFEF;color:#black;" value="sub<%= String.valueOf(subsection.getId()) %>">
									&nbsp;&nbsp;<bean:write name="subsection" property="title"/>
								</option>
							</logic:iterate>
						</logic:iterate>
				</select>
			</td>
			<td>
				<input type="button" value="go" class="forumButtons" onClick="jumpTo()">
				<script language="JavaScript">
					function jumpTo(){
						var jumpToCombo = document.getElementsByName("jumpToCombo")[0];
						if (jumpToCombo.value.substring (0, 3)=="sec") {
							secId = jumpToCombo.value.substring (3, jumpToCombo.value.length);
						      <digi:context name="jumpToSec" property="context/module/moduleinstance/showSection.do" />
							var jumpToSecUrl = "<%= jumpToSec %>?sectionId=" + secId;
							window.location.href=jumpToSecUrl;
						} else if (jumpToCombo.value.substring (0, 3)=="sub"){
							subId = jumpToCombo.value.substring (3, jumpToCombo.value.length);
							<digi:context name="jumpToSub" property="context/module/moduleinstance/showSubsection.do" />
							var jumpToSubUrl = "<%= jumpToSub %>?subsectionId=" + subId;
							window.location.href=jumpToSubUrl;
						}

					}
				</script>
			</td>
		</tr>
	</table>
	<!-- end of quick jump -->


	<digi:secure actions="ADMIN">
	<table width="100%" border="0" cellpadding="0" cellspacing="3">
		<tr>
			<td colspan="5" align="center" class="groupHeader" width="70%" nowrap>
				<digi:link href="/adminShowAdminIndex.do" styleClass="forum">
					<digi:trn key="forum:forumAdministration">Forum administration</digi:trn>
				</digi:link>
			</td>
		</tr>
	</table>
	</digi:secure>
	<script language="JavaScript">
		init();
	</script>
</digi:form>
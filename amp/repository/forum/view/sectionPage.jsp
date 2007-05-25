<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
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


<bean:define id="section" name="forumPageForm" property="forumSection"/>

<digi:form method="post" action="/showSection.do">


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
<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
	<tr>
		<td colspan="7" class="dark">
			<table width="100%" border="0" cellpadding="0" cellspacing="3" class="trailContainer">
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

					<td width="100%">&nbsp;</td>
				</tr>
			</table>
		</td>
	</tr>

	<tr>
		<td colspan="7" class="sectionTitle" width="70%">
			<bean:write name="section" property="title"/>
		</td>
	</tr>
	<tr>
		<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
		   <digi:trn key="forum:forum">Forum</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:topics">Topics</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:posts">Posts</digi:trn>
		</th>
		<th class="groupHeader" align="center" width="10%" nowrap>
		   <digi:trn key="forum:lastPost">Last post</digi:trn>
		</th>
	</tr>
	<!-- Subsections -->
	<logic:iterate id="subsection" name="section" property="subsections" type="ForumSubsection">
		<tr>
			<td class="forumTitle" width="1" align="center">
			<logic:present name="subsection" property="lastPost">
				<bean:define id="isNew" name="forumPageForm" property="<%="isNewPostInSubsection[" + subsection.getId() + "]"%>"/>
				<logic:equal  name="isNew" value="true">
					<digi:img src="module/forum/images/bf_new.gif" border="0"/>
				</logic:equal>
				<logic:equal  name="isNew" value="false">
					<digi:img src="module/forum/images/bf_nonew.gif" border="0"/>
				</logic:equal>
			</logic:present>
			<logic:notPresent name="subsection" property="lastPost">
				<digi:img src="module/forum/images/bf_nonew.gif" border="0"/>
			</logic:notPresent>
			</td>
			<td class="forumTitle" width="70%">
				<logic:equal name="subsection" property="locked" value="true">
					<font class="attentionText"><digi:trn key="forum:locked">Locked</digi:trn>: </font>
				</logic:equal>
				<digi:link href="<%= "/showSubsection.do?subsectionId=" + String.valueOf(subsection.getId()) %>" styleClass="forum">
					<bean:write name="subsection" property="title"/>
				</digi:link>
				<br>
				<font class="smallInfoText"><bean:write name="subsection" property="comment"/></font>

			</td>
			<td class="forumInfo" width="10%" align="center">
				<bean:write name="subsection" property="threadCount"/>
			</td>
			<td class="forumInfo" width="10%" align="center">
				<bean:write name="subsection" property="totalPosts"/>
			</td>
			<td class="forumInfo" width="10%" align="center" nowrap>
				<logic:present name="subsection" property="lastPost">
					<font class="smallLink">
						<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(subsection.getLastPost().getThread().getId()) %>" styleClass="forum">
							<bean:write name="subsection" property="lastPost.thread.title"/>
						</digi:link>
					</font>
					<br>
					<font class="smallInfoText">
						<%= ForumManager.getFormatedDateTime(subsection.getLastPost().getPostTime()) %>
					</font>
					<br>
					<font class="smallLink">
						<logic:present name="subsection" property="lastPost.authorUserSettings">
							<logic:present name="subsection" property="lastPost.authorUserSettings">
								<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(subsection.getLastPost().getAuthorUserSettings().getId()) %>" styleClass="forum">
									<bean:write name="subsection" property="lastPost.authorUserSettings.nickName"/>
								</digi:link>
							</logic:present>						
						</logic:present>
						
						<logic:notPresent name="subsection" property="lastPost.authorUserSettings">
							<a class="forum" href="mailto:<bean:write name="subsection" property="lastPost.unregisteredEmail"/>">
								<bean:write name="subsection" property="lastPost.unregisteredFullName"/>
							</a>
						</logic:notPresent>							
					</font>
						<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(subsection.getLastPost().getThread().getId()) + "&postId=" + String.valueOf(subsection.getLastPost().getId())%>" styleClass="forum">
							<digi:img src="module/forum/images/icon_latest_reply.gif" border="0"/>
						</digi:link>
				</logic:present>
				<logic:notPresent name="subsection" property="lastPost">
					&nbsp;
				</logic:notPresent>
			</td>
		</tr>
	</logic:iterate>
	<!-- End of subsections -->
</table>

	<!-- quick jump -->
	<table border="0" cellpadding="0" cellspacing="3" width="100%">
		<tr>
			<td width="100%" align="right">
				<digi:trn key="forum:jumpTo">Jump to</digi:trn>:
			</td>
			<td align="right">
				<select name="jumpToCombo">
					<logic:iterate id="jumpSection" name="forumPageForm" property="forum.sections" type="ForumSection">
						<option style="background-color:#DEE3E7;color:#black;" value="sec<%= String.valueOf(jumpSection.getId()) %>">
							<bean:write name="jumpSection" property="title"/>
						</option>
							<logic:iterate id="subsection" name="jumpSection" property="subsections" type="ForumSubsection">
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


</logic:present>


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
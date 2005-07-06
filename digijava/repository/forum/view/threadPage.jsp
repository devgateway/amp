<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumPostEx" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>

<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<%@ page import="org.digijava.module.forum.util.ForumConstants" %>

<%@ page import="org.digijava.module.forum.form.ForumPageForm" %>
<%@ page import="org.digijava.module.forum.util.ForumPaginationItem" %>
<%@ page import="org.digijava.module.forum.util.LocationTrailItem" %>

<digi:instance property="forumPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/common/js/bbcode_fixed.js"/>"></script>

<digi:secure actions="ADMIN">
	<bean:define id="isAdmin" value="true"/>
</digi:secure>

<script language="JavaScript">
	function addPost(){
	
	<logic:equal name="forumPageForm" property="forum.allowUnregisteredUserPost" value="true">
		<digi:secure authenticated="false">
			var unregEmail = document.getElementsByName('unregisteredEmail')[0].value;
			var unregFullName = document.getElementsByName('unregisteredFullName')[0].value;
			var errMsg = "";
			if (unregFullName.length==0) {
				errMsg += "User name is mandatory.\n\r";
			}			
			if (unregEmail.length==0 || !checkEmail(unregEmail)) {
				errMsg += "Please, enter a valid email address.";
			}
			if (errMsg.length > 0) {
				alert (errMsg);
				return false;
			}
		</digi:secure>
	</logic:equal>
	
		<logic:greaterThan name="forumPageForm" property="forum.minMessageLength" value="0">
			var minLength = <bean:write name="forumPageForm" property="forum.minMessageLength"/>;
			if (document.getElementsByName("postContent")[0].value.length >= minLength) {
				<digi:context name="addForumPost" property="context/module/moduleinstance/addPost.do" />
				document.forumPageForm.action = "<%= addForumPost %>?threadId=<bean:write name="forumPageForm" property="threadId"/>";
				forumSubmit (document.forumPageForm);
				//document.forumPageForm.submit();
			} else {
				alert ("Post must be at least <bean:write name="forumPageForm" property="forum.minMessageLength"/> characters");
			}
		</logic:greaterThan>
		<logic:lessEqual name="forumPageForm" property="forum.minMessageLength" value="0">
			<digi:context name="addForumPost" property="context/module/moduleinstance/addPost.do" />
			document.forumPageForm.action = "<%= addForumPost %>?threadId=<bean:write name="forumPageForm" property="threadId"/>";
			forumSubmit (document.forumPageForm);
			//document.forumPageForm.submit();
		</logic:lessEqual>
	}

	function showPostPreview() {
			<logic:equal name="forumPageForm" property="forum.allowUnregisteredUserPost" value="true">
				<digi:secure authenticated="false">
					var unregEmail = document.getElementsByName('unregisteredEmail')[0].value;
					var unregFullName = document.getElementsByName('unregisteredFullName')[0].value;
					var errMsg = "";
					if (unregFullName.length==0) {
						errMsg += "User name is mandatory.\n\r";
					}			
					if (unregEmail.length==0 || !checkEmail(unregEmail)) {
						errMsg += "Please, enter a valid email address.";
					}
					if (errMsg.length > 0) {
						alert (errMsg);
						return false;
					}
				</digi:secure>
			</logic:equal>	
	
      <digi:context name="showPostPreview" property="context/module/moduleinstance/showPostPreview.do" />
      <%--document.forumPageForm.action = "<%= showPostPreview %>?postId=<bean:write name="forumPageForm" property="postId"/>&threadId=<bean:write name="forumPageForm" property="threadId"/>";--%>
	  document.forumPageForm.action = "<%= showPostPreview %>?threadId=<bean:write name="forumPageForm" property="threadId"/>";
      forumSubmit (document.forumPageForm);
	  //document.forumPageForm.submit();
	}

	function init(){
		setForm (document.getElementsByName("forumPageForm")[0]);
		setEditField (document.getElementsByName("postContent")[0]);
		<logic:notEqual name="forumPageForm" property="postId" value="0">
			scrollToPost('<bean:write name="forumPageForm" property="postId"/>');
		</logic:notEqual>
		<logic:greaterThan name="forumPageForm" property="newPmCount" value="0">
			checkNewPms();
		</logic:greaterThan>
	}

	function scrollToPost(postId){
		postName="post" + postId;
		postAnchor = document.getElementsByName(postName)[0];

		if (postAnchor != null) {
//			alert (postAnchor.tagName);
//			alert (postAnchor.scrollIntoView(true));
//			postAnchor.scrollIntoView(true);
//			window.scrollTo(0, 100);
//			window.location.href="#post" + postId;
		}
	}


	function checkNewPms() {
	  openNewWindow(320, 200);
      <digi:context name="alertNewPm" property="context/module/moduleinstance/alertNewPm.do" />
      document.forumPageForm.action = "<%= alertNewPm %>";
	  document.forumPageForm.target = popupPointer.name;
      document.forumPageForm.submit();
	}
	
	function showFiltered(){
      <digi:context name="showFiltered" property="context/module/moduleinstance/showThread.do" />
      document.forumPageForm.action = "<%= showFiltered %>?threadId=<bean:write name="forumPageForm" property="forumThread.id"/>";
      document.forumPageForm.submit();	
	}

</script>

<digi:form enctype="multipart/form-data" action="/showThread.do">

<digi:errors property="forumGlobalError"/>


<digi:secure authenticated="true">
	<table width="100%" cellpadding="0" cellspacing="3">
		<tr>
			<td>
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


			</td>
		</tr>
	</table>
</digi:secure>

<bean:define id="forumForm" name="forumPageForm" type="ForumPageForm"/>
<br>

	<digi:link href="<%= "/showPostPreview.do?threadId=" + String.valueOf (forumForm.getThreadId()) %>" styleClass="forum"><digi:trn key="forum:postReply">Post reply</digi:trn></digi:link>
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
                        <digi:link href="<%= "/showAddThread.do?subsectionId=" + String.valueOf(forumForm.getForumThread().getSubsection().getId()) %>" styleClass="forum">
                            <digi:trn key="forum:newTopic">New topic</digi:trn>
                        </digi:link>
					</td>
					</digi:secure>

					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td colspan="2" class="sectionTitle" width="100%" align="right">

				<bean:define id="prevThreadId" name="forumPageForm" property="prevThreadId"/>
				<bean:define id="nextThreadId" name="forumPageForm" property="nextThreadId"/>

				<logic:greaterThan name="prevThreadId" value="0">
                        <digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(prevThreadId) %>" styleClass="forum">
                           <digi:trn key="forum:previousTopic">Previous topic</digi:trn>
                        </digi:link>
				</logic:greaterThan>
				
				<logic:greaterThan name="prevThreadId" value="0">
					<logic:greaterThan name="nextThreadId" value="0">
						::
					</logic:greaterThan>
				</logic:greaterThan>
				
				<logic:greaterThan name="nextThreadId" value="0">
                        <digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(nextThreadId) %>" styleClass="forum">
                            <digi:trn key="forum:nextTopic">Next topic</digi:trn>
                        </digi:link>
				</logic:greaterThan>				

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
		<logic:present name="forumPageForm" property="postList">
		<logic:iterate name="forumPageForm" property="postList" id="post" indexId="postIndex" type="ForumPostEx">
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
						<logic:present name="post" property="authorUserSettings.avatarUrl">
							<bean:define id="urlLength" value="<%= String.valueOf(post.getAuthorUserSettings().getAvatarUrl().trim().length()) %>"/>
							<logic:greaterThan name="urlLength" value="0">
								<img src="<%= post.getAuthorUserSettings().getAvatarUrl() %>" border="0"/>
							</logic:greaterThan>
							<br>
						</logic:present>
							<font class="verySmallInfoText">
								Registered:
								<%= ForumManager.getFormatedDate(post.getAuthorUserSettings().getRegisterDate()) %>&nbsp;<br>
								Posts: <bean:write name="post" property="authorUserSettings.totalPosts"/><br>
								Location: 
							</font>
					</logic:present>
					<logic:notPresent name="post" property="authorUserSettings">
						<a class="forum" href="mailto:<bean:write name="post" property="unregisteredEmail"/>">
							<bean:write name="post" property="unregisteredFullName"/>
						</a>
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
							<td width="100%">
							</td>
							<digi:secure authenticated="true">
							<td align="right">
								<digi:link href="<%= "/showPostPreview.do?threadId=" + String.valueOf(forumForm.getThreadId()) + "&quoteId=" + String.valueOf(post.getId()) %>">
									<digi:img src="module/forum/images/icon_quote.gif" border="0"/>
								</digi:link>
							</td>
							</digi:secure>
							<logic:present name="post" property="authorUserSettings">
								<logic:notPresent name="isAdmin">
									<digi:secure authenticated="true">
											<logic:equal name="forumPageForm" property="forumUserSettings.id" value="<%= String.valueOf(post.getAuthorUserSettings().getId()) %>">
												<td>
													<digi:link href="<%= "/showEditPost.do?postId=" + String.valueOf(post.getId())%>">
														<digi:img src="module/forum/images/icon_edit.gif" border="0"/>
													</digi:link>
												</td>
											</logic:equal>
									</digi:secure>
								</logic:notPresent>
								<logic:present name="isAdmin">
									<td>
										<digi:link href="<%= "/showEditPost.do?postId=" + String.valueOf(post.getId())%>">
											<digi:img src="module/forum/images/icon_edit.gif" border="0"/>
										</digi:link>
									</td>
								</logic:present>
							</logic:present>
							

							<digi:secure authenticated="true">
								<logic:notPresent name="isAdmin">
								<td>
									<logic:equal name="forumPageForm" property="postId" value="<%= String.valueOf(post.getId()) %>">
										<logic:equal name="forumPageForm" property="forumUserSettings.id" value="<%= String.valueOf(post.getAuthorUserSettings().getId()) %>">
											<digi:link href="<%= "/deletePost.do?postId=" + String.valueOf(post.getId())%>">
												<digi:img src="module/forum/images/icon_delete.gif" border="0"/>
											</digi:link>
										</logic:equal>
									</logic:equal>									
								</td>
								</logic:notPresent>
							</digi:secure>
							<logic:present name="isAdmin">
								<td>
									<digi:link href="<%= "/deletePost.do?postId=" + String.valueOf(post.getId())%>">
										<digi:img src="module/forum/images/icon_delete.gif" border="0"/>
									</digi:link>
								</td>
								</logic:present>							
						</tr>
					</table>

					<hr class="postStatusSeparator">
					<bean:write name="post" property="parsedContent" filter="false"/>
					<logic:present name="post" property="editedBy">
						<hr class="postStatusSeparator">
						<logic:present name="post" property="editedBy"/>
							<font class="verySmallInfoText">
							post has been edited by: <bean:write name="post" property="editedBy.nickName"/>
							&nbsp;on&nbsp; <%= ForumManager.getFormatedDateTime(post.getEditedOn()) %>
							</font>
						</logic:present>
						
					</logic:present>
					
					
				</td>
			</tr>
			<logic:equal name="oddEven" value="1">
			<tr class="postOdd"></logic:equal>
				<logic:equal name="oddEven" value="0">
				<tr class="postEven"></logic:equal>
					<td>
					<a href="javascript:scrollToTop()" class="forum"><digi:trn key="forum:top">top</digi:trn></a>
					</td>
					<td>
						<table whidth="100%" cellpadding="0" cellspacing="3" border="0">
							<tr>
								<td>
									<digi:secure authenticated="true">
										<logic:present name="post" property="authorUserSettings">
											<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(post.getAuthorUserSettings().getId())%>">
												<digi:img src="module/forum/images/icon_profile.gif" border="0"/>
											</digi:link>
										</logic:present>
									</digi:secure>
								</td>
								<td>
									<digi:secure authenticated="true">
											<logic:present name="post" property="authorUserSettings">
												<digi:link href="<%= "/showPmDetails.do?userId=" + String.valueOf(post.getAuthorUserSettings().getId())%>">
													<digi:img src="module/forum/images/icon_pm.gif" border="0"/>
												</digi:link>
											</logic:present>
									</digi:secure>
								</td>
								<%--
								<td>
									<digi:img src="module/forum/images/icon_email.gif" border="0"/>
								</td>
								--%>
								<td width="100%">
									&nbsp;
								</td>
								<digi:secure actions="ADMIN">
									<logic:equal name="forumPageForm"
												 property="forumThread.subsection.requiresPublishing"
												 value="true">
												 <logic:equal name="post" property="published" value="false">
												 	<td nowrap>
														<digi:link href="<%= "/publishPost.do?postId=" + String.valueOf(post.getId())%>" styleClass="forum">
														 	Publish post
														</digi:link>
													</td>
												 </logic:equal>
												<logic:equal name="post" property="published" value="true">
												 	<td nowrap>
														Published
													</td>
												 </logic:equal>
									</logic:equal>
								</digi:secure>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td class="postSplitter" colspan="2">
					</td>
				</tr>
			</logic:iterate>
		<tr>
			<td colspan="2" class="sectionTitle" width="100%" align="center" nowrap>
				<font class="verySmallInfoText">
					<digi:trn key="forum:displayPostsFromPrev">Display posts from previous</digi:trn>: 
				</font>
					<html:select name="forumPageForm" property="filterPostsFrom">
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_ALL) %>">All posts</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_ONE_DAY) %>">1 day</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_ONE_WEEK) %>">1 week</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_TWO_WEEKS) %>">2 weeks</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_ONE_MONTH) %>">1 month</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_THREE_MONTH) %>">3 month</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_SIX_MONTH) %>">6 month</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.FILTER_POSTS_ONE_YEAR) %>">1 year</html:option>
					</html:select>
					<html:select name="forumPageForm" property="sortOrder">
						<html:option value="<%= String.valueOf(ForumConstants.SORT_ASC) %>">Oldest first</html:option>
						<html:option value="<%= String.valueOf(ForumConstants.SORT_DESC) %>">Newest first</html:option>
					</html:select>
					<input type="button" value=" Go " onClick="showFiltered()">
			</td>
		</tr>	
		</logic:present>
	</table><br>



	<digi:secure authenticated="true">

		<logic:equal name="forumPageForm" property="forumThread.subsection.locked" value="false">
		<logic:equal name="forumPageForm" property="forumThread.locked" value="false">
		<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		
			<tr>
				<td class="light" width="20%">
				    <digi:trn key="forum:subject">Subject</digi:trn>
				</td>
				<td class="dark" width="80%">
					<html:text name="forumPageForm" property="postTitle" styleClass="forumControls" size="70"/>
				</td>
			</tr>
			<tr>
				<td class="light" width="20%">&nbsp;
				</td>			
				<TD class="dark">
					<html:file name="forumPageForm" property="formFile" styleClass="forumControls" size="70"/>
				</TD>
			</tr>			
			<tr>
				<td class="light" valign="top" width="20%">
					<TABLE cellSpacing="0" cellPadding="5" width="100" border="0">
						<TBODY>
							<TR align="middle">
								<TD class="gensmall" colSpan="4"><B>
									<digi:trn key="forum:clickableSmiles">clickable smiles</digi:trn></B>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':D')">
									<digi:img title="Very Happy" alt="Very Happy" src="module/common/images/smiles/icon_biggrin.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':)')">
									<digi:img title="Smile" alt="Smile" src="module/common/images/smiles/icon_smile.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':(')">
									<digi:img title="Sad" alt="Sad" src="module/common/images/smiles/icon_sad.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':o')">
									<digi:img title="Surprised" alt="Surprised" src="module/common/images/smiles/icon_surprised.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':shock:')">
									<digi:img title="Shocked" alt="Shocked" src="module/common/images/smiles/icon_eek.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':?')">
									<digi:img title="Confused" alt="Confused" src="module/common/images/smiles/icon_confused.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon('8)')">
									<digi:img title="Cool" alt="Cool" src="module/common/images/smiles/icon_cool.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':lol:')">
									<digi:img title="Laughing" alt="Laughing" src="module/common/images/smiles/icon_lol.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':x')">
									<digi:img title="Mad" alt="Mad" src="module/common/images/smiles/icon_mad.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':P')">
									<digi:img title="Razz" alt="Razz" src="module/common/images/smiles/icon_razz.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':oops:')">
									<digi:img title="Embarassed" alt="Embarassed" src="module/common/images/smiles/icon_redface.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':cry:')">
									<digi:img title="Crying or Very sad" alt="Crying or Very sad" src="module/common/images/smiles/icon_cry.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':evil:')">
									<digi:img title="Evil or Very Mad" alt="Evil or Very Mad" src="module/common/images/smiles/icon_evil.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':twisted:')">
									<digi:img title="Twisted Evil" alt="Twisted Evil" src="module/common/images/smiles/icon_twisted.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':roll:')">
									<digi:img title="Rolling Eyes" alt="Rolling Eyes" src="module/common/images/smiles/icon_rolleyes.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':wink:')">
									<digi:img title="Wink" alt="Wink" src="module/common/images/smiles/icon_wink.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':!:')">
									<digi:img title="Exclamation" alt="Exclamation" src="module/common/images/smiles/icon_exclaim.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':?:')">
									<digi:img title="Question" alt="Question" src="module/common/images/smiles/icon_question.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':idea:')">
									<digi:img title="Idea" alt="Idea" src="module/common/images/smiles/icon_idea.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':arrow:')">
									<digi:img title="Arrow" alt="Arrow" src="module/common/images/smiles/icon_arrow.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</td>
				<td class="dark" width="80%">
					<table width="1" border="0" cellpadding="0" cellspacing="3">
						<td>
							<INPUT class="forumButtons" style="FONT-WEIGHT: bold; WIDTH: 30px" accessKey="b" onClick="bbstyle(0)" type="button" value=" B " name="addbbcode0">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 30px; FONT-STYLE: italic" accessKey="i" onClick="bbstyle(2)" type="button" value=" i " name="addbbcode2">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 30px; TEXT-DECORATION: underline" accessKey="u" onClick="bbstyle(4)" type="button" value=" u " name="addbbcode4">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 50px" accessKey="q" onClick="bbstyle(6)" type="button" value="Quote" name="addbbcode6">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="c" onClick="bbstyle(8)" type="button" value="Code" name="addbbcode8">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="l" onClick="bbstyle(10)" type="button" value="List" name="addbbcode10">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="o" onClick="bbstyle(12)" type="button" value="List=" name="addbbcode12">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="p" onClick="bbstyle(14)" type="button" value="Img" name="addbbcode14">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px; TEXT-DECORATION: underline" accessKey="w" onClick="bbstyle(16)" type="button" value="URL" name="addbbcode16">
						</td>
			</tr>
		</table>
		<table width="1" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td><font class="smallInfoText"><digi:trn key="forum:color">Color</digi:trn>:</font>
				</td>
				<td>
					<SELECT onMouseOver="helpline('s')" onChange="bbfontstyle('[color=' + this.options[this.selectedIndex].value + ']', '[/color]')" name="addbbcode18">
						<OPTION value="#444444" selected style="COLOR: black; BACKGROUND-COLOR: #fafafa">Font Color</OPTION>
						<OPTION style="COLOR: darkred; BACKGROUND-COLOR: #fafafa" value="darkred">Dark Red</OPTION>
						<OPTION class="genmed" style="COLOR: red; BACKGROUND-COLOR: #fafafa" value="red">Red</OPTION>
						<OPTION class="genmed" style="COLOR: orange; BACKGROUND-COLOR: #fafafa" value="orange">Orange</OPTION>
						<OPTION class="genmed" style="COLOR: brown; BACKGROUND-COLOR: #fafafa" value="brown">Brown</OPTION>
						<OPTION class="genmed" style="COLOR: yellow; BACKGROUND-COLOR: #fafafa" value="yellow">Yellow</OPTION>
						<OPTION class="genmed" style="COLOR: green; BACKGROUND-COLOR: #fafafa" value="green">Green</OPTION>
						<OPTION class="genmed" style="COLOR: olive; BACKGROUND-COLOR: #fafafa" alue="olive">Olive</OPTION>
						<OPTION class="genmed" style="COLOR: cyan; BACKGROUND-COLOR: #fafafa" value="cyan">Cyan</OPTION>
						<OPTION class="genmed" style="COLOR: blue; BACKGROUND-COLOR: #fafafa" value="blue">Blue</OPTION>
						<OPTION class="genmed" style="COLOR: darkblue; BACKGROUND-COLOR: #fafafa" value="darkblue">Dark Blue</OPTION>
						<OPTION class="genmed" style="COLOR: indigo; BACKGROUND-COLOR: #fafafa" value="indigo">Indigo</OPTION>
						<OPTION class="genmed" style="COLOR: violet; BACKGROUND-COLOR: #fafafa" value="violet">Violet</OPTION>
						<OPTION class="genmed" style="COLOR: white; BACKGROUND-COLOR: #fafafa" value="white">White</OPTION>
						<OPTION class="genmed" style="COLOR: black; BACKGROUND-COLOR: #fafafa" value="black">Black</OPTION>
					</SELECT>
				</td>
				<td><font class="smallInfoText"><digi:trn key="forum:size">Size</digi:trn>:</font>
				</td>
				<td>
					<SELECT onMouseOver="helpline('f')" onChange="bbfontstyle('[size=' + this.form.addbbcode20.options[this.form.addbbcode20.selectedIndex].value + ']', '[/size]')" name="addbbcode20">
						<OPTION class="genmed" value="7">Tiny</OPTION>
						<OPTION class="genmed" value="9">Small</OPTION>
						<OPTION class="genmed" value="12" selected>Normal</OPTION>
						<OPTION class="genmed" value="18">Large</OPTION>
						<OPTION class="genmed" value="24">Huge</OPTION>
					</SELECT>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<TD>
					<html:textarea rows="14" cols="70" styleClass="forumControls" name="forumPageForm" property="postContent"></html:textarea>
				</TD>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="enableEmotions"/>
					<digi:trn key="forum:enableEmotions">Enable emotions</digi:trn>
				</td>
			</tr>
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="allowHtml"/>
					<digi:trn key="forum:allowHTML">Allow HTML</digi:trn>
				</td>
			</tr>
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="notifyOnReply"/>
					<digi:trn key="forum:enableNotificationsOnReply">Enable notifications on reply</digi:trn>
				</td>
			</tr>
		</table>
	</td>
	</tr>
		<tr>
			<td colspan="2" class="sectionTitle" align="center">
				<input class="forumButtons" type="button" value="Preview" onClick="showPostPreview()">&nbsp;
				<input class="forumButtons" type="button" value="Reply" onClick="addPost()">
			</td>
		</tr>
	</table>
	
	
	<script language="JavaScript">
		init();
	</script>
	</logic:equal>
	</logic:equal>
	
	</digi:secure>
	

	<%--UnRegistered users--%>	
	<logic:equal name="forumPageForm" property="forum.allowUnregisteredUserPost" value="true">
	<digi:secure authenticated="false">
		<logic:equal name="forumPageForm" property="forumThread.subsection.locked" value="false">
		<logic:equal name="forumPageForm" property="forumThread.locked" value="false">
		<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		
			<tr>
				<td class="light" width="20%">
				    <digi:trn key="forum:fullName">Name (Required)</digi:trn>
				</td>
				<td class="dark" width="80%">
					<html:text name="forumPageForm" property="unregisteredFullName" styleClass="forumControls" size="70"/>
				</td>
			</tr>			
			<tr>
				<td class="light" width="20%">
				    <digi:trn key="forum:email">Email (Required)</digi:trn>
				</td>
				<td class="dark" width="80%">
					<html:text name="forumPageForm" property="unregisteredEmail" styleClass="forumControls" size="70"/>
				</td>
			</tr>
			</logic:notPresent>
		
		
			<tr>
				<td class="light" width="20%">
				    <digi:trn key="forum:subject">Subject</digi:trn>
				</td>
				<td class="dark" width="80%">
					<html:text name="forumPageForm" property="postTitle" styleClass="forumControls" size="70"/>
				</td>
			</tr>
			<tr>
				<td class="light" width="20%">&nbsp;
				</td>			
				<TD class="dark">
					<html:file name="forumPageForm" property="formFile" styleClass="forumControls" size="70"/>
				</TD>
			</tr>			
			<tr>
				<td class="light" valign="top" width="20%">
					<TABLE cellSpacing="0" cellPadding="5" width="100" border="0">
						<TBODY>
							<TR align="middle">
								<TD class="gensmall" colSpan="4"><B>
									<digi:trn key="forum:clickableSmiles">clickable smiles</digi:trn></B>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':D')">
									<digi:img title="Very Happy" alt="Very Happy" src="module/common/images/smiles/icon_biggrin.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':)')">
									<digi:img title="Smile" alt="Smile" src="module/common/images/smiles/icon_smile.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':(')">
									<digi:img title="Sad" alt="Sad" src="module/common/images/smiles/icon_sad.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':o')">
									<digi:img title="Surprised" alt="Surprised" src="module/common/images/smiles/icon_surprised.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':shock:')">
									<digi:img title="Shocked" alt="Shocked" src="module/common/images/smiles/icon_eek.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':?')">
									<digi:img title="Confused" alt="Confused" src="module/common/images/smiles/icon_confused.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon('8)')">
									<digi:img title="Cool" alt="Cool" src="module/common/images/smiles/icon_cool.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':lol:')">
									<digi:img title="Laughing" alt="Laughing" src="module/common/images/smiles/icon_lol.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':x')">
									<digi:img title="Mad" alt="Mad" src="module/common/images/smiles/icon_mad.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':P')">
									<digi:img title="Razz" alt="Razz" src="module/common/images/smiles/icon_razz.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':oops:')">
									<digi:img title="Embarassed" alt="Embarassed" src="module/common/images/smiles/icon_redface.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':cry:')">
									<digi:img title="Crying or Very sad" alt="Crying or Very sad" src="module/common/images/smiles/icon_cry.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':evil:')">
									<digi:img title="Evil or Very Mad" alt="Evil or Very Mad" src="module/common/images/smiles/icon_evil.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':twisted:')">
									<digi:img title="Twisted Evil" alt="Twisted Evil" src="module/common/images/smiles/icon_twisted.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':roll:')">
									<digi:img title="Rolling Eyes" alt="Rolling Eyes" src="module/common/images/smiles/icon_rolleyes.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':wink:')">
									<digi:img title="Wink" alt="Wink" src="module/common/images/smiles/icon_wink.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
							<TR vAlign="center" align="middle">
								<TD><A href="javascript:emoticon(':!:')">
									<digi:img title="Exclamation" alt="Exclamation" src="module/common/images/smiles/icon_exclaim.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':?:')">
									<digi:img title="Question" alt="Question" src="module/common/images/smiles/icon_question.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':idea:')">
									<digi:img title="Idea" alt="Idea" src="module/common/images/smiles/icon_idea.gif" border="0" width="15" height="15"/></A>
								</TD>
								<TD><A href="javascript:emoticon(':arrow:')">
									<digi:img title="Arrow" alt="Arrow" src="module/common/images/smiles/icon_arrow.gif" border="0" width="15" height="15"/></A>
								</TD>
							</TR>
						</TBODY>
					</TABLE>
				</td>
				<td class="dark" width="80%">
					<table width="1" border="0" cellpadding="0" cellspacing="3">
						<td>
							<INPUT class="forumButtons" style="FONT-WEIGHT: bold; WIDTH: 30px" accessKey="b" onClick="bbstyle(0)" type="button" value=" B " name="addbbcode0">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 30px; FONT-STYLE: italic" accessKey="i" onClick="bbstyle(2)" type="button" value=" i " name="addbbcode2">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 30px; TEXT-DECORATION: underline" accessKey="u" onClick="bbstyle(4)" type="button" value=" u " name="addbbcode4">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 50px" accessKey="q" onClick="bbstyle(6)" type="button" value="Quote" name="addbbcode6">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="c" onClick="bbstyle(8)" type="button" value="Code" name="addbbcode8">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="l" onClick="bbstyle(10)" type="button" value="List" name="addbbcode10">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="o" onClick="bbstyle(12)" type="button" value="List=" name="addbbcode12">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px" accessKey="p" onClick="bbstyle(14)" type="button" value="Img" name="addbbcode14">
						</td>
						<td>
							<INPUT class="forumButtons" style="WIDTH: 40px; TEXT-DECORATION: underline" accessKey="w" onClick="bbstyle(16)" type="button" value="URL" name="addbbcode16">
						</td>
			</tr>
		</table>
		<table width="1" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td><font class="smallInfoText"><digi:trn key="forum:color">Color</digi:trn>:</font>
				</td>
				<td>
					<SELECT onMouseOver="helpline('s')" onChange="bbfontstyle('[color=' + this.options[this.selectedIndex].value + ']', '[/color]')" name="addbbcode18">
						<OPTION value="#444444" selected style="COLOR: black; BACKGROUND-COLOR: #fafafa">Font Color</OPTION>
						<OPTION style="COLOR: darkred; BACKGROUND-COLOR: #fafafa" value="darkred">Dark Red</OPTION>
						<OPTION class="genmed" style="COLOR: red; BACKGROUND-COLOR: #fafafa" value="red">Red</OPTION>
						<OPTION class="genmed" style="COLOR: orange; BACKGROUND-COLOR: #fafafa" value="orange">Orange</OPTION>
						<OPTION class="genmed" style="COLOR: brown; BACKGROUND-COLOR: #fafafa" value="brown">Brown</OPTION>
						<OPTION class="genmed" style="COLOR: yellow; BACKGROUND-COLOR: #fafafa" value="yellow">Yellow</OPTION>
						<OPTION class="genmed" style="COLOR: green; BACKGROUND-COLOR: #fafafa" value="green">Green</OPTION>
						<OPTION class="genmed" style="COLOR: olive; BACKGROUND-COLOR: #fafafa" alue="olive">Olive</OPTION>
						<OPTION class="genmed" style="COLOR: cyan; BACKGROUND-COLOR: #fafafa" value="cyan">Cyan</OPTION>
						<OPTION class="genmed" style="COLOR: blue; BACKGROUND-COLOR: #fafafa" value="blue">Blue</OPTION>
						<OPTION class="genmed" style="COLOR: darkblue; BACKGROUND-COLOR: #fafafa" value="darkblue">Dark Blue</OPTION>
						<OPTION class="genmed" style="COLOR: indigo; BACKGROUND-COLOR: #fafafa" value="indigo">Indigo</OPTION>
						<OPTION class="genmed" style="COLOR: violet; BACKGROUND-COLOR: #fafafa" value="violet">Violet</OPTION>
						<OPTION class="genmed" style="COLOR: white; BACKGROUND-COLOR: #fafafa" value="white">White</OPTION>
						<OPTION class="genmed" style="COLOR: black; BACKGROUND-COLOR: #fafafa" value="black">Black</OPTION>
					</SELECT>
				</td>
				<td><font class="smallInfoText"><digi:trn key="forum:size">Size</digi:trn>:</font>
				</td>
				<td>
					<SELECT onMouseOver="helpline('f')" onChange="bbfontstyle('[size=' + this.form.addbbcode20.options[this.form.addbbcode20.selectedIndex].value + ']', '[/size]')" name="addbbcode20">
						<OPTION class="genmed" value="7">Tiny</OPTION>
						<OPTION class="genmed" value="9">Small</OPTION>
						<OPTION class="genmed" value="12" selected>Normal</OPTION>
						<OPTION class="genmed" value="18">Large</OPTION>
						<OPTION class="genmed" value="24">Huge</OPTION>
					</SELECT>
				</td>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<TD>
					<html:textarea rows="14" cols="70" styleClass="forumControls" name="forumPageForm" property="postContent"></html:textarea>
				</TD>
			</tr>
		</table>
		<table width="100%" border="0" cellpadding="0" cellspacing="3">
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="enableEmotions"/>
					<digi:trn key="forum:enableEmotions">Enable emotions</digi:trn>
				</td>
			</tr>
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="allowHtml"/>
					<digi:trn key="forum:allowHTML">Allow HTML</digi:trn>
				</td>
			</tr>
			<tr>
				<td>
					<html:checkbox name="forumPageForm" property="notifyOnReply"/>
					<digi:trn key="forum:enableNotificationsOnReply">Enable notifications on reply</digi:trn>
				</td>
			</tr>
		</table>
	</td>
	</tr>
		<tr>
			<td colspan="2" class="sectionTitle" align="center">
				<input class="forumButtons" type="button" value="Preview" onClick="showPostPreview()">&nbsp;
				<input class="forumButtons" type="button" value="Reply" onClick="addPost()">
			</td>
		</tr>
	</table>
	
	
	<script language="JavaScript">
		init();
	</script>
	</logic:equal>
	</logic:equal>
	
	</digi:secure>
	</logic:equal>

	
	
	
	
	
	<logic:equal name="forumForm" property="forum.allowUnregisteredUserPost" value="true"/>
	

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
							<digi:link styleClass="forum" href="<%= "/showThread.do?threadId=" + String.valueOf(forumForm.getThreadId()) + "&firstPost=true" %>">
								<!--First-->
								<bean:write name="pageItem" property="displayString"/>
							</digi:link>
						</logic:equal>
						<logic:equal name="pageItem" property="itemType" value="<%= String.valueOf(ForumPaginationItem.GENERIC_PAGE) %>">
							<digi:link styleClass="forum" href="<%= "/showThread.do?threadId=" + String.valueOf(forumForm.getThreadId()) + "&startForm=" + String.valueOf(pageItem.getParameter()) %>">
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
							<digi:link styleClass="forum" href="<%= "/showThread.do?threadId=" + String.valueOf(forumForm.getThreadId()) + "&lastPost=true" %>">
								<!--Last-->
								<bean:write name="pageItem" property="displayString"/>
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
</digi:form>
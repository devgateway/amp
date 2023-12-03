<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.ampModule.forum.dbentity.ForumThread" %>
<%@ page import="org.digijava.ampModule.forum.util.ForumManager" %>
<digi:instance property="forumForm"/>

<digi:ref href="css/demoUI.css" rel="stylesheet" type="text/css" />


<digi:form method="post" action="/renderTeaser.do">


<table border="0" cellpadding="0" cellspacing="0" width="250">
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserTitleLeft.gif"/></td>
		<td width="100%" height="25" class="teaserTitleBody">&nbsp;<digi:trn key="forum:discussionForum">Discussion forum</digi:trn></td>
		<td width="5" height="25"><digi:img src="images/ui/teaserTitleRight.gif"/></td>
	</tr>
	<tr>
		<td width="10" height="25"><digi:img src="images/ui/teaserExtrainfoLeft.gif"/></td>
		<td width="100%" height="25" class="teaserExtraBody">
		&nbsp;
			<logic:present name="forumForm" property="forum">
				<bean:write name="forumForm" property="forum.name"/>
			</logic:present>
			<logic:notPresent name="forumForm" property="forum">
				<digi:trn key="forum:forumNotActivated">Forum is not activated</digi:trn>
			</logic:notPresent>
			
		</td>
		<td width="5" height="25"><digi:img src="images/ui/teaserExtrainfoRight.gif"/></td>
	</tr>
	<tr>
	<td class="bodyLeftTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	<td class="bodyField" width="100%">
	<!-- Body -->
		<table border="0"	 cellpadding="0" cellspacing="3" width="100%">
			<%--<tr>
				<td>
					New threads <bean:write name="forumForm" property="newThreads"/>
				</td>
			</tr>
			<tr>
				<td>
					Total threads <bean:write name="forumForm" property="totalThreads"/>
				</td>
			</tr>
			<tr>
				<td class="contentSplitter" height="4">
					<digi:img src="images/ui/spacer.gif" height="4"/>
				</td>
			</tr>			
			<tr>
				<td>
					New posts <bean:write name="forumForm" property="newPosts"/>
				</td>
			</tr>
			<tr>
				<td>
					Total posts <bean:write name="forumForm" property="totalPosts"/>
				</td>
			</tr>--%>
			
			<logic:present name="forumForm" property="lastPosts">
				<logic:iterate id="thread" name="forumForm" property="lastPosts" type="ForumThread">
					<tr>
						<td>
							<bean:write name="thread" property="lastPost.title"/>
						</td>
					</tr>
					<tr>
						<td>
							<digi:trn key="forum:postedBy">posted by</digi:trn>: 
						
								<logic:present name="thread" property="lastPost.authorUserSettings">
									<digi:link href="<%= "/showUserDetails.do?userId=" + String.valueOf(thread.getLastPost().getAuthorUserSettings().getId()) %>">
										<bean:write name="thread" property="lastPost.authorUserSettings.nickName"/>
									</digi:link>
								</logic:present>
								<logic:notPresent name="thread" property="lastPost.authorUserSettings">
									<a class="forum" href="mailto:<bean:write name="thread" property="lastPost.unregisteredEmail"/>">
										<bean:write name="thread" property="lastPost.unregisteredFullName"/>
									</a>
								</logic:notPresent>
							<digi:trn key="forum:on">on</digi:trn>
							<%= ForumManager.getFormatedDateTime(thread.getLastPost().getPostTime()) %>							
						</td>
					</tr>
					<tr>
						<td>
						<digi:trn key="forum:inThread">in thread</digi:trn>:
							<digi:link href="<%= "/showThread.do?threadId=" + String.valueOf(thread.getId()) %>">
								<bean:write name="thread" property="title"/>
							</digi:link>
						</td>
					</tr>
					<tr>
						<td class="contentSplitter" height="4">
							<digi:img src="images/ui/spacer.gif" height="4"/>
						</td>
					</tr>						
				</logic:iterate>
			</logic:present>
			<tr>
				<td align="right">
					<digi:link href="/index.do"><digi:trn key="forum:showAll">Show All</digi:trn></digi:link>				
				</td>
			</tr>
		</table>
	<!-- Body -->
	</td>
	<td class="bodyRightTile" width="10"><digi:img src="images/ui/spacer.gif"/></td>
	</tr>
	
	<tr>
	<td width="10" height="11"><digi:img src="images/ui/teaserBottomLeft.gif"/></td>
	<td width="100%" height="11" class="teaserBottomBody"><digi:img src="images/ui/spacer.gif"/></td>
	<td width="5" height="11"><digi:img src="images/ui/teaserBottomRight.gif"/></td>
	</tr>	
</table>

</digi:form>
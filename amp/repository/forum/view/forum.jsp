<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumThread" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<digi:instance property="forumForm"/>



<digi:form method="post" action="/renderTeaser.do">

<digi:errors property="forumGlobalError"/>

		<table width="100%" border="1" cellspacing="3" cellpadding="0" bordercolor="#000000" style="border-collapse: collapse;">
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
				</logic:iterate>
			</logic:present>
			<tr>
				<td align="right">
					<digi:link href="/index.do"><digi:trn key="forum:showAll">Show All</digi:trn></digi:link>				
				</td>
			</tr>
		</table>
</digi:form>
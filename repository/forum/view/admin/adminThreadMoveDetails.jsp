<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumThread" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>

<digi:instance property="forumAdminPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">

<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">
	function cancel() {
      <digi:context name="showSubsection" property="context/module/moduleinstance/adminShowSubsection.do" />
      document.forumAdminPageForm.action = "<%= showSubsection %>?subsectionId=" + <bean:write name="forumAdminPageForm" property="subsectionId"/>;
      document.forumAdminPageForm.submit();
	}

	function save() {
      <digi:context name="adminSaveThreads" property="context/module/moduleinstance/adminSaveThreads.do" />
      document.forumAdminPageForm.action = "<%= adminSaveThreads %>?subsectionId=" + <bean:write name="forumAdminPageForm" property="subsectionId"/>;
      document.forumAdminPageForm.submit();
	}
</script>

<digi:form method="post" action="/adminShowMoveThreads.do">

<digi:errors property="forumGlobalError"/>

<logic:iterate id="selThreadId" indexId="indId" name="forumAdminPageForm" property="checkboxList">
	<html:hidden name="forumAdminPageForm" property="checkboxList" value="<%=selThreadId.toString()%>"/>
</logic:iterate>



<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:selectDestinationForum">Select destination forum</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:forum">Forum</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="moveToSectionId">
				<logic:iterate id="section" name="forumAdminPageForm" property="forum.sections" type="ForumSection">
					<html:option style="background-color:#DEE3E7;color:#black;" value="">
						<bean:write name="section" property="title"/>
					</html:option>
						<logic:iterate id="subsection" name="section" property="subsections" type="ForumSubsection">
							<html:option style="background-color:#EFEFEF;color:#black;" value="<%= String.valueOf(subsection.getId()) %>">
								&nbsp;&nbsp;<bean:write name="subsection" property="title"/>
							</html:option>
						</logic:iterate>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:leaveShadowTopic">Leave shadow topic</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="leaveShadow">
					<html:option value="false">No</html:option>
					<html:option value="true">Yes</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Move"
					   class="forumButtons"
					   onClick="save()">
				&nbsp;
				<input type="button"
					   value="Cancel"
					   class="forumButtons"
					   onClick="cancel()">
			</th>
		</tr>
	</table>
<script language="JavaScript">
	var selection = <bean:write name="forumAdminPageForm" property="subsectionId"/>;
	function testFunc(e) {
		if (document.getElementsByName("moveToSectionId")[0].value == "") {
			document.getElementsByName("moveToSectionId")[0].value = selection;
		} else {
			selection = document.getElementsByName("moveToSectionId")[0].value;
		}
	}

	document.getElementsByName("moveToSectionId")[0].onchange = testFunc;
</script>
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
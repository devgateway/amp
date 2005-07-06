<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<digi:instance property="forumAdminPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>


<script language="JavaScript">

	function saveSubsection(){
      <digi:context name="saveSubsection" property="context/module/moduleinstance/adminSaveSubsection.do" />
      document.forumAdminPageForm.action = "<%= saveSubsection %>";
      document.forumAdminPageForm.submit();
	}

	function cancel() {
      <digi:context name="showAdminIndex" property="context/module/moduleinstance/adminShowAdminIndex.do" />
      document.forumAdminPageForm.action = "<%= showAdminIndex %>";
      document.forumAdminPageForm.submit();
	}

</script>

<digi:form action="/adminSaveSubsection.do">
	<html:hidden name="forumAdminPageForm" property="subsectionId"/>


<digi:errors property="forumGlobalError"/>

	<table class="forumListContainer" width="96%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:globalForumSettings">Global forum settings</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:forumName">Forum name</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text name="forumAdminPageForm"
						   property="subsectionTitle"
						   styleClass="forumControls"
						   size="30"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:description">Description</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:textarea name="forumAdminPageForm"
							   property="subsectionComment"
							   styleClass="forumControls"
							   cols="30"
							   rows="4"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:category">Category</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="sectionId">
					<logic:iterate name="forumAdminPageForm"
								   property="forum.sections"
								   id="section"
								   type="ForumSection">
								   <html:option value="<%= String.valueOf(section.getId()) %>"><bean:write name="section" property="title"/></html:option>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:forumStatus">Forum status</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="subsectionLocked">
					<html:option value="false"><digi:trn key="forum:unlocked">Unlocked</digi:trn></html:option>
					<html:option value="true"><digi:trn key="forum:locked">Locked</digi:trn></html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:forumMode">Forum mode</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="requiresPublishing">
					<html:option value="false">Instant publishing</html:option>
					<html:option value="true">Deferred publishing</html:option>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:sendApproveMsgs">Send approve messages</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:radio name="forumAdminPageForm"
							value="true"
							property="sendApproveMsg"/><digi:trn key="forum:yes">Yes</digi:trn> &nbsp;
				<html:radio name="forumAdminPageForm"
							value="false"
							property="sendApproveMsg"/><digi:trn key="forum:no">No</digi:trn> &nbsp;

				<html:textarea rows="2"
							  cols="30"
							  name="forumAdminPageForm"
							  property="approveMsg"
							  styleClass="forumControls"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:sendDeleteMsgs">Send delete messages</digi:trn></digi:list></td>
			<td class="dark" width="70%" align="left">
				<html:radio name="forumAdminPageForm"
							value="true"
							property="sendDeleteMsg"/><digi:trn key="forum:yes">Yes</digi:trn> &nbsp;
				<html:radio name="forumAdminPageForm"
							value="false"
							property="sendDeleteMsg"/><digi:trn key="forum:no">No</digi:trn> &nbsp;

				<html:textarea rows="2"
							  cols="30"
							  name="forumAdminPageForm"
							  property="deleteMsg"
							  styleClass="forumControls"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Save"
					   class="forumButtons"
					   onClick="saveSubsection()">
				&nbsp;
				<input type="button"
					   value="Cancel"
					   class="forumButtons"
					   onClick="cancel()">
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
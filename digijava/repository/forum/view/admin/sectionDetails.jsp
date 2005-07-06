<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<digi:instance property="forumAdminPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">
	function saveSection(){
      <digi:context name="saveSection" property="context/module/moduleinstance/adminSaveSection.do" />
      document.forumAdminPageForm.action = "<%= saveSection %>";
      document.forumAdminPageForm.submit();
	}

	function cancel() {
      <digi:context name="showAdminIndex" property="context/module/moduleinstance/adminShowAdminIndex.do" />
      document.forumAdminPageForm.action = "<%= showAdminIndex %>";
      document.forumAdminPageForm.submit();
	}

</script>

<digi:form action="/adminSaveSection.do">
	<html:hidden name="forumAdminPageForm" property="sectionId"/>


<digi:errors property="forumGlobalError"/>

	<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
		<tr>
			<th colspan="2" align="center" class="groupHeader" width="70%" nowrap>
				<digi:trn key="forum:generalCategorySettings">General category settings</digi:trn>
			</th>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:categoryName">Category name</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text name="forumAdminPageForm"
						   property="sectionTitle"
						   styleClass="forumControls"
						   size="30"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:description">Description</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:textarea name="forumAdminPageForm"
							   property="sectionComment"
							   styleClass="forumControls"
							   cols="30"
							   rows="4"/>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Save"
					   class="forumButtons"
					   onClick="saveSection()">
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
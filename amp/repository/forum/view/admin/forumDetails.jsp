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

	function saveForum(){
      <digi:context name="adminSaveForum" property="context/module/moduleinstance/adminSaveForum.do" />
      document.forumAdminPageForm.action = "<%= adminSaveForum %>";
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
			<td class="light" width="30%" align="left">
			   <digi:trn key="forum:forumName">Forum name</digi:trn>
		    </td>
			<td class="dark" width="70%" align="left">
				<html:text name="forumAdminPageForm"
						   property="forumName"
						   styleClass="forumControls"
						   size="30"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:topicsPerPage">Topics per page</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="3"
						   name="forumAdminPageForm"
						   property="topicsPerPage"
						   styleClass="forumControls"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:postsPerPage">Posts per page</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="3"
						   name="forumAdminPageForm"
						   property="postsPerPage"
					   	   styleClass="forumControls"/>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:systemTimezone">System timezone</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:select name="forumAdminPageForm" property="systemTimezone">
					<logic:iterate id="zone" name="forumAdminPageForm" property="gmtTimeZones" type="java.util.TimeZone">
					<html:option value="<%= String.valueOf(zone.getRawOffset()) %>">
						<bean:write name="zone" property="displayName"/>
					</html:option>
					</logic:iterate>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:minMsgLength">Min. message length</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="3" maxlength="3"
						   name="forumAdminPageForm"
						   property="minMessageLength"
					   	   styleClass="forumControls"/>&nbsp;<digi:trn key="forum:characters">characters</digi:trn>.  (0 - <digi:trn key="forum:noLimit">no limit</digi:trn>)
			</td>
		</tr>

		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:uploadedImgMaxSize">Uploaded image max. size</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="6" maxlength="6"
						   name="forumAdminPageForm"
						   property="maxImageSize"
					   	   styleClass="forumControls"/>&nbsp;<digi:trn key="forum:bytes">bytes</digi:trn>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:uploadedImgMaxDimentions">Uploaded image max. dimentions (W X H)</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="4" maxlength="4"
						   name="forumAdminPageForm"
						   property="maxImageWidth"
					   	   styleClass="forumControls"/>
						   X
				<html:text size="4" maxlength="4"
						   name="forumAdminPageForm"
						   property="maxImageHeigth"
					   	   styleClass="forumControls"/>&nbsp;<digi:trn key="forum:px">px</digi:trn>	(0X0 <digi:trn key="forum:noLimit">no	limit</digi:trn>)
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:uploadedFileMaxSize">Uploaded file max. size</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="6" maxlength="6"
						   name="forumAdminPageForm"
						   property="maxUploadSize"
					   	   styleClass="forumControls"/>&nbsp;<digi:trn key="forum:bytes">bytes</digi:trn>
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:minIntervalBetweenPosts">Min. interval between user posts</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:text size="3"
						   name="forumAdminPageForm"
						   property="postIntervalLimit"
					   	   styleClass="forumControls"/>&nbsp;<digi:trn key="forum:sec">sec</digi:trn>.
			</td>
		</tr>
		<tr>
			<td class="light" width="30%" align="left"><digi:trn key="forum:allowUnregPost">Allow post for unregistered users</digi:trn></td>
			<td class="dark" width="70%" align="left">
				<html:radio name="forumAdminPageForm"
							   property="allowUnregisteredUserPost"
							   value="false"/>
				&nbsp;<digi:trn key="forum:no">no</digi:trn>
				<html:radio name="forumAdminPageForm"
							   property="allowUnregisteredUserPost"
							   value="true"/>
				&nbsp;<digi:trn key="forum:yes">yes</digi:trn>				
			</td>
		</tr>		
		<tr>
			<td colspan="2" align="center" class="sectionTitle" nowrap>
				<input type="button"
					   value="Save"
					   class="forumButtons"
					   onClick="saveForum()">
				&nbsp;
				<input type="button"
					   value="Cancel"
					   class="forumButtons"
					   onClick="cancel()">
			</td>
		</tr>
	</table>
</digi:form>
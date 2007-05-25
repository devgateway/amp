<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>


<%@ page import="org.apache.struts.action.ActionErrors" %>

<%@ page import="org.digijava.module.forum.dbentity.ForumSection" %>
<%@ page import="org.digijava.module.forum.dbentity.ForumSubsection" %>
<%@ page import="org.digijava.module.forum.util.ForumManager" %>
<%@ page import="org.digijava.module.forum.util.ForumConstants" %>
<%@ page import="org.digijava.module.forum.util.LocationTrailItem" %>

<digi:instance property="forumAdminPageForm"/>

<link rel="stylesheet" href="<digi:file src="module/forum/css/forum.css"/>">
<script language="JavaScript" type="text/javascript" src="<digi:file src="module/forum/scripts/forum.js"/>"></script>

<script language="JavaScript">

	function activateForum(){
      <digi:context name="activateForum" property="context/module/moduleinstance/adminActivateForum.do" />
      document.forumAdminPageForm.action = "<%= activateForum %>";
      document.forumAdminPageForm.submit();
	}

	<%--Section functions--%>
	function addSection () {
		document.getElementsByName("sectionId")[0].value = 0;
      <digi:context name="showAddSection" property="context/module/moduleinstance/adminShowAddSection.do" />
      document.forumAdminPageForm.action = "<%= showAddSection %>";
      document.forumAdminPageForm.submit();
	}

	function editSection (id) {
		document.getElementsByName("sectionId")[0].value = id;
      <digi:context name="showEditSection" property="context/module/moduleinstance/adminShowEditSection.do" />
      document.forumAdminPageForm.action = "<%= showEditSection %>";
      document.forumAdminPageForm.submit();
	}

	function deleteSection (id, secTitle) {
		if (confirm ('Do you realy want to delete category \"' + secTitle + "\" ?")) {
			document.getElementsByName("sectionId")[0].value = id;
			<digi:context name="deleteSection" property="context/module/moduleinstance/adminDeleteSection.do" />
			document.forumAdminPageForm.action = "<%= deleteSection %>";
			document.forumAdminPageForm.submit();
		}
	}

	function moveSectionUp(id){
		document.getElementsByName("moveDirection")[0].value = <%=ForumConstants.MOVE_UP%>;
		document.getElementsByName("sectionId")[0].value = id;
		<digi:context name="moveSectionUp" property="context/module/moduleinstance/adminMoveSection.do" />
		document.forumAdminPageForm.action = "<%= moveSectionUp %>";
		document.forumAdminPageForm.submit();
	}

	function moveSectionDown(id){
		document.getElementsByName("moveDirection")[0].value = <%=ForumConstants.MOVE_DOWN%>;
		document.getElementsByName("sectionId")[0].value = id;
		<digi:context name="moveSectionDown" property="context/module/moduleinstance/adminMoveSection.do" />
		document.forumAdminPageForm.action = "<%= moveSectionDown %>";
		document.forumAdminPageForm.submit();
	}

	<%--end of Section functions--%>

	<%--Subsection functions--%>
	function addSubsection (secIndex, id) {
		document.getElementsByName("subsectionTitle")[0].value =
			document.getElementsByName("subsTitle")[secIndex].value;
		document.getElementsByName("sectionId")[0].value = id;
      <digi:context name="showAddSubsection" property="context/module/moduleinstance/adminShowAddSubsection.do" />
      document.forumAdminPageForm.action = "<%= showAddSubsection %>";
      document.forumAdminPageForm.submit();
	}

	function editSubsection (secId, subsecId) {
		document.getElementsByName("sectionId")[0].value = secId;
		document.getElementsByName("subsectionId")[0].value = subsecId;
      <digi:context name="showEditSubsection" property="context/module/moduleinstance/adminShowEditSubsection.do" />
      document.forumAdminPageForm.action = "<%= showEditSubsection %>";
      document.forumAdminPageForm.submit();
	}

	function deleteSubsection (id, subsName) {
		if (confirm ('Do you realy want to delete forum \"' + subsName + "\" ?")) {
			document.getElementsByName("subsectionId")[0].value = id;
			<digi:context name="deleteSubsection" property="context/module/moduleinstance/adminDeleteSubsection.do" />
			document.forumAdminPageForm.action = "<%= deleteSubsection %>";
			document.forumAdminPageForm.submit();
	  }
	}

	function moveSubsectionUp(secId, subsecId){
		document.getElementsByName("moveDirection")[0].value = <%=ForumConstants.MOVE_UP%>;
		document.getElementsByName("sectionId")[0].value = secId;
		document.getElementsByName("subsectionId")[0].value = subsecId;
		<digi:context name="moveSubsectionUp" property="context/module/moduleinstance/adminMoveSubsection.do" />
		document.forumAdminPageForm.action = "<%= moveSubsectionUp %>";
		document.forumAdminPageForm.submit();
	}

	function moveSubsectionDown(secId, subsecId){
		document.getElementsByName("moveDirection")[0].value = <%=ForumConstants.MOVE_DOWN%>;
		document.getElementsByName("sectionId")[0].value = secId;
		document.getElementsByName("subsectionId")[0].value = subsecId;
		<digi:context name="moveSubsectionDown" property="context/module/moduleinstance/adminMoveSubsection.do" />
		document.forumAdminPageForm.action = "<%= moveSubsectionDown %>";
		document.forumAdminPageForm.submit();
	}

	<%--end of Subsection functions--%>


	function showSubsection (id) {
      <digi:context name="showThreads" property="context/module/moduleinstance/adminShowSubsection.do" />
      document.forumAdminPageForm.action = "<%= showThreads %>?subsectionId=" + id;
      document.forumAdminPageForm.submit();
	}

	function showThread(id) {
      <digi:context name="showPosts" property="context/module/moduleinstance/showThread.do" />
      document.forumAdminPageForm.action = "<%= showPosts %>?threadId=" + id;
      document.forumAdminPageForm.submit();
	}
</script>



<digi:form method="post" action="/adminShowAdminIndex.do">

<html:hidden name="forumAdminPageForm" property="sectionId"/>
<html:hidden name="forumAdminPageForm" property="subsectionId"/>
<html:hidden name="forumAdminPageForm" property="subsectionTitle"/>
<html:hidden name="forumAdminPageForm" property="moveDirection"/>

<digi:errors property="forumGlobalError"/>

<logic:notPresent name="forumAdminPageForm" property="forum">
	<digi:trn key="forum:forumNotActivated">Forum is not activated</digi:trn>.<br>
	<a href="javascript:activateForum()"><digi:trn key="forum:activateForumAccount">Activate forum account</digi:trn></a>
</logic:notPresent>

<logic:present name="forumAdminPageForm" property="forum">
<bean:define id="forumObj" name="forumAdminPageForm" property="forum" type="org.digijava.module.forum.dbentity.Forum"/>
<bean:define id="lastSectionIndex" value="<%= String.valueOf(forumObj.getSections().size()-1) %>"/>

		<table class="forumListContainer" width="100%" cellpadding="3" cellspacing="0" border="1">
			<tr>
				<th colspan="7" align="center" class="groupHeader" width="70%" nowrap>
					<digi:trn key="forum:forumAdministration">Forum administration</digi:trn>
				</th>
			</tr>
			<TR>
				<td colspan="7" class="dark">
					<table border="0" cellpadding="0" cellspacing="3" class="trailContainer" width="100%">
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
							<td width="100%" align="right" nowrap>
								<digi:link href="/adminShowEditForum.do" styleClass="forum">
									<digi:trn key="forum:globalForumSettings">Global forum settings</digi:trn>
								</digi:link>
							</td>
						<!-- End of Trail -->
						</tr>
					</table>
				</td>
			</TR>

			<!-- Sections -->
			<logic:iterate id="section" indexId="sectionIndex" name="forumObj" property="sections" type="ForumSection">
				<tr>
					<td class="sectionTitle" width="70%" colspan="3">
						<bean:write name="section" property="title"/>
					</td>
					<td class="sectionTitle" width="5%" nowrap align="center">
						<font class="smallLink">
							<a class="forum" href="javascript:editSection(<bean:write name="section" property="id"/>)">
								<digi:trn key="forum:edit">Edit</digi:trn>
							</a>
						</font>
					</td>
					<td class="sectionTitle" width="5%" nowrap align="center">
						<font class="smallLink">
							<a class="forum" href="javascript:deleteSection(<bean:write name="section" property="id"/>,
																			'<bean:write name="section" property="title"/>')">
								<digi:trn key="forum:delete">Delete</digi:trn>
							</a>
						</font>
					</td>
					<td class="sectionTitle" width="5%" nowrap>
						<font class="smallLink">
						<%--sectionCount--%>

						<logic:greaterThan name="sectionIndex" value="0">
							<a class="forum"
							   href="javascript:moveSectionUp(<bean:write name="section" property="id"/>)">
							   <digi:img src="module/forum/images/upArrow.gif" border="0" alt="Move up"/>
							</a>
						</logic:greaterThan>
						<logic:lessEqual name="sectionIndex" value="0">
							<digi:img src="module/forum/images/upArrowDisabled.gif" border="0" alt="Move up"/>
						</logic:lessEqual>
						|
						<logic:lessThan name="sectionIndex" value="<%= lastSectionIndex %>">
							<a class="forum"
							   href="javascript:moveSectionDown(<bean:write name="section" property="id"/>)">
							   <digi:img src="module/forum/images/downArrow.gif" border="0" alt="Move down"/>
							</a>
						</logic:lessThan>
						<logic:equal name="sectionIndex" value="<%= lastSectionIndex %>">
							<digi:img src="module/forum/images/downArrowDisabled.gif" border="0" alt="Move down"/>
						</logic:equal>
						</font>
					</td>
				</tr>
				<!-- Subsections -->
				<bean:define id="lastSubsectionIndex" value="<%= String.valueOf(section.getSubsections().size()-1) %>"/>
				<logic:iterate id="subsection" indexId="subsectionIndex" name="section" property="subsections" type="ForumSubsection">
					<tr>
						<td class="forumInfo" width="70%">
							<a class="forum" href="javascript:showSubsection(<bean:write name="subsection" property="id"/>)">
								<bean:write name="subsection" property="title"/>
							</a>
							<br>
							<font class="smallInfoText">
								<bean:write name="subsection" property="comment"/>
							</font>
						</td>
						<td class="forumTitle" width="10%" align="center">
							<font class="smallInfoText">
								<bean:write name="subsection" property="threadCount"/>
							</font>
						</td>
						<td class="forumInfo" width="10%" align="center">
							<font class="smallInfoText">
								577
							</font>
						</td>
						<td class="forumTitle" width="10%" align="center" nowrap>
							<font class="smallLink">
								<a class="forum" href="javascript:editSubsection(<bean:write name="section" property="id"/>, <bean:write name="subsection" property="id"/>)">
									<digi:trn key="forum:edit">Edit</digi:trn>
								</a>
							</font>
						</td>
						<td class="forumInfo" width="10%" align="center" nowrap>
							<font class="smallLink">
								<a class="forum" href="javascript:deleteSubsection(<bean:write name="subsection" property="id"/>,
																					'<bean:write name="subsection" property="title"/>')">
									<digi:trn key="forum:delete">Delete</digi:trn>
								</a>
							</font>
						</td>
						<td class="forumTitle" width="10%" align="center" nowrap>
							<font class="smallLink">
								<logic:greaterThan name="subsectionIndex" value="0">
									<a class="forum" href="javascript:moveSubsectionUp(<bean:write name="section" property="id"/>,
																					<bean:write name="subsection" property="id"/>)">
										<digi:img src="module/forum/images/upArrow.gif" border="0" alt="Move up"/>
									</a>
								</logic:greaterThan>
								<logic:lessEqual name="subsectionIndex" value="0">
									<digi:img src="module/forum/images/upArrowDisabled.gif" border="0" alt="Move up"/>
								</logic:lessEqual>
								|
								<logic:lessThan name="subsectionIndex" value="<%= lastSubsectionIndex %>">
									<a class="forum" href="javascript:moveSubsectionDown(<bean:write name="section" property="id"/>,
																					<bean:write name="subsection" property="id"/>)">
										<digi:img src="module/forum/images/downArrow.gif" border="0" alt="Move down"/>
									</a>
								</logic:lessThan>
								<logic:equal name="subsectionIndex" value="<%= lastSubsectionIndex %>">
									<digi:img src="module/forum/images/downArrowDisabled.gif" border="0" alt="Move down"/>
								</logic:equal>
							</font>
						</td>
					</tr>
				</logic:iterate>
				<!-- End of subsections -->
				<tr>
					<td colspan="7" align="left" class="forumInfo" width="70%" nowrap>
						<input type="text" name="subsTitle" class="forumControls" size="30">&nbsp;
						<input class="forumButtons"
							   type="Button"
							   value="Create new forum"
							   onClick="addSubsection (<bean:write name="sectionIndex"/>, <bean:write name="section" property="id"/>)">
					</td>
				</tr>
				<tr>
					<td class="sectionSplitter" colspan="7"></td>
				</tr>
			</logic:iterate>
			<!-- End of sections -->
			<tr>
				<td colspan="7" align="left" class="forumInfo" width="70%" nowrap>
					<html:text name="forumAdminPageForm" property="sectionTitle" styleClass="forumControls" size="30"/>&nbsp;
					<input class="forumButtons"
						   type="Button"
						   value="Create new category"
						   onClick="addSection()">
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
</logic:present>
</digi:form>
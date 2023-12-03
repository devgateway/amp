<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/category" prefix="category" %>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">



	<!--
	
	function searchOrganization() {
		<digi:context name="addChild" property="context/ampModule/moduleinstance/addChildWorkspaces.do" />
		document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest=admin&childorgs=true";
		document.aimUpdateWorkspaceForm.submit();
		return true;

	}

	function updateCheckboxes() {
		var value = document.getElementById("selectAllOrgs").checked;
		var a = document.getElementsByName("selChildOrgs");
		for (i=0;i<a.length;i++)
			a[i].checked=value;
	}
	
	function resetForm() {
		document.aimUpdateWorkspaceForm.ampOrgTypeId.value=-1;
		document.aimUpdateWorkspaceForm.keyword.value="";
	}	


	function load() {

		if (window.opener.document.aimUpdateWorkspaceForm.currUrl.value == "") {

			window.opener.document.aimUpdateWorkspaceForm.currUrl.value = "/addChildWoekspace";

		}

	}



	function unload() {

		window.opener.document.aimUpdateWorkspaceForm.currUrl.value="";

	}



	function closeWindow() {

		//window.opener.document.aimUpdateWorkspaceForm.currUrl.value="";

		window.close();

	}



	function getChildWorkspaces() {

		var dest   = document.aimUpdateWorkspaceForm.dest.value;

		var wsType = document.aimUpdateWorkspaceForm.childWorkspaceType.value;

		//var tCat   = document.aimUpdateWorkspaceForm.childTeamTypeId.value;

		<digi:context name="getChild" property="context/ampModule/moduleinstance/addChildWorkspaces.do" />

		document.aimUpdateWorkspaceForm.action = "<%= getChild %>?wType="+wsType+"&popupReset=true&dest="+dest;

		document.aimUpdateWorkspaceForm.target = "_self"

	   document.aimUpdateWorkspaceForm.submit();

	}



	function childWorkspacesAdded() {

		var dest = document.aimUpdateWorkspaceForm.dest.value;

		<digi:context name="addChild" property="context/ampModule/moduleinstance/childWorkspacesAdded.do" />

		document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest="+dest;

		document.aimUpdateWorkspaceForm.target = window.opener.name;

	    document.aimUpdateWorkspaceForm.submit();

		window.close();

	}

function childOrgsAdded() {

		var dest = document.aimUpdateWorkspaceForm.dest.value;

		<digi:context name="addChild" property="context/ampModule/moduleinstance/childWorkspacesAdded.do" />

		document.aimUpdateWorkspaceForm.action = "<%=addChild%>?childorgs=true&dest="+dest;

		document.aimUpdateWorkspaceForm.target = window.opener.name;

	    document.aimUpdateWorkspaceForm.submit();

		window.close();

	}

		function clearFormCheckBoxes() {

			var el_collection=eval("document.forms.aimUpdateWorkspaceForm.selChildWorkspaces");
			if(el_collection)
			{
				for (c=0;c<el_collection.length;c++)
					el_collection[c].checked=false;
				return true;
			}
		}

	-->



</script>

<digi:instance property="aimUpdateWorkspaceForm" />
<digi:form action="/childWorkspacesAdded.do" method="post">


<logic:empty name="aimUpdateWorkspaceForm"  property="actionType">
	<html:hidden property="teamId" />
	<html:hidden property="dest" />
	<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
		<tr>
			<td vAlign="top">
			<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
				class=box-border-nopadding>
				<tr>
					<td align=left valign="top">
					<table width="100%" cellPadding=2 cellspacing="1" vAlign="top"
						align="left" bgcolor="#006699">
						<tr>
							<td width="33%" bgcolor="#ECF3FD" align="right"><digi:trn
								key="aim:workspaceType">

									Workspace Type

								</digi:trn></td>
							<td width="33%" bgcolor="#ECF3FD" align="center"><html:select
								property="childWorkspaceType" styleClass="inp-text">
								<html:option value="">
									<digi:trn key="aim:addChildWorkspaceTypeAll">
										ALL
									</digi:trn>
								</html:option>
								<html:option value="Management">
									<digi:trn key="aim:addChildWorkspaceTypeManagement">
										Management
									</digi:trn>
								</html:option>
								<html:option value="Team">
									<digi:trn key="aim:addChildWorkspaceTypeTeam">
										Team
									</digi:trn>
								</html:option>
							</html:select></td>
							
							<td width="33%" align="left" bgcolor="#ECF3FD">
								<c:set var="translation">
										<digi:trn key="btn:addChildWorkspaceGo">
											GO
										</digi:trn>
								</c:set>
							
								<input type="button" value="${translation}" onclick="getChildWorkspaces()" class="dr-menu">
							</td>
						</tr>
					</table>
					</td>
				</tr>
				
				<tr>
					<td align=left valign="top">
					<table width="100%" cellPadding=2 cellspacing="1" valign="top"
						align="left" bgcolor="#006699">
						<tr>
							<td align="center" class="textalb" height="20"><digi:trn
								key="aim:listOfAllTeams">

									List of all child teams

								</digi:trn></td>
						</tr>
						<tr>
							<td bgcolor="#ECF3FD"><c:if
								test="${!empty aimUpdateWorkspaceForm.availChildWorkspaces}">
								<table width="100%" cellSpacing=2 cellPadding=2 vAlign="top"
									align="left" border="0">
									<c:forEach var="workspaces"
										items="${aimUpdateWorkspaceForm.availChildWorkspaces}">
										<c:if test="${workspaces.isolated == false}">
											<c:if
												test="${workspaces.ampTeamId != aimUpdateWorkspaceForm.teamId}">
												<tr>
													<td width="3" align="left">
													<html:multibox property="selChildWorkspaces">
														<c:out value="${workspaces.ampTeamId}" />
													</html:multibox>
													</td>
													<td width="98%"><c:out value="${workspaces.name}" /></td>
												</tr>
											</c:if>
										</c:if>
									</c:forEach>									<tr>
										<td colspan="2" align="center">
										<table cellPadding=5>
											<tr>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceAdd">
																	Add
															</digi:trn>
													</c:set>
													<input type="button" value="${translation}" class="dr-menu"
													onclick="return childWorkspacesAdded()">
												</td>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceClear">
																	Clear
															</digi:trn>
													</c:set>
													<input type="reset" value="${translation}" class="dr-menu" onclick="return clearFormCheckBoxes();">
												</td>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceClose">
																	Close
															</digi:trn>
													</c:set>
													<input type="button" value="${translation}" class="dr-menu"
													onclick="return closeWindow();">
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
							</c:if></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>	
	</logic:empty>
	
	
	<c:if test="${aimUpdateWorkspaceForm.actionType != null}">
	<html:hidden property="dest" />
<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border="0">
	<tr><td vAlign="top">
		<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%" class=box-border-nopadding>
			<tr>
				<td align=left valign="top">
					<table bgcolor=#f4f4f2 cellpadding="0" cellspacing="0" width="100%" class=box-border-nopadding>
						<tr bgcolor="#4A687A">
							<td vAlign="center" width="100%" align ="center" class="textalb" height="20">
								<digi:trn key="aim:searchOrganization">
								Search Organizations</digi:trn>
							</td></tr>
						<tr>
							<td align="center" bgcolor="#F8F8F8">
								<table cellSpacing=2 cellPadding=2 border="0">
									<tr>
										<td>
											<digi:trn key="aim:selectOrganizationType">
											Select Organization type</digi:trn>
										</td>
										<td>
											<c:set var="translation">
												<digi:trn key="aim:addActivityAllOrganizationTypes">All</digi:trn>
											</c:set>
											<html:select property="ampOrgTypeId" styleClass="inp-text">
												<html:option value="-1">${translation}</html:option>
												<logic:notEmpty name="aimUpdateWorkspaceForm" property="orgTypes">
													<html:optionsCollection name="aimUpdateWorkspaceForm" property="orgTypes"
														value="ampOrgTypeId" label="orgType" />
												</logic:notEmpty>
											</html:select>
										</td>
									</tr>
									<tr>
										<td>
											<digi:trn key="aim:enterKeyword">
											Enter a keyword</digi:trn>
										</td>
										<td>
											<html:text property="keyword" styleClass="inp-text" onkeypress="return avoidEnter(event, this.form)"/>
										</td>
									</tr>
									<script type="text/javascript" language="javascript">
										function avoidEnter(e,form){
											var key=e.keyCode || e.which;
											if (key==13){
												if(window.event.keyCode)
													window.event.keyCode = 0;
												return false;
											}
											else
											{
												return true;
											}
										}
										</script>
									
									<tr>
										<td align="center" colspan=2>
											<html:submit  styleClass="buttonx" property="submitButton" onclick="return searchOrganization()">
												<digi:trn key="btn:search">Search</digi:trn> 
											</html:submit>
											&nbsp;
											<html:button  styleClass="buttonx" property="resetButton" onclick="resetForm()">
												<digi:trn key="btn:clear">Clear</digi:trn> 
											</html:button>
											&nbsp;
											<html:button  styleClass="buttonx" property="submitButton" onclick="closeWindow()">
												<digi:trn key="btn:close">Close</digi:trn> 
											</html:button>
										</td>

									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
					<td align=left valign="top">
						<table bgcolor="#f4f4f2" cellPadding="0" cellSpacing="0" width="100%" class="box-border-nopadding">
							<tr bgcolor="#4A687A">
								<td width="3">
										<input type="checkbox" id="selectAllOrgs" onclick="updateCheckboxes()"/>
									</td>
								<td vAlign="center" width="100%" align="center" class="textalb"	height="20">
									<digi:trn>List of Organizations</digi:trn>
								</td>
							</tr>
								<c:forEach var="org" items="${aimUpdateWorkspaceForm.allOrganizations}">
											<tr>
												<td width="3" align="left">
												<html:multibox property="selChildOrgs">
													<c:out value="${org.ampOrgId}" />
												</html:multibox>
												</td>
												<td width="98%"><c:out value="${org.name}" /></td>
											</tr>
									</c:forEach>
								</table>
								</td>
								</tr>									
									<tr>
										<td align="center">
										<table cellPadding=5>
											<tr>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceAdd">
																	Add
															</digi:trn>
													</c:set>
													<input type="button" value="${translation}" class="buttonx"
													onclick="return childOrgsAdded()">
												</td>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceClear">
																	Clear
															</digi:trn>
													</c:set>
													<input type="reset" value="${translation}" class="buttonx" onclick="return clearFormCheckBoxes();">
												</td>
												<td>
													<c:set var="translation">
															<digi:trn key="btn:addChildWorkspaceClose">
																	Close
															</digi:trn>
													</c:set>
													<input type="button" value="${translation}" class="buttonx"
													onclick="return closeWindow();">
												</td>
											</tr>
										</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>

	</c:if>
</digi:form>



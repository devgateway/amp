<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>

<digi:ref href="css/styles.css" type="text/css" rel="stylesheet" />

<script language="JavaScript">

	<!-- 

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
		var tCat   = document.aimUpdateWorkspaceForm.childTeamCategory.value;
		<digi:context name="getChild" property="context/module/moduleinstance/addChildWorkspaces.do" />
		document.aimUpdateWorkspaceForm.action = "<%= getChild %>?wType="+wsType+"&tCategory="+tCat+"&popupReset=true&dest="+dest;
		document.aimUpdateWorkspaceForm.target = "_self"
	   document.aimUpdateWorkspaceForm.submit();
	}

	function childWorkspacesAdded() {
		var dest = document.aimUpdateWorkspaceForm.dest.value;
		<digi:context name="addChild" property="context/module/moduleinstance/childWorkspacesAdded.do" />
		document.aimUpdateWorkspaceForm.action = "<%=addChild%>?dest="+dest;
		document.aimUpdateWorkspaceForm.target = window.opener.name;
	    document.aimUpdateWorkspaceForm.submit();
		window.close();
	}	

	-->

</script>

<digi:instance property="aimUpdateWorkspaceForm" />
<digi:form action="/childWorkspacesAdded.do" method="post">

	<html:hidden property="teamId" />
	<html:hidden property="dest" />

	<table width="100%" cellSpacing=5 cellPadding=5 vAlign="top" border=0>
		<tr>
			<td vAlign="top">
			<table bgcolor=#f4f4f2 cellPadding=5 cellSpacing=5 width="100%"
				class=box-border-nopadding>
				<tr>
					<td align=left vAlign=top>
					<table width="100%" cellPadding=2 cellSpacing=1 vAlign="top"
						align="left" bgcolor="#006699">
						<tr>
							<td width="20%" bgcolor="#ECF3FD"><digi:trn
								key="aim:workspaceType">
									Workspace Type
								</digi:trn></td>
							<td width="20%" bgcolor="#ECF3FD"><html:select
								property="childWorkspaceType" styleClass="inp-text">
								<html:option value="">ALL</html:option>
								<html:option value="management">Management</html:option>
								<html:option value="team">Team</html:option>
							</html:select></td>
							<td width="20%" bgcolor="#ECF3FD"><digi:trn key="aim:teamType">
									Team Type
								</digi:trn></td>
							<td width="20%" bgcolor="#ECF3FD"><html:select
								property="childTeamCategory" styleClass="inp-text">
								<html:option value="">ALL</html:option>
								<html:option value="bilateral">Bilateral</html:option>
								<html:option value="multilateral">Multilateral</html:option>
							</html:select></td>
							<td width="20%" align="right" bgcolor="#ECF3FD"><input
								type="button" value="GO" onclick="getChildWorkspaces()"
								class="dr-menu"></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td align=left vAlign=top>
					<table width="100%" cellPadding=2 cellSpacing=1 valign="top"
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
									align="left" border=0>
									<c:forEach var="workspaces"
										items="${aimUpdateWorkspaceForm.availChildWorkspaces}">
										<c:if
											test="${workspaces.ampTeamId != aimUpdateWorkspaceForm.teamId}">
											<tr>
												<td width="3" align="left"><html:multibox
													property="selChildWorkspaces">
													<c:out value="${workspaces.ampTeamId}" />
												</html:multibox></td>
												<td width="98%"><c:out value="${workspaces.name}" /></td>
											</tr>
										</c:if>
									</c:forEach>
									<tr>
										<td colspan="2" align="center">
										<table cellPadding=5>
											<tr>
												<td><input type="button" value="Add" class="dr-menu"
													onclick="return childWorkspacesAdded()"></td>
												<td><input type="reset" value="Clear" class="dr-menu"></td>
												<td><input type="button" value="Close" class="dr-menu"
													onclick="return closeWindow();"></td>
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
</digi:form>

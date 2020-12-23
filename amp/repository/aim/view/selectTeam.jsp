<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="aimLoginForm" />
<!-- BREADCRUMP START -->
	<!--<div class="breadcrump">
		<div class="centering">
			<div class="breadcrump_cont">
			</div>
		</div>
	</div>-->
<!-- BREADCRUMP END -->
<div class="content-dir">
<!-- SELECT TEAM -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td valign="top">
    <div class="welcome_title">
		<digi:trn key="aim:selectTheTeam">Select the workspace you want to use for this session</digi:trn>
	</div>
	<div class="workspace_choose">
					<c:forEach var="workspaceGroup" items="${aimLoginForm.workspaceGroups}">
						<table border=0 cellPadding=0 cellSpacing=0 width="100%" id="table_${workspaceGroup}" 
	                    style="display:none;margin:0px 0px 15px 0px;">
							<tr>
								<td>
									<div class="subtitle-blue" style="border-bottom:1px dotted #8ba7c2; padding:0px 0px 5px 0px;margin:0px 0px 10px 0px;width:1000px;"><digi:trn><c:out value="${workspaceGroup}"/></digi:trn></div>
							</td>
							</tr>
							
							<c:forEach var="members" items="${aimLoginForm.members}">
								<c:set var="nonEmptyGroup">false</c:set>
								<c:if test="${members.ampTeam.workspaceGroup.id eq workspaceGroup.id}">
									<c:set var="nonEmptyGroup">true</c:set>
									<tr>
										<td class="wrklist">
											<div class="workspacelist">
												<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
												<c:set target="${urlParams}" property="id">
													<c:out value="${members.ampTeamMemId}"/>
												</c:set>
												<digi:link href="/selectTeam.do" name="urlParams">
													<c:out value="${members.ampTeam.name}"/>
												</digi:link>
											</div>
										</td>
									</tr>
								</c:if>
								<c:if test="${nonEmptyGroup}">
									<script language="javascript">
										document.getElementById("table_${workspaceGroup}").style.display = "block";
									</script>
								</c:if>
							</c:forEach>
						</table>
					</c:forEach>

					<c:set var="nonEmptyGroup">false</c:set>
					<table border=0 cellPadding=0 cellSpacing=0 width="100%" id="table_other" style="display:none;margin:0px 0px 15px 0px;">
						<tr>
							<td>
								<div class="subtitle-blue" style="border-bottom:1px dotted #8ba7c2; padding:0px 0px 5px 0px;margin:0px 0px 10px 0px;width:1000px;"><digi:trn key="aim:other">Other</digi:trn></div>
							</td>
						</tr>
						<c:forEach var="members" items="${aimLoginForm.members}">
							<c:if test="${empty members.ampTeam.workspaceGroup}">
							<c:set var="nonEmptyGroup">true</c:set>
							<tr>
								<td class="wrklist"><div class="workspacelist">
									
									<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams2}" property="id">
										<c:out value="${members.ampTeamMemId}"/>
									</c:set>								
									<digi:link href="/selectTeam.do" name="urlParams2">
									<c:out value="${members.ampTeam.name}"/></digi:link></div>
							</td></tr>
							</c:if>
						</c:forEach>
						<c:if test="${nonEmptyGroup}">
							<script language="javascript">
								document.getElementById("table_other").style.display = "block";
							</script>
						</c:if>
					</table>
	</div>
	</td>
  </tr>
</table>
<!-- SELECT TEAM END -->
</div>

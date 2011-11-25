<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="aimLoginForm" />

<table width="100%" valign="top" align="left" cellpadding=0 cellSpacing=0 border=0>
<tr><td width="100%" valign="top" align="left">
<table bgColor=#ffffff border=0 cellPadding=0 cellSpacing=0 width=772 height="201">
	<tr>
		<td width=40>&nbsp;
		</td>
		<td align=left vAlign=top width=600><br>
			<table border=0 cellPadding=3 cellSpacing=3 width="100%">
				<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="page-title">
					<digi:trn key="aim:selectTheTeam">
					Select the team you want to use in this current session</digi:trn></span>
				</td></tr>			
				<tr><td>
					<c:forEach var="workspaceGroup" items="${aimLoginForm.workspaceGroups}">
					<table border=0 cellPadding=3 cellSpacing=0 width="100%" id="table_${workspaceGroup}" style="display:none;">
						<tr>
							<td>
								<div class="subtitle-blue"><c:out value="${workspaceGroup}"/></div>
						</td>
						</tr>
						<c:forEach var="members" items="${aimLoginForm.members}">
							<c:set var="nonEmptyGroup">false</c:set>
							<c:if test="${members.ampTeam.workspaceGroup.id eq workspaceGroup.id}">
								<c:set var="nonEmptyGroup">true</c:set>
								<tr>
									<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<IMG height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
										<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
										<c:set target="${urlParams}" property="id">
											<c:out value="${members.ampTeamMemId}"/>
										</c:set>								
										<digi:link href="/selectTeam.do" name="urlParams">
										<c:out value="${members.ampTeam.name}"/></digi:link>
								</td></tr>
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
					<table border=0 cellPadding=3 cellSpacing=0 width="100%" id="table_other" style="display:none;">
						<tr>
							<td>
								<div class="subtitle-blue"><digi:trn key="aim:other">Other</digi:trn></div>
						</td>
						</tr>
						<c:forEach var="members" items="${aimLoginForm.members}">
							<c:if test="${empty members.ampTeam.workspaceGroup}">
							<c:set var="nonEmptyGroup">true</c:set>
							<tr>
								<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<IMG height=10 src="../ampTemplate/images/arrow-014E86.gif" width=15>
									<jsp:useBean id="urlParams2" type="java.util.Map" class="java.util.HashMap"/>
									<c:set target="${urlParams2}" property="id">
										<c:out value="${members.ampTeamMemId}"/>
									</c:set>								
									<digi:link href="/selectTeam.do" name="urlParams2">
									<c:out value="${members.ampTeam.name}"/></digi:link>
							</td></tr>
							</c:if>
						</c:forEach>
					</table>
					<c:if test="${nonEmptyGroup}">
						<script language="javascript">
							document.getElementById("table_other").style.display = "block";
						</script>
					</c:if>
				</td></tr>			
			</table>
		</td>
	</tr>
</table>
</td></tr>
</table>

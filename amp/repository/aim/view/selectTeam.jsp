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
<!-- SELECT TEAM -->
<table width="1000" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td valign="top">
    <div class="welcome_title">
		<digi:trn key="aim:selectTheTeam">Select the team you want to use in this current session</digi:trn>
	</div>
	<div class="workspace_choose">
		<c:forEach var="members" items="${aimLoginForm.members}">
			<img src=img_2/ico_wrkspc.gif style="margin-right:7px;">
			<jsp:useBean id="urlParams" type="java.util.Map" class="java.util.HashMap"/>
				<c:set target="${urlParams}" property="id">
					<c:out value="${members.ampTeamMemId}"/>
				</c:set>								
				<digi:link href="/selectTeam.do" name="urlParams">
					<c:out value="${members.ampTeam.name}"/><br>
				</digi:link>
		</c:forEach>
		</div>
	</td>
  </tr>
</table>
<!-- SELECT TEAM END -->


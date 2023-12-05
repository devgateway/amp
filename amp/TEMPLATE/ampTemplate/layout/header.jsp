<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>

<logic:notEmpty name="currentMember" scope="session">
	<bean:define id="teamMember" name="currentMember" scope="session" type="org.digijava.module.aim.helper.TeamMember" />
</logic:notEmpty>
<logic:empty name="currentMember" scope="session">
	<logic:notEmpty name="currentUser" scope="session">
		<bean:define id="userLogged" name="currentUser" scope="session" type="org.digijava.kernel.user.User" />
	</logic:notEmpty>
</logic:empty>

<!-- HEADER START -->
<c:if test='${empty sessionScope.currentMember}'>
	<div class="login_nav" style="display:none;">
		<digi:insert attribute="loginWidget"/>
	</div>
</c:if>
<!-- HEADER END -->
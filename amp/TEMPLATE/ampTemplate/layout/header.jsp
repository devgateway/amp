<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>

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
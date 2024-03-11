<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

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
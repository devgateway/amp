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
<div class="header">
	<c:if test='${empty sessionScope.currentMember}'>
		<div class="login_nav">
				<digi:insert attribute="loginWidget"/>
		</div>
	</c:if>
	<div class="centering">
		<div class="logo">
					<c:set var="homeRedirectUrl" value="/"/>
					<c:if test="${!empty sessionScope.currentMember}">
						<c:set var="homeRedirectUrl" value="/index.do"/>
					</c:if>
					<a href="${homeRedirectUrl}"><img src="/aim/default/displayFlag.do" /></a>
					<div class="title">
						<span class="label"><a href="${homeRedirectUrl}"><digi:trn key="aim:aidManagementPlatform">Aid Management Platform (AMP)</digi:trn></a></span>
					</div>
		</div>
		
		<c:if test='${not empty sessionScope.currentMember}'>
			<div id="usr_menu_logged2">
				<a href="javascript:showUserProfile(${teamMember.memberId})">${teamMember.memberName}</a>			
				<a>${userLogged.name}</a>			
				<img src="/TEMPLATE/ampTemplate/img_2/top_sep.gif" class="top_sep">		
				
				<c:set var="translation">
	                <digi:trn>Workspace Name</digi:trn>
	              </c:set>
	              <span title="${translation}">                
	                	<strong style="color:#FFFFFF"><c:out value="${teamMember.teamName}"/></strong>	
	              </span>
			</div>
		</c:if>
	</div>
</div>
<!-- HEADER END -->
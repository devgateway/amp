<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="userAccountForm" />

<table border="0" cellpadding="2" cellspacing="0" align="center" width="500px">
<tr><td nowrap class="PageTitle"><digi:trn key="um:myGateway">My Gateway</digi:trn></td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:accountInformation">Account Information</digi:trn></td></tr>
<tr><td>
      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:firstName">First Name</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.firstName}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:lastName">Last Name</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.lastName}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:emailAddress">E-mail Address</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.email}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:mailingAddress">Mailing Address</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.mailingAddress}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:organizationName">Organization Name</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.organizationName}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:organizationType">Organization Type</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.organizationType}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:countryOfResidence">Country of Residence</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.countryOfResidence}"/></b></td>
      </tr>
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text"><digi:trn key="um:website">Website</digi:trn></td>
          <td align="left" nowrap class="text"><b><c:out value="${userAccountForm.url}"/></b></td>
      </tr>
      </table>
</td></tr>
<tr><td>
      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
       <tr><td><digi:link href="/showUserProfile.do" styleClass="text"><digi:trn key="um:updateMyAccountInfo">Update my account information</digi:trn></digi:link></td></tr>
       <tr><td><digi:link href="/showUpdateForm.do" styleClass="text"><digi:trn key="um:changeMyPassword">Change my password</digi:trn></digi:link></td></tr>
      </table>
</td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:myInterests">My Interests</digi:trn></td></tr>
<tr><td>
      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
      <tr bgcolor="#D5D5D5">
          <td align="left" nowrap class="title"><digi:trn key="um:focus">Focus</digi:trn></td>
          <td align="center" nowrap class="title"><digi:trn key="um:recieveNewsletter">Receive newsletter</digi:trn></td>
          <td align="center" nowrap class="title"><digi:trn key="um:contentAlerts">Content alerts</digi:trn></td>
      </tr>
    <!-- START ITERATE HERE -->
      <tr bgcolor="#F0F0F0">
          <td align="left" nowrap class="text">&nbsp;</td>
          <td align="left" nowrap class="text">&nbsp;</td>
          <td align="left" nowrap class="text">&nbsp;</td>
      </tr>
    <!-- END ITERATE HERE -->
      </table>
</td></tr>
<tr><td>
      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
       <tr><td align="left" nowrap class="text" bgcolor="#F0F0F0"><digi:trn key="um:recieveGatewayENewsletter">Receive the Gateway e-newsletter?:</digi:trn><b>
          <c:if test="${userAccountForm.receiveNewsletter}"><digi:trn key="um:yes">Yes</digi:trn></c:if>
          <c:if test="${! userAccountForm.receiveNewsletter}"><digi:trn key="um:no">No</digi:trn></c:if>
          </b>
       </td></tr>
       <tr><td><digi:link href="/showUserProfile.do" styleClass="text"><digi:trn key="um:editThisInfo">Edit this information</digi:trn></digi:link></td></tr>
      </table>
</td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:myUserProfile">My user profile</digi:trn></td></tr>
<tr><td align="left" nowrap class="text" bgcolor="#F0F0F0"><digi:trn key="um:displMyProfile">Display my profile:</digi:trn><b>
      <c:if test="${userAccountForm.displayMyProfile}"><digi:trn key="um:yes">Yes</digi:trn></c:if>
      <c:if test="${! userAccountForm.displayMyProfile}"><digi:trn key="um:no">No</digi:trn></c:if>
       </b>
</td></tr>
<tr><td><digi:link href="/showUserBio.do" styleClass="text"><digi:trn key="um:uploadChangeMyPicBio">Upload / change my picture and bio</digi:trn></digi:link></td></tr>
<tr><td><digi:link href="/viewProfileAction.do" styleClass="text"><digi:trn key="um:viewMyUserProfile">View my user profile</digi:trn></digi:link></td></tr>
<tr><td><digi:link href="/showUserUpdate.do" styleClass="text"><digi:trn key="um:editThisInfo">Edit this information</digi:trn></digi:link></td></tr>

<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:myLangPreferences">My language preferences</digi:trn></td></tr>
<tr><td align="left" nowrap class="text" bgcolor="#F0F0F0"><digi:trn key="um:defNavLanguage">Default Navigation language:</digi:trn>&nbsp;<b><c:out value="${userAccountForm.navigationLanguage.name}" /></b></td></tr>
<tr><td align="left" nowrap class="text" bgcolor="#F0F0F0"><digi:trn key="um:contentLanguage">Content languages:</digi:trn>&nbsp;
<c:forEach var="lang" items="${userAccountForm.contentLanguages}">
<b><c:out value="${lang.name}" /></b>&nbsp;
</c:forEach>
</td></tr>
<tr><td><digi:link href="/showUserProfile.do" styleClass="text"><digi:trn key="um:editMyLangSettings">Edit my language settings</digi:trn></digi:link></td></tr>

<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td><digi:link href="/unsubscribeAction.do" styleClass="text"><digi:trn key="um:unsubscrTempOrPerm">Unsubscribe temporarily or permanently</digi:trn></digi:link></td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
</table>
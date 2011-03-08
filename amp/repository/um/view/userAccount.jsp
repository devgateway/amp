<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="userAccountForm" />

<table border="0" cellpadding="2" cellspacing="0" align="center" width="500px">
<tr><td nowrap class="PageTitle"><digi:trn key="um:myAccount">My Account</digi:trn></td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:accountInfo">Account Information</digi:trn></td></tr>
<tr><td>
      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
      <tr bgcolor="#F0F0F0">
	  <td align="left" nowrap class="text"><c:out value="${userAccountForm.firstName}"/>&nbsp;
		<c:out value="${userAccountForm.lastName}"/>&nbsp;(<c:out value="${userAccountForm.email}"/>)</td>
      </tr>
      <tr bgcolor="#F0F0F0">
	  <td align="left" nowrap class="text">
		<c:out value="${userAccountForm.countryOfResidence}"/>
	  </td>
      </tr>
      <tr bgcolor="#F0F0F0">
	<td>
	      <table border="0" cellpadding="2" cellspacing="1" width="100%" >
	       <tr><td><digi:link href="/showUserUpdate.do" styleClass="text"><digi:trn key="um:updateMyAccountInfo">Update my account information</digi:trn></digi:link></td></tr>
	       <tr><td><digi:link href="/showUserChangePassword.do" styleClass="text"><digi:trn key="um:changeMyPassword">Change my password</digi:trn></digi:link></td></tr>
	       <tr><td><digi:link href="/showUserBio.do"  onclick="newWindow(this.href,'Portrait_and_Bio', 'width=600,height=430,status=no, menusbar=no, toolbar=no');return false;"><digi:trn key="change_my_pic_bio">Upload / change my picture and bio</digi:trn></digi:link></td></tr>
	      </table>
	</td>
      </tr>
      </table>
</td></tr>
<tr><td nowrap class="title"><digi:trn key="um:mySubscrNotices">My subscription and notices</digi:trn></td></tr>
<tr><td><img src="/images/trans.gif" height=15 width=1 border="0"></td></tr>
<tr><td nowrap class="title"><digi:trn key="um:myInterests">My Interests</digi:trn></td></tr>
</table>

<script language="javascript">
<!--

function newWindow(file,window,features) {
    msgWindow=open(file,window,features);
    if (msgWindow.opener == null) msgWindow.opener = self;
}
// -->
</script>
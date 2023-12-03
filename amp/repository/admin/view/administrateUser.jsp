<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="administrateUserForm" />

<digi:form action="/administrateUser.do">
<table border="0" cellpadding="0" cellspacing="0" width="100%" height="100%">
	<tr class="yellow">
		<td><digi:img src="ampModule/admin/images/yellowLeftTile.gif" border="0" width="20"/></td>
		<td width="100%">
			<font class="sectionTitle">
				<digi:trn key="admin:userAdministration">User Administration</digi:trn>!!!
			</font>
		</td>
	</tr>
	<tr>
		<td colspan="2" class="yellow" valign="top" align="left">

<digi:errors/>
  <table border="0" cellpadding="0" cellspacing="0" >
    <tr>
  	<td width="50%" bgcolor="#EEEEEE" align="center"><c:out value="${administrateUserForm.firstNames}" /> 
                                                     <c:out value="${administrateUserForm.lastName}" /> 
        </td>
          <td width="50%"  align="right"><digi:link href="/becomeUser.do" paramName="administrateUserForm"  paramId="selectedUserId" paramProperty="selectedUserId">&nbsp;<digi:trn key="admin:becomeUser">Become user</digi:trn></digi:link>     
        </td>
    </tr>
  </table>
  <br> 
<HR>
  <table border="0" class="border" width="30%">
    <tr>
      <td width="10%" align="right"><b><digi:trn key="admin:password">Password:</digi:trn> </b></td>
      <td width="20%"><html:password name="administrateUserForm" property="newPassword" /></td>
    </tr>
    <tr>
      <td width="10%" align="right"><b><digi:trn key="admin:confirm">Confirm:</digi:trn> </b></td>
      <td width="20%"><html:password name="administrateUserForm" property="confirmnewPassword" /></td>
    </tr>
    <tr><td colspan="2">&nbsp;</td></tr>
<digi:secure globalAdmin="true">

      <tr><td width="100%" colspan="2"><html:checkbox name="administrateUserForm" property="globalAdmin"><digi:trn key="admin:makeUserGlobalAdmin">Make user Global Admin</digi:trn></html:checkbox></td></tr>
      <tr><td width="100%" colspan="2"><html:checkbox name="administrateUserForm" property="ban"><digi:trn key="admin:banUser">Ban a user</digi:trn></html:checkbox></td></tr>
      <tr><td width="100%" colspan="2"><html:checkbox name="administrateUserForm" property="emailVerified"><digi:trn key="admin:userEmailVerified">Email verified</digi:trn></html:checkbox></td></tr>
</digi:secure>
  </table>
  <br>
  <table>
    <tr><td align="left"><html:submit value="Submit" /></td></tr>    
  </table>
</digi:form>
		</td>
	</tr>
</table>
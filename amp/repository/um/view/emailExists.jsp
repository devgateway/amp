<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<digi:instance property="userRegisterForm" />

<table border="0" cellpadding="0" cellspacing="0" width="100%">
  <tr>
    <td width="100%">
       <font size="large"><b><digi:trn key="um:unableToCreateNewUser">It was not possible to create a new user</digi:trn></b></font>
    </td>
  </tr>
  <tr>
    <td width="100%"><hr></td>
  </tr>
  <tr>
    <td width="100%">
      <digi:trn key="um:emailAddressAlreadyExists">This email address already exists in our system:</digi:trn><b><c:out value="${userRegisterForm.email}" /></b>
    <br>
    - <digi:trn key="um:ifYouForgetYourPassword">if you have forgotten your password</digi:trn>
    <digi:link href="/showEmailForm.do"><digi:trn key="um:clickHere">click here</digi:trn></digi:link>. <br>
    - <digi:trn key="um:pleaseGo">Please go</digi:trn> <a href="javascript:history.go(-1)"><digi:trn key="um:back">back</digi:trn></a> <digi:trn key="um:correctEmail">to correct the email address.</digi:trn>
    </td>
  </tr>
  <tr>
    <td width="100%"><hr></td>
  </tr>
  <tr>
    <td width="100%">&nbsp;</td>
  </tr>
</table>
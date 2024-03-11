<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<digi:instance property="administrateUserForm" />

<digi:errors/>

<table>
  <tr>
    <td noWrap><digi:trn key="admin:userInfoUpdatedFor">User information updated for:</digi:trn> 
     <b><c:out value="${administrateUserForm.firstNames}"/>      
        <c:out value="${administrateUserForm.lastName}"/></b>
    </td>
  <tr>
  <tr>
    <td noWrap><digi:link href="/searchUser.do"><digi:trn key="admin:backToSearchUser">Back to Search User</digi:trn></digi:link></td>
  </tr>
</table>
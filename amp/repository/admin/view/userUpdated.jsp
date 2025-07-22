<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
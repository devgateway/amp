<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>

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
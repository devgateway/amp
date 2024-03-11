<%@ page language="java" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c" %>

<digi:instance property="userContactForm" />

<table>
  <tr>
     <td><b><digi:trn key="um:messageSentSuccessfully">Message successfully sent to:</digi:trn></b></td>
  </tr>
  <tr bgcolor="#F0F0F0">
      <td>
        <c:out value="${userContactForm.recipientName}"/>
      </td>
  </tr> 
</table>
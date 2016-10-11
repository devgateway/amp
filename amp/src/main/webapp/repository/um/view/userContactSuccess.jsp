<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

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
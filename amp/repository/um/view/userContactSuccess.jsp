<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
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
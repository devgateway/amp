<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<digi:secure actions="ADMIN">
  <table border="2" cellpadding="5" bordercolor="#B22222" bgcolor="FFD700" style="border-collapse: collapse;">
    <tr>
      <td>
        <a href='<digi:site property="url"/>/admin/'>
            <digi:trn key="admin:adminLink">Admin</digi:trn>
        </a>
      </td>
    </tr>
  </table>
</digi:secure>
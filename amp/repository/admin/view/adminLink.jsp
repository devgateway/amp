<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
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
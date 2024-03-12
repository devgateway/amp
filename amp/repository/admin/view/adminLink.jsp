<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
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
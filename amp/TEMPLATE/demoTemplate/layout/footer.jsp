<%@ page language="java" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
<digi:base />
	
<digi:secure actions="ADMIN">		
<table width="98%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center">
          <table border="0" cellpadding="1" cellspacing="0">
            <tr>
              <td bgColor="firebrick">
                <table border="0" cellpadding="3" cellspacing="1">
                  <tr align="center" bgcolor="Gold">

                      <td>
						<a href='<digi:site property="url"/>/admin/'>Admin</a>
                      </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
</digi:secure>
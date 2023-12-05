<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
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
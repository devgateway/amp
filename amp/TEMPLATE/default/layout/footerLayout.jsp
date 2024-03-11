<%@ page language="java" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

<digi:secure actions="ADMIN, TRANSLATE">
	<div align="center">
	<table border="0" cellpadding="5" cellspacing="0">
      <tr>
      <td nowrap valign="top">
			<digi:insert attribute="adminLink"/>
		</td>
      </tr>
    </table>
	</div>
</digi:secure>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center">
			<digi:insert attribute="flatLangSwitch" >
			   <tiles:put name="redirectToRoot" value="True" />
			</digi:insert>
		</td>
      </tr>
</table>

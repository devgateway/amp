<%@ page language="java" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

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

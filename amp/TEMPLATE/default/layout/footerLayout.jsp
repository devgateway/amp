<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
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

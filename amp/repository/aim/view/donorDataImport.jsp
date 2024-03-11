<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>


<digi:context name="digiContext" property="context" />
<digi:errors/>
<digi:instance property="aimDonorDataImportForm"/>
<digi:context name="digiContext" property="context"/>

<digi:form action="/donorDataImport.do" method="post">
<TABLE width="80%" height="200" cellpadding="0" cellspacing="0" valign="top" align="left">
<TR><TD width="100%" >
<table width="80%" align="center">
  <tr>
    <td width="100%">
      <h2 align="center">Data Import</h2></td>
  </tr>
  <tr>
    <td width="100%">
      <table width="100%">
        <tr>
          <td width="100%"><html:radio name="aimDonorDataImportForm" property="type" value="C" />Comma
            Separated File (CSV)</td>
        </tr>
        <tr>
          <td width="100%"><html:radio name="aimDonorDataImportForm" property="type" value="M" />Manual
            Entry</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td width="100%"><html:text name="aimDonorDataImportForm" property="fileName"/><html:submit value="Browse" /></td>
  </tr>
  <tr>
    <td width="100%">
      <table width="100%">
        <tr>
          <td width="50%" align="right"><html:reset value="Cancel" /></td>
          <td width="50%" align="left">
		<c:set var="translation">
			<digi:trn key="aim:clickToGoToNext">next</digi:trn>
		</c:set>
		  <html:button property="aimDonorDataImportForm" title="${translation}" value="Next &gt;&gt;" /></td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</TD></TR>
<TR><TD width="100%" valign="top" align="left">
</TD></TR>
</TABLE>

</digi:form>




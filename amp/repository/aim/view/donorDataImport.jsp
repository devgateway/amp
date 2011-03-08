<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


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




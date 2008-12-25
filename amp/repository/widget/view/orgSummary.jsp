<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

   

<table id="orgTable" border="1" width="100%">
    <th colspan="2" class="tableHeaderCls">Summary</th>
    <tr>
        <td width="30%">Organization Name:</td><td>${organization.name}&nbsp;</td>
    </tr>
      <tr>
        <td width="30%">Organization Acronym:</td><td>${organization.acronym}&nbsp;</td>
    </tr>
      <tr>
        <td width="30%">Donor Group:</td><td>${organization.orgGrpId.orgGrpName}&nbsp;</td>
    </tr>
      <tr>
        <td width="30%">Web Link:</td><td>${organization.orgUrl}&nbsp;</td>
    </tr>
     <tr>
        <td width="30%">Contact Name:</td><td>${organization.contactPersonName}&nbsp;</td>
    </tr>
     <tr>
        <td width="30%">Contact Phone:</td><td>${organization.phone}&nbsp;</td>
    </tr>
     <tr>
        <td width="30%">Contact Email:</td><td>${organization.email}&nbsp;</td>
    </tr>
	
</table>
 
<script language="javascript">
setStripsTable("orgTable", "tableEven", "tableOdd");
setHoveredTable("orgTable");
</script>

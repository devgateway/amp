<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>
<%@ taglib uri="/taglib/globalsettings" prefix="gs" %>

    <div class="yui-content" style="height:auto;font-size:11px;font-family:Verdana,Arial,Helvetica,sans-serif;">

<table>
    <th colspan="2">Summary</th>
    <tr>
        <td>Organization Name:</td><td>${organization.name}</td>
    </tr>
      <tr>
        <td>Organization Acronym:</td><td>${organization.acronym}</td>
    </tr>
      <tr>
        <td>Donor Group:</td><td>${organization.orgGrpId.orgGrpName}</td>
    </tr>
      <tr>
        <td>Web Link:</td><td>${organization.orgUrl}</td>
    </tr>
     <tr>
        <td>Contact Name:</td><td>${organization.contactPersonName}</td>
    </tr>
     <tr>
        <td>Contact Phone:</td><td>${organization.phone}</td>
    </tr>
     <tr>
        <td>Contact Email:</td><td>${organization.email}</td>
    </tr>
	
</table>
    </div>
 


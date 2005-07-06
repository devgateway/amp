<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html:errors/>
<digi:form action="/createOrganisationList.do" method="post">
<html:hidden name="aimAmpActivityForm" property="ampRoleId" value="3" />
<table width="100%" align="center">
<tr><td><h2 align=center><digi:trn key="aim:addReportingAgency">Add Reporting Agency</digi:trn></h2></td></tr>
<tr><td>
<table width=80% align=center>
<tr><td colspan=2 height="21"><digi:trn key="aim:reportingAgency">Reporting Agency : </digi:trn></td></tr><tr>
<td colspan=2><html:select name="aimAmpActivityForm" property="ampOrgId" >
			<html:optionsCollection name="aimAmpActivityForm" property="reportingagency" value="ampOrgId" label="name" /> 
			</html:select>
			</td></tr></td></tr>


<tr>
	<td align="center" height="21"> <html:reset> Reset </html:reset> </td>
	<td align="center" height="21"> <html:submit> Save </html:submit> </td> 
</tr>
</table>
</digi:form>

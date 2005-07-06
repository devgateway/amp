<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html:errors/>
<digi:form action="/createSubSectorList.do" method="post">
<table width="100%" align="center">
<tr><td><h2 align=center><digi:trn key="aim:addSubSector">Add Sub Sector</digi:trn></h2></td></tr>
<tr><td>
<table width=80% align=center>
<tr><td colspan=2 height="21"><digi:trn key="aim:subSector">Sub Sector : </digi:trn></td></tr><tr>
<td colspan=2> <html:select name="aimAmpActivityForm" property="ampSectorId" >
			<html:optionsCollection name="aimAmpActivityForm" property="subSectors" value="ampSectorId" label="name" />

			</html:select></td></tr>


<tr>
	<td align="center" height="21"> <html:reset> Reset </html:reset> </td>
	<td align="center" height="21"> <html:submit> Save </html:submit> </td> 
</tr>
</table>
</digi:form>

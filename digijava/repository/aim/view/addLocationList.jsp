<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>

<html:errors/>
<digi:form action="/createLocationList.do" method="post">
<table width="100%" align="center">
<tr><td><h2 align=center><digi:trn key="aim:addLocation">Add Location</digi:trn></h2></td></tr>
<tr><td>
<table width=80% align=center>
<tr><td><digi:trn key="aim:region">Region : </digi:trn></td>
<td > <html:select name="aimAmpActivityForm" property="ampLocationId" >
			<html:optionsCollection name="aimAmpActivityForm" property="regions" value="ampLocationId" label="region" />

			</html:select></td></tr>
<tr><td><digi:trn key="aim:country">Country : </digi:trn></td>
<td > <html:select name="aimAmpActivityForm" property="ampLocationId" >
			<html:optionsCollection name="aimAmpActivityForm" property="countries" value="ampLocationId" label="country" />

			</html:select></td></tr>


<tr>
	<td align="center" height="21"> <html:reset> Reset </html:reset> </td>
	<td align="center" height="21"> <html:submit> Save </html:submit> </td> 
</tr>
</table>
</digi:form>

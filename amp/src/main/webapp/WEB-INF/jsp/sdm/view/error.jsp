
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	<digi:errors/>
	<digi:instance property="sdmForm"/>
	<table width="60%" border="0">
		<tr>
			<td>	Please resubmit your entry:
			</td>
		</tr>
		<tr>
			<td>	You must specify something for <b>Content Title</b>
			</td>
		</tr>
		<tr>
			<td>	Please click on the back button on your browser and resubmit your entry.
			</td>
		</tr>
	</table>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
	<digi:errors/>
	<digi:link href="/showDocumentsList.do"><font color="#1A8CFF"><small>
	<digi:trn key="sdm:viewAddDoc">View/Add Documents</digi:trn></small></font></digi:link><BR><BR>
	<table class="border" width="100%">
		<tr>
			<td>
				<digi:edit key="k"><small>some text</small></digi:edit>
				<digi:edit key="testi"><small>some text</small></digi:edit>
			</td>
		</tr>
	</table>
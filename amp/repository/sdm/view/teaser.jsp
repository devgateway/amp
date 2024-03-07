
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>
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
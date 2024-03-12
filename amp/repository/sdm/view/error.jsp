
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
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
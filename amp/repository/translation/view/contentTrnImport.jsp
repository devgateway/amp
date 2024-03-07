<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi" %>

<digi:instance property="contentTrnImportExportForm"/>
<h1 class="admintitle">
			<span class="subtitle-blue"><digi:trn key="gis:widgetPlaceMan:pageHeader">Content Translation Import</digi:trn></span>
</h1>

<table width="100%" border="0" cellpadding="0">
	<tr>
		<td>	
	
			<digi:form action="/contentTrnImportExport.do?action=importing" method="post" enctype="multipart/form-data">
				<html:file name="contentTrnImportExportForm" property="upFile"/> 
					<html:submit value="Import"/>
			</digi:form>
	
		</td>
	</tr>
</table>
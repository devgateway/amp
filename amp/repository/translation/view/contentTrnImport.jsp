<%@page pageEncoding="UTF-8"%>
<%@page contentType="text/html;charset=UTF-8"%>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>

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
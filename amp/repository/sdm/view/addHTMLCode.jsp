
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>

	<digi:errors/>
	<digi:form action="/saveHTMLCode.do" method="post">
	<div align="center">
		<table width="80%">
			<tr>
				<td noWrap align="center">
				    <i><b><font size="3" color="#00008B">
					<digi:trn key="sdm:sumbitHTMLtoDoc">Submit an &quot;HTML&quot; Code to be included in the Document!</digi:trn></font></b></i>
				</td>
			</tr>
			<tr>
				<td align="center">
					<html:textarea name="sdmForm" property="contentTitle" rows="15" cols="100"/>
				</td>
			</tr>
			<tr>
				<td>
					<html:submit value="Submit"/>
				</td>
			</tr>
		</table>
	</div></digi:form>
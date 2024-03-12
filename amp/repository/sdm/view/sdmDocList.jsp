
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/struts-html.tld" prefix="html" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/webapp/WEB-INF/tld/c.tld" prefix="c" %>

	<digi:instance property="sdmForm"/>
	<digi:errors/>
	<digi:secure actions="ADMIN">
	<table width="100%">
		<tr>
			<td noWrap align="left">
				<digi:link href="/showCreateDocument.do">
				<digi:trn key="sdm:admin">Admin</digi:trn></digi:link>
			</td>
		</tr>
	</table></digi:secure>
	<table width="100%">
	 <c:if test="${!empty sdmForm.documentsList}">
		<tr>
			<td noWrap><b><font color="#008000" size="3">
				<digi:trn key="sdm:docList">Document List!</digi:trn></font></b>
			</td>
		</tr>
		<c:forEach var="documentsList" items="${sdmForm.documentsList}">
		<tr>
			<td>
				<digi:link href="/previewDocument.do" paramName="documentsList" paramId="activeDocumentId" paramProperty="id">
				<c:out value="${documentsList.name}"/></digi:link>
			</td>
		</tr></c:forEach></c:if>
		<c:if test="${empty sdmForm.documentsList}">
		<tr>
			<td noWrap><b><font color="#008000" size="3">
				<digi:trn key="sdm:collectionEmpty">This Collection is Empty!</digi:trn></font></b>
			</td>
		</tr></c:if>
	</table>
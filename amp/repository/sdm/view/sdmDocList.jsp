
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %> 

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
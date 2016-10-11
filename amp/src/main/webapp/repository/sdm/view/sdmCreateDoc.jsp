
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %> 

	<digi:errors/>
	<digi:form action="/createDocument.do" method="post">
	<c:if test="${ empty sdmForm.documentsList}">
	<table width="100%">
		<tr>
			<td noWrap align="left"><font color="#000080" size="3"><b>
				<digi:trn key="sdm:noDocToUpdateCreateDoc">There is No document to update! Create A document first!</digi:trn></b>
      </font>
			</td>
		</tr>
		<tr>
			<td align="left">
				<html:text name="sdmForm" property="documentTitle" size="100"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<html:submit value="Create New Document"/>
			</td>
		</tr>
	</table></c:if>
	<c:if test="${! empty sdmForm.documentsList}">
	<table width="100%">
		<tr>
			<td noWrap align="left"><font color="#008000" size="3"><b>
				<digi:trn key="sdm:giveDocTitle">To Create a New Document Give a Document Title:</digi:trn></b>
      </font>
			</td>
		</tr>
		<tr>
			<td align="left">
				<html:text name="sdmForm" property="documentTitle" size="100"/>
			</td>
		</tr>
		<tr>
			<td align="left">
				<html:submit value="Create New Document"/>
			</td>
		</tr>
	</table>
	<table width="100%">
		<tr>
			<td noWrap align="left"><font color="#000080" size="3"><b>
				<digi:trn key="sdm:chooseDocBelow">To Update a Document Choose Below to update:</digi:trn></b>
      </font>
			</td>
		</tr>
		<c:forEach var="documentsList" items="${sdmForm.documentsList}">
		<tr>
			<td>
				<digi:link href="/showEditDocument.do" paramName="documentsList" paramId="activeDocumentId" paramProperty="id">
				<c:out value="${documentsList.name}"/></digi:link>
			</td>
		</tr></c:forEach>
	</table></c:if></digi:form>
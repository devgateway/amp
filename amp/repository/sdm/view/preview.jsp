
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %> 

	<digi:errors/>
	<digi:instance property="sdmForm"/>
	<table width="100%">
		<tr>
			<td align="center"><font color="#000080" size="4">
				<c:out value="${sdmForm.documentTitle}"/></font>
			</td>
		</tr>
		<tr>
			<td>	&nbsp;
			</td>
		</tr>
		<c:if test="${ !empty sdmForm.documentItemsList}"><!-- iterate throught items -->
		<table border="0" cellspacing="1" width="100%">
			<c:forEach var="docId" items="${sdmForm.documentItemsList}">
			<c:if test="${docId.text}"><!-- text item -->
			<tr>
				<td width="88%" height="19">
 				    <c:out value="${docId.contentTitle}" escapeXml="false" />
				</td>
			</tr></c:if>
			<c:if test="${docId.htmlCode}"><!-- html Code item -->
			<tr>
				<td width="88%" height="19">
 				    <c:out value="${docId.contentTitle}" escapeXml="false" />
				</td>
			</tr></c:if>
			<c:if test="${docId.link}"><!-- link item -->
			<tr>
				<td width="88%" height="19">
					<a href='<c:out value="${docId.content}"/>' class="text">
					<c:out value="${docId.contentTitle}" escapeXml="false" /></a>
				</td>
			</tr></c:if>
			<c:if test="${docId.picture}"><!-- image -->
			<tr>
				<td width="88%" height="19">
					<c:out value="${docId.contentTitle}" escapeXml="false" />				
					<p align='<c:out value="${docId.alignment}" />'>
					<digi:context name="showImage" property="context/ampModule/moduleinstance/showImage.do" />
					   <img src='<%= showImage%>?activeParagraphOrder=<c:out value="${docId.paragraphOrder}" />&documentId=<c:out value="${docId.id}" />'/>
				   </p>
				</td>
			</tr></c:if>
			<c:if test="${docId.file}"><!-- file -->
			<tr>
				<td width="88%" height="19">
					<p align='<c:out value="${docId.alignment}" />'>
					<digi:context name="showFile" property="context/ampModule/moduleinstance/showFile.do"/>
					  <a href='<%= showFile%>?activeParagraphOrder=<c:out value="${docId.paragraphOrder}" />&documentId=<c:out value="${docId.id}" />'>
					<c:out value="${docId.contentTitle}" escapeXml="false" /></a></p>
				</td>
			</tr></c:if></c:forEach></c:if>
		</table><BR><BR>
		<c:if test="${sdmForm.goToEdit}">
		<div align="center">
			<table width="30%">
				<tr>
					<td>
						<HR>
					</td>
				</tr>
				<tr>
					<td align="center">
						<digi:link href="/showEditDocument.do"><font size="1">
						<digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
					</td>
				</tr>
			</table>
		</div></c:if>
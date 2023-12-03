
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
		<c:if test="${empty sdmForm.documentItemsList}">
		<tr>
			<td noWrap><font size="2">
				<digi:trn key="sdm:doEmtyUseBelowToCreateDoc">The document is Empty! Use below to create content!</digi:trn></font>
			</td>
		</tr>
		<tr>
			<td>	&nbsp;
			</td>
		</tr></c:if>
		<c:if test="${! empty sdmForm.documentItemsList}"><!-- iterate throught items -->
		<table>
			<tr>
				<td noWrap><font color="#808000" size="2">
					<digi:link href="/previewDocument.do">
					<digi:trn key="sdm:previewDoc">Preview the Document!</digi:trn></digi:link></font>
				</td>
			</tr>
		</table>
		<table border="1" cellspacing="1" width="100%">
		 <c:forEach items="${sdmForm.documentItemsList}" var="docId">
			<c:if test="${docId.text}"><!-- text item -->
			<tr>
				<td width="88%" height="19">
					<c:out value="${docId.contentTitle}" escapeXml="false" />				
				</td>
				<td noWrap width="12%" height="19">
					<c:if test="${docId.moveUp}">
					  <digi:link href="/moveUpItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveUp">Move Up</digi:trn></font></digi:link><BR></c:if>
					<c:if test="${docId.moveDown}">
					  <digi:link href="/moveDownItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveDown">Move Down</digi:trn></font></digi:link><BR></c:if>
					  <digi:link href="/deleteItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:delete">Delete</digi:trn></font></digi:link><BR>
					  <digi:link href="/showEditText.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
				</td>
			</tr></c:if>
			<c:if test="${docId.htmlCode}"><!-- html Code item -->
			<tr>
				<td width="88%" height="19">
					<c:out value="${docId.contentTitle}" escapeXml="false" />				
				</td>
				<td noWrap width="12%" height="19">
					<c:if test="${docId.moveUp}">
					  <digi:link href="/moveUpItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveUp">Move Up</digi:trn></font></digi:link><BR></c:if>
					<c:if test="${docId.moveDown}">
					  <digi:link href="/moveDownItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveDown">Move Down</digi:trn></font></digi:link><BR></c:if>
					  <digi:link href="/deleteItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:delete">Delete</digi:trn></font></digi:link><BR>
					  <digi:link href="/showEditHTMLCode.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
				</td>
			</tr></c:if>
			<c:if test="${docId.link}"><!-- link item -->
			<tr>
				<td width="88%" height="19">
					<a href='<c:out value="${docId.content}"/>' class="text">				
					    <c:out value="${docId.contentTitle}" escapeXml="false" />				
					</a>	
				</td>
				<td noWrap width="12%" height="19">
					<c:if test="${docId.moveUp}">
					  <digi:link href="/moveUpItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveUp">Move Up</digi:trn></font></digi:link><BR></c:if>
					<c:if test="${docId.moveDown}">
					  <digi:link href="/moveDownItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveDown">Move Down</digi:trn></font></digi:link><BR></c:if>
					  <digi:link href="/deleteItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:delete">Delete</digi:trn></font></digi:link><BR>
					  <digi:link href="/showEditLink.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
				</td>
			</tr></c:if>
			<c:if test="${docId.picture}"><!-- image -->
			<tr>
				<td width="88%" height="19">
					<c:out value="${docId.contentTitle}" escapeXml="false" />				
					<p align='<c:out value="${docId.alignment}" />'>
					<digi:context name="showImage" property="context/ampModule/moduleinstance/showImage.do"/>
				    <img src='<%= showImage%>?activeParagraphOrder=<c:out value="${docId.paragraphOrder}" />&documentId=<c:out value="${docId.id}" />'/>
					</p>
				</td>
				<td noWrap width="12%" height="19">
					<c:if test="${docId.moveUp}">
					  <digi:link href="/moveUpItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveUp">Move Up</digi:trn></font></digi:link><BR></c:if>
					<c:if test="${docId.moveDown}">  
  					  <digi:link href="/moveDownItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveDown">Move Down</digi:trn></font></digi:link><BR></c:if>
					  <digi:link href="/deleteItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:delete">Delete</digi:trn></font></digi:link><BR>
					  <digi:link href="/showEditPicture.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
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
				<td noWrap width="12%" height="19">
					<c:if test="${docId.moveUp}">
					  <digi:link href="/moveUpItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveUp">Move Up</digi:trn></font</digi:link><BR></c:if>
					<c:if test="${docId.moveDown}">					  
					  <digi:link href="/moveDownItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:moveDown">Move Down</digi:trn></font></digi:link><BR></c:if>
					  <digi:link href="/deleteItem.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:delete">Delete</digi:trn></font></digi:link><BR>
					  <digi:link href="/showEditFile.do" paramName="docId" paramId="activeParagraphOrder" paramProperty="paragraphOrder"><font size="2">
					  <digi:trn key="sdm:edit">Edit</digi:trn></font></digi:link>
				</td>
			</tr></c:if></c:forEach></c:if>
		</table><BR>
		<table>
			<tr>
				<td noWrap><b><font color="#000080" size="2">
					<digi:trn key="sdm:toAddContentSelectBelow">To add new content select below!</digi:trn></font></b>
				</td>
			</tr>
			<tr>
				<td noWrap>
					<digi:link href="/showEditText.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:createText">Create text</digi:trn></font></digi:link><BR>
					<digi:link href="/showEditFile.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:addFile">Add a File</digi:trn></font></digi:link><BR>
					<digi:link href="/showEditLink.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:createHyperLink">Create Hyper Link</digi:trn></font></digi:link><BR>
					<digi:link href="/showEditPicture.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:addPicture">Add Picture</digi:trn></font></digi:link><BR>
					<digi:link href="/showEditHTMLCode.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:addHTML">Add HTML Code</digi:trn></font></digi:link>
				</td>
			</tr>
			<tr>
				<td>
					<HR>
					<HR>
				</td>
			</tr>
			<tr>
				<td noWrap><font color="#FF0000" size="4">
					<digi:trn key="sdm:toDeleteDocUserThis">To Delete the Entire Document Use This!</digi:trn></font>
				</td>
			</tr>
			<tr>
				<td noWrap>
					<digi:link href="/deleteDocument.do"><font color="#0099CC" size="2">
					<digi:trn key="sdm:deleteDoc">Delete the Entire Document</digi:trn></font></digi:link>
				</td>
			</tr>
		</table>
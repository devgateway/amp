 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://digijava.org/digi" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>

<c:set var="translationKey">cr:deleteDocumentMsg:${DeleteDocumentResponseCode}</c:set>
<c:choose>
	<c:when test="${DeleteDocumentResponseCode=='ok'}">
		<div id='successfullDiv'>
			<digi:trn key="${translationKey}">
				<bean:write name="DeleteDocumentResponse"/>
			</digi:trn>
		</div>
	</c:when>
	<c:when test="${DeleteDocumentResponseCode=='errDocInUse'}">
		
		<font color="red">
			<digi:trn key="${translationKey}">
			This document cannot be deleted as it is still used by
			</digi:trn>:
			<br />
			
			<logic:iterate id="msgEntry" name="DeleteDocumentResponse">
				<digi:trn key="${translationKey}:${msgEntry.key}">
					<bean:write name="msgEntry" property="key"/>
				</digi:trn>: 
				<bean:write name="msgEntry" property="value" />
			</logic:iterate>
		</font>
	</c:when>
	<c:otherwise>
		<font color="red">
			<digi:trn key="${translationKey}">
				<bean:write name="DeleteDocumentResponse"/>
			</digi:trn>
		</font>
	</c:otherwise>
</c:choose>
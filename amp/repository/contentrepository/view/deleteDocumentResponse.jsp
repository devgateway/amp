 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
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
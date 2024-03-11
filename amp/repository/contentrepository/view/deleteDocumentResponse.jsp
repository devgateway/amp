 <%@ page pageEncoding="UTF-8"%> 
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
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
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://digijava.org" prefix="digi"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://digijava.org/fields" prefix="field"%>

<bean:define name="largeTextFeature" id="largeTextFeature"
	scope="request" />
<bean:define name="largeTextLabel" id="largeTextLabel" scope="request" />
<bean:define name="largeTextKey" id="largeTextKey" scope="request" />

<c:if test="${largeTextKey!=null}">
<TR>
	<TD bgcolor="#ffffff"><field:display feature="${largeTextFeature}"
		name="${largeTextLabel}">
		<i><digi:trn>${largeTextLabel}</digi:trn></i>:
		<digi:edit key="${largeTextKey}"/>
	</field:display></TD>
</TR>
</c:if>
<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field"%>

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
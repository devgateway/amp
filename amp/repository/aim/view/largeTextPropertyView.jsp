<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module"%>

<bean:define name="largeTextFeature" id="largeTextFeature"
	scope="request" />
<bean:define name="largeTextLabel" id="largeTextLabel" scope="request" />
<bean:define name="largeTextKey" id="largeTextKey" scope="request"/>
<bean:define id="boldText" toScope="request" value="true"/>
<bean:define id="useColonInLabel" toScope="request" value="true"/>


<module:display name="${moduleName}" parentModule="${parentModule}">
	<digi:trn key="aim:${largeTextLabel}">${largeTextLabel}</digi:trn><c:if test="${useColonInLabel}">:&nbsp;</c:if>
	<c:if test="${largeTextKey!=null}">
		<c:if test="${boldText}">
			<b><digi:edit key="${largeTextKey}"></digi:edit></b>
		</c:if>
		<c:if test="${!boldText}">
			<digi:edit key="${largeTextKey}"></digi:edit>
		</c:if>
	</c:if>
	<hr />
</module:display>

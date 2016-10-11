<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module"%>

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

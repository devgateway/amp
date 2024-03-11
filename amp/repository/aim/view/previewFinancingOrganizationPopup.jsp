<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/resources/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/resources/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/resources/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/resources/tld/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi"%>

<bean:define id="org" name="breakdown"
	type="org.digijava.module.aim.helper.FinancingBreakdown" scope="request" toScope="page" />

<div style='position:relative;display:none;' id='org-<bean:write name="org" property="organisation.ampOrgId"/>'>
<ul>
<c:if test="${!empty org.organisation.name}">
	<li><digi:trn key="aim:organizationName">Organization Name</digi:trn>: <bean:write name="org" property="organisation.name"/></li>
</c:if>
<c:if test="${!empty org.organisation.acronym}">
	<li><digi:trn key="aim:organizationAcronym">Organization Acronym</digi:trn>: <bean:write name="org" property="organisation.acronym"/></li>
</c:if>
<c:if test="${!empty org.organisation.orgGrpId}">
	<c:if test="${!empty org.organisation.orgGrpId.orgGrpName}">
		<li><digi:trn key="aim:organizationGroup">Organization Group</digi:trn>: <bean:write name="org" property="organisation.orgGrpId.orgGrpName"/></li>
		<li><digi:trn key="aim:organizationType">Organization Type</digi:trn>: <bean:write name="org" property="organisation.orgGrpId.orgType.orgType"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.organisation.orgCode}">
	<li><digi:trn key="aim:organizationCode">Organization Code</digi:trn>: <bean:write name="org" property="organisation.orgCode"/></li>
</c:if>

</ul>
</div>
<div align="left" onMouseOver="stm(['<bean:write name="org" property="organisation.orgCode"/> Details',document.getElementById('org-<bean:write name="org" property="organisation.ampOrgId"/>').innerHTML],Style[0])" onMouseOut="htm()"><u><bean:write name="org" property="organisation.name"/></u>
</div>

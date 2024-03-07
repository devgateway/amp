<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/src/main/webapp/WEB-INF/digijava.tld" prefix="digi"%>
<bean:define id="ampFeatures" name="selectedFeatures"
	type="java.util.Collection" scope="request" toScope="page" />

<logic:iterate name="aimFeatureManagerForm" property="templates" id="template"
		type="org.digijava.module.aim.dbentity.FeatureTemplates">
	
<div style='position:relative;display:none;' id='org-<bean:write name="org" property="organisation.ampOrgId"/>'> 
<ul>
<c:if test="${!empty org.organisation.name}">
	<li>Organization Name: <bean:write name="org" property="organisation.name"/></li>
</c:if>
<c:if test="${!empty org.organisation.acronym}">
	<li>Organization Acronym: <bean:write name="org" property="organisation.acronym"/></li>
</c:if>
<c:if test="${!empty org.organisation.orgTypeId}">
	<c:if test="${!empty org.organisation.orgTypeId.orgType}">
		<li>Organization Type: <bean:write name="org" property="organisation.orgTypeId.orgType"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.organisation.orgGrpId}">
	<c:if test="${!empty org.organisation.orgGrpId.orgGrpName}">
		<li>Organization Group: <bean:write name="org" property="organisation.orgGrpId.orgGrpName"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.organisation.orgCode}">
	<li>Organization Code: <bean:write name="org" property="organisation.orgCode"/></li>
</c:if>

</ul>
</div>
<ul>
<li>
<div align="left" onMouseOver="stm(['<bean:write name="org" property="organisation.orgCode"/> Details',document.getElementById('org-<bean:write name="org" property="organisation.ampOrgId"/>').innerHTML],Style[0])" onMouseOut="htm()">[<u><bean:write name="org" property="organisation.name"/></u>]
</div>
</li>
</ul>
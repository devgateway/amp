<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<bean:define id="org" name="currentOrg"
	type="org.digijava.module.aim.helper.RelOrganization" scope="request"
	toScope="page" />
<div style='position:relative;display:none;' id='org-<bean:write name="org" property="orgCode"/>'> 
<ul>
<c:if test="${!empty org.orgName}">
	<li> Organization Name: <bean:write name="org" property="orgName"/></li>
</c:if>
<c:if test="${!empty org.acronym}">
	<li>Organization Acronym: <bean:write name="org" property="acronym"/></li>
</c:if>
<c:if test="${!empty org.orgTypeId}">
	<c:if test="${!empty org.orgTypeId.orgType}">
		<li>Organization Type: <bean:write name="org" property="orgTypeId.orgType"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.orgGrpId}">
	<c:if test="${!empty org.orgGrpId.orgGrpName}">
		<li>Organization Group: <bean:write name="org" property="orgGrpId.orgGrpName"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.orgCode}">
<li>Organization Code: <bean:write name="org" property="orgCode"/></li>
</c:if>
<c:if test="${!empty org.role}">
<li>Role: <bean:write name="org" property="role"/></li>
</c:if>
</ul>
</div>
<ul>
<li>
<div align="left" width="2" onMouseOver="stm(['<bean:write name="org" property="orgCode"/> Details',document.getElementById('org-<bean:write name="org" property="orgCode"/>').innerHTML],Style[0])" onMouseOut="htm()">[<u><bean:write name="org" property="orgName"/></u>]
</div>
</li>
</ul>
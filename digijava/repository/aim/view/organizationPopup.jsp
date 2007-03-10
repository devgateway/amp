<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<bean:define id="org" name="currentOrg"
	type="org.digijava.module.aim.helper.RelOrganization" scope="request"
	toScope="page" />
<div style='position:relative;display:none;' id='org-<bean:write name="org" property="orgName"/>'> 
<ul>
<li>Organization Name: <bean:write name="org" property="orgName"/></li>
<li>Organization Acronym: <bean:write name="org" property="acronym"/></li>
<li>Organization Type: <bean:write name="org" property="orgTypeId.orgType"/></li>
<li>Organization Group: <bean:write name="org" property="orgGrpId.orgGrpName"/></li>
<li>Organization Code: <bean:write name="org" property="orgCode"/></li>
<li>Role: <bean:write name="org" property="role"/></li>

</ul>
</div>
<ul>
<li>
<div align="left" onMouseOver="stm(['<bean:write name="org" property="orgName"/> Details',document.getElementById('org-<bean:write name="org" property="orgName"/>').innerHTML],Style[2])" onMouseOut="htm()">[<u><bean:write name="org" property="orgName"/></u>]
</div>
</li>
</ul>
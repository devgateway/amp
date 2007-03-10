<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>

<bean:define id="org" name="selectedOrgForPopup"
	type="org.digijava.module.aim.helper.OrgProjectId" scope="request" toScope="page" />

<div style='position:relative;display:none;' id='org-<bean:write name="org" property="organisation.name"/>'> 
<ul>
<li>Organization Name: <bean:write name="org" property="organisation.name"/></li>
<li>Organization Acronym: <bean:write name="org" property="organisation.acronym"/></li>
<li>Organization Type: <bean:write name="org" property="organisation.orgTypeId.orgType"/></li>
<li>Organization Group: <bean:write name="org" property="organisation.orgGrpId.orgGrpName"/></li>
<li>Organization Code: <bean:write name="org" property="organisation.orgCode"/></li>


</ul>
</div>
<ul>
<li>
<div align="left" onMouseOver="stm(['<bean:write name="org" property="organisation.name"/> Details',document.getElementById('org-<bean:write name="org" property="organisation.name"/>').innerHTML],Style[2])" onMouseOut="htm()">[<u><bean:write name="org" property="organisation.name"/></u>]
</div>
</li>
</ul>
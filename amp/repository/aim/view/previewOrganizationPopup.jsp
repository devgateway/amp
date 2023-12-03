<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>





<style>
  label {
    display: inline-block;
    width: 5em;
    font-size:10px;
  }
  .new_tooltip, .ui-tooltip{
  padding: 0px;
  
  font-size: 12px;
  border: none;
  }
  .tooltip_header{
  color:white;
  display:block;
  width: 100%;
  background:#6b7a82;
  }
  
  .tooltip_list {
  padding-right: 10px;
  
  }
  
  .tooltip_wrapper{
  	border-width: 2px;
  	border-style: solid;
  	border-color: #6b7a82;
  }
  </style>

<bean:define id="org" name="selectedOrgForPopup"
	type="org.digijava.ampModule.aim.helper.OrgProjectId" scope="request" toScope="page" />

<c:if test="${!empty org.organisation.ampOrgId}">
<div style='position:relative;display:none;' id='org-<bean:write name="org" property="organisation.ampOrgId"/>'> 
<ul>

<c:if test="${!empty org.organisation.name}">
	<li>Organization Name: <bean:write name="org" property="organisation.name"/></li>
</c:if>
<c:if test="${!empty org.organisation.acronym}">
	<li>Organization Acronym: <bean:write name="org" property="organisation.acronym"/></li>
</c:if>
<c:if test="${!empty org.organisation.orgGrpId}">
	<c:if test="${!empty org.organisation.orgGrpId.orgGrpName}">
		<li>Organization Group: <bean:write name="org" property="organisation.orgGrpId.orgGrpName"/></li>
		<li>Organization Type: <bean:write name="org" property="organisation.orgGrpId.orgType.orgType"/></li>
	</c:if>
</c:if>
<c:if test="${!empty org.organisation.orgCode}">
	<li>Organization Code: <bean:write name="org" property="organisation.orgCode"/></li>
</c:if>

</ul>
</div>
<ul>
<li>

<script>
  $(function() {
    $( ".nice_tooltip" ).tooltip({
    	track: true,
    	show: false,
    	tooltipClass: "new_tooltip",
        content: function() {
        	var element = '#org' +"<bean:write name="org" property="organisation.ampOrgId"/>";
        	
        	var header = "<span class=\"tooltip_header\"><bean:write name="org" property="organisation.orgCode"/> Details </span>";
        	var list = "<div class=\"tooltip_list\">" + $('#org-<bean:write name="org" property="organisation.ampOrgId"/>').html() + "</div>";
        	
        	return "<div class=\"tooltip_wrapper\">" + header + list + "</div>";
        }
    });
  });
  </script>


<div align="left" class="nice_tooltip" title=""  ">
[<u>
	<bean:write name="org" property="organisation.name"/>  
	<span class="word_break"><bean:write name="org" property="projectId"/></span>
</u>]

</div>
</li>
</ul>
</c:if>
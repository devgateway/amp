<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org/digi" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category" %>

<ul>
	<logic:iterate scope="request" name="unorgLocCollection" id="loc" type="org.digijava.module.aim.dbentity.AmpCategoryValueLocations">
		<bean:define id="locCV" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue" name="loc" property="parentCategoryValue" />
	
		<li id="lid-${loc.id}" class="dhtmlgoodies_sheet.gif" >
			<a  class="atree" id="aid-${loc.id}"><c:out value="${loc.name}"/></a> 
			<span style="display: none;">${locCV.index}</span> 
			<span class="spantree">[<c:out value="${locCV.value }"/>]</span>
			
			<img src="/TEMPLATE/ampTemplate/images/application_edit.png" style="height: 13px; cursor: pointer;" 
					onclick="editLocation(${loc.id})" />
			<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" style="height: 10px; cursor: pointer;" />
			<span class="spantree" style="display: none; color: red;">ERROR</span>
			
		</li>
	</logic:iterate>
</ul>
	
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/src/main/resources/tld/digijava.tld" prefix="digi" %>
<%@ taglib uri="/src/main/resources/tld/c.tld" prefix="c" %>

<%@ taglib uri="/src/main/resources/tld/fieldVisibility.tld" prefix="field" %>
<%@ taglib uri="/src/main/resources/tld/featureVisibility.tld" prefix="feature" %>
<%@ taglib uri="/src/main/resources/tld/moduleVisibility.tld" prefix="module" %>
<%@ taglib uri="/src/main/resources/tld/fn.tld" prefix="fn" %>
<%@ taglib uri="/src/main/resources/tld/category.tld" prefix="category" %>

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
	
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<ul>
	<logic:iterate scope="request" name="unorgLocCollection" id="loc" type="org.digijava.ampModule.aim.dbentity.AmpCategoryValueLocations">
		<bean:define id="locCV" type="org.digijava.ampModule.categorymanager.dbentity.AmpCategoryValue" name="loc" property="parentCategoryValue" />
	
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
	
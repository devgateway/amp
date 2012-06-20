<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<%@ taglib uri="/taglib/fieldVisibility" prefix="field" %>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature" %>
<%@ taglib uri="/taglib/moduleVisibility" prefix="module" %>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn" %>
<%@ taglib uri="/taglib/category" prefix="category" %>

<logic:notEmpty scope="request" name="locCollection">
	<ul>
		<logic:iterate scope="request" name="locCollection" id="loc" type="org.digijava.module.aim.dbentity.AmpCategoryValueLocations">
			<bean:define id="locCV" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue" name="loc" property="parentCategoryValue" />
		
			<bean:define id="liClassString" toScope="page" value=""/>
			<logic:empty name="loc" property="childLocations">
				<bean:define id="liClassString" toScope="page" value="class='dhtmlgoodies_sheet.gif'"/>
			</logic:empty>
			<li id="lid-${loc.id}" ${liClassString} >
				<a  class="atree" id="aid-${loc.id}"><c:out value="${loc.name}"/></a> 
				<span style="display: none;">${locCV.index}</span> 
				<span class="spantree">[<digi:trn><c:out value="${locCV.value }"/></digi:trn>]</span>
				<c:if test="${locCV.index < myForm.numOfLayers - 1}">
					<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 13px; cursor: pointer;" 
						title="Add ${locCV.ampCategoryClass.possibleValues[locCV.index+1].value}" 
						onclick="addLocation(${loc.id}, ${locCV.ampCategoryClass.possibleValues[locCV.index+1].id})" />
				</c:if>
				<img src="/TEMPLATE/ampTemplate/images/application_edit.png" style="height: 13px; cursor: pointer;" 
						onclick="editLocation(${loc.id})" title="Edit" />
				<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" style="height: 10px; cursor: pointer;" 
						onclick="deleteLocation(${loc.id})" title="Delete"/>
				<span class="l_sm"><c:if test="${not empty loc.gsLat}"><digi:trn>Lat</digi:trn>:${loc.gsLat}&nbsp;</c:if><c:if test="${not empty loc.gsLong}"><digi:trn>Long</digi:trn>:${loc.gsLong}&nbsp;</c:if><c:if test="${not empty loc.geoCode}"><digi:trn>GeoId</digi:trn>:${loc.geoCode}&nbsp;</c:if></span>
				<span class="spantree" style="display: none; color: red;"><digi:trn>ERROR</digi:trn></span>
				<logic:notEmpty name="loc" property="childLocations">
					<bean:define id="tempLocCollection" toScope="request" name="locCollection" scope="request"/>
					<bean:define id="locCollection" toScope="request" name="loc" property="childLocations" />
					<jsp:include page="listLocations.jsp" />
					<bean:define id="locCollection" toScope="request" name="tempLocCollection" scope="request"/>
				</logic:notEmpty>
			</li>
		</logic:iterate>
	</ul>
</logic:notEmpty>
	
<%@ page pageEncoding="UTF-8" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://digijava.org" prefix="digi" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://digijava.org/fields" prefix="field" %>
<%@ taglib uri="http://digijava.org/features" prefix="feature" %>
<%@ taglib uri="http://digijava.org/modules" prefix="module" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://digijava.org/CategoryManager" prefix="category" %>

<logic:notEmpty scope="request" name="locCollection">
	<ul>
		<c:set var="lvlIndex" scope="request" value="${lvlIndex+1}" />
		<logic:iterate scope="request" name="locCollection" id="loc" type="org.digijava.module.aim.dbentity.AmpCategoryValueLocations">
			<c:if test="${loc.deleted != true}"> 
				<bean:define id="locCV" type="org.digijava.module.categorymanager.dbentity.AmpCategoryValue" name="loc" property="parentCategoryValue" />
			
				<bean:define id="liClassString" toScope="page" value=""/>
				<logic:empty name="loc" property="childLocations">
					<bean:define id="liClassString" toScope="page" value="class='dhtmlgoodies_sheet.gif'"/>
				</logic:empty>
				<li id="lid-${loc.id}" ${liClassString} >
					<a class="atree" id="aid-${loc.id}"><c:out value="${loc.name}"/></a> 
					<span style="display: none;">${locCV.index}</span> 
					<span class="spantree">[<digi:trn><c:out value="${locCV.value }"/></digi:trn>]</span>
					<c:if test="${lvlIndex < fn:length(locationLevels)}">
						<img src="/TEMPLATE/ampTemplate/images/green_plus.png" style="height: 13px; cursor: pointer;" 
							title="Add <digi:trn><c:out value="${locationLevels[lvlIndex].value}"/></digi:trn>" 
							onclick="addLocation(${loc.id}, ${locationLevels[lvlIndex].id})" />
					</c:if>
					<img src="/TEMPLATE/ampTemplate/images/application_edit.png" style="height: 13px; cursor: pointer;" 
							onclick="editLocation(${loc.id})" title="Edit" />
					<img src="/TEMPLATE/ampTemplate/images/deleteIcon.gif" style="height: 10px; cursor: pointer;" 
							onclick="deleteLocation(${loc.id})" title="Delete"/>
					<c:if test="${not empty loc.gsLat||not empty loc.gsLong||not empty loc.geoCode}"><span class="geostyle"><digi:trn>Lat</digi:trn>:${loc.gsLat}&nbsp;<digi:trn>Long</digi:trn>:${loc.gsLong}&nbsp;<digi:trn>GeoId</digi:trn>:${loc.geoCode}&nbsp;</span></c:if>
					<span class="spantree" style="display: none; color: red;"><digi:trn>ERROR</digi:trn></span>
					<logic:notEmpty name="loc" property="childLocations">
						<bean:define id="tempLocCollection" toScope="request" name="locCollection" scope="request"/>
						<bean:define id="locCollection" toScope="request" name="loc" property="childLocations" />
						<jsp:include page="listLocations.jsp" />
						<bean:define id="locCollection" toScope="request" name="tempLocCollection" scope="request"/>
						<c:set var="lvlIndex" scope="request" value="${lvlIndex-1}" />
					</logic:notEmpty>
				</li>
			</c:if>
		</logic:iterate>
	</ul>
</logic:notEmpty>

	
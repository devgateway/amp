<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="gisWidgetPlacesForm" />
<digi:form action="/widgetplaces.do">
<h1 class="admintitle">
			<span class="subtitle-blue"><digi:trn key="gis:widgetPlaceMan:pageHeader">Widget Place Manager</digi:trn></span>
</h1>
<table width="60%" border="0" cellpadding="15">
	<!--<tr>
		<td>
			<span class="crumb">
              <c:set var="translation">
                <digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
              </c:set>
              <html:link  href="/aim/admin.do" styleClass="comment" title="${translation}" >
                <digi:trn key="aim:AmpAdminHome">Admin Home</digi:trn>
              </html:link>&nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:WidgetPlaces">Widgets Places</digi:trn>
			</span>
		</td>
	</tr>-->

	<tr>
		<td>
		
			<table border="0" width="100%"  class="inside">
				<tr style="background-color:#c7d4db">
					<td nowrap="nowrap" class="inside">
						<strong><digi:trn key="gis:widgetPlacesTable:lastRenderTime">Last render time</digi:trn></strong>
					</td>
					<td nowrap="nowrap" class="inside">
						<strong><digi:trn key="gis:widgetPlacesTable:placeName">Place Name (parameter for teaser)</digi:trn></strong>
					</td>
					<td nowrap="nowrap" class="inside">
						<strong><digi:trn key="gis:widgetPlacesTable:assignedWidgetName">Assigned widget name</digi:trn></strong>
					</td>
					<td nowrap="nowrap" class="inside">
						<strong><digi:trn key="gis:widgetPlacesTable:assignedWidgetType">Assigned widget type</digi:trn></strong>
					</td>
					<td nowrap="nowrap" class="inside">
						<strong><digi:trn key="gis:widgetPlacesTable:operations">Operation</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="pl" items="${gisWidgetPlacesForm.places}" varStatus="stat">
					<tr>
						<td nowrap="nowrap" class="inside">
							<bean:write name="pl" property="placeLastRenderTime" format="dd MMMM yyyy - hh:mm:ss"/>
						</td>
						<td nowrap="nowrap" class="inside">
							${pl.placeName}
						</td>
						<td class="inside">
						 	${pl.widgetName}
						</td>
						<td nowrap="nowrap" class="inside">
						 	${pl.widgetTypeName}
						</td>
						<td nowrap="nowrap" class="inside">
							<digi:link href="/widgetplaces.do~actType=assignWidgt~placeId=${pl.placeId}"><digi:trn key="gis:widgetPlacesTable:assignWidgetLink">Assign widget</digi:trn></digi:link>
							&nbsp;
							<digi:link href="/widgetplaces.do~actType=delete~placeId=${pl.placeId}" onclick="return confirm('Are you sure?')"><img border="0" src='<digi:file src="images/deleteIcon.gif"/>'></digi:link>
						</td>
					</tr>
				</c:forEach>
			</table>
		
		
		</td>
	</tr>	
	<tr>
		<td>
			<small>
				<digi:trn key="gis:widgetPlacesTable:note1">Please note that place objects may reappear after deletion if pages they exists on are rendered again.</digi:trn>
			</small>
		</td>
	</tr>
</table>

</digi:form>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<digi:instance property="gisWidgetPlacesForm" />
<digi:form action="/widgetplaces.do">

<table width="60%" border="0" cellpadding="15">
	<tr>
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
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:widgetPlaceMan:pageHeader">Widget Place Manager</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
		
			<table border="0" width="100%" align="center" style="font-family:verdana;font-size:11px;">
				<tr bgColor="#d7eafd">
					<td>
						<strong><digi:trn key="gis:widgetPlacesTable:placeName">Place Name (parameter for teaser)</digi:trn></strong>
					</td>
					<td>
						<strong><digi:trn key="gis:widgetPlacesTable:pagemodule">Page module</digi:trn></strong>
					</td>
					<td>
						<strong><digi:trn key="gis:widgetPlacesTable:lastRenderTime">Last render time</digi:trn></strong>
					</td>
					<td>
						<strong><digi:trn key="gis:widgetPlacesTable:operations">Operation</digi:trn></strong>
					</td>
				</tr>
				<c:forEach var="pl" items="${gisWidgetPlacesForm.places}" varStatus="stat">
					<tr>
						<td nowrap="nowrap">
							${pl.name}
						</td>
						<td nowrap="nowrap">
						 	${pl.module}
						</td>
						<td>
							<bean:write name="pl" property="lastRendered" format="dd MMMM yyyy - hh:mm:ss"/>
						</td>
						<td>
							<a href="#">Delete</a>
						</td>
					</tr>
				</c:forEach>
			</table>
		
		
		</td>
	</tr>	
</table>

</digi:form>
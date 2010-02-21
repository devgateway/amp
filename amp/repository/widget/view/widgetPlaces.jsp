<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<style>
.contentbox_border{
	border: 	1px solid #666666;
	width: 		850x;
	background-color: #f4f4f2;
}


.tableEven {
	background-color:#dbe5f1;
	font-size:8pt;
	padding:2px;
}

.tableOdd {
	background-color:#FFFFFF;
	font-size:8pt;!important
	padding:2px;
}
 
.Hovered {
	background-color:#a5bcf2;
}

</style>
<script language="javascript">
function setStripsTable(tableId, classOdd, classEven) {
	var tableElement = document.getElementById(tableId);
	rows = tableElement.getElementsByTagName('tr');
	for(var i = 0, n = rows.length; i < n; ++i) {
		if(i%2 == 0)
			rows[i].className = classEven;
		else
			rows[i].className = classOdd;
	}
	rows = null;
}
function setHoveredTable(tableId, hasHeaders) {

	var tableElement = document.getElementById(tableId);
	if(tableElement){
    var className = 'Hovered',
        pattern   = new RegExp('(^|\\s+)' + className + '(\\s+|$)'),
        rows      = tableElement.getElementsByTagName('tr');

		for(var i = 0, n = rows.length; i < n; ++i) {
			rows[i].onmouseover = function() {
				this.className += ' ' + className;
			};
			rows[i].onmouseout = function() {
				this.className = this.className.replace(pattern, ' ');

			};
		}
		rows = null;
	}
	


}
</script>
<digi:instance property="gisWidgetPlacesForm" />
<digi:form action="/widgetplaces.do">

<table width="72%" border="0" cellpadding="15">
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
			<div class="contentbox_border" style="padding: 20px 1px 20px 1px;">
				<table width="100%" cellspacing="2" cellpadding="20" bgcolor="#f4f4f2" align="center" border="0">
				 <tr>
					<td>
						<table id="dataTable" width="98%" cellspacing="0" cellpadding="3" valign="top" border="0" >
							<tr>
								<!-- 
								<td nowrap="nowrap">
									<strong><digi:trn key="gis:widgetPlacesTable:lastRenderTime">Last render time</digi:trn></strong>
								</td>
								 -->
								<td nowrap="nowrap" bgcolor="#999999" style="color:black">
									<strong><digi:trn key="gis:widgetPlacesTable:placeName">Place Name (parameter for teaser)</digi:trn></strong>
								</td>
								<td nowrap="nowrap" style="color: black;" bgcolor="#999999">
									<strong><digi:trn key="gis:widgetPlacesTable:assignedWidgetName">Assigned widget name</digi:trn></strong>
								</td>
								<td nowrap="nowrap" style="color: black;" bgcolor="#999999">
									<strong><digi:trn key="gis:widgetPlacesTable:assignedWidgetType">Assigned widget type</digi:trn></strong>
								</td>
								<td nowrap="nowrap" style="color: black;" bgcolor="#999999">
									<strong><digi:trn key="gis:widgetPlacesTable:operations">Operation</digi:trn></strong>
								</td>
							</tr>
							<c:forEach var="pl" items="${gisWidgetPlacesForm.places}" varStatus="stat">
								<tr>
								<!-- 
									<td nowrap="nowrap">
										<bean:write name="pl" property="placeLastRenderTime" format="dd MMMM yyyy - hh:mm:ss"/>
									</td>
								 -->	
									<td nowrap="nowrap">
										${pl.placeName}
									</td>
									<td nowrap="nowrap">
									 	${pl.widgetName}
									</td>
									<td nowrap="nowrap">
									 	${pl.widgetTypeName}
									</td>
									<td nowrap="nowrap">
										<digi:link href="/widgetplaces.do~actType=assignWidgt~placeId=${pl.placeId}"><digi:trn key="gis:widgetPlacesTable:assignWidgetLink">Assign widget</digi:trn></digi:link>
										&nbsp;
										<digi:link href="/widgetplaces.do~actType=delete~placeId=${pl.placeId}" onclick="return confirm('Are you sure?')"><digi:trn key="gis:widgetPlacesTable:unassignwidget">Unassign widget</digi:trn></digi:link>
									</td>
								</tr> 
							</c:forEach>
						</table>
					 </td>
				  </tr>
				</table>
			</div>
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
<script language="javascript">
setStripsTable("dataTable", "tableEven", "tableOdd");
setHoveredTable("dataTable", false);
</script>

</digi:form>
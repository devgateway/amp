<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>

<script type="text/javascript">
<!--
	function cancelEdit(myForm){
		<digi:context name="justSubmit" property="context/module/moduleinstance/widgetplaces.do" />
		myForm.action="<%=justSubmit%>";  
		myForm.submit();
	}
//-->
</script>

<digi:instance property="gisWidgetPlacesForm" />
<digi:form action="/widgetplaces.do~actType=save">

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
                <digi:trn key="admin:Navigation:widgetPlaces">Widget Places</digi:trn>
                &nbsp;&gt;&nbsp;
                <digi:trn key="admin:Navigation:widgetPlacesEdit">Assign widget to place</digi:trn>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<span class="subtitle-blue"><digi:trn key="gis:assignWidgetToPlace:pageHeader">Assign widget to place</digi:trn></span>
		</td>
	</tr>
	<tr>
		<td>
			<html:hidden name="gisWidgetPlacesForm" property="placeId"/>
			<table>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:assignWidgetToPlace:placeName">Place Name</digi:trn>
						</strong>
					</td>
					<td>
						${gisWidgetPlacesForm.place.placeName}
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:assignWidgetToPlace:placeCode">Place Code</digi:trn>
						</strong>
					</td>
					<td>
						${gisWidgetPlacesForm.place.placeCode}
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:assignWidgetToPlace:lastRendered">Last Rendered on</digi:trn>
						</strong>
					</td>
					<td>
						<bean:write name="gisWidgetPlacesForm" property="place.placeLastRenderTime" format="dd:MMMM:yyyy - mm:hh:ss"/>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td nowrap="nowrap" align="right">
						<strong>
							<digi:trn key="gis:assignWidgetToPlace:assignedWidget">Assigned widget</digi:trn>
						</strong>
					</td>
					<td>
						<html:select name="gisWidgetPlacesForm" property="widgetId">
							<html:option value="-1">&nbsp;</html:option>
							<html:optionsCollection name="gisWidgetPlacesForm" property="widgets" label="widgetCombinedName" value="widgetId"/>
						</html:select>
					</td>
					<td>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td colspan="3">
						<hr>
					</td>
				</tr>
				<tr>
					<td align="right">
						<html:submit>
							<digi:trn key="gis:editIndicatorChartWidget:btnSave">Save</digi:trn>
						</html:submit>
					</td>
					<td colspan="2">
						<input type="button" value="Cancel" onclick="cancelEdit(this.form)">
					</td>
				</tr>
			</table>

		</td>
	</tr>
</table>
</digi:form>
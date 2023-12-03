<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="/taglib/struts-bean" prefix="bean"%>
<%@ taglib uri="/taglib/struts-logic" prefix="logic"%>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles"%>
<%@ taglib uri="/taglib/struts-html" prefix="html"%>
<%@ taglib uri="/taglib/digijava" prefix="digi"%>
<%@ taglib uri="/taglib/jstl-core" prefix="c"%>
<%@ taglib uri="/taglib/fieldVisibility" prefix="field"%>
<%@ taglib uri="/taglib/featureVisibility" prefix="feature"%>
<%@ taglib uri="/taglib/moduleVisibility" prefix="ampModule"%>
<%@ taglib uri="/taglib/jstl-functions" prefix="fn"%>
<style>
.contentbox_border {
	border: 1px solid #666666;
	width: 100%;
	background-color: #f4f4f2;
	padding: 20 0 20 0;
}
</style>
<style>
.link {
	text-decoration: none;
	font-size: 8pt;
	font-family: Tahoma;
}
</style>

<style>
.tableEven {
	background-color: #dbe5f1;
	font-size: 8pt;
	padding: 2px;
}

.tableOdd {
	background-color: #FFFFFF;
	font-size: 8pt; !
	important padding: 2px;
}

.Hovered {
	background-color: #a5bcf2;
}
</style>
<script language="javascript">
	function setHoveredTable(tableId, hasHeaders) {

		var tableElement = document.getElementById(tableId);
		if (tableElement) {
			var className = 'Hovered', pattern = new RegExp('(^|\\s+)'
					+ className + '(\\s+|$)'), rows = tableElement
					.getElementsByTagName('tr');

			for ( var i = 0, n = rows.length; i < n; ++i) {
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
	function setStripsTable(tableId, classOdd, classEven) {
		var tableElement = document.getElementById(tableId);
		if (tableElement) {
			rows = tableElement.getElementsByTagName('tr');
			for ( var i = 0, n = rows.length; i < n; ++i) {
				if (i % 2 == 0)
					rows[i].className = classEven;
				else
					rows[i].className = classOdd;
			}
			rows = null;
		}
	}
</script>
<digi:instance property="mapsconfigurationform" />
<h1 class="admintitle">
	<digi:trn>Map configuration</digi:trn>
</h1>
<table bgColor=#ffffff cellpadding="0" cellspacing="0" width=772>
	<tr>
		<td class=r-dotted-lg width=14>&nbsp;</td>
		<td align=left class=r-dotted-lg valign="top" width=750>
			<table cellPadding=5 cellspacing="0" width="100%">
				<tr>
					<!-- Start Navigation -->
					<td height=33><span class=crumb> <c:set
								var="translation">
								<digi:trn key="aim:clickToViewAdmin">Click here to goto Admin Home</digi:trn>
							</c:set> <digi:link href="/admin.do" styleClass="comment"
								title="${translation}" contextPath="/aim">
								<digi:trn key="aim:AmpAdminHome">
						Admin Home
						</digi:trn>
							</digi:link>&nbsp;&gt;&nbsp; <digi:trn>Map Configuration</digi:trn>
					</td>
					<!-- End navigation -->
				</tr>
				<table width="100%" height="100%" border="0" align="center"
					cellpadding="0" cellspacing="0" id="dataTable">
					<tr>
						<td colspan="6" align="center" style="font-weight: bold;"><digi:trn>Current Configuration</digi:trn>
						</td>
					</tr>
					<tr>
						<td style="font-weight: bold;"><digi:trn>Type</digi:trn></td>
						<td style="font-weight: bold;"><digi:trn>Map Url</digi:trn></td style="font-weight: bold;">
						<td style="font-weight: bold;"><digi:trn>Admin 1</digi:trn></td>
						<td style="font-weight: bold;"><digi:trn>Admin 2</digi:trn></td>
						<td style="font-weight: bold;"><digi:trn>GeoId</digi:trn></td>
						<td style="font-weight: bold;"><digi:trn>Action</digi:trn></td>
					</tr>
					<tr>
						<td><digi:trn>Base Map</digi:trn></td>
						<td><c:out value="${mapsconfigurationform.basemap}" /></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>Main Map</digi:trn></td>
						<td>${mapsconfigurationform.mainmap}</td>
						<td><c:out value="${mapsconfigurationform.admin1}" /></td>
						<td><c:out value="${mapsconfigurationform.admin2}" /></td>
						<td><c:out value="${mapsconfigurationform.geoid}" /></td>
					</tr>
					<tr>
						<td><digi:trn>Geometry Service</digi:trn></td>
						<td>${mapsconfigurationform.geometry}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>ESRI API</digi:trn></td>
						<td>${mapsconfigurationform.api}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>Base maps gallery</digi:trn></td>
						<td>${mapsconfigurationform.basemapsroot}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>Geo Locator Service</digi:trn></td>
						<td>${mapsconfigurationform.geolocator}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>National Border</digi:trn></td>
						<td>${mapsconfigurationform.national}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>Poverty Indicator</digi:trn></td>
						<td>${mapsconfigurationform.poverty}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
					<tr>
						<td><digi:trn>population Indicator</digi:trn></td>
						<td>${mapsconfigurationform.population}</td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</table>
				<br>
				<digi:form action="/MapsConfiguration.do?action=save" method="post">
					<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td><digi:trn>Map Type</digi:trn>
							</td>
							<td><digi:trn>Map Url</digi:trn>
							</td>
							<td><digi:trn>Admin 1</digi:trn>
							</td>
							<td><digi:trn>Admin 2</digi:trn>
							</td>
							<td><digi:trn>Geoid Field</digi:trn>
							</td>
							<td></td>
						</tr>
						<tr>
							<td><html:select property="selectedtype">
									<html:option value="1">
										<digi:trn>Base Map</digi:trn>
									</html:option>
									<html:option value="2">
										<digi:trn>Main Map</digi:trn>
									</html:option>
									<html:option value="4">
										<digi:trn>Geometry Service</digi:trn>
									</html:option>
									<html:option value="5">
										<digi:trn>API Url</digi:trn>
									</html:option>
									<html:option value="7">
										<digi:trn>Geo Locator</digi:trn>
									</html:option>
									<html:option value="8">
										<digi:trn>Base Maps Gallery</digi:trn>
									</html:option>
									<html:option value="9">
										<digi:trn>National Border</digi:trn>
									</html:option>
									<html:option value="10">
										<digi:trn>Indicator Poverty</digi:trn>
									</html:option>
									<html:option value="11">
										<digi:trn>Indicator Population</digi:trn>
									</html:option>
								</html:select></td>
							<td align="left"><html:text property="selectedurl" size="90" />
							</td>
							<td align="left"><html:text property="selectedadmin1"
									size="10" /></td>
							<td align="left"><html:text property="selectedadmin2"
									size="10" /></td>
							<td align="left"><html:text property="selectedgeoid"
									size="10" /></td>
							<td><html:submit styleClass="buttonx"><digi:trn>Save</digi:trn></html:submit>
							</td>
						</tr>
					</table>
				</digi:form>
				<br>
				<hr>
				<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td align="center" colspan="2" style="font-weight: bold;padding: 10px;">
							<digi:trn>Indicators Legends</digi:trn> - <digi:trn>The expected format is JPG</digi:trn>
						</td>
					</tr>
					<tr>
						<td align="center" style="font-weight: bold;">
							<digi:trn>Poverty Indicator Legend</digi:trn>
						</td>
						<td align="center"  style="font-weight: bold;">
							<digi:trn>Population Indicator Legend</digi:trn>
						</td>
					</tr>
					<tr>
						<td style="padding: 10px" align="center">
							<img src="/TEMPLATE/ampTemplate/img_2/gis/legend-poverty.jpg">
						</td>
						<td style="padding: 10px" align="center">
							<img src="/TEMPLATE/ampTemplate/img_2/gis/population-legend.jpg">
						</td>
					</tr>
				</table>
				<digi:form action="/MapsConfiguration.do?action=savelegend" method="post" enctype="multipart/form-data">
					<table>
						<tr>
							<td align="center" style="font-weight: bold;">
								<digi:trn>Indicator Map Type</digi:trn>
							</td>
							<td align="center" style="font-weight: bold;">
								<digi:trn>File</digi:trn>
							</td>
							<td><digi:trn>Submit</digi:trn></td>
						</tr>
						<tr>
							<td align="right">
								<html:select property="selectedlegend"> 
									<html:option value="1">
										<digi:trn>Poverty</digi:trn>
									</html:option>
									<html:option value="2">
										<digi:trn>Population</digi:trn>
									</html:option>
								</html:select>
							</td>
							<td align="left"><html:file property="legend" /></td>
							<td align="center" colspan="2">
								<html:submit styleClass="buttonx"><digi:trn>Save</digi:trn></html:submit>
							</td>
						</tr>
					</table>
				</digi:form>
				<hr>
			</table>
		</td>
	</tr>
</table>

<script language="javascript">
	setStripsTable("dataTable", "tableEven", "tableOdd");
	setHoveredTable("dataTable", false);
</script>

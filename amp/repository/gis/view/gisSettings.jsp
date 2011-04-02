<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/struts-logic" prefix="logic" %>
<%@ taglib uri="/taglib/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/jstl-core" prefix="c" %>


<digi:instance property="gisSettingsForm"/>
	
	
<digi:form action="/showGisSettings.do?action=update">
	
	
	<table cellPadding=5 cellSpacing=0 width="500" border=0>
		<tr>
			<!-- Start Navigation -->
			<td height=33><span class=crumb>
				<c:set var="translation">
					<digi:trn>Click here to goto Admin Home</digi:trn>
				</c:set>
				<digi:link href="/admin.do" module="aim" styleClass="comment" title="${translation}" >
				<digi:trn>Admin Home</digi:trn>
				</digi:link>&nbsp;&gt;&nbsp;
				<digi:trn>Result Matrix/GIS Settings</digi:trn>
			</td>
			<!-- End navigation -->
		</tr>
		<tr>
			<td>
				<span class="subtitle-blue">Result Matrix/GIS settings</span>
			</td>
		</tr>
		<tr>
			<td>
	
				<%--
				<table>
					<tr>
						<td>
							
						</td>
						<td>
							<html:radio name="gisSettingsForm" property="gisSettings.sectorSchemeFilterMode" value="0"/>
						</td>
						<td>
							Display only primary and multisector classifications
						</td>
						<td>
							<html:radio name="gisSettingsForm" property="gisSettings.sectorSchemeFilterMode" value="1"/>
						</td>
					</table>
					--%>
					<fieldset>
						<legend>Sector classification settiongs</legend>
						<html:select name="gisSettingsForm" property="gisSettings.sectorSchemeFilterMode" style="width:500px;">
							<html:option value="0">Display sectors selected</html:option>
							<html:option value="1">Display only primary and multisector classifications</html:option>
						</html:select>
						<hr>
				
						<table width="100%" style="border-collapse:collapse;" border="1" borderColor="#ECF3FD">
							<tr>
								<td height="20" colspan="2" bgcolor="#d7eafd">
									<b>Sector classification name</b>
								</td>
							</tr>	
							<logic:iterate name="gisSettingsForm" property="secShcemes" id="secScheme">
								<tr>
									<td>
										<bean:write name="secScheme" property="secSchemeName"/>
									</td>
									<td width="20">
										<html:multibox name="gisSettingsForm" property="secSchemesSelected" value="${secScheme.ampSecSchemeId}"/>
									</td>
								</tr>	
							</logic:iterate>
						</table>
					</fieldset>
					<br>
					<fieldset>
						<legend>Project settiongs</legend>
						
						<table width="100%" style="border-collapse:collapse;" border="1" borderColor="#ECF3FD">
							<tr>
								<td height="20" colspan="2" bgcolor="#d7eafd">
									<b>Program name</b>
								</td>
							</tr>	
							<logic:iterate name="gisSettingsForm" property="topLevelPrograms" id="program">
								<tr>
									<td>
										<bean:write name="program" property="name"/>
									</td>
									<td width="20">
										<html:multibox name="gisSettingsForm" property="programsSelected" value="${program.ampThemeId}"/>
									</td>
								</tr>	
							</logic:iterate>
						</table>
					</fieldset>	
			</td>
		</tr>
		<tr>
			<td align="center">
				<html:submit styleClass="dr-menu" value="update"/>
			</td>	
		</tr>
	</table>
	
	
</digi:form>
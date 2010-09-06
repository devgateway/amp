<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>	

<digi:instance property="gisDashboardForm"/>
<style>
<!--
.toolbar{
	width: 350px;
	background: #addadd; 
	background-color: #addadd; 
	padding: 3px 3px 3px 3px; 
	position: relative; 
	top: 10px; 
	left: 10px;
	bottom: 100px;
		
}
.toolbartable{
	border-color: #FFFFFF;
	border-width: 2px;
	border-bottom-width: 2px; 
	border-right-width: 2px;"
	border-left-width: 2px;
	border-style: solid;
	height: 25px;
}
-->
</style>


	<table border="0" width="100%" align="left"  cellpadding="3"  cellspacing="5" bgcolor="#FFFFFF">
			<td>
					<table width="100%" border="0" align="left" bgcolor="#EDF5FF" >
						<tr>
							<td nowrap="nowrap" height="30px">
								<digi:trn>From Date:</digi:trn>
							</td>
							<td noWrap align=left valign="middle">
								<html:select name="gisDashboardForm" property="selectedFromYear" onchange="mapYearChanged()">
									<html:optionsCollection name="gisDashboardForm" property="yearsFrom" label="label" value="value"/>
								</html:select>
							</td>
							<td nowrap="nowrap">
								<digi:trn>To Date:</digi:trn>
							</td>
							<td noWrap align=left valign="middle">
								<html:select name="gisDashboardForm" property="selectedToYear" onchange="mapYearChanged()">
									<html:optionsCollection name="gisDashboardForm" property="yearsTo" label="label" value="value"/>
								</html:select>
							</td>
						</tr>
					</table>				
			</td>
		</tr>
	</table>

<br>

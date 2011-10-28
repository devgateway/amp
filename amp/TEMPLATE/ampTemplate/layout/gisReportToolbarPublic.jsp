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
							<td height="30px" colspan="4">
						        <img style="width: 16px; height: 16px; vertical-align: middle;" src="/TEMPLATE/ampTemplate/images/info.png" /> <digi:trn>Select the date range for the funding information on the map</digi:trn>
							</td>
						</tr>
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
			<td bgcolor="#EDF5FF">
				<table border="0" align="center" bgcolor="#EDF5FF">
						<tr>
							<td noWrap align=left valign="middle" style="cursor:pointer;" height="30px">
								<a target="_blank" onclick="exportPDF(); return false;">
									<digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/pdf.gif" border="0" alt='Export to PDF'/>
								</a>
							</td>

							<td noWrap align=left valign="middle">
                                <digi:link styleId="printWin" href="#" onclick="window.print(); return false;">
					            <digi:img width="17" height="20" hspace="2" vspace="2" src="module/aim/images/printer.gif" border="0" alt="Printer Friendly"/>
                                </digi:link>
							</td>
						</tr>
					</table>
			</td>
		</tr>
	</table>

<br>

<script language="javascript">
	function exportPDF() {
  
	var selectedFromYear = document.getElementsByName("selectedFromYear")[0].value;
	var selectedToYear = document.getElementsByName("selectedToYear")[0].value;


	if (showDevinfo) {
			var sectorId =  document.getElementById("sectorsMapCombo").value;
			var indicatorId = document.getElementById("indicatorsCombo").value;	
			var subgroupId = document.getElementById("indicatorSubgroupCombo").value;
			var timeInterval = document.getElementById("indicatorYearCombo").value;
		  
			openURLinWindow("/gis/pdfExport.do?mapMode=DevInfo&publicMode=true&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&sectorId=" + sectorId + "&indicatorId=" + indicatorId + "&subgroupId=" + subgroupId + "&indYear=" + timeInterval, 780, 500);
		} else {
			var sectorId =  document.getElementById("sectorsMapComboFin").value;
			var fundingType = document.getElementById("fundingType").value;	
			var donorId = document.getElementById("donorsCombo").value;
		  
			openURLinWindow("/gis/pdfExport.do?mapMode=FinInfo&publicMode=true&selectedFromYear=" + selectedFromYear+ "&selectedToYear=" + selectedToYear + "&sectorId=" + sectorId + "&fundingType=" + fundingType + "&donorId=" + donorId , 780, 500);

		}
  }
</script>
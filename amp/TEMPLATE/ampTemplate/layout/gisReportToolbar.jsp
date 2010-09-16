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


	<table border="0" align="left"  cellpadding="3"  cellspacing="5" bgcolor="#FFFFFF">
        <tr>
            <td colspan="3">&nbsp;</td>
        </tr>
		<tr>
			<td>
			
					<table border="0" align="left" bgcolor="#EDF5FF" >
						<tr>
							<td nowrap="nowrap" height="30px">
								<digi:trn>From Date:</digi:trn>
							</td>
							<td noWrap align=left valign="middle">
								<html:select name="gisDashboardForm" property="selectedFromYear" onchange="yearChanged();mapYearChanged()">
									<html:optionsCollection name="gisDashboardForm" property="yearsFrom" label="label" value="value"/>
								</html:select>
							</td>
							<td nowrap="nowrap">
								<digi:trn>To Date:</digi:trn>
							</td>
							<td noWrap align=left valign="middle">
								<html:select name="gisDashboardForm" property="selectedToYear" onchange="yearChanged();mapYearChanged()">
									<html:optionsCollection name="gisDashboardForm" property="yearsTo" label="label" value="value"/>
								</html:select>
							</td>
						</tr>
					</table>				
			
			</td>
            <td>
                &nbsp;
            </td>
            <td>
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
							<td noWrap align=left valign="middle">
								<digi:link href="#" target="_blank" onclick="javascript:window.close(); return false;">
									<digi:img src="module/aim/images/close.gif" border="0" alt="Close"/>
								</digi:link>
							</td>
						</tr>
					</table>
				
			</td>
		</tr>
	</table>

<br>

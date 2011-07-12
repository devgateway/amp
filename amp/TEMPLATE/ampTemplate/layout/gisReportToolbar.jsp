<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="/taglib/struts-bean" prefix="bean" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>
<%@ taglib uri="/taglib/struts-html" prefix="html" %>
<%@ taglib uri="/taglib/digijava" prefix="digi" %>	

<digi:instance property="gisDashboardForm"/>



<div class="gis_wht" style="height:35px; padding-top:7px; margin-right:4px;" ><div class="gis_cont">
	<div style="float:right;">
		<table border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<digi:img src="img_2/ico_pdf.gif" align="left" style="margin-right:5px;"/>
				</td>
				<td>
					<a class="l_sm" href="#" onclick="exportPDF(); return false;"><digi:trn>Export to PDF</digi:trn></a>
				</td>
				<td width=10></td>
				<td>
					<digi:img src="img_2/ico_print.gif" style="margin-right:5px;"/>
				</td>
				<td>
					<digi:link styleClass="l_sm" href="#" onclick="window.print(); return false;"><digi:trn>Print</digi:trn></digi:link>
				</td>
			</tr>
		</table>
	
		<div class="dash_ico">
			<div class="dash_ico_link">
			</div>
		</div>
		<div class="dash_ico">
			<div class="dash_ico_link">
			</div>
		</div>
	</div>
	
	<div style="float:left;">
		<digi:trn>From Date:</digi:trn> 
		<html:select styleClass="dropdwn_sm" style="width:145px;" name="gisDashboardForm" property="selectedFromYear" onchange="yearChanged();mapYearChanged()">
			<html:optionsCollection name="gisDashboardForm" property="yearsFrom" label="label" value="value"/>
		</html:select>
		&nbsp;&nbsp;
		<digi:trn>To Date:</digi:trn> 
		<html:select styleClass="dropdwn_sm" style="width:145px;" name="gisDashboardForm" property="selectedToYear" onchange="yearChanged();mapYearChanged()">
			<html:optionsCollection name="gisDashboardForm" property="yearsTo" label="label" value="value"/>
		</html:select>
	</div>
</div>



<br>
